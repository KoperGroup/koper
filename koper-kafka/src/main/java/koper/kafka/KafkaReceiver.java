/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package koper.kafka;

import com.alibaba.fastjson.JSON;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import koper.ListenerRegistry;
import koper.MessageCenter;
import koper.MsgBean;
import koper.client.MessageReceiver;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Kafka 消息接收器. 统一负责和Broker连接订阅消息，之后它作为接收消息的总入口。
 * 收到消息后放到本地临时队列由 MessageDispatcher分发到具体的Listener
 *
 * @author kk
 * @since 1.0
 */
public class KafkaReceiver implements MessageReceiver {

    private static Logger log = LoggerFactory.getLogger(KafkaReceiver.class);

    private MessageCenter messageCenter;

    private ListenerRegistry listenerRegistry;

    private Properties properties;

    private ConsumerConfig consumerConfig;

    private ConsumerConnector consumer;

    private int partitions;

    private Object lock;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void setMessageCenter(MessageCenter messageCenter) {
        this.messageCenter = messageCenter;
    }

    @Override
    public void setListenerRegistry(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
    }

    @Override
    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    /**
     * 启动 MessageReceiver，开始监听topic消息
     */
    @Override
    public void start() {

        if (consumer == null) {
            //sync init
            synchronized (lock) {
                init();
            }
        }

        String topicString = buildTopicsString();

        Whitelist topicFilter = new Whitelist(topicString);
        List<KafkaStream<byte[], byte[]>> streamList = consumer.createMessageStreamsByFilter(topicFilter, partitions);

        if (org.apache.commons.collections.CollectionUtils.isEmpty(streamList))
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.warn(e.getMessage(), e);
            }
        processStreamsByTopic(topicString, streamList);

    }

    private void processStreamsByTopic(String topicKeys, List<KafkaStream<byte[], byte[]>> streamList) {
        // init stream thread pool
        ExecutorService streamPool = Executors.newFixedThreadPool(partitions);
        String[] topics = StringUtils.split(topicKeys, ",");
        if (log.isDebugEnabled())
            log.debug("准备处理消息流集合 KafkaStreamList,topic count={},topics={}, partitions/topic={}", topics.length, topicKeys, partitions);

        //遍历stream
        AtomicInteger index = new AtomicInteger(0);
        for (KafkaStream<byte[], byte[]> stream : streamList) {
            Thread streamThread = new Thread() {

                @Override
                public void run() {
                    int i = index.getAndAdd(1);
                    if (log.isDebugEnabled())
                        log.debug("处理消息流KafkaStream -- No.={}, partitions={}", i, partitions + ":" + i);

                    ConsumerIterator<byte[], byte[]> consumerIterator = stream.iterator();

                    processStreamByConsumer(topicKeys, consumerIterator);
                }
            };
            streamPool.execute(streamThread);
        }
    }

    private void processStreamByConsumer(String topicKey, ConsumerIterator<byte[], byte[]> consumerIterator) {

        int i = 0;
        while (consumerIterator.hasNext()) {
            MessageAndMetadata<byte[], byte[]> msgAndMeta = consumerIterator.next();

            byte[] keyBytes = msgAndMeta.key();
            byte[] messageBytes = msgAndMeta.message();

            try {
                String key = keyBytes != null ? new String(keyBytes, "UTF-8") : null;
                String msgString = new String(messageBytes, "UTF-8");

                if (log.isDebugEnabled())
                    log.debug("*********** Stream收到消息:No.={},key={},message={}", i++, key, msgString);

                @SuppressWarnings("unchecked")
                MsgBean<String, String> msgBean = JSON.parseObject(msgString, MsgBean.class);
                //put to local buffer
                msgBean.setReceiveTime(new Date());
                messageCenter.putMessage(msgBean);

                // dispatcher = new DefaultMessageDispatcher();
                // dispatcher.setListenerRegistry(listenerRegistry);
                // dispatcher.invokeMessageHandler(msgBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * topic string, e.g.  "topic123,topic456,topic789"
     *
     * @return
     */
    private String buildTopicsString() {

        final Map<String, ?> listnersMap = listenerRegistry.getListenerMap();

        String topicStr = "";
        int i = 0;
        int size = listnersMap.keySet().size();
        for (String key : listnersMap.keySet()) {
            topicStr += key;
            if (i++ < (size - 1)) topicStr += ",";
        }
        return topicStr;
    }

    private ConsumerConfig createConsumerConfig() {
        this.consumerConfig = new ConsumerConfig(this.properties);
        return this.consumerConfig;
    }

    /**
     * @return the consumerConfig
     */
    public ConsumerConfig getConsumerConfig() {
        return consumerConfig;
    }

    /**
     * 初始化
     *
     * @return
     */
    @Override
    public boolean init() {
        if (consumerConfig == null)
            createConsumerConfig();
        this.consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
        return true;
    }
}

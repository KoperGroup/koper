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
package com.zhaimi.message.client;

import com.zhaimi.message.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kk
 * @since 1.0
 */
public class DefaultConsumerLauncher implements ConsumerLauncher, InitializingBean, ApplicationListener<ContextRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger(DefaultConsumerLauncher.class);

    private boolean autoStart = false;

    private Properties properties;

    private int partitions;

    private Class<MessageReceiver> messageReceiverClass;

    private Class<? extends MessageDispatcher> messageDispatcherClass = DefaultMessageDispatcher.class;

    //local message buffer
    private MessageCenter messageCenter;

    private ListenerRegistry listenerRegistry = new ListenerRegistry();

    private int dispatcherThreads = 16;

    public void setMessageDispatcherClass(Class<MessageDispatcher> messageDispatcherClass) {
        this.messageDispatcherClass = messageDispatcherClass;
    }

    public void setMessageReceiverClass(Class<MessageReceiver> messageReceiverClass) {
        this.messageReceiverClass = messageReceiverClass;
    }

    public void setMessageCenter(MessageCenter messageCenter) {
        this.messageCenter = messageCenter;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * 启动 ConsumerLauncher
     */
    public void start() {
        log.info("启动MQ Kafka Client");
        Map<String, Object> listeners = listenerRegistry.getListenerMap();
        if (log.isInfoEnabled())
            log.info("消息监听器注册表:{}", listeners);

        int receiverCount = 1; // just need 1 receiver thread
        int dispatcherCount = dispatcherThreads;

        if (messageCenter == null)
            this.messageCenter = new DefaultMessageCenter(dispatcherThreads);

        startMessageDispatcher(dispatcherCount);
        startMessageReceiver(receiverCount);
    }

    private void startMessageReceiver(int size) {
        ExecutorService receiverPool = Executors.newFixedThreadPool(size, new NamedThreadFactory(MessageReceiverThread.class.getSimpleName()));

        try {
            for (int i = 0; i < size; i++) {
                MessageReceiver messageReceiver = this.messageReceiverClass.newInstance();
                Runnable consumerThread = new MessageReceiverThread(properties, messageReceiver, messageCenter, this.listenerRegistry, this.partitions);
                receiverPool.execute(consumerThread);
            }
        } catch (Exception e) {
            log.error("startMessageReceiver 失败:{}", e);
            throw new RuntimeException(e);
        }
    }

    private void startMessageDispatcher(int size) {

        //Start dispatcher thread pool
        ExecutorService dispatcherPool = Executors.newFixedThreadPool(size, new NamedThreadFactory(MessageDispatcherThread.class.getSimpleName()));
        try {
            for (int i = 0; i < size; i++) {
                MessageDispatcher messageDispatcher = this.messageDispatcherClass.newInstance();
                MessageDispatcherThread messageDispatcherThread = new MessageDispatcherThread(messageDispatcher, this.messageCenter, this.listenerRegistry);
                dispatcherPool.execute(messageDispatcherThread);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("启动消息分发器失败, 异常为 [{}]", ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    public ListenerRegistry getListenerRegistry() {
        return listenerRegistry;
    }

    @Override
    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    /**
     * 分发线程数
     */
    @Override
    public void setDispatcherThreads(int threads) {
        this.dispatcherThreads = threads;
    }

    /**
     * 容器初始化完毕后执行。
     *
     * @see ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (autoStart)
            this.start();
    }

    public static void main(String[] args) {
        ConsumerLauncher consumerLauncher = new DefaultConsumerLauncher();
        consumerLauncher.start();
    }

}



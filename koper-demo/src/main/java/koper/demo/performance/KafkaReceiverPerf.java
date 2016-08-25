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
package koper.demo.performance;

import koper.client.ConsumerLauncher;
import koper.demo.dataevent.listener.OrderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author kk
 * @since 1.0
 */
public class KafkaReceiverPerf {
    private static Logger logger = LoggerFactory.getLogger(KafkaReceiverPerf.class);

    /**
     * 消费者程序入口
     * @param args : 第一个命令行参数(可选)设定统计信息每隔多少条消息记录一次
     */
    public static void main(String[] args) {
        final ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:kafka/context-data-consumer.xml");

        if (args.length > 0) {
            int statLine = Integer.parseInt(args[0]);
            OrderListener listener = context.getBean(OrderListener.class);
            listener.changeStatLine(statLine);
        }

        final ConsumerLauncher consumerLauncher = context.getBean(ConsumerLauncher.class);
        // we have close the switch in context-data-consumer.xml profile(autoStart) temporary
        consumerLauncher.start();

        logger.info("KafkaReceiverPerf started!");
    }
}

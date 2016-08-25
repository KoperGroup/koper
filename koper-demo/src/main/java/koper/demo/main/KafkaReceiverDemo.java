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
package koper.demo.main;

import koper.client.ConsumerLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author kk
 * @since 1.0
 */
public class KafkaReceiverDemo {

    private static Logger logger = LoggerFactory.getLogger(KafkaReceiverDemo.class);

    public static void main(String[] args) {
        final ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:kafka/context-data-consumer.xml");

        final ConsumerLauncher consumerLauncher = context.getBean(ConsumerLauncher.class);
        // we have close the switch in context-data-consumer.xml profile(autoStart) temporary
        consumerLauncher.start();

        logger.info("KafkaReceiverDemo started!");
    }
}

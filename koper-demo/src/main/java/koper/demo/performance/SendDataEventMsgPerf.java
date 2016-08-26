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

import koper.demo.dataevent.entity.Order;
import koper.demo.dataevent.service.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author caie
 * @since 1.2
 */
public class SendDataEventMsgPerf {

    /**
     * DataEventMsg 生产者入口
     * @param args 第一个命令行参数:设定发送消息的线程数
     *             第二个命令行参数:设定每发送一条消息后线程 sleep 的毫秒数
     */
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "kafka/context-data-message.xml",
                "kafka/context-data-producer.xml");

        Order order = new Order();
        order.setId(100);
        order.setOrderNo("order_no");
        order.setCreatedTime("oroder_created_time");

        OrderService orderService = (OrderService) context.getBean("orderServiceImpl");

        int threadNum = 1;
        long sleepMs = 1000L;
        if (args.length >= 1) {
            threadNum = Integer.parseInt(args[0]);
            sleepMs = Long.parseLong(args[1]);
        }
        final long finalSleepMs = sleepMs;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        for (int i = 0; i < threadNum; ++i) {
            executorService.execute(() -> {
                while (true) {
                    orderService.insertOrder(order);
                    try {
                        Thread.sleep(finalSleepMs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

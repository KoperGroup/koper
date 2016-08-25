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

import koper.AbstractMessageListener;
import koper.Listen;
import koper.demo.trading.mapper.Order;
import koper.sender.MessageSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 将消息的生产者, 消费者 组合在一起测试.
 * 每当发送消息后, 主动让主线程沉睡一定时间,
 * 从而使得启动的实例可以让消费者接收到从遥远的Kafka来的消息,
 * 并作相应断言.
 *
 * @author caie
 * @since 1.2
 */
@ContextConfiguration(locations = {
        "classpath:kafka/context-data-producer.xml",
        "classpath:kafka/context-data-message.xml",
        "classpath:kafka/context-data-consumer.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderListenerTestCase extends AbstractMessageListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageSender messageSender;
    private Order expectOrder;
    private String memberId = "member_id";
    private BigDecimal totalPrice = new BigDecimal(20);
    private String orderId = "order_id";
    private String orderName = "order_name";
    private final String topic = "koper.message.listener.OrderListenerTestCase";

    @Before
    public void setUp() {
        expectOrder = new Order();
        expectOrder.setMemberId(memberId);
        expectOrder.setOrderName(orderName);
        expectOrder.setOrderId(orderId);
        expectOrder.setTotalPrice(totalPrice);
    }

    @Test
    public void testMessageSenderNotNull() {
        assertNotNull(messageSender);
    }

    /**
     * 测试方法 与 onMessage(T t) 一一对应,
     * 如下, 一个 onMessage(String param) 对应了 testSendStringMsg().
     * <p>
     * 一个类中允许定义重载的 onMessage 方法, 但 @Listen 只允许定义一处.
     */
    @Test
    public void testSendStringMsg() throws Exception {
        final String msg = "test_order_listener_on_message";
        messageSender.send(topic, msg);
        Thread.sleep(30000);
    }

    @Listen(topic = "koper.message.listener.OrderListenerTestCase")
    @Override
    public void onMessage(String msg) {
        logger.info("on message successful, param is : {}", msg);

        assertNotNull(msg);
        final String expectMsg = "test_order_listener_on_message";
        final String actualMsg = msg;
        assertEquals(expectMsg, actualMsg);
    }

    @Test
    public void testSendObjectMsg() throws Exception {
        messageSender.send(topic, expectOrder);
        Thread.sleep(30000);
    }

    public void onMessage(Order order) {
        final Order actualOrder = order;
        logger.info("on message successful, expectOrder info : member id : {}," +
                        "expectOrder name : {}, expectOrder id : {}, total price : {}",
                actualOrder.getMemberId(), actualOrder.getOrderName(), actualOrder.getOrderId(),
                actualOrder.getTotalPrice());

        assertNotNull("onMessage 接收到的 订单 为空, 请检查.", actualOrder);
        assertEquals("onMessage 接收到的 memberId 与 预期的不相同, 请检查.", actualOrder.getMemberId(), expectOrder.getMemberId());
        assertEquals("onMessage 接收到的 orderName 与 预期的不相同, 请检查.", actualOrder.getOrderName(), expectOrder.getOrderName());
        assertEquals("onMessage 接收到的 orderId 与 预期的不相同, 请检查.", actualOrder.getOrderId(), expectOrder.getOrderId());
        assertEquals("onMessage 接收到的 totalPrice 与 预期的不相同, 请检查", actualOrder.getTotalPrice(), expectOrder.getTotalPrice());
    }

    @Test
    public void testSendIntegerMsg() throws Exception {
        final int id = 100;

        messageSender.send(topic, id);
        Thread.sleep(300002);

    }

    public void onMessage(Integer id) {
        final Integer actualId = id;
        final Integer expectId = 100;

        assertNotNull(id);
        assertEquals("onMessage 接收到的 id 与 预期的不相同, 请检查.", actualId, expectId);
    }

}

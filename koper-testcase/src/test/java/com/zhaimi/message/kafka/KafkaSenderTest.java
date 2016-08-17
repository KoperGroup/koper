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
package com.zhaimi.message.kafka;

import com.zhaimi.message.demo.trading.mapper.Order;
import com.zhaimi.message.sender.MessageSender;
import junit.textui.TestRunner;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author kk
 * @since 1.0
 */
public class KafkaSenderTest extends AbstractDependencyInjectionSpringContextTests {

	private MessageSender messageSender;

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[]{ "classpath:kafka/context-data-producer.xml"};
	}

	/**
	 * 测试，发10条消息。
	 * 调用  messageSender.send(topic2, msg) 发送消息
	 */
	@Test
	public void test() throws Exception {
		String topic2 = "XXXXX";
		String msgBase2 = "orderId_";
		for (int i = 0; i < 100 ; i++) {
			String key = msgBase2 + i +"_" + System.currentTimeMillis();
			String msg = key;
			messageSender.send(topic2, msg);
		}
		// async 模式,需要等待消息发送完毕
		//Thread.sleep(10 * 1000);
	}

	public static void main(String[] args) {
		TestRunner.run(KafkaSenderTest.class);
	}

	public void testSendOrderMsg() {
        String orderTopic = "zhaimi.orderListener";

        String demoTopic = "XXXXX";
        String msg = "123";

        messageSender.send(demoTopic, msg);

        // messageSender.send(orderTopic, msg);

        Order order = new Order();
        order.setOrderId("20160817");
        order.setTotalPrice(new BigDecimal(100));
        order.setOrderName("test_order_name");
        order.setCreateDate(new Date());

        messageSender.send(orderTopic, order);
    }
}

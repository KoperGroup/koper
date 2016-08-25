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

import koper.demo.trading.mapper.Order;
import koper.demo.trading.mapper.OrderMapper;
import junit.textui.TestRunner;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author kk
 * @since 1.0
 */
public class SendMsgAopTest extends AbstractDependencyInjectionSpringContextTests {

	private OrderMapper orderMapper;

	/**
	 * @param orderMapper the orderMapper to set
	 */
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[]{
							 "classpath:kafka/context-data-message.xml",
							 "classpath:kafka/context-data-producer.xml"
							};
	}


	public void testMethodSendMessage() {
		System.out.println("kk research lab.");

		Order order = new Order();
		order.setOrderId("002");
		order.setOrderName("鲜花订单002");
		orderMapper.createOrder("A001",order);

		String newOrder = orderMapper.getOrder("088", order);
		System.out.println("****** getOrder " + newOrder);

		newOrder = orderMapper.insertOrder( order);
		System.out.println("****** insert Order  " + order);


		newOrder = orderMapper.updateOrder( order);
		System.out.println("****** updateOrder " + order);
	}

	/**
	 * 数据操作执行demo。 以下操作会被构造为数据消息发送到消息中心。
	 */
	public void testMethodSendMessages() {

		Order order = new Order();
		order.setOrderId("002");
		order.setOrderName("鲜花订单002");

		String orderId = orderMapper.deleteOrder("x788", order);
		System.out.println("...... delete order  " + order);

		String ret = orderMapper.deleteOldOrder(119, false);
		System.out.println("...... delete old order  " + order);

		orderMapper.cancelOrder(order);
		System.out.println("...... cancelOrder: order  " + order);

	}


	public static void main(String[] args) {
		TestRunner.run(SendMsgAopTest.class);
	}
}

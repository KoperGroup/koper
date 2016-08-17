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
import com.zhaimi.message.demo.trading.mapper.OrderMapper;
import junit.textui.TestRunner;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * KafkaBatchSenderTest
 *
 * @author kk hekun@zhai.me
 * @since 1.0
 * 2016年3月25日
 */
public class KafkaBatchSenderTest extends AbstractDependencyInjectionSpringContextTests {

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
							 "classpath:kafka/context-data-aop.xml",
							 "classpath:kafka/context-data-producer.xml"
							};
	}

	private Order newOrder(int i) {
		Order order = new Order();
		
		String orderId = "D" + i;
		order.setOrderId(orderId);
		order.setOrderName("鲜花订单002");
		order.setMemberId("Jacky001");
		order.setCreateDate(new Date());
		order.setTotalPrice(new BigDecimal("120.10"));
		return order;
	}
	
	/**
	 * 测试，发10条消息。
	 * 调用  messageSender.send(topic2, msg) 发送消息
	 */
	public void testInsertOrder() {
		
		int c = 100;
		int n = 10;
		for (int i = 0; i < c ; i++) {
			
			ExecutorService executorService = Executors.newFixedThreadPool(16);
			
			int x = i;
			executorService.execute(new Runnable() {
				
				@Override
				public void run() {
					for (int j = 0; j < n ; j++) {
						Order order = newOrder(x*10 + j);
						String newOrderId = orderMapper.insertOrder( order);
						System.out.println("****** insert Order  " + newOrderId);
					}
					
				}
			});
		}
	}
	
	public static void main(String[] args) {
		TestRunner.run(KafkaBatchSenderTest.class);
	}

}

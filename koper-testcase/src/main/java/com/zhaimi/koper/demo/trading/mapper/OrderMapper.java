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
package com.zhaimi.koper.demo.trading.mapper;

/**
 * @author kk
 * @since 1.0
 */
public interface OrderMapper {
	
	String getOrder(String orderId, Order order);

	String cancelOrder(Order order);

	String createOrder(String memberId, Order order);
	
	String updateOrder(Order order);
	
	String insertOrder(Order order);

	/**
	 * @param orderId
	 * @param order
	 * @return
	 */
	String deleteOrder(String orderId, Order order);
	
	String deleteOldOrder(int orderId, boolean reserve);

	String cancelOldOrder(Order order);
}

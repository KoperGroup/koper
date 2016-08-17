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
package com.zhaimi.message.event;

import com.alibaba.fastjson.JSON;
import com.zhaimi.message.demo.trading.mapper.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * InsertOrderListener. 订单日志监听器
 *
 * @author kk hekun@zhai.me
 * @since 1.0
 * 2016年3月21日
 */
@Component
@DataListener(dataObject="com.zhaimi.message.demo.trading.mapper.impl.OrderMapperImpl")
public class OrderLogListener {

	private static Logger log = LoggerFactory.getLogger(OrderLogListener.class);

	public void onCancelOrder_B(DataEvent event) {
		log.info("onCancelOrder_B" + event.getArgs());
	}

	/**
	 * deleteOrder事件响应处理方法
	 * 从event中获取数据：输入参数args，返回值 result。
	 * 数据格式为JSON。使用 fastjson工具类 进行解析
	 * @param event
	 */
	public void onCancelOrder(DataEvent event) {

		List<?> args = event.getArgs();
		//数据解析：需要根据生产接口定义的参数个数，数据类型进行解析
		//根据定义，第一个参数String类型
		//根据定义，第二个参数类型为 Order 类型
		Order order = (Order) JSON.parseObject( args.get(0).toString(),Order.class);

		long productTime = event.getMsgBean().getProduceTime().getTime();
		log.info("onCancelOrder order {}, time:{}" ,order, productTime);

		//根据生产接口定义：返回值类型为 String
		String result = (String)event.getResult();
		log.info("onCancelOrder 事件结果" + result);
	}

	/**
	 * 处理完成订单事件
	 * @param event
	 */
	public void onCompleteOrder(DataEvent event) {

	}

	/**
	 * 处理取消订单失败处理
	 * @param event
	 */
	public void cancelOrder_X(DataEvent event) {

		String exceptionString = event.getException();
		log.info("onCancelOrder_X" + event.getArgs());
		log.info("onCancelOrder_X 异常:" + exceptionString);
	}

}

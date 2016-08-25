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
package koper.event;

import com.alibaba.fastjson.JSON;
import koper.demo.trading.mapper.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OrderAsyncListener
 *
 * @author kk hekun@zhai.me
 * @since 1.0
 * 2016年2月19日
 */
@Component
@DataListener(dataObject="koper.demo.trading.mapper.impl.OrderMapperImpl")
public class OrderAsyncListener {
	private static Logger log = LoggerFactory.getLogger(OrderAsyncListener.class);


	public void onDeleteOrder_B(DataEvent event) {
		log.info("onDeleteOrder_B" + event.getArgs());
	}

	/**
	 * deleteOrder事件响应处理方法
	 * 从event中获取数据：输入参数args，返回值 result。
	 * 数据格式为JSON。使用 fastjson工具类 进行解析
	 * @param event
	 */
	public void onDeleteOrder(DataEvent event) {

		List<?> args = event.getArgs();
		//数据解析：需要根据生产接口定义的参数个数，数据类型进行解析
		//根据定义，第一个参数String类型
		String orderId = (String) args.get(0);
		//根据定义，第二个参数类型为 Order 类型
		Order order = JSON.parseObject( args.get(1).toString(),Order.class);

		log.info("onDeleteOrder orderId " + orderId);
		log.info("onDeleteOrder order " + order);

		//根据生产接口定义：返回值类型为 String
		String result = (String)event.getResult();
		log.info("onDeleteOrder 事件结果" + result);
	}

	public void onDeleteOrder_X(DataEvent event) {
		String exception = event.getException();
		System.out.println(exception);
		// log.info("deleteOrder_X" + event.getArgs());
	}
}

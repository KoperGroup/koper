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
package koper.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import koper.AbstractMessageListener;
import koper.Listen;
import koper.MsgBean;
import koper.demo.trading.mapper.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author kk
 * @since 1.0
 */
@Component
public class DemoListener3 extends AbstractMessageListener {

	private static Logger log = LoggerFactory.getLogger(DemoListener3.class);

	/**
	 * @see AbstractMessageListener#onMsgBean(MsgBean)
	 */
	@Listen(topic="koper.demo.trading.mapper.impl.OrderMapperImpl.updateOrder_X")
	@Override
	public void onMsgBean(MsgBean<String, String> msgBean) {
		log.info("DemoListener2 onMsgBean 收到消息内容:{}, 接收用时:{},消费用时:{}" , msgBean,msgBean.getReceiveUseTime(),msgBean.getConsumeUseTime());
		//消息内容，json串
		String jsonMessage = msgBean.getValue();

		JSONObject jsonObject = JSON.parseObject(jsonMessage);
		//args是数组类型
		JSONArray  argArray = jsonObject.getJSONArray("args");
		//result 是 String类型
		String result = jsonObject.getString("result");

		//第2个参数是Order类型
		Order order = JSON.parseObject(argArray.getString(1), Order.class);
		log.info("order " + order.getOrderId());

		log.info("执行结果 " + result);
	}

	@Override
	public void onMessage(String msg) {

	}
}

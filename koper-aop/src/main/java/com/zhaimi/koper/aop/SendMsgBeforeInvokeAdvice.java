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
package com.zhaimi.koper.aop;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SendMsgBeforeInvokeAdvice. 发消息切面
 *
 * @author kk
 * @since 1.0
 */
public class SendMsgBeforeInvokeAdvice extends AbstractSendMessageAdvice {
	
	private static Logger log = LoggerFactory.getLogger(SendMsgBeforeInvokeAdvice.class);
	/** 
	 * 方法调用前发送消息。Send message before method invocation, using args
	 * @param method
	 * @param topic MQ topic
	 * @param args
	 */
	@Override
	protected void sendMessageBefore(Method method, String topic, Object[] args) {
		List<Object> argList = Arrays.asList(args);
		
		Map<String,Object> invokeInfoMap = new HashMap<>();
		invokeInfoMap.put("args", argList);
		
		String jsonMessage = JSON.toJSONString(invokeInfoMap);
		
		if(log.isDebugEnabled())
			log.debug("sendMessageBefore Topic={}, jsonMessage={}" ,topic, jsonMessage);
		
		getMessageSender().send(topic, jsonMessage);
	}
	
	/**
	 * Before topic format ,  xxxx.xxxx_B
	 * After topic format ,  xxxx.xxxx
	 * Exception topic format ,  xxxx.xxxx_X 
	 */
	@Override
	protected String fixTopicName(String topicName0) {
		return topicName0 + "_B";
	}
}

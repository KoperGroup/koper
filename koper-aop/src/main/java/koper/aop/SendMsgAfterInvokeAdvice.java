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
package koper.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.annotation.Aspect;
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
@Aspect
public class SendMsgAfterInvokeAdvice extends AbstractSendMessageAdvice {
	
	private static Logger log = LoggerFactory.getLogger(SendMsgAfterInvokeAdvice.class);
	/**
	 * 方法调用后发送消息
	 * @see com.kk.AbstractSendMessageAdvice#sendMessageAfter(Method, String, Object[], Object)
	 */
	@Override
	protected void sendMessageAfter(Method method, String topic, Object[] args, Object result) {
		
		List<Object> argList = Arrays.asList(args);
		
		Map<String,Object> invokeInfoMap = new HashMap<>();
		invokeInfoMap.put("args", argList);
		invokeInfoMap.put("result", result);
		
		String jsonMessage = JSON.toJSONString( invokeInfoMap );
		
		if(log.isDebugEnabled())
			log.debug("sendMessageAfter Topic={}, jsonMessage={}" ,topic, jsonMessage);
		
		getMessageSender().send(topic, jsonMessage);
	}
}

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

import java.io.PrintWriter;
import java.io.StringWriter;
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
public class SendMsgAfterExceptionAdvice extends AbstractSendMessageAdvice {

	private static Logger log = LoggerFactory.getLogger(SendMsgAfterExceptionAdvice.class);

	private String exceptionTopic;

	private boolean addStackTrace = false;

	/**
	 * 方法调用异常时发送消息
	 * @see com.kk.AbstractSendMessageAdvice#sendMessageAfterException(Method, String, Object[], Throwable)
	 */
	@Override
	protected void sendMessageAfterException(Method method, String topic, Object[] args, Throwable t) {
		String epTopic;
		if(exceptionTopic == null || "".equals(exceptionTopic)){
			epTopic = topic;
		}else{
			epTopic = exceptionTopic;
		}
		List<Object> argList = Arrays.asList(args);

		Map<String,Object> invokeInfoMap = new HashMap<>();
		invokeInfoMap.put("args", argList);
		invokeInfoMap.put("method",topic.substring(0,topic.length()-2));
		if(addStackTrace){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			invokeInfoMap.put("exception", sw.toString());
		}else{
			invokeInfoMap.put("exception", t.toString());
		}
		String jsonMessage = JSON.toJSONString(invokeInfoMap);

		if(log.isDebugEnabled())
			log.debug("sendMessageAfterException Topic={}, jsonMessage={}" ,topic, jsonMessage);

		getMessageSender().send(epTopic, jsonMessage);
	}

	public String getExceptionTopic() {
		return exceptionTopic;
	}

	public void setExceptionTopic(String exceptionTopic) {
		this.exceptionTopic = exceptionTopic;
	}

	public boolean isAddStackTrace() {
		return addStackTrace;
	}

	public void setAddStackTrace(boolean addStackTrace) {
		this.addStackTrace = addStackTrace;
	}

	/**
	 * Before topic format ,  xxxx.xxxx_B
	 * After topic format ,  xxxx.xxxx
	 * Exception topic format ,  xxxx.xxxx_X
	 */
	@Override
	protected String fixTopicName(String topicName0) {
		return topicName0 + "_X";
	}
}

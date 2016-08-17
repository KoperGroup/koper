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
package com.zhaimi.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kk
 * @since 1.0
 */
public class ListenerRegistry {
	
	private Map<String, Object> listenerMap = new ConcurrentHashMap<>();
	
	/**
	 * 注册监听器。 Resigster listener
	 * @param topic
	 * @param listener
	 */
	public void register(String topic, Object listener) {
		
		Object listener1 = listenerMap.get(topic);
		if(listener1 != null ) {
			throw new IllegalArgumentException("Listener with the same topic has been registered! topic=" + topic 
					+ ". Existed listener " + listener1 + ". New listener " + listener1);
		}else
			listenerMap.put(topic, listener);
	
	}
	
	public Map<String,Object> getListenerMap() {
		return listenerMap;
	}
}

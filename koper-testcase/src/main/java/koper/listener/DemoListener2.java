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

import koper.AbstractMessageListener;
import koper.Listen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author kk
 * @since 1.0
 */
@Component
public class DemoListener2 extends AbstractMessageListener {
	
	private static Logger log = LoggerFactory.getLogger(DemoListener2.class);
	
	@Listen(topic="koper.demo.trading.mapper.impl.OrderMapperImpl.updateOrder_B")
	public void onMessage(String msg) {
		log.info("DemoListener2 收到updateOrder_B消息内容:{}" , msg);
		
		
	}
}

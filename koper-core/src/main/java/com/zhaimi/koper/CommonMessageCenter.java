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
package com.zhaimi.koper;

import java.util.concurrent.LinkedBlockingQueue;

import com.zhaimi.koper.router.RoundRobinRouter;
import com.zhaimi.koper.router.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息中心默认实现。
 * 本地维持一个阻塞队列，缺省消息最大条数32万条。
 *
 * @author kk
 * @since 1.2
 */
public class CommonMessageCenter<T> implements MessageCenter<T> {
	
	private static Logger log = LoggerFactory.getLogger(CommonMessageCenter.class);


	private Router router;

	public static final int LOCAL_Q_SIZE = 32;
	private LinkedBlockingQueue<T> msgQ;  //default max message count 3.2M
	
	/**
	 * 
	 */
	public CommonMessageCenter(int qSize,Router router) {
		
		int size = qSize<=0 ? LOCAL_Q_SIZE : qSize;
		this.msgQ  = new LinkedBlockingQueue<>(size);
		this.router = router;
	}

	/**
	 * use {@Link RoundRobinRouter} as default router
	 * @param qSize
	 */
	public CommonMessageCenter(int qSize) {

		int size = qSize<=0 ? LOCAL_Q_SIZE : qSize;
		this.msgQ  = new LinkedBlockingQueue<>(size);
		this.router = new RoundRobinRouter();
	}
	
	/**
	 * 消息本地入队
	 */
	@Override
	public void putMessage(T msg) {
		try {
			this.getRouter().dispatch(msg);
		} catch (InterruptedException e) {
			throw new RuntimeException("Fail to put msg to Q!" + e.getMessage(),e);
		}
	}

	@Override
	public Router getRouter() {
		return this.router;
	}

	@Deprecated
	@Override
	public T  takeMessage() {
		try {
			return msgQ.take();
		} catch (InterruptedException e) {
			throw new RuntimeException("Fail to take msg from Q!" + e.getMessage(),e);
		}
	}
}

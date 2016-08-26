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
package koper.reactor.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import koper.reactor.ReactorRef;
import koper.sender.MessageSender;
import scala.util.Random;

/**
 * ProducerApp
 * @author Raymond He, raymondhekk9527@gmail.com
 * @since 1.0
 * 2016年8月25日
 *
 */
public class ProducerApp {
	
	/**
	 * Reactor produce demo.
	 * Create prototype(multiple instances) reactor and send message to them.
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:kafka/context-data-producer.xml");
		MessageSender messageSender = (MessageSender) context.getBean("messageSender");

		ReactorRef ref = ReactorRef.create(Digger.class);
		ref.setMessageSender(messageSender);
		
		System.out.println("ref created " + ref.getId()  +"," + ref);
		for (int i = 0; i < 3 ; i++) {
			String msg = "Hello" + i;
			System.out.println("send->" + msg);
			ref.send("Hello" + i);
		}
		
		ReactorRef ref1 = ReactorRef.create(Digger.class);
		ref1.setMessageSender(messageSender);
		ref1.send("NewYork" );
		
		ReactorRef ref2= ReactorRef.create(Digger.class);
		ref2.setMessageSender(messageSender);
		ref2.send("SF" );
	}
}

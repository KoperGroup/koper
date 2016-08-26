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
package koper.reactor;

import java.util.Random;

import koper.sender.MessageSender;

/**
 * ReactorRef
 * @author Raymond He, raymondhekk9527@gmail.com
 * @since 1.0
 * 2016年8月26日
 *
 */
public class ReactorRef {
	
	private long id;
	private Class<? extends Reactor> reactorClass;
	public MessageSender messageSender;

	private static final Random rd = new Random();
	/**
	 * 
	 */
	private ReactorRef(Class<? extends Reactor> reactorClass) {
		this.reactorClass = reactorClass;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Create a remote object. Specify a random global id;
	 * @param reactorClass
	 * @return
	 */
	public static ReactorRef create(Class<? extends Reactor> reactorClass) {
		ReactorRef ref = new ReactorRef(reactorClass);
		long id = rd.nextLong();
		id = id<0 ? -id : id;
		ref.setId(id);
		return  ref;
	}
	
	/**
	 * Refer to an existing remote object with a given id.
	 * @param id
	 * @param reactorClass
	 * @return
	 */
	public static ReactorRef ref( Class<? extends Reactor> reactorClass,long id) {
		ReactorRef ref = new ReactorRef(reactorClass);
		ref.setId(id);
		return ref;
	}
	
	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}
	
	/**
	 * send message to remote object refered by this ref
	 * @param message
	 */
	public void send(Object message) {
		String className = reactorClass.getName();
		String classAndMethod = className ;
		System.out.println("id " + id);
		messageSender.send(classAndMethod , String.valueOf( id ) , message);
	}
	
	public static void main(String[] args) {
		Random rd = new Random(1000);
		for(int i=0;i<10;i++) {
			long id = rd.nextLong();
			id = id<0? -id : id;
			System.out.println("x " + id);
		}
	}
}

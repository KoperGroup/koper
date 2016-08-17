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
package com.zhaimi.message.kafka;

import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSON;
import com.zhaimi.message.MsgBean;
import com.zhaimi.message.sender.MessageSender;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * Kafka Message Sender
 *
 * @author kk
 * @since 1.0
 *
 */
public class KafkaSender implements MessageSender , InitializingBean {

	private static Logger log = LoggerFactory.getLogger(KafkaSender.class);

	private  Properties properties;

	private Producer<String, String> producer;

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	/**
	 * send msg to topic
	 */
	@Override
	public void send(String topic, String msg) {
		send(topic, null, msg);
	}

	@Override
	public void send(String topic, String key, String msg) {
		sendMsg(topic, key, msg);
	}

	/**
	 * Send Object message as  Json format
	 */
	@Override
	public void send(String topic, Object msgObj) {
		send(topic, null, msgObj);
	}

	private void sendMsg(String topic, String key, Object msgObj) {
		String msgString;
        String valueType = msgObj.getClass().getName();
		if( msgObj instanceof String) {
			msgString = (String) msgObj;
		} else {
			 msgString = JSON.toJSONString(msgObj);
		}

		KeyedMessage<String,String> keyedMessage;

		MsgBean<String, String> msgBean = new MsgBean<>();
		msgBean.setTopic(topic);
		msgBean.setKey(key);
		msgBean.setValue( msgString );
        msgBean.setValueType(valueType);
		msgBean.setProduceTime(new Date());
		msgBean.setProduceTid(Thread.currentThread().getId());

		String json = JSON.toJSONString(msgBean);
		if( key == null)
			keyedMessage = new KeyedMessage<>(topic,json);
		else
			keyedMessage = new KeyedMessage<>(topic, key,json);

		this.producer.send( keyedMessage);

		if(log.isDebugEnabled()) {
			log.debug("发送消息. topic='{}',key='{}',msg='{}',json='{}'",topic,key, msgObj,json);
		}
	}

	/**
	 * 发送消息
	 *
	 * @param topic 消息主题
	 * @param key   消息键值
	 * @param msgObj   消息内容
	 */
	@Override
	public void send(String topic, String key, Object msgObj) {
		sendMsg(topic, key, msgObj);
	}


	@Override
	public void afterPropertiesSet() throws Exception {
        if( properties == null) {
			throw new IllegalArgumentException("Properties are required when init KafkaSender!");
		}
		this.producer = new Producer<>( new ProducerConfig(this.properties));
	}

}

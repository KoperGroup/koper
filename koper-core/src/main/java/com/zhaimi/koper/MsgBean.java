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

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Message。消息封装类。能维护topic,key,value消息的生产和接收消费时间。
 *
 * @author kk
 * @since 1.0
 */
public class MsgBean<K,V> implements Serializable {

	private static final long serialVersionUID = -4387811273059946901L;

	private String topic;
	private K key;
	private V value;
	private String valueType;

	private Date produceTime;
	private String produceIP;
	private long producePid;
	private long produceTid;

	private Date receiveTime;
	private Date consumeTime;
	private String cosumerIP;
	private long consumerPid;
	private long cosumerTid;

	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
    public String getValueType() {
        return valueType;
    }
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Date getProduceTime() {
		return produceTime;
	}
	public void setProduceTime(Date produceTime) {
		this.produceTime = produceTime;
	}

	/**
	 * @param receiveTime the receiveTime to set
	 */
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	/**
	 * @return the receiveTime
	 */
	public Date getReceiveTime() {
		return receiveTime;
	}

	public Date getConsumeTime() {
		return consumeTime;
	}
	public void setConsumeTime(Date consumeTime) {
		this.consumeTime = consumeTime;
	}


	public String getProduceIP() {
		return produceIP;
	}
	public void setProduceIP(String produceIP) {
		this.produceIP = produceIP;
	}
	public long getProducePid() {
		return producePid;
	}
	public void setProducePid(long producePid) {
		this.producePid = producePid;
	}
	public long getProduceTid() {
		return produceTid;
	}
	public void setProduceTid(long produceTid) {
		this.produceTid = produceTid;
	}
	public String getConsumerIP() {
		return cosumerIP;
	}
	public void setConsumerIP(String cosumerIP) {
		this.cosumerIP = cosumerIP;
	}
	public long getConsumerPid() {
		return consumerPid;
	}
	public void setConsumerPid(long ccosumerPid) {
		this.consumerPid = ccosumerPid;
	}
	public long getConsumerTid() {
		return cosumerTid;
	}
	public void setConsumerTid(long cosumerTid) {
		this.cosumerTid = cosumerTid;
	}

	public long getReceiveUseTime() {
		return consumeTime==null || produceTime==null ? -1 :  receiveTime.getTime() - produceTime.getTime();
	}

	public long getConsumeUseTime() {
		return consumeTime==null || produceTime==null ? -1 : consumeTime.getTime() - produceTime.getTime();
	}

	@Override
	public boolean equals(Object obj) {
		//TODO:待实现
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


}

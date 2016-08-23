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
package com.zhaimi.koper.event;

import com.zhaimi.koper.MsgBean;

import java.util.List;

/**
 * DefaultDataEvent
 *
 * @author kk hekun@zhai.me
 * @since 1.0
 * 2016年2月19日
 */
public class DefaultDataEvent implements DataEvent {

    private String topic;
    private String dataObjectName;
    private String eventName;
    private MsgBean<String, String> msgBean;

    private List<?> args;
    private Object result;
    private String exception;


    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String getDataObjectName() {
        return dataObjectName;
    }

    @Override
    public void setDataObjectName(String dataObjectName) {
        this.dataObjectName = dataObjectName;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public MsgBean<String, String> getMsgBean() {
        return msgBean;
    }

    @Override
    public void setMsgBean(MsgBean<String, String> msgBean) {
        this.msgBean = msgBean;
    }

    @Override
    public List<?> getArgs() {
        return args;
    }

    @Override
    public void setArgs(List<?> args) {
        this.args = args;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String getException() {
        return exception;
    }

    @Override
    public void setException(String exception) {
        this.exception = exception;
    }
}

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
 * DataEvent.
 * 数据事件。封装Event和数据消息内容。
 *
 * @author kk hekun@zhai.me
 * @since 1.0
 * 2016年2月19日
 */
public interface DataEvent {

    void setException(String exception);

    String getException();

    void setResult(Object result);

    Object getResult();

    void setArgs(List<?> args);

    List<?> getArgs();

    void setMsgBean(MsgBean<String, String> msgBean);

    MsgBean<String, String> getMsgBean();

    void setEventName(String eventName);

    String getEventName();

    void setDataObjectName(String dataObjectName);

    String getDataObjectName();

    void setTopic(String topic);

    String getTopic();

}

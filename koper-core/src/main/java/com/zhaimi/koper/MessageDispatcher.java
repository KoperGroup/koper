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

/**
 * @author kk
 * @author caie
 * @since 1.2
 */
public interface MessageDispatcher {

    /**
     * 设置监听注册表
     *
     * @param listenerRegistry 监听注册表
     */
    void setListenerRegistry(ListenerRegistry listenerRegistry);

    /**
     * 设置消息中心
     *
     * @param messageCenter 消息中心
     */
    void setMessageCenter(MessageCenter messageCenter);

    /**
     * 启动消息分发器
     */
    void start();

    /**
     * 调用消息处理器
     *
     * @param msgBean 消息Bean
     */
    void invokeMessageHandler(MsgBean<String, String> msgBean);
}

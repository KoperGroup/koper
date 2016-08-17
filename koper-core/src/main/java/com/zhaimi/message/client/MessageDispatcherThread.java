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
package com.zhaimi.message.client;

import com.zhaimi.message.ListenerRegistry;
import com.zhaimi.message.MessageCenter;
import com.zhaimi.message.MessageDispatcher;

/**
 * 消息分发器线程
 *
 * @author kk
 * @author caie
 * @since 1.2
 */
public class MessageDispatcherThread implements Runnable {
    private MessageCenter messageCenter;
    private ListenerRegistry listenerRegistry;
    private MessageDispatcher messageDispatcher;

    public MessageDispatcherThread(MessageDispatcher messageDispatcher, MessageCenter messageCenter, ListenerRegistry listenerRegistry) {
        this.messageDispatcher = messageDispatcher;
        this.messageCenter = messageCenter;
        this.listenerRegistry = listenerRegistry;
    }

    @Override
    public void run() {
        messageDispatcher.setMessageCenter(messageCenter);
        messageDispatcher.setListenerRegistry(listenerRegistry);
        messageDispatcher.start();
    }


}

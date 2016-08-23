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
package com.zhaimi.koper.client;

import com.zhaimi.koper.ListenerRegistry;
import com.zhaimi.koper.MessageCenter;

import java.util.Properties;

/**
 * ConsumerThread. 每个topic由一个线程接收。
 *
 * @author kk
 * @since 1.0
 */
public class MessageReceiverThread implements Runnable {
    private Properties properties;
    private MessageReceiver messageReceiver;
    private MessageCenter messageCenter;
    private ListenerRegistry listenerRegistry;
    private int partitions;

    public MessageReceiverThread( Properties properties,  MessageReceiver messageReceiver, MessageCenter messageCenter ,ListenerRegistry listenerRegistry,int partitions) {
        this.properties = properties;
        this.messageReceiver = messageReceiver;
        this.messageCenter= messageCenter;
        this.listenerRegistry = listenerRegistry;
        this.partitions = partitions;
    }

    @Override
    public void run() {
        messageReceiver.setProperties(this.properties);
        messageReceiver.setMessageCenter(this.messageCenter);
        messageReceiver.setListenerRegistry(listenerRegistry);
        messageReceiver.setPartitions(partitions);
        messageReceiver.init();
        messageReceiver.start();
    }
}

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
package com.zhaimi.message.router;

import java.util.concurrent.BlockingQueue;

/**
 * this class is used to dispatch Message for {@Link PullMessageCenter}
 * you can customize Router's action by implements this interface and
 * change <router></router> config in context-data-consumer.xml
 *
 * @author wds
 * @since 1.2
 */
public interface Router {

    /**
     * register dispatcher thread 's mail box here, mq receiver will dispatch {@Link MsgBean} to mail box
     * @param thread which thread's main box
     * @param mailBox dispatcher thread 's local cache
     */
    void registerThreadMailBox(Runnable thread,BlockingQueue mailBox);

    /**
     * dispatch {@Link MsgBean} to mail box
     * @param msgBean msg
     */
    void dispatch(Object msgBean) throws InterruptedException;

    /**
     * call it when thread is interrupted
     * @param thread
     */
    void deRegister(Runnable thread);
}

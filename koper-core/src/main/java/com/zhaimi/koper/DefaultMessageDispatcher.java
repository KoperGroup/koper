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

import com.zhaimi.koper.convert.ConverterCenter;
import com.zhaimi.koper.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author kk
 * @author caie
 * @since 1.2
 */
public class DefaultMessageDispatcher implements MessageDispatcher {

    private static Logger log = LoggerFactory.getLogger(DefaultMessageDispatcher.class);

    private ListenerRegistry listenerRegistry;

    private MessageCenter messageCenter;

    private BlockingQueue<MsgBean> mailBox;

    @Override
    public void setListenerRegistry(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
    }

    public ListenerRegistry getListenerRegistry() {
        return listenerRegistry;
    }

    public MessageCenter getMessageCenter() {
        return messageCenter;
    }

    @Override
    public void setMessageCenter(MessageCenter messageCenter) {
        this.messageCenter = messageCenter;
    }

    public BlockingQueue<MsgBean> getMailBox() {
        return mailBox;
    }

    /**
     * Loop for receive message and dispatcher to listener by topic
     */
    @Override
    public void start() {
        mailBox = new LinkedBlockingQueue<>(1);
        //register mail box to Router
        messageCenter.getRouter().registerThreadMailBox(Thread.currentThread(), mailBox);
        //start dispatcher
        while (true) {
            //on message of topic
            MsgBean<String, String> msgBean;
            try {
                msgBean = mailBox.take();
            } catch (Exception e) {
                //deRegister this thread in MessageCenter
                messageCenter.getRouter().deRegister(Thread.currentThread());
                break;
            }
            if (msgBean != null) {
                setConsumeInfo(msgBean);
                if (log.isInfoEnabled())
                    log.info("消费一条消息:" + msgBean);

                invokeMessageHandler(msgBean);

            } //TODO. edit to:   else then sleep1(1);
            sleep1(10);
        }
    }


    /**
     * 调用消息处理器
     *
     * @param msgBean 消息Bean
     */
    @Override
    public void invokeMessageHandler(MsgBean<String, String> msgBean) {
        if (log.isDebugEnabled())
            log.debug("取到一条消息:" + msgBean);
        String topic = msgBean.getTopic(); //get topic by msg
        Object listener = listenerRegistry.getListenerMap().get(topic);

        //trigger Event
        if (listener == null) {
            log.info("已订阅Topic,但对应的监听器未找到,topic={}", topic);
            return;
        }

        triggerMessageListener(msgBean, listener);
    }

    protected void triggerMessageListener(MsgBean<String, String> msgBean, Object listener) {
        if (listener instanceof MsgBeanListener) {
            MsgBeanListener msgBeanListener = (MsgBeanListener) listener;
            msgBeanListener.onMsgBean(msgBean);
            invokeOnMessage(msgBean, listener);
        }

    }

    private void invokeOnMessage(MsgBean<String, String> msgBean, Object listener) {
        // 1. 获取方法(根据 valueType)
        final Optional<Method> methodOptional = ReflectUtil.getMethod(listener.getClass(), "onMessage",
                method -> method.getParameters().length == 1
                        && method.getParameterTypes()[0].getName().equals(msgBean.getValueType()));
        if (methodOptional.isPresent()) {
            final Method method = methodOptional.get();
            // 2. 构造方法入参
            final String value = msgBean.getValue();
            final String valueType = msgBean.getValueType();

            final Object parameter = ConverterCenter.convertValue(ReflectUtil.getClass(valueType), value);
            Object[] parameters = new Object[1];
            parameters[0] = parameter;
            // 3. 调用方法
            ReflectUtil.invoke(listener, method, parameters);
        } else {
            log.error("failed to find the method(onMessage) which in listener : [{}]", listener);
        }
    }

    protected void setConsumeInfo(MsgBean<String, String> msgBean) {
        msgBean.setConsumeTime(new Date());
        msgBean.setConsumerTid(Thread.currentThread().getId());
    }

    /**
     * 休眠等待
     *
     * @param sleepTime
     */
    private void sleep1(int sleepTime) {
        long timeSleepNanos = sleepTime;
        long lastTime = System.nanoTime();

        while (true) {
            if (timeSleepNanos <= 0)
                break;
            else {
                long now = System.nanoTime();
                timeSleepNanos -= (now - lastTime);
                lastTime = now;
            }
        }
    }

}

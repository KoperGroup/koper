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
package com.zhaimi.message.event;

import com.alibaba.fastjson.JSON;
import com.zhaimi.message.DefaultMessageDispatcher;
import com.zhaimi.message.MsgBean;
import com.zhaimi.message.MsgBeanListener;
import com.zhaimi.message.convert.ConverterCenter;
import com.zhaimi.message.util.ReflectUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 数据事件消息转发器
 *
 * @author caie
 * @since 1.2
 */
public class DataEventMessageDispatcher extends DefaultMessageDispatcher {

    private static Logger log = LoggerFactory.getLogger(DataEventMessageDispatcher.class);

    @Override
    protected void triggerMessageListener(MsgBean<String, String> msgBean, Object listener) {
        try {
            // 调用底层的onMessageBean事件方法，传递底层的msgBean对象
            if (listener instanceof MsgBeanListener) {
                MsgBeanListener msgBeanListener = (MsgBeanListener) listener;
                super.triggerMessageListener(msgBean, msgBeanListener);
            } else {
                triggerDataEventMethod(listener, msgBean);
            }

        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param listener
     */
    private void triggerDataEventMethod(Object listener, MsgBean<String, String> msgBean) {

        Class<?> clazz = listener.getClass();
        DataListener dataListener = clazz.getAnnotation(DataListener.class);
        String dataObject = dataListener.dataObject();

        triggerDataEvent(listener, dataObject, msgBean);
    }

    public void triggerDataEvent(Object listener, String dataObject, MsgBean<String, String> msgBean) {
        if (log.isDebugEnabled())
            log.debug("AbstractDataEventListener收到数据消息: " + msgBean);

        Class<?> clazz = listener.getClass();

        DataEvent dataEvent = JSON.parseObject(msgBean.getValue(), DefaultDataEvent.class);
        // args : [{"xxx" : xxx, "xxx" : xxx}]
        dataEvent.setTopic(msgBean.getTopic());
        dataEvent.setDataObjectName(dataObject);
        String event = msgBean.getTopic();
        String eventName = getEventName(event);

        dataEvent.setEventName(eventName);
        dataEvent.setMsgBean(msgBean);

        try {

            if (log.isDebugEnabled())
                log.debug("查找DataEvent事件处理方法:{}", event);


            final Optional<Method> methodOptional = ReflectUtil.getMethod(clazz, eventName,
                    method -> method.getParameters().length == dataEvent.getArgs().size()
                            || method.getParameters().length == dataEvent.getArgs().size() + 1
                            || method.getParameters().length == 1);
            if (methodOptional.isPresent()) {
                final Method method = methodOptional.get();
                Object[] methodParameters = getDataEventMethodParameters(method, dataEvent);
                ReflectUtil.invoke(listener, method, methodParameters);
            } else {
                log.warn("Message received! But no event handle method defined {}(DataEvent dataEvent) Message is {}",
                        event, msgBean.getValue());
            }
        } catch (Exception e) {
            log.warn("Fail to call event handle method  {}(DataEvent dataEvent). Message is {}. Exception:{}", event, msgBean.getValue(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得方法调用时的参数 (参数绑定)
     *
     * @param method    具体事件方法
     * @param dataEvent 事件上下文信息
     * @return 该方法的所有参数(返回是一个Object[])
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private Object[] getDataEventMethodParameters(Method method, DataEvent dataEvent)
            throws InstantiationException, IllegalAccessException {

        final List<?> args = dataEvent.getArgs();
        final int length = method.getParameters().length;

        Object[] parameters = new Object[length];
        for (int i = 0; i < length; i++) {
            final Parameter parameter = method.getParameters()[i];

            final Class<?> parameterTypeClass = parameter.getType();

            Object o1;
            // 如果角标大小等于args的大小, 说明最后一位是 DataEvent
            if (i == args.size()) {
                o1 = dataEvent;
            } else {
                Object o = args.get(i);

                if (parameterTypeClass.getName().equals("com.zhaimi.message.event.DataEvent")) {
                    o1 = dataEvent;
                } else {
                    o1 = ConverterCenter.convertValue(parameterTypeClass, o);
                }
            }
            parameters[i] = o1;
        }
        return parameters;
    }

    /**
     * 根据Topic转换为事件的名字, 一般的规则为 onXXX.
     * 例如:
     * 将 com.zhaimi.message.demo.trading.mapper.impl.OrderMapperImpl.insertOrder
     * 转换成事件名 则 为 onInsertOrder
     *
     * @param event topic字符串
     * @return 方法名
     */
    private String getEventName(String event) {
        event = StringUtils.substring(event, event.lastIndexOf('.') + 1);

        event = StringUtils.substring(event, 0, 1).toUpperCase() + StringUtils.substring(event, 1);

        event = "on" + event;
        return event;
    }

}

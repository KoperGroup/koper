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
package koper.event;

import koper.MessageListenerBeanPostProcessor;
import koper.MsgBeanListener;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * DataEventListenerPostProcessor
 *
 * @author kk hekun@zhai.me
 * @since 1.0
 * 2016年2月19日
 */
public class DataEventListenerPostProcessor extends MessageListenerBeanPostProcessor {

    /**
     * bean初始化后注册DataEvent数据时间监听器
     *
     * @see MessageListenerBeanPostProcessor#postProcessAfterInitialization(Object, String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object bean0 = bean;
        if (bean instanceof MsgBeanListener) {
            if (bean0 instanceof DataEventListener) {
                registerInterfaceDataEventListener((DataEventListener) bean0);
            } else {
                super.postProcessAfterInitialization(bean0, beanName);
            }

        } else {
            DataListener listenOnAnnotation = bean.getClass().getAnnotation(DataListener.class);
            if (listenOnAnnotation != null) {
                registerAnnotationDataEventListener(bean0, listenOnAnnotation);
            }
        }
        return bean0;
    }

    /**
     * 注册DataEventListener。被注册的对象实现了 DataEventListener 接口
     *
     * @param dataEventListener
     */
    protected void registerInterfaceDataEventListener(DataEventListener dataEventListener) {

        String dataObjectName = dataEventListener.dataObjectName();
        registerDataEventListener(dataEventListener, dataObjectName);
    }

    /**
     * 注册DataEventListener。被注册的对象添加了 DataListener 标注。
     *
     * @param bean0
     */
    protected void registerAnnotationDataEventListener(Object dataEventListener, DataListener listenOnAnnotation) {
        String dataObjectName = listenOnAnnotation.dataObject();

        registerDataEventListener(dataEventListener, dataObjectName);
    }


    /**
     * @param bean0
     */
    protected void registerDataEventListener(Object dataEventListener, String dataObjectName) {

        if (StringUtils.isBlank(dataObjectName)) {
            throw new IllegalArgumentException("dataObjectName can't be blank of dataEventListener class! Use @DataListener or implements DataEventListener interface." + dataEventListener.getClass());
        }
        Class<?> clazz = dataEventListener.getClass();

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!isExcludedMethod(method)) {
                if (method.getName().startsWith("on")) {
                    registerDataEventListener(dataEventListener, dataObjectName, method);
                }
            }
        }
    }

    /**
     * 根据Listener的数据对象名和方法名构造topic, 并向Listener注册中心注册。
     * 方法名: 例如: insertOrder_B, onCompoleteOrder, compoleteOrder, completeOrder_X
     *
     * @param listener
     * @param method
     */
    private void registerDataEventListener(Object listener, String dataObjectName, Method method) {
        String methodName = method.getName();

        String topic = null;

        String eventName = methodName;
        if (methodName.startsWith("on")) {
            eventName = StringUtils.substring(methodName, 2);
            eventName = String.valueOf(eventName.charAt(0)).toLowerCase() + eventName.substring(1);
        }

        topic = dataObjectName + "." + eventName;
        //注册
        registerListener(listener, topic);
    }

    /**
     * 排除这些方法
     */
    private static Map<String, Object> excludeMethods = new HashMap();

    static {
        excludeMethods.put("wait", null);
        excludeMethods.put("notify", null);
        excludeMethods.put("notifyAll", null);
        excludeMethods.put("clone", null);
        excludeMethods.put("toString", null);
        excludeMethods.put("equals", null);
        excludeMethods.put("hashCode", null);
        excludeMethods.put("getClass", null);
        excludeMethods.put("onMsgBean", null);
    }

    /**
     * @param method
     * @return
     */
    private boolean isExcludedMethod(Method method) {

        return excludeMethods.containsKey(method.getName());
    }

}

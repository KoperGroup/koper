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
package koper;

import koper.client.ConsumerLauncher;
import koper.util.ReflectUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * MessageListenerBeanPostProcessor
 *
 * @author kk
 * @since 1.0
 */
public class MessageListenerBeanPostProcessor implements BeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(MessageListenerBeanPostProcessor.class);

    @Autowired
    private ConsumerLauncher consumerLauncher;


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Find and register message listeners.
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof MsgBeanListener) {
            log.info("*** MsgBeanListener found:" + bean);
            registerMsgBeanListener((MsgBeanListener) bean);
        }
        return bean;
    }

    /**
     * 注册消息监听器
     *
     * @param listener
     */
    protected void registerMsgBeanListener(MsgBeanListener listener) {

        Class<?> clazz = listener.getClass();
        Method method;
        String topic = null;
        Listen listen;
        method = getMethod(clazz, "onMsgBean");
        listen = method == null ? null : method.getAnnotation(Listen.class);

        if (listen == null) {
            // 先拿类上的 Listen 注解, 没有再拿方法上的Listen
            final Listen clazzAnnotation = ReflectUtil.getListenAnnotation(clazz);
            if (clazzAnnotation == null) {
                method = getMethod(clazz, "onMessage");
                listen = method == null ? null : method.getAnnotation(Listen.class);
            } else {
                listen = clazzAnnotation;
            }
        }

        if (listen == null) {
            log.info("Tip>>> Topic not specified! @Listen annotation not declared on event method 'onMessage or onMsgBean' of bean " + listener);
        } else {
            topic = listen.topic();
        }

        registerListener(listener, topic);
    }

    /**
     * registerListener to registry.
     *
     * @param listener
     * @param topic
     */
    protected void registerListener(Object listener, String topic) {
        if (StringUtils.isNotBlank(topic)) {
            consumerLauncher.getListenerRegistry().register(topic, listener);
            log.info("注册监听器 Register listener '{}' on topic '{}'", listener, topic);
        }
    }

    /**
     * 获取第一个方法
     *
     * @param clazz
     * @param methodName
     * @return
     */
    protected Method getMethod(Class<?> clazz, String methodName) {

        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz); //clazz.getDeclaredMethods();
        Method findMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)
                    && method.getAnnotations().length != 0) {
                findMethod = method;
            }
        }
        return findMethod;
    }
}

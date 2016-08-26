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
package koper.util;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import koper.Listen;
import org.springframework.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * ReflectUtil
 *
 * @author Raymond He, raymondhekk9527@gmail.com
 * @author caie
 * @since 1.2
 * 2016年8月5日
 */
public class ReflectUtil {

    public void add(int abc, int xyz, String yyy) {

    }

    private static Map<String, Class<?>> classMap = new HashMap<>();

    /**
     * 获取类上的Listen注解
     *
     * @param clazz Class类
     * @return Listen注解
     */
    public static Listen getListenAnnotation(Class<?> clazz) {
        return clazz.getAnnotation(Listen.class);
    }

    /**
     * Get method arg names.
     *
     * @param clazz
     * @param methodName
     * @return
     */
    public static <T> String[] getMethodArgNames(Class<T> clazz, String methodName) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass cc = pool.get(clazz.getName());

            CtMethod cm = cc.getDeclaredMethod(methodName);

            // 使用javaassist的反射方法获取方法的参数名
            MethodInfo methodInfo = cm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr == null) {
                throw new RuntimeException("LocalVariableAttribute of method is null! Class " + clazz.getName() + ",method name is " + methodName);
            }
            String[] paramNames = new String[cm.getParameterTypes().length];
            int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++)
                paramNames[i] = attr.variableName(i + pos);

            return paramNames;
        } catch (NotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Class<?> getClass(String className) {
        return classMap.computeIfAbsent(className, clazzName -> {
            try {
                return Class.forName(clazzName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void invoke(Object targetObject, Method method, Object[] objects) {
        final Class<?> clazz = targetObject.getClass();
        final FastClass fastClass = FastClass.create(clazz);
        try {
            fastClass.getMethod(method).invoke(targetObject, objects);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<Method> getMethod(Class<?> clazz, String eventName, Predicate<Method> methodPredicate) {
        final Method[] methods = clazz.getDeclaredMethods();

        return Arrays.stream(methods)
                .filter(method -> method.getName().equals(eventName))
                .filter(methodPredicate)
                .findAny();
    }

    public static void main(String[] args) throws NotFoundException {

        String[] paramNames = ReflectUtil.getMethodArgNames(ReflectUtil.class, "add");
        // paramNames即参数名
        for (int i = 0; i < paramNames.length; i++) {
            System.out.println(paramNames[i]);
        }
    }

}

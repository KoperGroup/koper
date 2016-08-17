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
package com.zhaimi.message.convert;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author caie
 * @since 1.2
 */
public class ConverterCenter {

    private static Map<String, Converter> converterMap = ImmutableMap.<String, Converter>builder()
            .put("java.lang.Long", new LongConverter())
            .put("java.lang.Double", new DoubleConverter())
            .put("java.lang.Character", new CharacterConverter())
            .put("java.lang.Float", new FloatConverter())
            .put("java.lang.Short", new ShortConverter())
            .put("java.lang.Byte", new ByteConverter())
            .put("java.math.BigDecimal", new BigDecimalConverter())
            .put("java.lang.Integer", new IntegerConverter())
            .build();

    public static Converter getConverter(String clazzName) {
        return converterMap.get(clazzName);
    }

    /**
     * 根据类型匹配转换每一个调用时需要的参数.
     *
     * @param parameterTypeClass 方法形参类型
     * @param obj                需要转换的值(经过JSON转换后的值)
     * @return 转换后的值
     */
    public static Object convertValue(Class<?> parameterTypeClass, Object obj) {

        if (null == obj)
            return obj;

        if (obj.getClass() != parameterTypeClass) {
            Object convertValue;
            final Converter converter = getConverter(parameterTypeClass.getName());
            if (converter != null) {
                // primitive type or wrapper
                convertValue = converter.convert(obj);
            } else {
                // pojo
                if (obj.toString().contains("{")) {
                    convertValue = JSON.parseObject(obj.toString(), parameterTypeClass);
                } else {
                    convertValue = obj;
                }
            }
            return convertValue;
        } else {
            return obj;
        }
    }

}

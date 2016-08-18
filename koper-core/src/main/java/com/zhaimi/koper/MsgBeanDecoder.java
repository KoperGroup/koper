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

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;

import kafka.serializer.Decoder;

/**
 * @author kk
 * @since 1.0
 *
 */
public class MsgBeanDecoder implements Decoder<MsgBean<String,String>> {
	
	@Override
	public MsgBean<String,String> fromBytes(byte[] bytes) {
		
		String json;
		try {
			json = new String(bytes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Fail to decode msgbean " + e.getMessage() ,e);
		}
		@SuppressWarnings("unchecked")
		MsgBean<String,String> msgBean = JSON.parseObject( json,MsgBean.class );
		return msgBean;
	}
}

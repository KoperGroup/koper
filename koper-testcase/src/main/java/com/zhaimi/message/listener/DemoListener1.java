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
package com.zhaimi.message.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhaimi.message.AbstractMessageListener;
import com.zhaimi.message.Listen;
import com.zhaimi.message.demo.trading.mapper.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author kk
 * @since 1.0
 */
@Component
public class DemoListener1 extends AbstractMessageListener {

    private static Logger log = LoggerFactory.getLogger(DemoListener1.class);

    @Listen(topic = "com.zhaimi.message.demo.trading.mapper.impl.OrderMapperImpl.updateOrder")
    public void onMessage(String msg) {

        log.info("DemoListener1接收到updateOrder 消息，message={}", msg);
        JSONObject jsonObject = JSON.parseObject(msg);
        //args是数组类型
        JSONArray argArray = jsonObject.getJSONArray("args");
        //result 是 String类型
        String result = jsonObject.getString("result");

        //第一个参数是Order类型
        Order order = JSON.parseObject(argArray.getString(0), Order.class);
        log.info("order " + order.getOrderId());

        log.info("执行结果 " + result);
    }

}

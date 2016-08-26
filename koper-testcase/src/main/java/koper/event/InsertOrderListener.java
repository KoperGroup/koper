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

import com.alibaba.fastjson.JSON;
import koper.demo.trading.mapper.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * InsertOrderListener. 订单日志监听器
 *
 * @author kk raymondhekk9527@gmail.com
 * @since 1.0
 * 2016年3月21日
 */
@Component
@DataListener(dataObject = "com.zhaimi.koper.demo.trading.mapper.impl.OrderMapperImpl")
public class InsertOrderListener {

    private static Logger log = LoggerFactory.getLogger(InsertOrderListener.class);

    public static AtomicInteger counter = new AtomicInteger();

    public static long start;
    public static long end;

    public ReentrantLock lock = new ReentrantLock();
    public Condition condition = lock.newCondition();

    /**
     * onInsertOrder事件响应处理方法
     * 从event中获取数据：输入参数args，返回值 result。
     * 数据格式为JSON。使用 fastjson工具类 进行解析
     *
     * @param event
     */
    public void onInsertOrder(DataEvent event) {

        if (start == 0)
            start = new Date().getTime();
        List<?> args = event.getArgs();
        //数据解析：需要根据生产接口定义的参数个数，数据类型进行解析
        //根据定义，第一个参数类型为 Order 类型
        Order order = JSON.parseObject(args.get(0).toString(), Order.class);

        long receiveUseTime = event.getMsgBean().getReceiveUseTime() / 1000;
        long consumeUseTime = event.getMsgBean().getConsumeUseTime() / 1000;
        int total = counter.incrementAndGet();

        long ellapse = (new Date().getTime() - start);
        long avg = ellapse / total;

        log.info("getProduceTime:{}, getReceiveTime:{}", event.getMsgBean().getProduceTime().getTime(), event.getMsgBean().getReceiveTime().getTime());
        log.info("onInsertOrder order {}, receiveUseTime:{}, consumeUseTime{}, totalCount:{},ellapse:{},avg:{}", order.getOrderId(), receiveUseTime, consumeUseTime, total, ellapse, avg);

        sleep(2000);
    }


    private void sleep(int s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

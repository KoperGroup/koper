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
package koper.demo.dataevent.listener;

import koper.MsgBean;
import koper.demo.dataevent.entity.Order;
import koper.event.DataEvent;
import koper.event.DataListener;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author caie
 * @since 1.2
 */
@Component
@DataListener(dataObject = "koper.demo.dataevent.mapper.impl.OrderMapperImpl")
public class OrderListener {
    private static int WRITE_PERIOD = 10000;

    private static AtomicLong msgCount = new AtomicLong(0);
    private static AtomicLong lastTs = new AtomicLong(0);
    private static AtomicLong sumReceiveTime = new AtomicLong(0);
    private static AtomicLong sumConsumeTime = new AtomicLong(0);

    private static final File file = new File(String.format("OrderListenerStatistic_%d.txt", System.currentTimeMillis()));

    public void onInsertOrder(Order order, DataEvent dataEvent) {
        // 用写入文件来模拟真实的消息消费
        File output = new File("output.txt");
        try {
            FileUtils.writeStringToFile(output,
                    "\norderNo : " + order.getOrderNo() + ", create time : " + order.getCreatedTime()
                    , true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 统计信息
        MsgBean<String, String> msgBean = dataEvent.getMsgBean();
        long receive = msgBean.getReceiveUseTime();
        long consume = msgBean.getConsumeUseTime();
        sumReceiveTime.getAndAdd(receive);
        sumConsumeTime.getAndAdd(consume - receive);

        long cnt = msgCount.incrementAndGet();
        if (cnt % WRITE_PERIOD == 0) writeFile(file);
    }

    private void writeFile(File file) {
        try {
            long currTs = System.currentTimeMillis();
            long period = currTs - lastTs.getAndSet(currTs);
            long sumReceive = sumReceiveTime.getAndSet(0);
            long sumConsume = sumConsumeTime.getAndSet(0);
            String record = String.format("TimeStamp = %d, Count = %d, MillSeconds = %d, TPS = %.2f, AvgReceive = %d, AvgConsume = %d\n",
                    currTs,
                    msgCount.get(),
                    period,
                    WRITE_PERIOD / ((double) period / 1000),
                    sumReceive / WRITE_PERIOD,
                    sumConsume / WRITE_PERIOD);
            FileUtils.writeStringToFile(file, record, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeStatLine(int line) {
        if (line > 0) WRITE_PERIOD = line;
    }

    public void onUpdateOrder(Order order, DataEvent dataEvent) {
        System.out.println("orderNo : " + order.getOrderNo());
        System.out.println("create time :" + order.getCreatedTime());
    }

}


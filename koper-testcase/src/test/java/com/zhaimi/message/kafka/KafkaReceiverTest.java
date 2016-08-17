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
package com.zhaimi.message.kafka;

import com.zhaimi.message.client.ConsumerLauncher;
import junit.textui.TestRunner;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * @author kk
 * @since 1.0
 */
public class KafkaReceiverTest extends AbstractDependencyInjectionSpringContextTests {

    private ConsumerLauncher consumerLauncher;

    public void setConsumerLauncher(ConsumerLauncher consumerLauncher) {
        this.consumerLauncher = consumerLauncher;
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{"classpath:kafka/context-data-consumer.xml"};
    }

    @Test
    public void test() {
        // 配置了 autoStart,所以无需手动 start()
        //consumerLauncher.start();
    }

    /**
     * 启动Test类, consumerLauncher 会随Spring容器自动启动
     *
     * @param args
     */
    public static void main(String[] args) {
        TestRunner.run(KafkaReceiverTest.class);
    }
}

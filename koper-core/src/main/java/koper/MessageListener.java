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

/**
 * 消息监听器接口。
 * 实现该接口及onMessage(String)方法可以订阅并收取消息。消息格式为JSON. <br/>
 * {"args":参数列表,"result":结果列表,"exception":异常信息 } </br/>
 * 例如：<br/>
 * {"args":["388",{"orderId":"002","orderName":"鲜花订单002"}],"exception":"java.lang.NullPointerException: 更新订单失败。"}
 *
 * @author kk
 * @since 1.0
 *
 */
public interface MessageListener extends MsgBeanListener {

	public void onMessage(String msg);
}

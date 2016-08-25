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
package koper.event.binding;

import koper.demo.member.mapper.Member;
import koper.event.DataEvent;
import koper.event.DataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author caie
 * @since 1.2
 */
@Component
@DataListener(dataObject = "koper.demo.member.mapper.impl.MemberMapperImpl")
public class MemberListener {

    private Logger logger = LoggerFactory.getLogger(MemberListener.class);

    /**
     * test with Integer String type param
     */
    public void onInsertMember(Integer id, String name, String phone) {
        logger.info("on insert Member successful, id : {}, name : {}, phone : {}", id, name, phone);
    }

    /**
     * test with pojo and Integer type param
     */
    public void onCancelMember(Integer id, Member member) {
        logger.info("on cancel member successful, member info : {}, id : {}",
                member.toString(), id);
    }

    /**
     * test with DataEvent type param
     */
    public void onDeleteMember(DataEvent dataEvent) {
        logger.info("on delete member successful, dataObjectName : {}, eventName : {}" +
                        "args : {}",
                dataEvent.getDataObjectName(), dataEvent.getEventName(),
                dataEvent.getArgs());
    }

    /**
     * test with Double Long BigDecimal type param
     */
    public void onUpdateMember(Double id, Long name, BigDecimal account) {
        logger.info("on update member successful, id : {}, name : {}, account : {}", id, name, account);
    }

    /**
     * test with Float Short Byte Character type param
     */
    public void onInsertWithFloatAndShortAndByteAndChar(Float fl, Short s, Byte b, Character c) {
        logger.info("on insert with Float and Short and Byte and char successful, float : {}, short : {}, byte : {}, char : {}",
                fl, s, b, c);
    }

    /**
     * test with Integer String DataEvent type param(DataEvent is the last parameter)
     */
    public void onDeleteWithIntegerAndStringAndDataEvent(Integer id, String name, DataEvent dataEvent) {
        logger.info("on delete with id and name and DataEvent successful," +
                        "id : {}, name : {}, DataEvent : {}",
                id, name, dataEvent.toString());
    }
}

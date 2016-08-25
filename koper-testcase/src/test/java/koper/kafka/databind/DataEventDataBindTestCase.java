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
package koper.kafka.databind;

import koper.demo.member.mapper.Member;
import koper.demo.member.mapper.MemberMapper;
import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.math.BigDecimal;

/**
 * 对应的 Listener 为 MemberListener
 *
 * @author caie
 * @since 1.2
 * 2016年8月5日
 */
public class DataEventDataBindTestCase extends AbstractDependencyInjectionSpringContextTests {

    private MemberMapper memberMapper;

    public void setOrderMapper(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{"kafka/context-data-message.xml",
                "kafka/context-data-producer.xml"
        };
    }

    @Test
    public void testInsertMember() {

        final Integer id = 1;
        final String name = "test_name";
        final String phone = "15397300000";

        memberMapper.insertMember(id, name, phone);
    }

    @Test
    public void testCancelMember() {

        final Integer id = 1;
        Member member = new Member();
        member.setId(id);
        member.setName("member_test_name");
        member.setPhone("test_phone");

        memberMapper.cancelMember(id, member);
    }

    @Test
    public void testDeleteMember() {

        final Integer id = 1;
        final String name = "delete_test_name";

        memberMapper.deleteMember(id, name);
    }

    @Test
    public void testUpdateMember() {

        final Double id = 1D;
        final Long name = 123L;
        final BigDecimal account = new BigDecimal(20);

        memberMapper.updateMember(id, name, account);
    }

    @Test
    public void testInsertWithFloatAndShortAndByteAndChar() {

        final Float fl = 123F;
        final Short s = 1234;
        final Byte b = 1;
        final Character c = 'b';

        memberMapper.insertWithFloatAndShortAndByteAndChar(fl, s, b, c);
    }

    @Test
    public void testDeleteWithIntegerAndStringAndDataEvent() {

        final Integer id = 1;
        final String name = "test_name";

        memberMapper.deleteWithIntegerAndStringAndDataEvent(id, name);
    }

}

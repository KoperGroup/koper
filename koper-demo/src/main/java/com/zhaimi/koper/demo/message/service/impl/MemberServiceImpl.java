package com.zhaimi.koper.demo.message.service.impl;

import com.zhaimi.koper.demo.message.entity.Member;
import com.zhaimi.koper.demo.message.mapper.MemberMapper;
import com.zhaimi.koper.demo.message.service.MemberService;
import com.zhaimi.koper.sender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author caie
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    private static final String MEMBER_SIGNUP_TOPIC = "com.zhaimi.koper.demo.message.notifyMemberAfterSignup";

    @Autowired
    private MessageSender messageSender;

    public void signup(Member member) {
        memberMapper.createMember(member);
        messageSender.send(MEMBER_SIGNUP_TOPIC, "Signed up successfully! " + member.getPhoneNo());
    }
}

package koper.demo.message.service.impl;

import koper.demo.message.entity.Member;
import koper.demo.message.mapper.MemberMapper;
import koper.demo.message.service.MemberService;
import koper.sender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author caie
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    private static final String MEMBER_SIGNUP_TOPIC = "koper.demo.message.notifyMemberAfterSignup";

    @Autowired
    private MessageSender messageSender;

    public void signup(Member member) {
        memberMapper.createMember(member);
        messageSender.send(MEMBER_SIGNUP_TOPIC, "Signed up successfully! " + member.getPhoneNo());
    }
}

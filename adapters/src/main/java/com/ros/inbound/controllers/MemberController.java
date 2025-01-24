package com.ros.inbound.controllers;

import com.ros.dtos.AddMemberDTO;
import com.ros.exceptions.EmailAlreadyExistsException;
import com.ros.exceptions.MemberAlreadyExistsException;
import com.ros.exceptions.UsernameAlreadyExistsException;
import com.ros.ports_inbound.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(@Qualifier("memberServiceImpl") MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("members")
    public void addMember(@RequestBody AddMemberDTO addMemberDTO) throws MemberAlreadyExistsException, UsernameAlreadyExistsException, EmailAlreadyExistsException {
        memberService.add(addMemberDTO);
    }

}

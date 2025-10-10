package com.ros.lms.adapters.inbound.controllers;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.exceptions.*;
import com.ros.lms.ports.inbound.service_contracts.MemberService;
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
    public void addMember(@RequestBody AddMemberDTO addMemberDTO) throws StaffNotFoundException, MemberValidationException {
        memberService.add(addMemberDTO);
    }
}

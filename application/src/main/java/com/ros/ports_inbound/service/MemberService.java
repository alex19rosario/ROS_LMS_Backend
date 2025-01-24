package com.ros.ports_inbound.service;

import com.ros.dtos.AddMemberDTO;
import com.ros.exceptions.EmailAlreadyExistsException;
import com.ros.exceptions.MemberAlreadyExistsException;
import com.ros.exceptions.UsernameAlreadyExistsException;

public interface MemberService {
    void add(AddMemberDTO member) throws MemberAlreadyExistsException, UsernameAlreadyExistsException, EmailAlreadyExistsException;
}

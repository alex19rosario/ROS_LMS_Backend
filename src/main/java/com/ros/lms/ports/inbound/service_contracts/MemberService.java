package com.ros.lms.ports.inbound.service_contracts;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.exceptions.EmailAlreadyExistsException;
import com.ros.lms.domain.exceptions.MemberAlreadyExistsException;
import com.ros.lms.domain.exceptions.UsernameAlreadyExistsException;

public interface MemberService {
    void add(AddMemberDTO member) throws MemberAlreadyExistsException, UsernameAlreadyExistsException, EmailAlreadyExistsException;
}

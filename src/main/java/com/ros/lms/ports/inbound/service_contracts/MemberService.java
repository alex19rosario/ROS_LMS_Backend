package com.ros.lms.ports.inbound.service_contracts;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.exceptions.*;

public interface MemberService {
    void add(AddMemberDTO member) throws MemberValidationException, StaffNotFoundException;
}

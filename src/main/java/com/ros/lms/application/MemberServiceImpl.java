package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.entities.AuthorityType;
import com.ros.lms.domain.entities.User;
import com.ros.lms.domain.enums.MemberValidationStatus;
import com.ros.lms.domain.enums.RoleType;
import com.ros.lms.domain.exceptions.*;
import com.ros.lms.ports.inbound.service_contracts.MemberService;
import com.ros.lms.ports.outbound.repository_contracts.AuthorityTypeDAO;
import com.ros.lms.ports.outbound.repository_contracts.MemberDAO;
import com.ros.lms.ports.outbound.repository_contracts.StaffDAO;
import com.ros.lms.ports.outbound.repository_contracts.UserDAO;
import com.ros.lms.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberDAO memberDAO;
    private final UserDAO userDAO;
    private final StaffDAO staffDAO;
    private final AuthorityTypeDAO authorityTypeDAO;
    private final PasswordEncoder passwordEncoder;
    private final Mapper mapper;

    @Autowired
    public MemberServiceImpl(
            @Qualifier("memberDAOJpaImpl") MemberDAO memberDAO,
            @Qualifier("userDAOJpaImpl") UserDAO userDAO,
            @Qualifier("staffDAOJpaImpl") StaffDAO staffDAO,
            @Qualifier("authorityTypeDAOJpaImpl") AuthorityTypeDAO authorityTypeDAO,
            PasswordEncoder passwordEncoder,
            Mapper mapper
    ) {
        this.memberDAO = memberDAO;
        this.userDAO = userDAO;
        this.staffDAO = staffDAO;
        this.authorityTypeDAO = authorityTypeDAO;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }


    @Override
    @Transactional
    public void add(AddMemberDTO dto) throws MemberValidationException, StaffNotFoundException {

        Set<MemberValidationStatus> conflicts = memberDAO.validateMemberUniqueness(dto.governmentID(), dto.username(), dto.email());

        if (!conflicts.isEmpty()) {
            StringBuilder message = getErrorMessage(dto, conflicts);

            throw new MemberValidationException(message.toString().trim());
        }

        if (staffDAO.findByUsername(dto.staffUsername()).isEmpty()) {
            throw new StaffNotFoundException("Staff not found with username: " + dto.staffUsername());
        }

        AuthorityType memberRole = authorityTypeDAO.findByLabel(RoleType.MEMBER)
                .orElseThrow(() -> new IllegalStateException("Role MEMBER not found in DB"));

        User user = mapper.addMemberDtoToUser(dto, memberRole, passwordEncoder);
        userDAO.create(user);
        memberDAO.create(mapper.addMemberDtoToMember(dto, user));
    }

    private static StringBuilder getErrorMessage(AddMemberDTO dto, Set<MemberValidationStatus> conflicts) {
        StringBuilder message = new StringBuilder("Validation failed due to the following conflicts: ");

        if (conflicts.contains(MemberValidationStatus.GOVERNMENT_ID_EXISTS)) {
            message.append("[Government ID " + dto.governmentID() + " already exists] ");
        }
        if (conflicts.contains(MemberValidationStatus.USERNAME_EXISTS)) {
            message.append("[Username " + dto.username() + " already exists] ");
        }
        if (conflicts.contains(MemberValidationStatus.EMAIL_EXISTS)) {
            message.append("[Email " + dto.email() + " already exists] ");
        }
        return message;
    }
}

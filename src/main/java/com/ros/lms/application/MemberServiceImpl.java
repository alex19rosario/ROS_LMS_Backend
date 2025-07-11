package com.ros.lms.application;


import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.entities.Authority;
import com.ros.lms.domain.entities.AuthorityId;
import com.ros.lms.domain.entities.Member;
import com.ros.lms.domain.entities.User;
import com.ros.lms.domain.enums.Roles;
import com.ros.lms.domain.exceptions.EmailAlreadyExistsException;
import com.ros.lms.domain.exceptions.MemberAlreadyExistsException;
import com.ros.lms.domain.exceptions.UsernameAlreadyExistsException;
import com.ros.lms.ports.inbound.service_contracts.MemberService;
import com.ros.lms.ports.outbound.repository_contracts.MemberDAO;
import com.ros.lms.ports.outbound.repository_contracts.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.function.Function;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberDAO memberDAO;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public MemberServiceImpl(@Qualifier("memberDAOJpaImpl") MemberDAO memberDAO, @Qualifier("userDAOJpaImpl") UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.memberDAO = memberDAO;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void add(AddMemberDTO dto) throws MemberAlreadyExistsException, UsernameAlreadyExistsException, EmailAlreadyExistsException {
        if (memberDAO.findByGovernmentID(dto.governmentID()).isPresent()) {
            throw new MemberAlreadyExistsException("Member with government ID " + dto.governmentID() + " already exists");
        }
        if (memberDAO.findByUsername(dto.username()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username " + dto.username() + " already exists");
        }
        if (memberDAO.findByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email " + dto.email() + " already exists");
        }
        userDAO.create(userMapper(dto));
        memberDAO.create(memberMapper(dto));
    }

    private Member memberMapper(AddMemberDTO dto){
        String[] nameParts = dto.firstName().split(" ", 2);
        String firstName = nameParts[0];
        String middleName = nameParts.length > 1 ? nameParts[1] : null;

        Member member = new Member();
        member.setGovernmentID(dto.governmentID());
        member.setFirstName(firstName);
        member.setMiddleName(middleName);
        member.setLastName(dto.lastName());
        member.setPhone(dto.phone());
        member.setDateOfBirth(dto.dateOfBirth());
        member.setSex(dto.sex());
        member.setEmail(dto.email());
        member.setUsername(dto.username());
        return member;
    }

    private User userMapper(AddMemberDTO dto){
        User user = new User();
        user.setUsername(dto.username());
        String hashedPassword = passwordEncoder.encode(dto.password());
        user.setPassword(hashedPassword);
        user.setEnabled(true);
        Set<Authority> authorities = Set.of(authorityMapper.apply(dto));
        user.setAuthorities(authorities);
        return user;
    }

    private final Function<AddMemberDTO, Authority> authorityMapper = dto -> {
        Authority authority = new Authority();
        // Initialize the id field
        AuthorityId authorityId = new AuthorityId();
        authorityId.setUsername(dto.username());
        authorityId.setAuthority(Roles.MEMBER.str());

        authority.setId(authorityId); // Set the initialized id
        return authority;
    };
}

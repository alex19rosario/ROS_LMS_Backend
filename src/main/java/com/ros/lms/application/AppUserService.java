package com.ros.lms.application;


import com.ros.lms.domain.entities.User;
import com.ros.lms.ports.outbound.repository_contracts.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppUserService implements UserDetailsService {

    private final UserDAO userDAO;

    @Autowired
    public AppUserService(@Qualifier("userDAOJpaImpl")UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userDAO.findByUsername(username);
        if(userOptional.isPresent()){
            User user = userOptional.get();

            // Map authorities to the Spring Security format
            Set<GrantedAuthority> authorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getId().getAuthority()))
                    .collect(Collectors.toSet());

            // Return UserDetails with roles
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword()) // Ensure this matches the password encoder
                    .authorities(authorities)
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(user.getEnabled() == 'N')
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}

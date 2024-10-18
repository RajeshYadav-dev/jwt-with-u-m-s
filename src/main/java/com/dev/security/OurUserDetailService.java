package com.dev.security;

import com.dev.entity.OurUser;
import com.dev.repository.OurUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OurUserDetailService implements UserDetailsService {
    @Autowired
    private OurUserRepository ourUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ourUserRepository.findByUsername(username).orElseThrow();
    }
}

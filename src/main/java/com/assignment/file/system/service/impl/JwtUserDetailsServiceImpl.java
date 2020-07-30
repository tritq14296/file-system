package com.assignment.file.system.service.impl;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.model.UserModel;
import com.assignment.file.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserModel userModel = userRepository.getUserModelByUserName(userName);
        if (userModel != null) {
            UserContext.setUserDid(userModel.getUserId());
            UserContext.setUserName(userModel.getUserName());
            return new User(userModel.getUserName(), userModel.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username:" + userName);
        }
    }
}

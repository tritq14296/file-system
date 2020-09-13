package com.assignment.file.system.service.impl;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.model.UserLogModel;
import com.assignment.file.system.model.UserModel;
import com.assignment.file.system.repository.UserLogRepository;
import com.assignment.file.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLogRepository userLogRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserModel userModel = userRepository.getUserModelByUserName(userName);
        if (userModel != null) {
            UserContext.setUserDid(userModel.getUserId());
            UserContext.setUserName(userModel.getUserName());

            Timestamp startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
            Timestamp endDate = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());
            UserLogModel userLogModel = userLogRepository.getLogTimeCurrentDay(userModel.getUserId(), startDate, endDate);

            if (userLogModel == null) {
                //save login time
                userLogModel = new UserLogModel();
                userLogModel.setUserModel(userModel);
                userLogModel.setUserLoginTime(Timestamp.valueOf(LocalDateTime.now()));
                userLogRepository.save(userLogModel);
            }

            return new User(userModel.getUserName(), userModel.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username:" + userName);
        }
    }
}

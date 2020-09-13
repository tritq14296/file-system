package com.assignment.file.system.service.impl;

import com.assignment.file.system.model.UserLogModel;
import com.assignment.file.system.model.UserModel;
import com.assignment.file.system.repository.UserLogRepository;
import com.assignment.file.system.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.Timestamp;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtUserDetailsServiceImplTest {
    @InjectMocks
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLogRepository userLogRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUserName() {
        UserModel userModel = new UserModel();
        userModel.setPassword("123");
        userModel.setUserName("tritq1");
        userModel.setUserId(1);

        when(userRepository.getUserModelByUserName(userModel.getUserName())).thenReturn(userModel);

        Timestamp startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());
        UserLogModel userLogModel = new UserLogModel();
        userLogModel.setUserLogId(1);
        userLogModel.setUserLogoutTime(endDate);
        userLogModel.setUserLoginTime(startDate);
        userLogModel.setUserModel(userModel);

        when(userLogRepository.save(userLogModel)).thenReturn(userLogModel);

        UserDetails userDetailsResponse = jwtUserDetailsService.loadUserByUsername(userModel.getUserName());

        Assert.assertEquals(userModel.getUserName(), userDetailsResponse.getUsername());
        Assert.assertEquals(userModel.getPassword(), userDetailsResponse.getPassword());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUserNameWithUserNameNotExist() {
        UserModel userModel = new UserModel();
        userModel.setUserName("tritq1");

        when(userRepository.getUserModelByUserName(userModel.getUserName())).thenReturn(null);

        jwtUserDetailsService.loadUserByUsername(userModel.getUserName());
    }
}

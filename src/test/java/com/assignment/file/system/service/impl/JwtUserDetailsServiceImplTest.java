package com.assignment.file.system.service.impl;

import com.assignment.file.system.model.UserModel;
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

import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtUserDetailsServiceImplTest {
    @InjectMocks
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadUserByUserName() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setPassword("123");
        userModel.setUserName("tritq1");
        userModel.setUserId(1);

        when(userRepository.getUserModelByUserName(userModel.getUserName())).thenReturn(userModel);

        UserDetails userDetailsResponse = jwtUserDetailsService.loadUserByUsername(userModel.getUserName());

        Assert.assertEquals(userModel.getUserName(), userDetailsResponse.getUsername());
        Assert.assertEquals(userModel.getPassword(), userDetailsResponse.getPassword());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUserNameWithUserNameNotExist() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setUserName("tritq1");

        when(userRepository.getUserModelByUserName(userModel.getUserName())).thenReturn(null);

        jwtUserDetailsService.loadUserByUsername(userModel.getUserName());
    }
}

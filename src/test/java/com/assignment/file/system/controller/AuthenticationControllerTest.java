package com.assignment.file.system.controller;

import com.assignment.file.system.domain.UserLoginRequestDto;
import com.assignment.file.system.service.impl.JwtUserDetailsServiceImpl;
import com.assignment.file.system.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    private MockMvc mockMvc;
    private String authenUrl = "/api/v1/login";
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void testAuthenticateSuccessfully() throws Exception {
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setUserName("tritq1");
        userLoginRequestDto.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        UserDetails userDetails = new User(userLoginRequestDto.getUserName(), userLoginRequestDto.getPassword(), new ArrayList<>());

        when(jwtUserDetailsService.loadUserByUsername(userLoginRequestDto.getUserName()))
                .thenReturn(userDetails);

        when(jwtTokenUtils.generateToken(userDetails)).thenReturn("asjdfgksdakgkvcasdfjajefakmsfas");

        mockMvc.perform(post(authenUrl).contentType(contentType).content(mapper.writeValueAsString(userLoginRequestDto))).andExpect(status().isOk());
    }
}

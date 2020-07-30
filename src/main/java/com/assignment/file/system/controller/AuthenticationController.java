package com.assignment.file.system.controller;

import com.assignment.file.system.domain.UserLoginRequestDto;
import com.assignment.file.system.domain.UserLoginResponseDto;
import com.assignment.file.system.service.impl.JwtUserDetailsServiceImpl;
import com.assignment.file.system.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class AuthenticationController {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    public ResponseEntity authenUser(@RequestBody UserLoginRequestDto loginRequestDto) {
        authenticate(loginRequestDto.getUserName(), loginRequestDto.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getUserName());
        final String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.ok(new UserLoginResponseDto(token));
    }

    private void authenticate(String userName, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
    }
}

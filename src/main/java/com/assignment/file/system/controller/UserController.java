package com.assignment.file.system.controller;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.domain.MembersDto;
import com.assignment.file.system.domain.UserDetailResponseDto;
import com.assignment.file.system.domain.UserLogResponseDto;
import com.assignment.file.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/details")
    public ResponseEntity<?> getUserDetailProfile() {
        UserDetailResponseDto userDetailResponse = userService.getUserDetailProfile(UserContext.getUserDid());
        if (userDetailResponse != null) {
            return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/logs/{user-id}")
    public ResponseEntity<?> getUserLogTimeCurrentDay(@PathVariable("user-id") int userId) {
        UserLogResponseDto userLogResponseDto = userService.getUserLogTimeCurrentDay(userId);
        if (userLogResponseDto != null) {
            return new ResponseEntity<>(userLogResponseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/members")
    public ResponseEntity<?> getMembers(Pageable pageable) {
        MembersDto membersDto = userService.getAllMemberByAdmin(pageable);
        return new ResponseEntity<>(membersDto, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        userService.logoutUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

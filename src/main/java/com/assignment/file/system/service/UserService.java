package com.assignment.file.system.service;

import com.assignment.file.system.domain.MembersDto;
import com.assignment.file.system.domain.UserDetailResponseDto;
import com.assignment.file.system.domain.UserLogResponseDto;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDetailResponseDto getUserDetailProfile(int userId);
    UserLogResponseDto getUserLogTimeCurrentDay(int userId);
    MembersDto getAllMemberByAdmin(Pageable pageable);
    void logoutUser();
}

package com.assignment.file.system.service.impl;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.domain.MemberDto;
import com.assignment.file.system.domain.MembersDto;
import com.assignment.file.system.domain.UserDetailResponseDto;
import com.assignment.file.system.domain.UserLogResponseDto;
import com.assignment.file.system.model.UserLogModel;
import com.assignment.file.system.model.UserModel;
import com.assignment.file.system.model.UserRoleModel;
import com.assignment.file.system.repository.UserLogRepository;
import com.assignment.file.system.repository.UserRepository;
import com.assignment.file.system.repository.UserRoleRepository;
import com.assignment.file.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserLogRepository userLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetailResponseDto getUserDetailProfile(int userId) {
        UserRoleModel userRole = userRoleRepository.findByUserId(userId);
        UserDetailResponseDto userDetailResponse = null;
        if (userRole != null) {
            userDetailResponse = new UserDetailResponseDto();
            userDetailResponse.setUserId(UserContext.getUserDid());
            userDetailResponse.setUserName(UserContext.getUserName());
            userDetailResponse.setUserRole(userRole.getUserRole());
        }

        return userDetailResponse;
    }

    @Override
    public UserLogResponseDto getUserLogTimeCurrentDay(int userId) {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());
        UserLogModel userLog = userLogRepository.getLogTimeCurrentDay(userId, startDate, endDate);
        UserLogResponseDto userLogResponseDto = null;

        if (userLog != null) {
            userLogResponseDto = new UserLogResponseDto();
            userLogResponseDto.setUserId(userId);
            userLogResponseDto.setUserLoginTime(userLog.getUserLoginTime());
            userLogResponseDto.setUserLogoutTime(userLog.getUserLogoutTime());
        }

        return userLogResponseDto;
    }

    @Override
    public MembersDto getAllMemberByAdmin(Pageable pageable) {
        int total = userRepository.countAllMember();

        List<UserModel> memberList = userRepository.getAllMember(pageable);
        MembersDto membersDto = new MembersDto();
        List<MemberDto> memberDtos = new ArrayList<>();
        for (UserModel user: memberList) {
            MemberDto memberDto = new MemberDto();
            memberDto.setUserId(user.getUserId());
            memberDto.setUserName(user.getUserName());
            memberDtos.add(memberDto);
        }
        membersDto.setMembers(memberDtos);
        membersDto.setTotal(total);
        return membersDto;
    }

    @Override
    public void logoutUser() {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());
        UserLogModel userLogModel = userLogRepository.getLogTimeCurrentDay(UserContext.getUserDid(), startDate, endDate);
        if (userLogModel != null) {
            userLogModel.setUserLogoutTime(Timestamp.valueOf(LocalDateTime.now()));
            userLogRepository.save(userLogModel);
        }
    }
}

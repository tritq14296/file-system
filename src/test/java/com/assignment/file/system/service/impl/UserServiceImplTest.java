package com.assignment.file.system.service.impl;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.domain.MembersDto;
import com.assignment.file.system.domain.UserDetailResponseDto;
import com.assignment.file.system.domain.UserLogResponseDto;
import com.assignment.file.system.model.UserLogModel;
import com.assignment.file.system.model.UserModel;
import com.assignment.file.system.model.UserRoleModel;
import com.assignment.file.system.repository.UserLogRepository;
import com.assignment.file.system.repository.UserRepository;
import com.assignment.file.system.repository.UserRoleRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserLogRepository userLogRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        UserContext.setUserName("tritq1");
        UserContext.setUserDid(1);
    }

    @Test
    public void testGetUserDetailProfile() {
        UserRoleModel userRoleModel = new UserRoleModel();
        userRoleModel.setUserId(1);
        userRoleModel.setUserRole("ADMIN");

        when(userRoleRepository.findByUserId(1)).thenReturn(userRoleModel);

        UserDetailResponseDto detailResponseDto = userService.getUserDetailProfile(1);

        Assert.assertEquals("tritq1", detailResponseDto.getUserName());
    }

    @Test
    public void testGetUserDetailProfileNull() {

        when(userRoleRepository.findByUserId(1)).thenReturn(null);

        UserDetailResponseDto detailResponseDto = userService.getUserDetailProfile(1);

        Assert.assertNull(detailResponseDto);
    }

    @Test
    public void testGetLogTime() {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());
        UserLogModel userLogModel = new UserLogModel();
        userLogModel.setUserLogId(1);
        userLogModel.setUserLogoutTime(endDate);
        userLogModel.setUserLoginTime(startDate);

        when(userLogRepository.getLogTimeCurrentDay(1, startDate, endDate)).thenReturn(userLogModel);

        UserLogResponseDto userLogResponseDto = userService.getUserLogTimeCurrentDay(1);

        Assert.assertNotNull(userLogResponseDto);
    }

    @Test
    public void testGetLogTimeNotExisted() {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());

        when(userLogRepository.getLogTimeCurrentDay(1, startDate, endDate)).thenReturn(null);

        UserLogResponseDto userLogResponseDto = userService.getUserLogTimeCurrentDay(1);

        Assert.assertNull(userLogResponseDto);
    }

    @Test
    public void testGetAllMember() {
        when(userRepository.countAllMember()).thenReturn(1);
        UserModel userModel = new UserModel();
        userModel.setUserId(1);
        userModel.setUserName("tritq1");
        Pageable pageable = PageRequest.of(0, 5);
        when(userRepository.getAllMember(pageable)).thenReturn(Collections.singletonList(userModel));

        MembersDto membersDto = userService.getAllMemberByAdmin(pageable);
        Assert.assertNotNull(membersDto);
        Assert.assertEquals(1, membersDto.getTotal());
    }

    @Test
    public void testLogoutUser() {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());
        UserLogModel userLogModel = new UserLogModel();
        userLogModel.setUserLogId(1);
        userLogModel.setUserLogoutTime(endDate);
        userLogModel.setUserLoginTime(startDate);
        when(userLogRepository.getLogTimeCurrentDay(1, startDate, endDate)).thenReturn(userLogModel);
        userService.logoutUser();
    }

    @Test
    public void testLogoutUserWithLogTimeNotExisted() {
        Timestamp startDate = Timestamp.valueOf(LocalDate.now().atStartOfDay());
        Timestamp endDate = Timestamp.valueOf(LocalDate.now().plusDays(1).atStartOfDay());
        when(userLogRepository.getLogTimeCurrentDay(1, startDate, endDate)).thenReturn(null);
        userService.logoutUser();
    }
}

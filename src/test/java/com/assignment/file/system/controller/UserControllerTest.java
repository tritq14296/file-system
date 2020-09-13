package com.assignment.file.system.controller;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.domain.MembersDto;
import com.assignment.file.system.domain.UserDetailResponseDto;
import com.assignment.file.system.domain.UserLogResponseDto;
import com.assignment.file.system.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
        UserContext.setUserDid(1);
        UserContext.setUserName("tritq1");
    }

    @Test
    public void testGetUserProfile() throws Exception {
        String apiUrl = "/api/v1/users/details";

        UserDetailResponseDto userDetailResponseDto = new UserDetailResponseDto();
        userDetailResponseDto.setUserId(UserContext.getUserDid());
        userDetailResponseDto.setUserName(UserContext.getUserName());
        userDetailResponseDto.setUserRole("MEMBER");
        when(userService.getUserDetailProfile(anyInt())).thenReturn(userDetailResponseDto);

        mockMvc.perform(get(apiUrl).contentType("application/json")).andExpect(status().isOk());
    }

    @Test
    public void testGetUserProfileNoContent() throws Exception {
        String apiUrl = "/api/v1/users/details";

        when(userService.getUserDetailProfile(anyInt())).thenReturn(null);

        mockMvc.perform(get(apiUrl).contentType("application/json")).andExpect(status().isNoContent());
    }

    @Test
    public void testGetUserLogTimeCurrentDay() throws Exception {
        String apiUrl = "/api/v1/users/logs/1";

        UserLogResponseDto logResponseDto = new UserLogResponseDto();
        logResponseDto.setUserId(1);

        when(userService.getUserLogTimeCurrentDay(anyInt())).thenReturn(logResponseDto);

        mockMvc.perform(get(apiUrl).contentType("application/json")).andExpect(status().isOk());
    }

    @Test
    public void testGetUserLogTimeCurrentDayNoContent() throws Exception {
        String apiUrl = "/api/v1/users/logs/1";

        when(userService.getUserLogTimeCurrentDay(anyInt())).thenReturn(null);

        mockMvc.perform(get(apiUrl).contentType("application/json")).andExpect(status().isNoContent());
    }

    @Test
    public void testGetListMember() throws Exception {
        String apiUrl = "/api/v1/users/members?page=0&size=5";
        MembersDto membersDto = new MembersDto();
        Pageable pageable = PageRequest.of(0, 5);
        when(userService.getAllMemberByAdmin(pageable)).thenReturn(membersDto);

        mockMvc.perform(get(apiUrl).contentType("application/json")).andExpect(status().isOk());
    }

    @Test
    public void testLogout() throws Exception {
        String apiUrl = "/api/v1/users/logout";
        doNothing().when(userService).logoutUser();

        mockMvc.perform(get(apiUrl).contentType("application/json")).andExpect(status().isOk());
    }
}

package com.assignment.file.system.domain;

import java.sql.Timestamp;

public class UserLogResponseDto {
    private int userId;
    private Timestamp userLoginTime;
    private Timestamp userLogoutTime;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getUserLoginTime() {
        return userLoginTime;
    }

    public void setUserLoginTime(Timestamp userLoginTime) {
        this.userLoginTime = userLoginTime;
    }

    public Timestamp getUserLogoutTime() {
        return userLogoutTime;
    }

    public void setUserLogoutTime(Timestamp userLogoutTime) {
        this.userLogoutTime = userLogoutTime;
    }
}

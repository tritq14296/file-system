package com.assignment.file.system.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_log")
public class UserLogModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_LOG_SEQ")
    @SequenceGenerator(sequenceName = "USER_LOG_SEQ", allocationSize = 1, name = "USER_LOG_SEQ")
    @Column(name = "user_log_id")
    private int userLogId;

    @Column(name = "user_log_in_time")
    private Timestamp userLoginTime;

    @Column(name = "user_log_out_time")
    private Timestamp userLogoutTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel userModel;

    public int getUserLogId() {
        return userLogId;
    }

    public void setUserLogId(int userLogId) {
        this.userLogId = userLogId;
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

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}

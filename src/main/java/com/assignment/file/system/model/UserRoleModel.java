package com.assignment.file.system.model;

import javax.persistence.*;

@Entity
@Table(name = "user_role")
public class UserRoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ROLE_SEQ")
    @SequenceGenerator(sequenceName = "USER_ROLE_SEQ", allocationSize = 1, name = "USER_ROLE_SEQ")
    @Column(name = "user_role_id")
    private int userId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel userModel;

    @Column(name = "user_role_name")
    private String userRole;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}

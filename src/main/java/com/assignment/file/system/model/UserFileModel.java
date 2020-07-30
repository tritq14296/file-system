package com.assignment.file.system.model;

import javax.persistence.*;

@Entity
@Table(name = "user_file")
public class UserFileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_FILE_SEQ")
    @SequenceGenerator(sequenceName = "USER_FILE_SEQ", allocationSize = 1, name = "USER_FILE_SEQ")
    @Column(name = "user_file_id")
    private int userFileId;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel userModel;

    public int getUserFileId() {
        return userFileId;
    }

    public void setUserFileId(int userFileId) {
        this.userFileId = userFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}

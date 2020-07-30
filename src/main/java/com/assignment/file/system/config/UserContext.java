package com.assignment.file.system.config;

public class UserContext {
    public static int userDid;
    public static String userName;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserContext.userName = userName;
    }

    public static int getUserDid() {
        return userDid;
    }

    public static void setUserDid(int userDid) {
        UserContext.userDid = userDid;
    }
}

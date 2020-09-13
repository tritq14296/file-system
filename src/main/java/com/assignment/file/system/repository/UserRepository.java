package com.assignment.file.system.repository;

import com.assignment.file.system.model.UserModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel getUserModelByUserName(String userName);

    @Query("SELECT u FROM UserModel u, UserRoleModel ur WHERE u.userId = ur.userModel.userId AND ur.userRole = 'MEMBER'")
    List<UserModel> getAllMember(Pageable pageable);

    @Query("SELECT count(u) FROM UserModel  u, UserRoleModel  ur WHERE u.userId = ur.userModel.userId AND ur.userRole = 'MEMBER'")
    int countAllMember();
}

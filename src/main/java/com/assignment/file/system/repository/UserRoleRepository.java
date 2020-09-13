package com.assignment.file.system.repository;

import com.assignment.file.system.model.UserRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRoleModel, Integer> {
    @Query("SELECT u FROM UserRoleModel u WHERE u.userModel.userId = :userId")
    UserRoleModel findByUserId(@Param("userId") int userId);
}

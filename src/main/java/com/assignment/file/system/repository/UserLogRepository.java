package com.assignment.file.system.repository;

import com.assignment.file.system.model.UserLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface UserLogRepository extends JpaRepository<UserLogModel, Integer> {
    @Query("SELECT ul FROM UserLogModel ul " +
            " WHERE ul.userModel.userId = :userId AND ul.userLoginTime >= :startDate AND ul.userLoginTime <= :endDate")
    UserLogModel getLogTimeCurrentDay(@Param("userId") int userId,
                                      @Param("startDate") Timestamp startDate,
                                      @Param("endDate") Timestamp endDate);
}

package com.assignment.file.system.repository;

import com.assignment.file.system.model.UserFileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserFileRepository extends JpaRepository<UserFileModel, Long> {
    @Query("SELECT uf FROM UserFileModel uf WHERE uf.fileName = :fileName and uf.userModel.userId = :userId")
    UserFileModel getUserFileModelByFileNameAndUserId(String fileName, int userId);
}

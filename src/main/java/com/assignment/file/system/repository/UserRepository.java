package com.assignment.file.system.repository;

import com.assignment.file.system.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel getUserModelByUserName(String userName);
}

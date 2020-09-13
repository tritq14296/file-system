package com.assignment.file.system.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ResponseEntity storeFile(MultipartFile file);
    Resource downloadFile(int userId, String userName) throws Exception;
}

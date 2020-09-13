package com.assignment.file.system.service.impl;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.domain.FileCommonResponseDto;
import com.assignment.file.system.model.UserFileModel;
import com.assignment.file.system.model.UserModel;
import com.assignment.file.system.repository.UserFileRepository;
import com.assignment.file.system.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static com.assignment.file.system.constants.Constants.*;

@Service
public class FileServiceImpl implements FileService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserFileRepository userFileRepository;

    /**
     * Function store file to directory on server and insert file name into database.
     * @param file
     * @return
     */
    @Override
    public ResponseEntity storeFile(MultipartFile file) {
        String storeDirectory = System.getProperty(USER_HOME) + uploadDir + File.separator + UserContext.getUserName();
        FileCommonResponseDto fileCommonResponseDto = new FileCommonResponseDto();
        try{
            Files.createDirectories(Paths.get(storeDirectory));
            Path fileUpload = Paths.get(storeDirectory).resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), fileUpload, StandardCopyOption.REPLACE_EXISTING);

            //set response
            fileCommonResponseDto.setStatus(true);
            fileCommonResponseDto.setMessage("Upload successfully with file name: " + Objects.requireNonNull(file.getOriginalFilename()));

            UserFileModel userFileModel = userFileRepository
                    .getUserFileModelByFileNameAndUserId(Objects.requireNonNull(file.getOriginalFilename()), UserContext.getUserDid());
            //file name is existed in DB -> return success
            if (userFileModel != null) {
                return new ResponseEntity(fileCommonResponseDto, HttpStatus.OK);
            }

            //get user details
            UserModel userModel = new UserModel();
            userModel.setUserId(UserContext.getUserDid());

            //store file name to database if file is not existed.
            userFileModel = new UserFileModel();
            userFileModel.setFileName(Objects.requireNonNull(file.getOriginalFilename()));
            userFileModel.setUserModel(userModel);
            userFileRepository.save(userFileModel);

            return new ResponseEntity(fileCommonResponseDto, HttpStatus.OK);
        } catch (IOException ex) {
            fileCommonResponseDto.setStatus(false);
            fileCommonResponseDto.setMessage("Failed upload with file name: " + file.getName());

            return new ResponseEntity(fileCommonResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Function get file from service with file name and user id, if file not exist, throw exception.
     * @param userId
     * @return resource
     * @throws Exception
     */
    @Override
    public Resource downloadFile(int userId, String userName) throws Exception {
        String storeDirectory = System.getProperty(USER_HOME) + uploadDir + File.separator + userName;

        UserFileModel userFileModel = userFileRepository.getUserFileModelByUserId(userId);

        if (userFileModel == null) {
            throw new FileNotFoundException(FILE_NOT_FOUND);
        }

        String fileUri = storeDirectory + File.separator + userFileModel.getFileName();

        try {
            Resource resource = new UrlResource(Paths.get(fileUri).toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException(FILE_NOT_FOUND + fileUri);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException(FILE_NOT_FOUND + fileUri);
        }
    }
}

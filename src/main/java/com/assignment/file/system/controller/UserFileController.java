package com.assignment.file.system.controller;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.domain.FileCommonResponseDto;
import com.assignment.file.system.service.impl.FileServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static com.assignment.file.system.constants.Constants.FILE_NOT_FOUND;

@RestController
@RequestMapping(value = "/api/v1/files")
public class UserFileController {
    @Autowired
    private FileServiceImpl fileService;

    @PostMapping
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.storeFile(file);
    }

    @GetMapping(value = "/{user-id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable("user-id") int userId,
                                                 @RequestParam("file-name") String fileName,
                                                 HttpServletRequest request) {
        FileCommonResponseDto fileCommonResponseDto = new FileCommonResponseDto();

        if (userId != UserContext.getUserDid()) {
            fileCommonResponseDto.setMessage("You do not have permission to download file " + fileName);
            fileCommonResponseDto.setStatus(false);
            return new ResponseEntity(fileCommonResponseDto, HttpStatus.FORBIDDEN);
        }

        if (StringUtils.isNotBlank(fileName)) {
            try {
                Resource resource = fileService.downloadFile(userId, fileName);
                String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

                if (contentType == null) {
                    contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }

                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } catch (Exception e) {
                fileCommonResponseDto.setMessage(FILE_NOT_FOUND + fileName);
                fileCommonResponseDto.setStatus(false);
                return new ResponseEntity(fileCommonResponseDto, HttpStatus.BAD_REQUEST);
            }
        } else {
            fileCommonResponseDto.setMessage("Missing file name");
            fileCommonResponseDto.setStatus(false);
            return new ResponseEntity(fileCommonResponseDto, HttpStatus.BAD_REQUEST);
        }
    }
}

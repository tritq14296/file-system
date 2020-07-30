package com.assignment.file.system.controller;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.domain.FileCommonResponseDto;
import com.assignment.file.system.service.impl.FileServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserFileControllerTest {
    @InjectMocks
    private UserFileController userFileController;

    @Mock
    private FileServiceImpl fileService;

    private MockMvc mockMvc;

    private String uploadFileUrl, downloadFileUrl, uploadDir, storeDirectory, fileName;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userFileController).build();
        UserContext.setUserDid(1);
        UserContext.setUserName("tritq1");

        downloadFileUrl = "/api/v1/files/1/download?file-name=test.txt";
        uploadFileUrl = "/api/v1/files";
        uploadDir = "/upload";
        fileName = "test.txt";

        storeDirectory = System.getProperty("user.home") + uploadDir + File.separator + UserContext.getUserName();

        File file = new File(storeDirectory + File.separator + fileName);

        try {
            Files.createFile(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @After
    public void deleteFile() {
        File file = new File(storeDirectory + File.separator + fileName);
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUploadFileSuccessfully() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName, "text/plain", "test data".getBytes());

        when(fileService.storeFile(mockMultipartFile)).thenReturn(new ResponseEntity(HttpStatus.OK));

        mockMvc.perform(multipart(uploadFileUrl).file(mockMultipartFile)).andExpect(status().isOk());
    }

    @Test
    public void testUploadFileWithUploadException() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName, "text/plain", "test data".getBytes());

        FileCommonResponseDto fileCommonResponseDto = new FileCommonResponseDto();
        fileCommonResponseDto.setStatus(false);
        fileCommonResponseDto.setMessage("Failed upload with file name: " + fileName);

        when(fileService.storeFile(mockMultipartFile)).thenReturn(new ResponseEntity(fileCommonResponseDto, HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(multipart(uploadFileUrl).file(mockMultipartFile)).andExpect(status().is5xxServerError());
    }

    @Test
    public void testDownloadFileSuccessfully() throws Exception {
        String storeDirectory = System.getProperty("user.home") + "/upload" + File.separator + UserContext.getUserName();
        String fileUri = storeDirectory + File.separator + fileName;

        Resource resource = new UrlResource(Paths.get(fileUri).toUri());

        when(fileService.downloadFile(1, fileName)).thenReturn(resource);

        mockMvc.perform(get(downloadFileUrl).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testDownloadFileWithForbidden() throws Exception {
        String forbiddenUrl = "/api/v1/files/2/download?file-name=test.txt";

        mockMvc.perform(get(forbiddenUrl).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
    }

    @Test
    public void testDowloadFileWithFileNameMissing() throws Exception {
        String missingFileNameUrl = "/api/v1/files/1/download?file-name=";

        mockMvc.perform(get(missingFileNameUrl).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    public void testDownloadFileWithFileNotFoundException() throws Exception {

        when(fileService.downloadFile(1, fileName)).thenThrow(FileNotFoundException.class);

        mockMvc.perform(get(downloadFileUrl).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}

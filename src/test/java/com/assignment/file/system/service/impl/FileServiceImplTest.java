package com.assignment.file.system.service.impl;

import com.assignment.file.system.config.UserContext;
import com.assignment.file.system.model.UserFileModel;
import com.assignment.file.system.repository.UserFileRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FileServiceImplTest {
    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private UserFileRepository userFileRepository;

    private String uploadDir = "/upload";
    private String storeDirectory;
    private String fileName = "test.txt";
    private String userName = "tritq1";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(fileService, "uploadDir", uploadDir);
        UserContext.setUserName(userName);
        UserContext.setUserDid(1);
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
    public void testStoreFileSuccessfully() {
        File file = new File(storeDirectory + File.separator + fileName);
        file.delete();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", fileName, "text/plain", "test data".getBytes());

        when(userFileRepository.getUserFileModelByFileNameAndUserId(anyString(), anyInt())).thenReturn(null);

        UserFileModel userFileModel = new UserFileModel();
        userFileModel.setFileName(fileName);

        when(userFileRepository.save(userFileModel)).thenReturn(userFileModel);

        ResponseEntity uploadResponse = fileService.storeFile(mockMultipartFile);
        Assert.assertEquals(HttpStatus.OK.value(), uploadResponse.getStatusCode().value());
    }

    @Test
    public void testStoreFileWithFileExistInDatabase() {
        File file = new File(storeDirectory + File.separator + fileName);
        file.delete();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", fileName, "text/plain", "test data".getBytes());

        UserFileModel userFileModel = new UserFileModel();
        userFileModel.setFileName(fileName);

        when(userFileRepository.getUserFileModelByFileNameAndUserId(anyString(), anyInt())).thenReturn(userFileModel);

        ResponseEntity uploadResponse = fileService.storeFile(mockMultipartFile);
        Assert.assertEquals(HttpStatus.OK.value(), uploadResponse.getStatusCode().value());
    }

    @Test
    public void testStoreFileWithException() {
        String fileName = null;
        File file = new File(storeDirectory + File.separator + fileName);
        file.delete();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", fileName, "text/plain", "test data".getBytes());

        ResponseEntity uploadResponse = fileService.storeFile(mockMultipartFile);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), uploadResponse.getStatusCode().value());
    }

    @Test
    public void testDownloadFileSuccessfully() throws Exception {
        UserFileModel userFileModel = new UserFileModel();
        userFileModel.setFileName(fileName);

        when(userFileRepository.getUserFileModelByUserId(anyInt())).thenReturn(userFileModel);

        Resource responseResource = fileService.downloadFile(1, userName);

        Assert.assertNotNull(responseResource);
    }

    @Test(expected = FileNotFoundException.class)
    public void testDownloadFileWithFileNameNotExistInDatabase() throws Exception {

        when(userFileRepository.getUserFileModelByUserId(anyInt())).thenReturn(null);

        fileService.downloadFile(1, userName);
    }

    @Test(expected = FileNotFoundException.class)
    public void testDownloadFileNotExistInServer() throws Exception {
        UserFileModel userFileModel = new UserFileModel();
        userFileModel.setFileName("test1.txt");

        when(userFileRepository.getUserFileModelByUserId(anyInt())).thenReturn(userFileModel);

        fileService.downloadFile(1, userName);

    }
}

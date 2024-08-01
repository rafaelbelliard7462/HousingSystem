package com.example.individualproject.business.impl;


import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ImageServiceUseCaseImplTest {
    @Mock
    private Storage storage;

    @InjectMocks
    private ImageServiceUseCaseImpl imageService;

    @Test
     void testUpload()  {

        lenient().when(storage.create(any(BlobInfo.class), any(byte[].class))).thenReturn(null);


        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "MockMultipartFile".getBytes());

        Map<String, String> result = imageService.upload(mockMultipartFile);

        assertEquals("https://firebasestorage.googleapis.com/v0/b/housingsystem.appspot.com",
                result.get("url").substring(0, 69));
    }


}
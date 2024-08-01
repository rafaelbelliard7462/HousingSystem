package com.example.individualproject.business;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImageServiceUseCase {
    Map<String, String> upload(MultipartFile multipartFile);
    boolean deleteImage(String fileName);
}

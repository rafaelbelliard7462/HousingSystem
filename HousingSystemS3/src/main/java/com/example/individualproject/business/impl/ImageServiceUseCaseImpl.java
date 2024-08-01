package com.example.individualproject.business.impl;

import com.example.individualproject.business.ImageServiceUseCase;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor

public class ImageServiceUseCaseImpl implements ImageServiceUseCase {
    private static final String BUCKET_NAME = "housingsystem.appspot.com";
    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        String contentType = Files.probeContentType(file.toPath());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        InputStream inputStream = ImageServiceUseCaseImpl.class.getClassLoader().getResourceAsStream("housingSystem.json");
        assert inputStream != null;
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        String downloadURL = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";
        return String.format(downloadURL, BUCKET_NAME, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    @Override
    public Map<String, String> upload(MultipartFile multipartFile) {
        Map<String, String> result = new HashMap<>();

        try {
            String originalFileName = multipartFile.getOriginalFilename();
            assert originalFileName != null;

            String fileName = UUID.randomUUID().toString().concat(this.getExtension(originalFileName));
            File file = this.convertToFile(multipartFile, fileName);

            String url = this.uploadFile(file, fileName);
            if (file.delete()) {
                result.put("url", url);
                result.put("filename", fileName);
            } else {
                result.put("error", "Image uploaded, but failed to delete the temporary file.");
            }
        } catch (Exception e) {
            result.put("error", "Image couldn't upload, Something went wrong");
        }

        return result;
    }
    @Override
    public boolean deleteImage(String fileName) {
        try {
            // Specify the BlobId for the file to be deleted
            BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
            InputStream inputStream = ImageServiceUseCaseImpl.class.getClassLoader().getResourceAsStream("housingSystem.json");
            assert inputStream != null;
            Credentials credentials = GoogleCredentials.fromStream(inputStream);

            // Create Storage service with the loaded credentials
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();


            return storage.delete(blobId);
        } catch (Exception e) {
            return false;
        }
    }

}

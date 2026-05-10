package com.example.kimeduardfinalproject.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class KimEduardFileStorageService {

    private final Path uploadPath;

    public KimEduardFileStorageService(@Value("${app.upload.dir:uploads}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
    }

    public String saveFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            String contentType = file.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Only image files are allowed");
            }

            Files.createDirectories(uploadPath);

            String originalFileName = file.getOriginalFilename();
            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + extension;

            Path targetPath = uploadPath.resolve(fileName).normalize();

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Could not save file", e);
        }
    }

    public Resource loadFile(String fileName) {
        try {
            Path filePath = uploadPath.resolve(fileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not load file", e);
        }
    }

    public void deleteFile(String fileName) {
        try {
            if (fileName == null) {
                return;
            }

            Path filePath = uploadPath.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Could not delete file", e);
        }
    }
}
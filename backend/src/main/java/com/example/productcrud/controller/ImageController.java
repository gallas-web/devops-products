package com.example.productcrud.controller;

import com.example.productcrud.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(
        @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Fichier vide"));
        }
        
        String imageUrl = s3Service.uploadImage(file);
        return ResponseEntity.ok(Map.of("url", imageUrl));
    }
}

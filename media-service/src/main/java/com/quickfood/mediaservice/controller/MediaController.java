package com.quickfood.mediaservice.controller;

import com.quickfood.mediaservice.dto.common.ApiResponse;
import com.quickfood.mediaservice.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "general") String folder) {
        String imageUrl = mediaService.uploadImage(file, folder);
        return ResponseEntity.ok(ApiResponse.success("Image uploaded successfully", imageUrl));
    }
}
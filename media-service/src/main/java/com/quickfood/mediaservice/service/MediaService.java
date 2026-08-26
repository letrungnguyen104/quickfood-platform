package com.quickfood.mediaservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.quickfood.mediaservice.exception.BusinessException;
import com.quickfood.mediaservice.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_IS_EMPTY);
        }

        try {
            String publicId = UUID.randomUUID().toString();

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "public_id", publicId,
                            "resource_type", "image"
                    ));

            return uploadResult.get("secure_url").toString();
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new BusinessException(ErrorCode.MEDIA_UPLOAD_FAILED);
        }
    }
}
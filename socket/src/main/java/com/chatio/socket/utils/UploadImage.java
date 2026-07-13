package com.chatio.socket.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UploadImage {

    private final Cloudinary cloudinary;

    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new MaxUploadSizeExceededException(5 * 1024 * 1024);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        System.out.println("Upload result: " + uploadResult);
        return Map.of(
                "publicId", uploadResult.get("public_id"),
                "url", uploadResult.get("url"));
    }

}

package com.self.flipcart.service;

import com.self.flipcart.util.SimpleResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ResponseEntity<SimpleResponseStructure> addStoreImage(String storeId, MultipartFile image);
}

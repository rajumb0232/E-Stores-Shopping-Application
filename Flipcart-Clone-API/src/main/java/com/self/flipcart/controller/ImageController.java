package com.self.flipcart.controller;

import com.self.flipcart.model.Image;
import com.self.flipcart.service.ImageService;
import com.self.flipcart.util.SimpleResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/fkv1")
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class ImageController {

    private ImageService imageService;

    @PostMapping("/stores/{storeId}/images")
    private ResponseEntity<SimpleResponseStructure> addStoreImage(@PathVariable String storeId, MultipartFile image){
        Specification<Image> specs = Specification.where(null);
        return imageService.addStoreImage(storeId, image);
    }
}

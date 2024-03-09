package com.self.flipcart.serviceimpl;

import com.self.flipcart.enums.ImageType;
import com.self.flipcart.model.Image;
import com.self.flipcart.model.StoreImage;
import com.self.flipcart.repository.ImageRepo;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.service.ImageService;
import com.self.flipcart.util.SimpleResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private ImageRepo imageRepo;
    private StoreRepo storeRepo;
    private SimpleResponseStructure simpleResponseStructure;

    private MongoTemplate mongoTemplate;

    @Override
    public ResponseEntity<SimpleResponseStructure> addStoreImage(String storeId, MultipartFile image) {
        System.err.println(storeId);
       return storeRepo.findById(storeId).map(store -> {
           StoreImage storeImage = new StoreImage();
           storeImage.setStoreId(storeId);
           storeImage.setImageType(ImageType.LOGO);
           try {
               storeImage.setImageBytes(image.getBytes());
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
           imageRepo.save(storeImage);
           Pageable pageable = PageRequest.of(0, 10, Sort.by("price").ascending());
           Page<Image> page = imageRepo.findAll(pageable);
           List<Image> list = page.getContent();
           return new ResponseEntity<>(simpleResponseStructure.setStatus(HttpStatus.OK.value())
                   .setMessage("Successfully save image"), HttpStatus.OK);
       }).orElseThrow();
    }
}

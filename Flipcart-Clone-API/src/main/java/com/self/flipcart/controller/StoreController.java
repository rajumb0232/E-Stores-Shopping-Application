package com.self.flipcart.controller;

import com.self.flipcart.model.Store;
import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.responsedto.StoreResponse;
import com.self.flipcart.service.StoreService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fkv1")
@AllArgsConstructor
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class StoreController {

    private StoreService storeService;

    @PostMapping("/stores")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<ResponseStructure<Store>> setUpStore(@RequestBody StoreRequest storeRequest){
        System.err.println("prime category: "+storeRequest.getTopCategory());
        return storeService.setUpStore(storeRequest);
    }

    @PutMapping("/stores/{storeId}")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<ResponseStructure<Store>> updateStore(@RequestBody StoreRequest storeRequest, @PathVariable String storeId){
        System.err.println(storeRequest);
        return storeService.updateStore(storeRequest, storeId);
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ResponseStructure<StoreResponse>> getStore(@PathVariable String storeId){
        return storeService.getStore(storeId);
    }

    @GetMapping("/stores-exist")
    public ResponseEntity<Boolean> checkIfStoreExistBySeller(){
        return storeService.checkIfStoreExistBySeller();
    }

    @GetMapping("/stores")
    public ResponseEntity<ResponseStructure<Store>> getStoreBySeller(){
        return storeService.getStoreBySeller();
    }
}

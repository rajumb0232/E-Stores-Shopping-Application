package com.self.flipcart.controller;

import com.self.flipcart.model.Store;
import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.requestdto.StoreRequestComplete;
import com.self.flipcart.responsedto.StoreResponse;
import com.self.flipcart.service.StoreService;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.Structure;
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
    public ResponseEntity<ResponseStructure<StoreResponse>> setUpStore(@RequestBody StoreRequestComplete storeRequestComplete){
        System.err.println("prime category: "+storeRequestComplete.getPrimeCategory());
        return storeService.setUpStore(storeRequestComplete);
    }

    @PutMapping("/stores/{storeId}")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<ResponseStructure<StoreResponse>> updateStore(@RequestBody StoreRequest storeRequest, @PathVariable String storeId){
        return storeService.updateStore(storeRequest, storeId);
    }

    @GetMapping("/stores/{storeId}/display-type/{displayType}")
    public ResponseEntity<? extends Structure<? extends  StoreResponse>> getStore(@PathVariable String storeId, @PathVariable String displayType){
        return storeService.getStore(storeId, displayType);
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

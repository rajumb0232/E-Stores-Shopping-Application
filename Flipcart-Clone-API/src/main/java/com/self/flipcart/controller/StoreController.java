package com.self.flipcart.controller;

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
@PreAuthorize("hasAuthority('SELLER')")
public class StoreController {

    private StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<ResponseStructure<StoreResponse>> setUpStore(@RequestBody StoreRequestComplete storeRequestComplete){
        return storeService.setUpStore(storeRequestComplete);
    }

    @PutMapping("/stores/{storeId}")
    public ResponseEntity<ResponseStructure<StoreResponse>> updateStore(@RequestBody StoreRequest storeRequest, @PathVariable String storeId){
        return storeService.updateStore(storeRequest, storeId);
    }

    @GetMapping("/stores/{storeId}/display-type/{displayType}")
    public ResponseEntity<? extends Structure<? extends  StoreResponse>> getStore(@PathVariable String storeId, @PathVariable String displayType){
        return storeService.getStore(storeId, displayType);
    }
}

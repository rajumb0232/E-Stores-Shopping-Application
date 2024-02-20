package com.self.flipcart.controller;

import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.responsedto.StoreResponseComplete;
import com.self.flipcart.service.StoreService;
import com.self.flipcart.util.ResponseStructure;
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
    public ResponseEntity<ResponseStructure<StoreResponseComplete>> setUpStore(@RequestBody StoreRequest storeRequest){
        return storeService.setUpStore(storeRequest);
    }

    @PutMapping("/stores/{storeId}")
    public ResponseEntity<ResponseStructure<StoreResponseComplete>> updateStore(@RequestBody StoreRequest storeRequest, @PathVariable String storeId){
        return storeService.updateStore(storeRequest, storeId);
    }
}

package com.self.flipcart.service;

import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.responsedto.StoreResponseComplete;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

public interface StoreService {
    ResponseEntity<ResponseStructure<StoreResponseComplete>> setUpStore(StoreRequest storeRequest);

    ResponseEntity<ResponseStructure<StoreResponseComplete>> updateStore(StoreRequest storeRequest, String storeId);
}

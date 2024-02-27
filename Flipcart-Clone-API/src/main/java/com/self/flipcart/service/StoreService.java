package com.self.flipcart.service;

import com.self.flipcart.model.Store;
import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.requestdto.StoreRequestComplete;
import com.self.flipcart.responsedto.StoreResponse;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.Structure;
import org.springframework.http.ResponseEntity;

public interface StoreService {
    ResponseEntity<ResponseStructure<StoreResponse>> setUpStore(StoreRequestComplete storeRequestComplete);

    ResponseEntity<ResponseStructure<StoreResponse>> updateStore(StoreRequest storeRequest, String storeId);


    ResponseEntity<? extends Structure<? extends  StoreResponse>> getStore(String storeId, String displayType);

    ResponseEntity<Boolean> checkIfStoreExistBySeller();

    ResponseEntity<ResponseStructure<Store>> getStoreBySeller();
}

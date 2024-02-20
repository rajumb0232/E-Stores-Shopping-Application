package com.self.flipcart.service;

import com.self.flipcart.requestdto.AddressRequest;
import com.self.flipcart.responsedto.AddressResponse;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

public interface AddressService {
    ResponseEntity<ResponseStructure<AddressResponse>> addAddressToStore(AddressRequest addressRequest, String storeId);
}

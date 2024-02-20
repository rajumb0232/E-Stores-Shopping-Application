package com.self.flipcart.serviceimpl;

import com.self.flipcart.model.Address;
import com.self.flipcart.repository.AddressRepo;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.requestdto.AddressRequest;
import com.self.flipcart.responsedto.AddressResponse;
import com.self.flipcart.service.AddressService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private AddressRepo addressRepo;
    private StoreRepo storeRepo;
    private ResponseStructure<AddressResponse> structure;
    @Override
    public ResponseEntity<ResponseStructure<AddressResponse>> addAddressToStore(AddressRequest addressRequest, String storeId) {
        return storeRepo.findById(storeId).map(store -> {
            Address address = mapToAddressEntity(addressRequest, new Address());
            address = addressRepo.save(address);
            store.setAddress(address);
            storeRepo.save(store);
            return new ResponseEntity<>(structure.setStatus(HttpStatus.CREATED.value())
                    .setMessage("Address saved successfully")
                    .setData(mapToAddressResponse(address)), HttpStatus.CREATED);
        }).orElseThrow();
    }

    private AddressResponse mapToAddressResponse(Address address) {
       return AddressResponse.builder()
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .areaVillage(address.getAreaVillage())
                .addressId(address.getAddressId())
                .cityDistrict(address.getCityDistrict())
                .state(address.getState())
                .Country(address.getCountry())
                .pincode(address.getPincode())
                .build();
    }

    private Address mapToAddressEntity(AddressRequest addressRequest, Address address) {
        address.setAddressLine1(addressRequest.getAddressLine1());
        address.setAddressLine2(addressRequest.getAddressLine2());
        address.setAreaVillage(addressRequest.getAreaVillage());
        address.setCityDistrict(addressRequest.getCityDistrict());
        address.setState(addressRequest.getState());
        address.setCountry(addressRequest.getCountry());
        address.setPincode(addressRequest.getPincode());
        return address;
    }
}

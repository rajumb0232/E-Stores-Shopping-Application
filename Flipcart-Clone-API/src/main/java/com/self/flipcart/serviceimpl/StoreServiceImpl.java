package com.self.flipcart.serviceimpl;

import com.self.flipcart.enums.DisplayType;
import com.self.flipcart.exceptions.InvalidDisplayTypeException;
import com.self.flipcart.exceptions.StoreNotFoundByIdException;
import com.self.flipcart.model.Store;
import com.self.flipcart.repository.SellerRepo;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.repository.UserRepo;
import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.requestdto.StoreRequestComplete;
import com.self.flipcart.responsedto.StoreResponse;
import com.self.flipcart.responsedto.StoreResponseBasic;
import com.self.flipcart.responsedto.StoreResponseComplete;
import com.self.flipcart.service.StoreService;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.Structure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {

    private UserRepo userRepo;
    private SellerRepo sellerRepo;
    private StoreRepo storeRepo;
    private ResponseStructure<StoreResponse> storeResponseStructure;
    private StoreResponseComplete storeResponseComplete;

    @Override
    public ResponseEntity<ResponseStructure<StoreResponse>> setUpStore(StoreRequestComplete storeRequest) {
        Store store = mapToStoreEntity(storeRequest, new Store());
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(user -> {
            return sellerRepo.findById(user.getUserId()).map(seller -> {
                Store uniqueStore = storeRepo.save(store);
                seller.setStore(uniqueStore);
                sellerRepo.save(seller);
                return new ResponseEntity<>(
                        storeResponseStructure.setStatus(HttpStatus.CREATED.value())
                                .setMessage("Store Created Successfully")
                                .setData(mapToStoreResponse(uniqueStore)), HttpStatus.CREATED);
            }).get();
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<StoreResponse>> updateStore(StoreRequest storeRequest, String storeId) {
        return storeRepo.findById(storeId).map(exStore -> {
            exStore = mapToStoreEntity(storeRequest, exStore);
            Store uniqueStore = storeRepo.save(exStore);
            return new ResponseEntity<>(
                    storeResponseStructure.setStatus(HttpStatus.OK.value())
                            .setMessage("Store Created Successfully")
                            .setData(mapToStoreResponse(uniqueStore)), HttpStatus.OK);
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<? extends Structure<? extends StoreResponse>> getStore(String storeId, String displayType) {
        DisplayType display = null;
        try {
            display = DisplayType.valueOf(displayType.toUpperCase());
        } catch (RuntimeException e) {
            throw new InvalidDisplayTypeException("Failed to find the store");
        }
        final DisplayType displayType2 = display;
        return storeRepo.findById(storeId)
                .map(store -> {
                    switch (displayType2) {
                        case BASIC -> {
                            return new ResponseEntity<>(storeResponseStructure.setStatus(HttpStatus.FOUND.value())
                                    .setMessage("Store data found")
                                    .setData(mapToStoreResponseBasic(store)), HttpStatus.FOUND);
                        }
                        case COMPLETE -> {
                            return new ResponseEntity<>(storeResponseStructure.setStatus(HttpStatus.FOUND.value())
                                    .setMessage("Store data found")
                                    .setData(mapToStoreResponse(store)), HttpStatus.FOUND);
                        }
                        default ->
                                throw new InvalidDisplayTypeException("Failed to find the store, display type card not supported");
                    }
                })
                .orElseThrow(() -> new StoreNotFoundByIdException("Failed to find the store data"));
    }


    private StoreResponseBasic mapToStoreResponseBasic(Store store) {
        return StoreResponseBasic.builder()
                .storeName(store.getStoreName())
                .storeId(store.getStoreId())
                .primeCategory(store.getPrimeCategory())
                .logoLink(store.getLogoLink())
                .build();
    }

    private StoreResponseComplete mapToStoreResponse(Store store) {
        storeResponseComplete.setStoreId(store.getStoreId());
        storeResponseComplete.setStoreName(store.getStoreName());
        storeResponseComplete.setLogoLink(store.getLogoLink());
        storeResponseComplete.setAbout(store.getAbout());
        storeResponseComplete.setPrimeCategory(store.getPrimeCategory());
        return storeResponseComplete;
    }

    private Store mapToStoreEntity(StoreRequest storeRequest, Store store) {
        store.setStoreName(storeRequest.getStoreName());
        store.setAbout(storeRequest.getAbout());
        return store;
    }

    private Store mapToStoreEntity(StoreRequestComplete storeRequestComplete, Store store) {
        store.setStoreName(storeRequestComplete.getStoreName());
        store.setAbout(storeRequestComplete.getAbout());
        store.setPrimeCategory(storeRequestComplete.getPrimeCategory());
        return store;
    }
}

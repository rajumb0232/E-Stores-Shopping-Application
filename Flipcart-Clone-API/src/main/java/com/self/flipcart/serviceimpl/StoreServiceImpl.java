package com.self.flipcart.serviceimpl;

import com.self.flipcart.exceptions.InvalidPrimeCategoryException;
import com.self.flipcart.exceptions.StoreNotFoundByIdException;
import com.self.flipcart.model.Store;
import com.self.flipcart.repository.SellerRepo;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.repository.UserRepo;
import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.requestdto.StoreRequestComplete;
import com.self.flipcart.responsedto.StoreResponse;
import com.self.flipcart.service.StoreService;
import com.self.flipcart.util.ResponseStructure;
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
    private ResponseStructure<StoreResponse> basicStructure;
    private ResponseStructure<Store> structure;

    @Override
    public ResponseEntity<ResponseStructure<Store>> setUpStore(StoreRequestComplete storeRequest) {
        Store store = mapToStoreEntity(storeRequest, new Store());
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(user -> sellerRepo.findById(user.getUserId())
                .map(seller -> {
                    Store uniqueStore = storeRepo.save(store);
                    seller.setStore(uniqueStore);
                    sellerRepo.save(seller);
                    return new ResponseEntity<>(
                            structure.setStatus(HttpStatus.CREATED.value())
                                    .setMessage("Store Created Successfully")
                                    .setData(uniqueStore), HttpStatus.CREATED);
                }).get()).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<Store>> updateStore(StoreRequest storeRequest, String storeId) {
        return storeRepo.findById(storeId).map(exStore -> {
            exStore = mapToStoreEntity(storeRequest, exStore);
            Store uniqueStore = storeRepo.save(exStore);
            return new ResponseEntity<>(
                    structure.setStatus(HttpStatus.OK.value())
                            .setMessage("Store Created Successfully")
                            .setData(uniqueStore), HttpStatus.OK);
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<StoreResponse>> getStore(String storeId) {
        return storeRepo.findById(storeId)
                .map(store -> new ResponseEntity<>(basicStructure.setStatus(HttpStatus.FOUND.value())
                        .setMessage("Store data found")
                        .setData(mapToStoreResponseBasic(store)), HttpStatus.FOUND))
                .orElseThrow(() -> new StoreNotFoundByIdException("Failed to find the store data"));
    }

    @Override
    public ResponseEntity<Boolean> checkIfStoreExistBySeller() {
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> sellerRepo.findById(user.getUserId())
                        .map(seller -> {
                            if (seller.getStore() != null) return ResponseEntity.ok(true);
                            else return ResponseEntity.ok(false);
                        })
                        .get())
                .orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<Store>> getStoreBySeller() {
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .map(user -> sellerRepo.findById(user.getUserId())
                        .map(seller -> {
                            if (seller.getStore() != null) {
                                return new ResponseEntity<>(structure.setStatus(HttpStatus.FOUND.value())
                                        .setMessage("Store found")
                                        .setData(seller.getStore()), HttpStatus.FOUND);
                            } else throw new RuntimeException("No Store found associated with seller");
                        }).get())
                .orElseThrow();
    }

    private StoreResponse mapToStoreResponseBasic(Store store) {
        return StoreResponse.builder()
                .storeName(store.getStoreName())
                .storeId(store.getStoreId())
                .primeCategory(store.getPrimeCategory())
                .logoLink(store.getLogoLink())
                .build();
    }

    /**
     * maps the store request object to entity ignoring the primeCategory
     */
    private Store mapToStoreEntity(StoreRequest storeRequest, Store store) {
        if (storeRequest.getPrimeCategory() == null)
            throw new InvalidPrimeCategoryException("Failed to update the store data");
        store.setStoreName(storeRequest.getStoreName());
        store.setAbout(storeRequest.getAbout());
        return store;
    }

    /**
     * maps the store request object to entity along with the primeCategory
     */
    private Store mapToStoreEntity(StoreRequestComplete storeRequestComplete, Store store) {
        if (storeRequestComplete.getPrimeCategory() == null)
            throw new InvalidPrimeCategoryException("Failed to update the store data");
        store.setStoreName(storeRequestComplete.getStoreName());
        store.setAbout(storeRequestComplete.getAbout());
        store.setPrimeCategory(storeRequestComplete.getPrimeCategory());
        return store;
    }
}

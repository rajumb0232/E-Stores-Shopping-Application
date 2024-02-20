package com.self.flipcart.serviceimpl;

import com.self.flipcart.model.Store;
import com.self.flipcart.repository.SellerRepo;
import com.self.flipcart.repository.StoreRepo;
import com.self.flipcart.repository.UserRepo;
import com.self.flipcart.requestdto.StoreRequest;
import com.self.flipcart.responsedto.StoreResponseComplete;
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
    private ResponseStructure<StoreResponseComplete> storeResponseCompleteStructure;
    private StoreResponseComplete storeResponseComplete;

    @Override
    public ResponseEntity<ResponseStructure<StoreResponseComplete>> setUpStore(StoreRequest storeRequest) {
        Store store = mapToStoreEntity(storeRequest, new Store());
        return userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).map(user -> {
            return sellerRepo.findById(user.getUserId()).map(seller -> {
                Store uniqueStore = storeRepo.save(store);
                seller.setStore(uniqueStore);
                sellerRepo.save(seller);
                return new ResponseEntity<ResponseStructure<StoreResponseComplete>>(
                        storeResponseCompleteStructure.setStatus(HttpStatus.CREATED.value())
                                .setMessage("Store Created Successfully")
                                .setData(mapToStoreResponseComplete(uniqueStore)), HttpStatus.CREATED);
            }).get();
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<StoreResponseComplete>> updateStore(StoreRequest storeRequest, String storeId) {
        return storeRepo.findById(storeId).map(exStore -> {
            exStore = mapToStoreEntity(storeRequest, exStore);
            Store uniqueStore = storeRepo.save(exStore);
            return new ResponseEntity<ResponseStructure<StoreResponseComplete>>(
                    storeResponseCompleteStructure.setStatus(HttpStatus.OK.value())
                            .setMessage("Store Created Successfully")
                            .setData(mapToStoreResponseComplete(uniqueStore)), HttpStatus.OK);
        }).orElseThrow();
    }

    private StoreResponseComplete mapToStoreResponseComplete(Store store) {
        storeResponseComplete.setStoreId(store.getStoreId());
        storeResponseComplete.setStoreName(store.getStoreName());
        storeResponseComplete.setLogoLink(store.getLogoLink());
        storeResponseComplete.setAbout(store.getAbout());
        storeResponseComplete.setPrimeCategory(store.getPrimeCategory());
        return storeResponseComplete;
    }

    private Store mapToStoreEntity(StoreRequest storeRequest, Store store) {
        store.setStoreName(storeRequest.getStoreName());
        store.setPrimeCategory(storeRequest.getPrimeCategory());
        store.setAbout(store.getAbout());
        return store;
    }
}

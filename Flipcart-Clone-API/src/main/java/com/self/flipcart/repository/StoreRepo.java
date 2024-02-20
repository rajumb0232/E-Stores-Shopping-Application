package com.self.flipcart.repository;

import com.self.flipcart.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepo extends JpaRepository<Store, String> {
}

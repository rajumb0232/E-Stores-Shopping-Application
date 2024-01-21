package com.self.flipcart.repository;

import com.self.flipcart.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepo extends JpaRepository<Seller, String> {
    boolean existsByEmail(String email);
}

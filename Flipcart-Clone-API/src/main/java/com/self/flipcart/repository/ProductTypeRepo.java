package com.self.flipcart.repository;

import com.self.flipcart.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepo extends JpaRepository<ProductType, String> {
}

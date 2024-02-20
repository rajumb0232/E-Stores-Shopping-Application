package com.self.flipcart.repository;

import com.self.flipcart.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<Address, String> {
}

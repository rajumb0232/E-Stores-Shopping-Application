package com.self.flipcart.repository;

import com.self.flipcart.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepo extends JpaRepository<AccessToken, String> {
}

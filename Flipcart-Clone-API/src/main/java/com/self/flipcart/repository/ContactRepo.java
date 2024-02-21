package com.self.flipcart.repository;

import com.self.flipcart.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepo extends JpaRepository<Contact, String> {
}

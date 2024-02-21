package com.self.flipcart.service;

import com.self.flipcart.model.Contact;
import com.self.flipcart.requestdto.ContactRequest;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContactService {
    ResponseEntity<ResponseStructure<Contact>> addContact(ContactRequest contactRequest, String addressId);

    ResponseEntity<ResponseStructure<Contact>> updateContact(ContactRequest contactRequest, String contactId);

    ResponseEntity<ResponseStructure<Contact>> getContactById(String contactId);

    ResponseEntity<ResponseStructure<List<Contact>>> getContactsByAddress(String addressId);
}

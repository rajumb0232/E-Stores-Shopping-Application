package com.self.flipcart.serviceimpl;

import com.self.flipcart.model.Contact;
import com.self.flipcart.repository.AddressRepo;
import com.self.flipcart.repository.ContactRepo;
import com.self.flipcart.requestdto.ContactRequest;
import com.self.flipcart.service.ContactService;
import com.self.flipcart.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private ContactRepo contactRepo;
    private AddressRepo addressRepo;
    private ResponseStructure<Contact> structure;
    private ResponseStructure<List<Contact>> structureList;
    @Override
    public ResponseEntity<ResponseStructure<List<Contact>>> addContact(ContactRequest contactRequest, String addressId) {
        return addressRepo.findById(addressId).map(address -> {
            Contact contact = mapToContactEntity(contactRequest, new Contact());
            contact.setAddress(address);
            contact = contactRepo.save(contact);
            return new ResponseEntity<>(structureList.setStatus(HttpStatus.CREATED.value())
                    .setMessage("Successfully saved contact")
                    .setData(address.getContacts()), HttpStatus.CREATED);
        }).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<List<Contact>>> updateContact(ContactRequest contactRequest, String contactId) {
        return contactRepo.findById(contactId).map(c -> {
            Contact contact = contactRepo.save(mapToContactEntity(contactRequest, c));
            List<Contact> contacts = contactRepo.findAllByAddress(contact.getAddress());
            return new ResponseEntity<>(structureList.setStatus(HttpStatus.OK.value())
                    .setMessage("Successfully updated contact")
                    .setData(contacts), HttpStatus.OK);
        } ).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<Contact>> getContactById(String contactId) {
        return contactRepo.findById(contactId).map(contact -> new ResponseEntity<>(structure.setStatus(HttpStatus.FOUND.value())
                .setMessage("contact found")
                .setData(contact), HttpStatus.FOUND)).orElseThrow();
    }

    @Override
    public ResponseEntity<ResponseStructure<List<Contact>>> getContactsByAddress(String addressId) {
       return addressRepo.findById(addressId).map(address -> new ResponseEntity<>(structureList.setStatus(HttpStatus.FOUND.value())
                .setMessage("contacts found b address")
                .setData(contactRepo.findAllByAddress(address)), HttpStatus.FOUND)).orElseThrow();

    }

    private Contact mapToContactEntity(ContactRequest contactRequest, Contact contact) {
        contact.setContactNumber(contactRequest.getContactNumber());
        contact.setContactName(contactRequest.getContactName());
        contact.setPrimary(contactRequest.isPrimary());
        return contact;
    }
}

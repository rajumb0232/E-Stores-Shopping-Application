package com.self.flipcart.serviceimpl;

import com.self.flipcart.enums.UserRole;
import com.self.flipcart.model.Seller;
import com.self.flipcart.model.User;
import com.self.flipcart.repository.SellerRepo;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.service.UserService;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private ResponseStructure<UserResponse> structure;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .userRole(user.getUserRole())
                .email(user.getEmail())
                .build();
    }

    private Seller mapToSellerEntity(UserRequest userRequest) {
        Seller seller = new Seller();
        seller.setUsername(userRequest.getEmail().split("@")[0]);
        seller.setEmail(userRequest.getEmail());
        String ps = generateRandomPassword();
        seller.setPassword(passwordEncoder.encode(ps));
        System.err.println(ps);
        seller.setUserRole(userRequest.getUserRole());
        return seller;
    }

    private String generateRandomPassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        String base = UUID.randomUUID().toString();
        for(int i=0; i<12; i++){
            char c = base.charAt(random.nextInt(base.length()));
            if(c != ' ') password.append(c); else i--;
        }
        return password.toString();
    }

    @Override
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) {
        User user = null;
        if(userRequest.getUserRole().equals(UserRole.SELLER)) {
           Seller seller = mapToSellerEntity(userRequest);
            user = sellerRepo.save(seller);
        }else
            throw new RuntimeException("Failed to register user.");

        return new ResponseEntity<>(
                structure.setStatus(HttpStatus.CREATED.value())
                        .setMessage("user registration successful.")
                        .setData(mapToUserResponse(user)), HttpStatus.CREATED);
    }


}

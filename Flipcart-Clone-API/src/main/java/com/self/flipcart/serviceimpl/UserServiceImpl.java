package com.self.flipcart.serviceimpl;

import com.self.flipcart.enums.UserRole;
import com.self.flipcart.exceptions.DuplicateEmailException;
import com.self.flipcart.exceptions.EmailNotFoundException;
import com.self.flipcart.model.Seller;
import com.self.flipcart.model.User;
import com.self.flipcart.repository.SellerRepo;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.service.UserService;
import com.self.flipcart.util.ResponseStructure;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private ResponseStructure<UserResponse> structure;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

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
        seller.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        seller.setUserRole(userRequest.getUserRole());
        return seller;
    }

    @Async
    private CompletableFuture<UserRequest> sendConfirmationMail(UserRequest userRequest) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(userRequest.getEmail());
            helper.setSubject("Welcome to flipcart");
            helper.setSentDate(new Date());
            helper.setText(
                    "Hi " + userRequest.getEmail().split("@")[0] + ",<br>"
                            + "You have successfully registered to Flipcart as a " + userRequest.getUserRole().name().toLowerCase()
                            + "<br><br>"
                            + "thanks and regards<br>"
                            + "<b>Flipcart<b>"
                    , true);
            javaMailSender.send(message);
            return CompletableFuture.completedFuture(userRequest);
        } catch (MessagingException e) {
            return CompletableFuture.failedFuture(new EmailNotFoundException("Failed to send confirmation mail."));
        }
    }

    private User saveUser(UserRequest userRequest) {
            User user = null;
            UserRole role = userRequest.getUserRole();
            switch (role) {
                case SELLER:
                    Seller seller = mapToSellerEntity(userRequest);
                    user = sellerRepo.save(seller);
                    break;
                case ADMIN:
                case CUSTOMER:
                case SUPER_ADMIN:
                    break;
            }
            return user;
    }

    @Override
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) throws ExecutionException, InterruptedException {
        // validating if there is already a user with the given email in the request
        if (!sellerRepo.existsByEmail(userRequest.getEmail())) {
            // verifying email for its existence by sending a confirmation mail
            return sendConfirmationMail(userRequest).thenApply(request -> new ResponseEntity<>(
                    structure.setStatus(HttpStatus.CREATED.value())
                            .setMessage("user registration successful.")
                            .setData(mapToUserResponse(saveUser(request))), HttpStatus.CREATED)).get();
        } else
            throw new DuplicateEmailException("Failed to register user.");
    }


}

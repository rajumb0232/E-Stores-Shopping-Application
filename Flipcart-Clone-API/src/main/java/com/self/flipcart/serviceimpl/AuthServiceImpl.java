package com.self.flipcart.serviceimpl;

import com.self.flipcart.cache.CacheStore;
import com.self.flipcart.enums.UserRole;
import com.self.flipcart.exceptions.DuplicateEmailException;
import com.self.flipcart.exceptions.EmailNotFoundException;
import com.self.flipcart.exceptions.OtpExpiredException;
import com.self.flipcart.exceptions.UserNotFoundByIdException;
import com.self.flipcart.model.Seller;
import com.self.flipcart.model.User;
import com.self.flipcart.repository.SellerRepo;
import com.self.flipcart.requestdto.OtpModel;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.service.AuthService;
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
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private ResponseStructure<String> verificationResponse;

    @Autowired
    private ResponseStructure<UserResponse> structure;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private CacheStore<OtpModel> otpCache;

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

    private Integer generateOTP() {
        return new Random().nextInt(100000, 999999);
    }

    @Async
    private CompletableFuture<String> sendConfirmationMail(User user) {
        // Generate the OTP and provide the ID of the OTP as a path variable to the confirmation link.
        OtpModel otp = OtpModel.builder()
                .otpId(UUID.randomUUID().toString())
                .otp(generateOTP())
                .userId(user.getUserId()).build();
        otpCache.add(otp.getOtpId(), otp);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to flipcart");
            helper.setSentDate(new Date());
            helper.setText(
                    "Hi " + user.getEmail().split("@")[0] + ",<br>"
                            + "Nice to see you interested in Flipcart, complete your registration by clicking the verify button below<br><br>"
                            + "<a href='http://localhost:7200/api/fcv1/ve/" + otp.getUserId() + "/" + otp.getOtpId() + "'" +
                            "style=\"color: #f2f2f2; font-size: 1rem; font-weight: 600; text-decoration: none; padding: 0.5em 1em; background-color: #03a5fc; border-radius: 10px;\">Verify</a>" // add the OTP ID (UUID)
                            + "<br><br>"
                            + "yours,<br>"
                            + "<b>Flipcart<b>"
                    , true);
            javaMailSender.send(message);
            return CompletableFuture.completedFuture("Please check your mail for email verification");
        } catch (MessagingException e) {
            throw new EmailNotFoundException("Failed to send confirmation mail.");
        }
    }

    private <T extends User> T saveUser(UserRequest userRequest) {
        User user = null;
        UserRole role = userRequest.getUserRole();
        switch (role) {
            case SELLER:
                Seller seller = mapToSellerEntity(userRequest);
                seller.setEmailVerified(false);
                user = sellerRepo.save(seller);
                break;
            case ADMIN:
            case CUSTOMER:
            case SUPER_ADMIN:
                break;
        }
        return (T) user;
    }

    @Override
    public ResponseEntity<ResponseStructure<String>> registerUser(UserRequest userRequest) throws ExecutionException, InterruptedException {
        // validating if there is already a user with the given email in the request
        User user = sellerRepo.findByUsername(userRequest.getEmail().split("@")[0]).orElseGet(() -> saveUser(userRequest));
        return sendConfirmationMail(user).thenApply(msg -> new ResponseEntity<>(
                verificationResponse.setStatus(HttpStatus.ACCEPTED.value())
                        .setMessage("user registration successful.")
                        .setData(msg), HttpStatus.ACCEPTED
        )).join();
    }

    @Override
    public ResponseEntity<String> verifyUserEmail(String userId, String otpId) {
        OtpModel otp = otpCache.get(otpId);
        if (otp == null)
            return new ResponseEntity<>("<h3 style=\"text-align: centre;\">OTP already expired, you can close this tab</h3> ", HttpStatus.BAD_REQUEST);
        else {
            return sellerRepo.findById(otp.getUserId()).map(user -> {
                user.setEmailVerified(true);
                sellerRepo.save(user);
                otpCache.remove(otpId);
                return new ResponseEntity<>("<h2 style=\"text-align: centre;\">Your registration is successfully completed<br> " +
                        "Click on the link below to Login<br><br>" +
                        "<a href='http://localhost:5173/login' target='_blank' " +
                        "style=\"color: #f2f2f2; font-weight: 600; text-decoration: none; padding: 0.5em 1em; background-color: #03a5fc; border-radius: 20px;\">Login</a></h2>", HttpStatus.CREATED);
            }).orElseThrow(() -> new UserNotFoundByIdException("Failed to register the user"));
        }
    }


}

package com.self.flipcart.serviceimpl;

import com.self.flipcart.cache.CacheStore;
import com.self.flipcart.enums.UserRole;
import com.self.flipcart.exceptions.*;
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
                .isEmailVerified(user.isEmailVerified())
                .build();
    }

    private <T extends User> T mapToChildEntity(UserRole userRole, UserRequest userRequest) {
        User user = null;
        switch (userRole) {
            case SELLER -> {
                user = new Seller();
            }
            case ADMIN -> {
            }
        }
        user.setUsername(userRequest.getEmail().split("@")[0]);
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setUserRole(userRequest.getUserRole());
        return (T) user;
    }

    private Integer generateOTP() {
        return new Random().nextInt(100000, 999999);
    }

    @Async
    private CompletableFuture<String> sendOTPToMailId(User user) {
        // Generate the OTP and provide the ID of the OTP as a path variable to the confirmation link.
        OtpModel otp = OtpModel.builder()
                .otp(generateOTP())
                .userId(user.getUserId()).build();
        otpCache.add(otp.getUserId(), otp);
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Verify your email for flipkart");
            helper.setSentDate(new Date());
            helper.setText(
                    "Hi " + user.getEmail().split("@")[0] + ",<br>"
                            + "<h4> Nice to see you interested in Flipkart, your OTP for email verification is,</h4><br><br>"
                            + "<h3 style=\"color: #f2f2f2; font-size: 1rem; font-weight: 600; text-decoration: none; padding: 0.5em 1em;" +
                            " background-color: #03a5fc; border-radius: 10px; width: max-content;\">" + otp.getOtp() + "</h3>" // add the OTP ID (UUID)
                            + "<br><br>"
                            + "<h4>yours,<br>"
                            + "Flipkart</h4>"
                    , true);
            javaMailSender.send(message);
            return CompletableFuture.completedFuture("Please check your mail for OTP");
        } catch (MessagingException e) {
            throw new EmailNotFoundException("Failed to send confirmation mail.");
        }
    }

    private <T extends User> T saveUser(UserRequest userRequest) {
        User user = null;
        UserRole role = userRequest.getUserRole();
        switch (role) {
            case SELLER -> {
                Seller seller = mapToChildEntity(UserRole.SELLER, userRequest);
                seller.setEmailVerified(false);
                user = sellerRepo.save(seller);
            }
            case ADMIN, CUSTOMER, SUPER_ADMIN -> {
            }

        }
        return (T) user;
    }

    @Override
    public ResponseEntity<ResponseStructure<String>> registerUser(UserRequest userRequest) throws ExecutionException, InterruptedException {
        // validating if there is already a user with the given email in the request
        User user = sellerRepo.findByUsername(userRequest.getEmail().split("@")[0])
                .map(u -> {
                    if (u.isEmailVerified()) throw new UserAlreadyExistsByEmailException("Failed To register the User");
                    else return u;
                })
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(userRequest.getPassword()));
                    return u;
                })
                .orElseGet(() -> saveUser(userRequest));
        return sendOTPToMailId(user).thenApply(msg -> new ResponseEntity<>(
                verificationResponse.setStatus(HttpStatus.ACCEPTED.value())
                        .setMessage("user registration successful.")
                        .setData(msg), HttpStatus.ACCEPTED
        )).join();
    }

    @Override
    public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(OtpModel otpModel) {
        OtpModel otp = otpCache.get(otpModel.getUserId());
        if (otp == null)
            throw new OtpExpiredException("Failed to verify Email");
        else {
            if (otp.getOtp() == otpModel.getOtp())
                return sellerRepo.findById(otp.getUserId()).map(user -> {
                    user.setEmailVerified(true);
                    sellerRepo.save(user);
                    otpCache.remove(otpModel.getUserId());
                    return new ResponseEntity<>(structure.setStatus(HttpStatus.OK.value())
                            .setMessage("User registration successful")
                            .setData(mapToUserResponse(user)), HttpStatus.CREATED);
                }).orElseThrow(() -> new UserNotFoundByIdException("Failed to register the user"));
            else throw new IncorrectOTPException("Failed to verify Email");
        }
    }


}

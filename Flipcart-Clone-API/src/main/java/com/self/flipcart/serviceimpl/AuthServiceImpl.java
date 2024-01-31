package com.self.flipcart.serviceimpl;

import com.self.flipcart.cache.CacheStore;
import com.self.flipcart.enums.UserRole;
import com.self.flipcart.exceptions.*;
import com.self.flipcart.model.Seller;
import com.self.flipcart.model.User;
import com.self.flipcart.repository.SellerRepo;
import com.self.flipcart.repository.UserRepo;
import com.self.flipcart.requestdto.AuthRequest;
import com.self.flipcart.dto.MessageData;
import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.AuthResponse;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.security.JwtService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ResponseStructure<String> verificationResponse;

    @Autowired
    private ResponseStructure<UserResponse> structure;

    @Autowired
    private ResponseStructure<AuthResponse> authStructure;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private CacheStore<OtpModel> otpCache;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager AuthenticationManager;

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
                    return updateUser(u);
                })
                .orElseGet(() -> saveUser(userRequest));
        return Optional.of(sendOTPToMailId(user)).map(msg -> new ResponseEntity<>(
                verificationResponse.setStatus(HttpStatus.ACCEPTED.value())
                        .setMessage("user registration successful.")
                        .setData(msg), HttpStatus.ACCEPTED
        )).get();
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
                    sendConfirmationMail(user);
                    return new ResponseEntity<>(structure.setStatus(HttpStatus.OK.value())
                            .setMessage("User registration successful")
                            .setData(mapToUserResponse(user)), HttpStatus.CREATED);
                }).orElseThrow(() -> new UserNotFoundByIdException("Failed to register the user"));
            else throw new IncorrectOTPException("Failed to verify Email");
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest) {
        String username = authRequest.getEmail().split("@")[0];
        Authentication auth = AuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

        if (auth.isAuthenticated())
            return userRepo.findByUsername(username).map(user -> new ResponseEntity<>(authStructure.setStatus(HttpStatus.OK.value())
                    .setMessage("Authentication successful")
                    .setData(AuthResponse.builder()
                            .username(username)
                            .role(user.getUserRole().name())
                            .accessToken(jwtService.generateAccessToken(username))
                            .refreshToken(jwtService.generateRefreshToken(username))
                            .build()), HttpStatus.OK)).get();
        else throw new UsernameNotFoundException("Authentication failed");
    }

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

    private Integer generateOTP() {
        return new Random().nextInt(100000, 999999);
    }

    private String sendOTPToMailId(User user) {
        // Generate the OTP and provide the ID of the OTP as a path variable to the confirmation link.
        OtpModel otp = OtpModel.builder()
                .otp(generateOTP())
                .userId(user.getUserId()).build();
        otpCache.add(otp.getUserId(), otp);

        sendMail(MessageData.builder()
                .to(user.getEmail())
                .subject("Verify your email for flipkart")
                .sentDate(new Date())
                .text(
                        "Hi " + user.getEmail().split("@")[0] + ",<br>"
                                + "<h4> Nice to see you interested in Flipkart, your OTP for email verification is,</h4><br><br>"
                                + "<h3 style=\"color: #f2f2f2; font-size: 1rem; font-weight: 600; text-decoration: none; padding: 0.5em 1em;" +
                                " background-color: #03a5fc; border-radius: 10px; width: max-content;\">" + otp.getOtp() + "</h3>" // add the OTP ID (UUID)
                                + "<br><br>"
                                + "With Best Regards,<br>"
                                + "Flipkart"
                ).build());

        return "Please check your mail for OTP";
    }

    @Async
    private void sendMail(MessageData messageData) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(messageData.getTo());
            helper.setSubject(messageData.getSubject());
            helper.setSentDate(messageData.getSentDate());
            helper.setText(messageData.getText(), true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailNotFoundException("Failed to send confirmation mail.");
        }

    }


    @Async
    private void sendConfirmationMail(Seller user) {
        sendMail(MessageData.builder()
                .to(user.getEmail())
                .subject("Welcome to Flipkart family")
                .sentDate(new Date())
                .text(
                        "<b>Congratulations, your now a part of flipkart family, your email verification is " +
                                "successfully completed</b>" +
                                "<br><br>" +
                                "With Best Regards,<br>" +
                                "Flipkart"
                )
                .build());
    }


    private User updateUser(User user) {
        UserRole role = user.getUserRole();
        switch (role) {
            case SELLER -> {
                user = sellerRepo.save((Seller) user);
            }
        }
        return user;
    }


}

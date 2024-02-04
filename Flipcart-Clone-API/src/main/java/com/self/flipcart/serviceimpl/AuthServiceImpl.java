package com.self.flipcart.serviceimpl;

import com.self.flipcart.cache.CacheStore;
import com.self.flipcart.dto.MessageData;
import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.enums.UserRole;
import com.self.flipcart.exceptions.*;
import com.self.flipcart.model.AccessToken;
import com.self.flipcart.model.RefreshToken;
import com.self.flipcart.model.Seller;
import com.self.flipcart.model.User;
import com.self.flipcart.repository.AccessTokenRepo;
import com.self.flipcart.repository.RefreshTokenRepo;
import com.self.flipcart.repository.SellerRepo;
import com.self.flipcart.repository.UserRepo;
import com.self.flipcart.requestdto.AuthRequest;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.AuthResponse;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.security.JwtService;
import com.self.flipcart.service.AuthService;
import com.self.flipcart.util.CookieManager;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccessTokenRepo accessTokenRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private ResponseStructure<UserResponse> structure;

    @Autowired
    private ResponseStructure<AuthResponse> authStructure;

    @Autowired
    private SimpleResponseStructure simpleResponseStructure;

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

    @Autowired
    private CookieManager cookieManager;

    @Override
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) throws ExecutionException, InterruptedException {
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
        return Optional.of(sendOTPToMailId(user)).map(msg -> new ResponseEntity<ResponseStructure<UserResponse>>(
                structure.setStatus(HttpStatus.ACCEPTED.value())
                        .setMessage("user registration successful. Please check your email for OTP")
                        .setData(mapToUserResponse(user)), HttpStatus.ACCEPTED
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
                            .setData(mapToUserResponse(user)), HttpStatus.OK);
                }).orElseThrow(() -> new UserNotFoundByIdException("Failed to register the user"));
            else throw new IncorrectOTPException("Failed to verify Email");
        }
    }

    @Override
    public ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest, HttpServletResponse response, String refreshToken, String accessToken) {
        // validate if the user is already logged in
        if (accessToken != null && refreshToken != null) throw new UserAlreadyLoggedInException("Failed to login");
        // getting username
        String username = authRequest.getEmail().split("@")[0];
        Authentication auth = AuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

        // validating if the user authentication is authenticated
        if (auth.isAuthenticated()) {
            return userRepo.findByUsername(username).map(user -> {
                // granting access to the user by providing access and refresh token cookies in response
                grantAccessToUser(user, response);
                return new ResponseEntity<>(authStructure.setStatus(HttpStatus.OK.value())
                        .setMessage("Authentication successful")
                        .setData(AuthResponse.builder()
                                .userId(user.getUserId())
                                .username(username)
                                .role(user.getUserRole().name())
                                .isAuthenticated(true)
                                .build()), HttpStatus.OK);
            }).get();

        } else throw new UsernameNotFoundException("Authentication failed");
    }

    @Override
    public ResponseEntity<SimpleResponseStructure> logout(String refreshToken, String accessToken, HttpServletResponse response) {
        // resetting tokens with null value and 0 maxAge
        response.addCookie(cookieManager.removeCookie(new Cookie("rt", null)));
        response.addCookie(cookieManager.removeCookie(new Cookie("at", null)));
        // blocking the tokens
        blockAccessToken(accessToken);
        blockRefreshToken(refreshToken);

        return ResponseEntity.ok().body(simpleResponseStructure.setStatus(HttpStatus.OK.value())
                .setMessage("Logout Successful"));
    }

    @Override
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String refreshToken, String accessToken, HttpServletResponse response) {
        if (refreshToken == null) throw new UserNotLoggedInException("Failed to refresh login");
        if (accessToken != null) blockAccessToken(accessToken);

        String username = jwtService.extractUsername(refreshToken);
        return userRepo.findByUsername(username).map(user -> {
            // granting access to User with new access and refresh token cookies in response
            grantAccessToUser(user, response);
            // blocking old token
            blockRefreshToken(refreshToken);

            return ResponseEntity.ok(authStructure.setStatus(HttpStatus.OK.value())
                    .setMessage("Login refreshed successfully")
                    .setData(AuthResponse.builder()
                            .userId(user.getUserId())
                            .username(user.getUsername())
                            .role(user.getUserRole().name())
                            .isAuthenticated(true)
                            .build()));
        }).orElseThrow(() -> new UsernameNotFoundException("Failed to refresh login"));
    }

    @Override
    public ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(String refreshToken, String accessToken, HttpServletResponse response) {
        if (refreshToken == null || accessToken == null)
            throw new UserNotLoggedInException("Failed to revoke access from all other devices");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByUsername(username).map(user -> {
            // blocking all other access tokens
            List<AccessToken> accessTokens = accessTokenRepo.findAllByUserAndIsBlocked(user, false).stream()
                    .map(at -> {
                        if (!at.getToken().equals(accessToken)) at.setBlocked(true);
                        return at;
                    })
                    .collect(Collectors.toList());
            accessTokenRepo.saveAll(accessTokens);
            // blocking all other refresh tokens
            List<RefreshToken> refreshTokens = refreshTokenRepo.findALLByUserAndIsBlocked(user, false).stream()
                    .map(rt -> {
                        if (!rt.getToken().equals(refreshToken)) rt.setBlocked(true);
                        return rt;
                    }).collect(Collectors.toList());
            refreshTokenRepo.saveAll(refreshTokens);

            return ResponseEntity.ok(simpleResponseStructure.setStatus(HttpStatus.OK.value())
                    .setMessage("Successfully revoked access from all other devices"));

        }).orElseThrow(() -> new UsernameNotFoundException("Failed to revoke access fromm all other devices"));
    }

    @Override
    public ResponseEntity<SimpleResponseStructure> revokeAllTokens(HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByUsername(username).map(user -> {
            // blocking all other access tokens
            List<AccessToken> accessTokens = accessTokenRepo.findAllByUserAndIsBlocked(user, false).stream()
                    .map(at -> {
                        at.setBlocked(true);
                        return at;
                    })
                    .collect(Collectors.toList());
            accessTokenRepo.saveAll(accessTokens);
            // blocking all other refresh tokens
            List<RefreshToken> refreshTokens = refreshTokenRepo.findALLByUserAndIsBlocked(user, false).stream()
                    .map(rt -> {
                        rt.setBlocked(true);
                        return rt;
                    }).collect(Collectors.toList());
            refreshTokenRepo.saveAll(refreshTokens);

            // setting null cookies with 0 maxAge
            response.addCookie(cookieManager.removeCookie(new Cookie("at", null)));
            response.addCookie(cookieManager.removeCookie(new Cookie("rt", null)));

            return ResponseEntity.ok(simpleResponseStructure.setStatus(HttpStatus.OK.value())
                    .setMessage("Successfully revoked access from all devices"));

        }).orElseThrow(() -> new UsernameNotFoundException("Failed to revoke access fromm all other devices"));
    }

    /* ----------------------------------------------------------------------------------------------------------- */
    private void grantAccessToUser(User user, HttpServletResponse response) {
        //generating access and refresh tokens
        String newRefreshToken = jwtService.generateRefreshToken(user.getUsername());
        String newAccessToken = jwtService.generateAccessToken(user.getUsername());

        // adding cookies to the response
        Cookie at = cookieManager.setConfig(new Cookie("at", newAccessToken));
        at.setMaxAge(60 * 60);
        response.addCookie(at);
        Cookie rt = cookieManager.setConfig(new Cookie("rt", newRefreshToken));
        rt.setMaxAge(180 * 24 * 60 * 60);
        response.addCookie(rt);

        // saving access and refresh tokens to the database
        accessTokenRepo.save(AccessToken.builder()
                .isBlocked(false)
                .token(newAccessToken)
                .user(user).build());
        refreshTokenRepo.save(RefreshToken.builder()
                .isBlocked(false)
                .token(newRefreshToken)
                .user(user).build());
    }

    private void blockAccessToken(String accessToken) {
        accessTokenRepo.findByToken(accessToken).ifPresent(at -> {
            at.setBlocked(true);
            accessTokenRepo.save(at);
        });
    }

    private void blockRefreshToken(String refreshToken) {
        refreshTokenRepo.findByToken(refreshToken).ifPresent(rt -> {
            rt.setBlocked(true);
            refreshTokenRepo.save(rt);
        });
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
                                + "<h3 style=\"color: #f2f2f2; font-size: 1rem; font-weight: 600; text-decoration: none; padding: 0.5em 1em;"
                                + "border-radius: 10px; width: max-content;\">" + otp.getOtp() + "</h3>" // add the OTP ID (UUID)
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

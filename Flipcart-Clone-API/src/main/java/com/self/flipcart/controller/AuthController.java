package com.self.flipcart.controller;

import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.requestdto.AuthRequest;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.AuthResponse;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.service.AuthService;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import jakarta.servlet.annotation.HttpConstraint;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/fcv1")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/users/register")
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(@RequestBody @Valid UserRequest userRequest) throws ExecutionException, InterruptedException {
        return authService.registerUser(userRequest);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(@RequestBody OtpModel otpModel) {
        return authService.verifyUserEmail(otpModel);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody AuthRequest authRequest, HttpServletResponse response,
                                                                 @CookieValue(name = "rt", required = false) String refreshToken, @CookieValue(name = "at", required = false) String accessToken) {
        return authService.login(authRequest, response, refreshToken, accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponseStructure> logout(@CookieValue(name = "rt") String refreshToken, @CookieValue(name = "at") String accessToken,
                                                          HttpServletResponse response) {
        return authService.logout(refreshToken, accessToken, response);
    }

    @PostMapping("/login/refresh")
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(@CookieValue(name = "rt", required = false) String refreshToken, @CookieValue(name = "at", required = false) String accessToken,
                                                                        HttpServletResponse response) {
        return authService.refreshLogin(refreshToken, accessToken, response);
    }

    @PostMapping("/revoke-other")
    public ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(@CookieValue(name = "rt") String refreshToken, @CookieValue(name = "at") String accessToken,
                                                                        HttpServletResponse response) {
        return authService.revokeAllOtherTokens(refreshToken, accessToken, response);
    }

    @PostMapping("/revoke-all")
    public ResponseEntity<SimpleResponseStructure> revokeAllTokens(@CookieValue(name = "rt") String refreshToken, @CookieValue(name = "at") String accessToken,
                                                                   HttpServletResponse response) {
        return authService.revokeAllTokens(refreshToken, accessToken, response);
    }

    @GetMapping("test")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String test(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        System.err.println(cookies);
        if (cookies != null)
            for (Cookie cookie : cookies) {
                System.err.println(cookie.getName());
                if (cookie.getName().equals("at")) {
                    System.out.println(cookie.getValue());
                }
            }
        return "accessing";
    }
}

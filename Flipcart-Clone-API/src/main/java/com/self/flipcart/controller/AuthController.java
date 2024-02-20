package com.self.flipcart.controller;

import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.requestdto.AuthRequest;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.AuthResponse;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.service.AuthService;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
@AllArgsConstructor
@RestController
@RequestMapping("/api/fkv1")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class AuthController {

    private AuthService authService;

    @PostMapping("/users/register")
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(@RequestBody @Valid UserRequest userRequest) throws ExecutionException, InterruptedException {
        System.out.println(userRequest.getEmail()+", "+userRequest.getPassword()+", "+userRequest.getUserRole());
        return authService.registerUser(userRequest);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(@RequestBody OtpModel otpModel) {
        return authService.verifyUserEmail(otpModel);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody AuthRequest authRequest,
                                                                 HttpServletResponse response,
                                                                 @CookieValue(name = "rt", required = false) String refreshToken,
                                                                 @CookieValue(name = "at", required = false) String accessToken) {
        return authService.login(authRequest, response, refreshToken, accessToken);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> logout(@CookieValue(name = "rt", required = false) String refreshToken,
                                                          @CookieValue(name = "at", required = false) String accessToken,
                                                          HttpServletResponse response) {
        return authService.logout(refreshToken, accessToken, response);
    }

    @PostMapping("/login/refresh")
    public ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(@CookieValue(name = "rt", required = false) String refreshToken,
                                                                        @CookieValue(name = "at", required = false) String accessToken,
                                                                        HttpServletResponse response) {
        return authService.refreshLogin(refreshToken, accessToken, response);
    }

    @PostMapping("/revoke-other")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(@CookieValue(name = "rt", required = false) String refreshToken,
                                                                        @CookieValue(name = "at", required = false) String accessToken,
                                                                        HttpServletResponse response) {
        return authService.revokeAllOtherTokens(refreshToken, accessToken, response);
    }

    @PostMapping("/revoke-all")
    @PreAuthorize("hasAuthority('ADMIN') OR hasAuthority('SUPER_ADMIN') OR hasAuthority('SELLER') OR hasAuthority('CUSTOMER')")
    public ResponseEntity<SimpleResponseStructure> revokeAllTokens(HttpServletResponse response) {
        return authService.revokeAllTokens(response);
    }
}

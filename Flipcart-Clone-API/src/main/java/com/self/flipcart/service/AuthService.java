package com.self.flipcart.service;

import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.requestdto.AuthRequest;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.AuthResponse;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

public interface AuthService {
    ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) throws ExecutionException, InterruptedException;

    ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(OtpModel otpModel);

    ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest, String refreshToken, String accessToken);

    ResponseEntity<SimpleResponseStructure> logout(String refreshToken, String accessToken);

    ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String refreshToken, String accessToken);

    ResponseEntity<SimpleResponseStructure> revokeAllOtherTokens(String refreshToken, String accessToken);

    ResponseEntity<SimpleResponseStructure> revokeAllTokens();
}

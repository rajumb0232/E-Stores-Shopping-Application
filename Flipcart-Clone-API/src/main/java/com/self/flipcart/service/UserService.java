package com.self.flipcart.service;

import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.util.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

public interface UserService {
    ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) throws ExecutionException, InterruptedException;
}
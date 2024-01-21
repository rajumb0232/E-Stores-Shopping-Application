package com.self.flipcart.controller;

import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.service.UserService;
import com.self.flipcart.util.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/fcv1")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(@RequestBody @Valid UserRequest userRequest) throws ExecutionException, InterruptedException {
        return userService.registerUser(userRequest);
    }

    @GetMapping("/test")
//    @PreAuthorize("hasAuthority('SELLER')")
    public String test(){
        return "accessing private resources...";
    }

    @GetMapping("/out")
//    @PreAuthorize("hasAuthority('SELLER')")
    public String out(){
        return "accessing private resources 2...";
    }
}

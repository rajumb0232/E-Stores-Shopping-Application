package com.self.flipcart.controller;

import com.self.flipcart.requestdto.AuthRequest;
import com.self.flipcart.dto.OtpModel;
import com.self.flipcart.requestdto.UserRequest;
import com.self.flipcart.responsedto.AuthResponse;
import com.self.flipcart.responsedto.UserResponse;
import com.self.flipcart.service.AuthService;
import com.self.flipcart.util.ResponseStructure;
import com.self.flipcart.util.SimpleResponseStructure;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/fcv1")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/users/register")
    public ResponseEntity<ResponseStructure<UserResponse>> registerUser(@RequestBody @Valid UserRequest userRequest) throws ExecutionException, InterruptedException {
        return authService.registerUser(userRequest);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ResponseStructure<UserResponse>> verifyUserEmail(@RequestBody OtpModel otpModel){
        return authService.verifyUserEmail(otpModel);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<AuthResponse>> login(@RequestBody AuthRequest authRequest, HttpServletResponse response){
        return authService.login(authRequest, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponseStructure> logout(HttpServletRequest request, HttpServletResponse response){
        return authService.logout(request, response);
    }

    @GetMapping("test")
//    @PreAuthorize("hasAuthority('SELLER')")
    public String test(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        System.err.println(cookies);
        if(cookies!=null)
        for(Cookie cookie : cookies){
            System.err.println(cookie.getName());
            if (cookie.getName().equals("at")){
                System.out.println(cookie.getValue());
            }
        }
        return "accessing";
    }
}

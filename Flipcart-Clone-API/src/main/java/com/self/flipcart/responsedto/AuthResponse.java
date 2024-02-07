package com.self.flipcart.responsedto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String userId;
    private String username;
    private String role;
    private boolean isAuthenticated;
    private LocalDateTime accessExpiration;
}

package com.self.flipcart.responsedto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String username;
    private String role;
    private AccessToken accessToken;
    private RefreshToken refreshToken;
}

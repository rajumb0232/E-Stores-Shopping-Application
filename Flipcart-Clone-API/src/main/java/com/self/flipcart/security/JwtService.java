package com.self.flipcart.security;

import com.self.flipcart.responsedto.AccessToken;
import com.self.flipcart.responsedto.RefreshToken;
import org.springframework.stereotype.Service;

public interface JwtService {

    public AccessToken generateAccessToken(String username);

    public RefreshToken generateRefreshToken(String username);

    public String extractUsername(String token);
}

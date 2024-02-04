package com.self.flipcart.security;

import java.util.Date;

public interface JwtService {

    public String generateAccessToken(String username);

    public String generateRefreshToken(String username);

    public String extractUsername(String token);

    public Date extractExpiry(String token);
}

package com.self.flipcart.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${myapp.jwt.secret}")
    private String secret;

    @Value("${token.expiry.access.seconds}")
    private long accessTokenExpirySeconds;
    @Value("${token.expiry.refresh.seconds}")
    private long refreshTokenExpirySeconds;

    public String generateAccessToken(String username) {
        log.info("Generating Access Token...");
        return createJwtToken(new HashMap<String, Object>(), username, accessTokenExpirySeconds * 1000l);
    }

    public String generateRefreshToken(String username) {
        log.info("Generating Refresh Token...");
        return createJwtToken(new HashMap<String, Object>(), username, refreshTokenExpirySeconds * 1000l);
    }

    private String createJwtToken(Map<String, Object> claims, String username, long expiryDuration) {
        log.info(new Date(System.currentTimeMillis()).toString());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis() + expiryDuration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignatureKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // parsing JWT

    public String extractUsername(String token) {
        log.info("Extracting username...");
        return extractClaim(parseClaims(token), Claims::getSubject);
    }

    public Date extractExpiry(String token){
        return extractClaim(parseClaims(token), Claims::getExpiration);
    }

    private <R> R extractClaim(Claims parsedClaims, Function<Claims, R> claimResolver) {
        return claimResolver.apply(parsedClaims);
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}

package com.self.flipcart.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieManager {

    public Cookie configure(Cookie cookie, int maxAge) {
        cookie.setSecure(false);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public Cookie invalidate(Cookie cookie){
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}

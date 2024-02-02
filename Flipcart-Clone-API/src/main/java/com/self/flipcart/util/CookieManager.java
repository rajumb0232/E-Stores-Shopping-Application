package com.self.flipcart.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieManager {

    public Cookie setConfig(Cookie cookie) {
        cookie.setSecure(false);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}

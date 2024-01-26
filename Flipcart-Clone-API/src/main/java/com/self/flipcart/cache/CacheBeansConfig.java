package com.self.flipcart.cache;

import com.self.flipcart.requestdto.OtpModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheBeansConfig {

    @Bean
    CacheStore<OtpModel> otpCache(){
        return new CacheStore<>(Duration.ofMinutes(5));
    }
}

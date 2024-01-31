package com.self.flipcart.responsedto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    private String token;
    private Date IssuedAt;
    private Date expiry;
}

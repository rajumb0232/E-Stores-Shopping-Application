package com.self.flipcart.requestdto;

import com.self.flipcart.enums.UserRole;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private UserRole userRole;
}

package com.self.flipcart.requestdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {
    private String contactName;
    private long contactNumber;
    private boolean isPrimary;
}

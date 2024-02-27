package com.self.flipcart.requestdto;

import com.self.flipcart.enums.PrimeCategory;
import lombok.*;

@Getter
@Setter
public class StoreRequest {
    private String storeName;
    private PrimeCategory primeCategory;
    private String about;
}

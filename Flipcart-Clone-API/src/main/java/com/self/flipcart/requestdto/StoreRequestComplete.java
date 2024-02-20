package com.self.flipcart.requestdto;

import com.self.flipcart.enums.PrimeCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequestComplete extends StoreRequest{
    private PrimeCategory primeCategory;
}

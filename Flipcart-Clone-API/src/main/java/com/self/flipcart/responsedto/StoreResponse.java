package com.self.flipcart.responsedto;

import com.self.flipcart.enums.PrimeCategory;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {
    private String storeId;
    private String storeName;
    private PrimeCategory primeCategory;
    private String logoLink;
}

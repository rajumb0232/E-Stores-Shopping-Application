package com.self.flipcart.responsedto;

import com.self.flipcart.enums.PrimeCategory;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class StoreResponseBasic extends StoreResponse {
    private String storeId;
    private String storeName;
    private PrimeCategory primeCategory;
    private String logoLink;
}

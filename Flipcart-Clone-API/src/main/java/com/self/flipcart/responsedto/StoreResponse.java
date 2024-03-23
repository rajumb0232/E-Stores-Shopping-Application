package com.self.flipcart.responsedto;

import com.self.flipcart.enums.TopCategory;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {
    private String storeId;
    private String storeName;
    private TopCategory topCategory;
    private String logoLink;
}

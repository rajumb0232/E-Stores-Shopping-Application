package com.self.flipcart.responsedto;

import com.self.flipcart.enums.AvailabilityStatus;
import com.self.flipcart.model.ProductType;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductPageResponse implements ProductResponse{
    private String productId;
    private String productTitle;
    private double productPrice;
    private int productQuantity;
    private AvailabilityStatus availabilityStatus;
    private int totalOrders;
    private int totalReviews;
    private float avgRating;
    private String description;
    // Refers to the ProductType
    private ProductType productType;
    // Refers to the Store
    private StoreResponse store;
    // Refers to the Reviews
    // Refers to the Questions
}

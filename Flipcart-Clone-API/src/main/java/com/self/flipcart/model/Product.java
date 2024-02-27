package com.self.flipcart.model;

import com.self.flipcart.enums.AvailabilityStatus;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
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
    private String productTypeId;
    // Refers to the Store
    private String storeId;
    // Refers to the Reviews
    // Refers to the Questions

}

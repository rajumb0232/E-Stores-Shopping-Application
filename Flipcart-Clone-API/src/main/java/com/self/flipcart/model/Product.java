package com.self.flipcart.model;

import com.self.flipcart.enums.AvailabilityStatus;
import com.self.flipcart.util.IdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String productId;
    private String productName;
    private String description;
    private double productPrice;
    private int productQuantity;
    private AvailabilityStatus availabilityStatus;

    @ManyToOne
    private ProductCategory productCategory;
}

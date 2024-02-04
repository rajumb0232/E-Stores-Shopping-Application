package com.self.flipcart.model;

import com.self.flipcart.enums.ProductType;
import com.self.flipcart.enums.VerificationStatus;
import com.self.flipcart.util.IdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String categoryId;
    private ProductType productType;
    private VerificationStatus verificationStatus;
}

package com.self.flipcart.model;

import com.self.flipcart.enums.TopCategory;
import com.self.flipcart.enums.SubCategory;
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
public class ProductType {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String categoryId;
    private TopCategory topCategory;
    private SubCategory subCategory;
    private String typeName;
    private VerificationStatus verificationStatus;
}

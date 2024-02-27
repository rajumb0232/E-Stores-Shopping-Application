package com.self.flipcart.model;

import com.self.flipcart.enums.SpecificationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "specifications")
public class Specification {
    @Id
    private String specificationId;
    private SpecificationType specificationType;
    private Map<String, String> attributes;
    // Refers to the product
    private String productId;
}

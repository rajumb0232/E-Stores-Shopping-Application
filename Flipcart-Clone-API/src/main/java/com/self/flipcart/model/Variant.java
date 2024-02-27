package com.self.flipcart.model;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "variants")
public class Variant {

    @Id
    private String variantId;
    private Map<String, Object> attributes;
    private String ImageURL;
    // Refers to the product
    private String productId;
}

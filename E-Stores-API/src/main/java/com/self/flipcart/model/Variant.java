package com.self.flipcart.model;

import com.self.flipcart.enums.MetricType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "variants")
public class Variant {

    @MongoId
    private String variantId;
    private double price;

    private Map<String, String> specifications;
    private MetricType metricType;
    private Set<UnitsByMetric> unitsAvailableByMetric;

    @DBRef(lazy = true)
    private Product product;
}

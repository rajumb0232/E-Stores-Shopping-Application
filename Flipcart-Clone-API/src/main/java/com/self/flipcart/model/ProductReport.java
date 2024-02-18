package com.self.flipcart.model;

import com.self.flipcart.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Month;
import java.time.Year;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product_reports")
public class ProductReport {

    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String reportId;
    private Month month;
    private Year year;
    // Refers to the Product as a foreign key
    private String productId;
    private int totalOrders;
    private int totalReturns;
    private double totalRevenue;

    @ManyToOne
    private StoreReport storeReport;
}

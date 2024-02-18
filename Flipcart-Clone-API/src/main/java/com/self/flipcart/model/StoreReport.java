package com.self.flipcart.model;

import com.self.flipcart.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Month;
import java.time.Year;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "store_reports")
public class StoreReport {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String reportId;
    private Month month;
    private Year year;
    private int ordersCompleted;
    private int ordersReturned;
    private double totalRevenue;

    @ManyToOne
    private Store store;

    @OneToMany(mappedBy = "storeReport")
    private List<ProductReport> ProductReports;
}

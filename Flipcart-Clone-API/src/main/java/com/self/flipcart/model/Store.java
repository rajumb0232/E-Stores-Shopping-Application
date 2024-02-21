package com.self.flipcart.model;

import com.self.flipcart.enums.PrimeCategory;
import com.self.flipcart.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stores")
public class Store {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String storeId;
    private String storeName;
    private PrimeCategory primeCategory;
    private String logoLink;
    private String about;

    @OneToOne
    private Address address;
}

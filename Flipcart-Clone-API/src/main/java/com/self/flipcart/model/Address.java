package com.self.flipcart.model;

import com.self.flipcart.util.IdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(generator = "custom")
    @GenericGenerator(name = "custom", type = IdGenerator.class)
    private String addressId;
    private String addressLine1;
    private String addressLine2;
    private String areaVillage;
    private String cityDistrict;
    private String state;
    private String Country;
    private int pincode;
}

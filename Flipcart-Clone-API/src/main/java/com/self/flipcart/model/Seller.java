package com.self.flipcart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seller extends User{
    @OneToMany(mappedBy = "seller")
    private List<Product> products;
}

package com.self.flipcart.model;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seller extends User{
    private List<Product> products;
}

package com.devb.estores.requestdto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleProductRequest extends ProductRequest{
    private int stockQuantity;
    private double price;
}

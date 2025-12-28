package com.demo.entity;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@ToString
public class Product {
    private final String name;
    private final BigDecimal price;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = setScale(price);
    }

    private BigDecimal setScale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}

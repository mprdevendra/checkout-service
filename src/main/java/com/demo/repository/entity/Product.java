package com.demo.repository.entity;
import lombok.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@ToString
@AllArgsConstructor
public class Product {
    private final String productCode;
    private final String name;
    private final BigDecimal price;
}

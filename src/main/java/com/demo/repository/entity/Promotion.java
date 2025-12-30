package com.demo.repository.entity;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@ToString
public final class Promotion {
    private final String productName;
    private final String promotionType;
    private final Integer buyXQuantity;
    private final Integer getYQuantity;
    private final Integer bundleSize;
    private final BigDecimal bundlePrice;
    private final boolean isActive;

    public Promotion(String productName, String promotionType, Integer buyXQuantity, Integer getYQuantity, Integer bundleSize, BigDecimal bundlePrice, boolean isActive) {
        this.productName = productName;
        this.promotionType = promotionType;
        this.buyXQuantity = buyXQuantity;
        this.getYQuantity = getYQuantity;
        this.bundleSize = bundleSize;
        this.bundlePrice = setScale(bundlePrice);
        this.isActive = isActive;
    }

    private BigDecimal setScale(BigDecimal value) {
        return value !=null ? value.setScale(2, RoundingMode.HALF_UP) : null;
    }
}

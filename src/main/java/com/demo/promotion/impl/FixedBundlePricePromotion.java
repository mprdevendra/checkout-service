package com.demo.promotion.impl;

import com.demo.dto.DiscountDetailsDto;
import com.demo.entity.Promotion;
import com.demo.promotion.IPromotionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component("fixedBundlePricePromotion")
public class FixedBundlePricePromotion implements IPromotionStrategy {

    @Override
    public Optional<DiscountDetailsDto> apply(int quantity, BigDecimal unitPrice, Promotion promotion){
        int offerOnBundleSize = promotion.getBundleSize();
        BigDecimal offerOnBundlePrice = promotion.getBundlePrice();
        if(quantity <= offerOnBundleSize){
            return Optional.empty();
        }
        String productName = promotion.getProductName();

        int bundleCount = quantity / offerOnBundleSize;

        BigDecimal totalLinePrice = BigDecimal.valueOf(quantity).multiply(unitPrice);
        BigDecimal totalBundlePrice = BigDecimal.valueOf(bundleCount).multiply(offerOnBundlePrice);
        BigDecimal discount = totalLinePrice.subtract(totalBundlePrice).setScale(2, RoundingMode.HALF_UP);

        String description = discountDescription(offerOnBundleSize, productName, offerOnBundlePrice);
        DiscountDetailsDto discountDetailsDto = new DiscountDetailsDto(description, discount);
        return Optional.of(discountDetailsDto);
    }

    private String discountDescription(int offerOnBundleSize,String productName, BigDecimal offerOnBundlePrice){
        return String.format("%d %s for £%s", offerOnBundleSize, productName, offerOnBundlePrice);
    }
}

package com.demo.service.impl;

import com.demo.api.dto.DiscountDto;
import com.demo.repository.entity.Promotion;
import com.demo.service.IPromotionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component("buyXGetYPromotion")
public class BuyXGetYPromotion implements IPromotionStrategy {

    @Override
    public Optional<DiscountDto> apply(int quantity, BigDecimal unitPrice, Promotion promotion){
        int buyXQuantity = promotion.getBuyXQuantity();
        if (unitPrice == null || quantity <= 0) {
            return Optional.empty();
        }
        int getFreeQuantity = promotion.getGetYQuantity();

        int bundleSize = buyXQuantity + getFreeQuantity;
        int freeItem = (quantity / bundleSize) * getFreeQuantity;

        BigDecimal discount = BigDecimal.valueOf(freeItem).multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);

        String description = discountDescription(buyXQuantity, getFreeQuantity, promotion.getProductName());
        DiscountDto discountDto = new DiscountDto(description,discount);
        return Optional.of(discountDto);
    }

    private String discountDescription(int buyXQuantity,int getFreeQuantity,String productName){
        return String.format("Buy %d, get %d free (%s)", buyXQuantity, getFreeQuantity, productName);
    }
}

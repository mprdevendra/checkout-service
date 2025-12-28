package com.demo.promotion.impl;

import com.demo.dto.DiscountDetailsDto;
import com.demo.entity.Promotion;
import com.demo.promotion.IPromotionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("buyXGetYPromotion")
public class BuyXGetYPromotion implements IPromotionStrategy {

    @Override
    public DiscountDetailsDto apply(int quantity, BigDecimal unitPrice, Promotion promotion){
        int buyXQuantity = promotion.getBuyXQuantity();
        if(quantity <= buyXQuantity){
            return null;
        }
        int getFreeQuantity = promotion.getGetYQuantity();

        int bundleSize = buyXQuantity + getFreeQuantity;
        int freeItem = (quantity / bundleSize) * getFreeQuantity;

        BigDecimal discount = BigDecimal.valueOf(freeItem).multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);

        String description = discountDescription(buyXQuantity, getFreeQuantity, promotion.getProductName());
        DiscountDetailsDto discountDetailsDto = new DiscountDetailsDto();
        discountDetailsDto.setDescription(description);
        discountDetailsDto.setAmount(discount);
        return discountDetailsDto;
    }

    private String discountDescription(int buyXQuantity,int getFreeQuantity,String productName){
        return String.format("Buy %d, get %d free (%s)", buyXQuantity, getFreeQuantity, productName);
    }
}

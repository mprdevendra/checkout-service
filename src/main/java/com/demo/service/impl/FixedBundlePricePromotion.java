package com.demo.service.impl;

import com.demo.api.dto.DiscountDto;
import com.demo.repository.entity.Condition;
import com.demo.repository.entity.Promotion;
import com.demo.repository.entity.Reward;
import com.demo.service.IPromotionStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component("fixedBundlePricePromotion")
public class FixedBundlePricePromotion implements IPromotionStrategy {

    @Override
    public Optional<DiscountDto> apply(int quantity, BigDecimal unitPrice, Promotion promotion){
        int offerOnBundleSize = Integer.parseInt(promotion.getCondition().getOfferValue());
        BigDecimal bundlePrice = new BigDecimal(promotion.getReward().getRewardValue());
        if(quantity <= offerOnBundleSize){
            return Optional.empty();
        }
        String productName = promotion.getProductName();

        int bundleCount = quantity / offerOnBundleSize;

        BigDecimal totalLinePrice = BigDecimal.valueOf(quantity).multiply(unitPrice);
        BigDecimal totalBundlePrice = BigDecimal.valueOf(bundleCount).multiply(bundlePrice);
        BigDecimal discount = totalLinePrice.subtract(totalBundlePrice).setScale(2, RoundingMode.HALF_UP);

        String description = discountDescription(offerOnBundleSize, productName, bundlePrice);
        DiscountDto discountDto = new DiscountDto(description, discount);
        return Optional.of(discountDto);
    }

    private String discountDescription(int offerOnBundleSize,String productName, BigDecimal offerOnBundlePrice){
        return String.format("%d %s for £%s", offerOnBundleSize, productName, offerOnBundlePrice);
    }
}

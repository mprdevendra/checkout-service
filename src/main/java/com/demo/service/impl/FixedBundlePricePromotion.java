package com.demo.service.impl;

import com.demo.api.dto.DiscountDto;
import com.demo.api.dto.ItemDetailsDto;
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
    public Optional<DiscountDto> apply(ItemDetailsDto item, Promotion promotion){
        int quantity = item.getQuantity();
        int bundleSize = Integer.parseInt(promotion.getCondition().getOfferValue());

        if (quantity < bundleSize) {
            return Optional.empty();
        }

        BigDecimal bundlePrice = new BigDecimal(promotion.getReward().getRewardValue());
        int bundles = quantity / bundleSize;

        BigDecimal discount = item.getLineTotal()
                .subtract(bundlePrice.multiply(BigDecimal.valueOf(bundles)))
                .setScale(2, RoundingMode.HALF_UP);

        String description = discountDescription(bundleSize, promotion.getProductCode(), bundlePrice);
        return Optional.of(new DiscountDto(description, discount));
    }

    private String discountDescription(int offerOnBundleSize,String productCode, BigDecimal offerOnBundlePrice){
        return String.format("%d %s for £%s", offerOnBundleSize, productCode, offerOnBundlePrice);
    }
}

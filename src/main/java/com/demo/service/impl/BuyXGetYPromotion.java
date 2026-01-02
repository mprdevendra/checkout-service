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

@Component("buyXGetYPromotion")
public class BuyXGetYPromotion implements IPromotionStrategy {

    @Override
    public Optional<DiscountDto> apply(ItemDetailsDto item, Promotion promotion){
        int quantity = item.getQuantity();
        BigDecimal unitPrice = item.getUnitPrice();
        int buyX = Integer.parseInt(promotion.getCondition().getOfferValue());
        if (unitPrice == null || quantity <= buyX) {
            return Optional.empty();
        }
        int getY = Integer.parseInt(promotion.getReward().getRewardValue());
        int freeItems = (quantity / (buyX + getY)) * getY;

        BigDecimal discount = unitPrice
                .multiply(BigDecimal.valueOf(freeItems))
                .setScale(2, RoundingMode.HALF_UP);

        String description = discountDescription(buyX, getY, promotion.getProductCode());
        return Optional.of(new DiscountDto(description,discount));
    }

    private String discountDescription(int buyXQuantity,int getFreeQuantity,String productCode){
        return String.format("Buy %d, get %d free (%s)", buyXQuantity, getFreeQuantity, productCode);
    }
}

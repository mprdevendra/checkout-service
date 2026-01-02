package com.demo.service.impl;

import com.demo.api.dto.DiscountDto;
import com.demo.api.dto.ItemDetailsDto;
import com.demo.repository.entity.Promotion;
import com.demo.exception.PromotionServiceException;
import com.demo.service.IPromotionStrategy;
import com.demo.service.PromotionRegistry;
import com.demo.repository.PromotionRepository;
import com.demo.service.IPromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class PromotionServiceImpl implements IPromotionService {

    @Autowired
    private PromotionRegistry promotionRegistry;

    @Autowired
    private PromotionRepository promotionRepository;

    @Override
    public List<DiscountDto> promotion(List<ItemDetailsDto> itemDetailsDtos) {
        if (itemDetailsDtos == null || itemDetailsDtos.isEmpty()) {
            return List.of();
        }
        try {
            List<DiscountDto> discounts = new ArrayList<>();
            for(ItemDetailsDto item : itemDetailsDtos){
                Optional<Promotion> promotionOpt = promotionRepository.find(item.getItemCode());
                if (promotionOpt.isEmpty() || !promotionOpt.get().isActive()) {
                    continue;
                }
                Promotion promotion = promotionOpt.get();
                IPromotionStrategy promotionStrategy = promotionRegistry.getPromotion(promotion.getPromotionType());
                if (promotionStrategy == null) {
                    continue;
                }
                Optional<DiscountDto> discount = promotionStrategy.apply(item, promotion);
                discount.ifPresent(discounts::add);
            }
            return discounts;
        } catch (Exception ex) {
            log.error("Error occurred while applying the promotion", ex);
            throw new PromotionServiceException("Error occurred while applying the promotion", ex);
        }
    }
}

package com.demo.service.impl;

import com.demo.dto.DiscountDetailsDto;
import com.demo.dto.ItemDetailsDto;
import com.demo.entity.Promotion;
import com.demo.promotion.IPromotionStrategy;
import com.demo.promotion.PromotionRegistry;
import com.demo.repository.PromotionRepository;
import com.demo.service.IPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PromotionServiceImpl implements IPromotionService {

    @Autowired
    private PromotionRegistry promotionRegistry;

    @Autowired
    private PromotionRepository promotionRepository;

    public List<DiscountDetailsDto> calculateDiscount(List<ItemDetailsDto> itemDetailsDtos) {
        List<DiscountDetailsDto> discountDetailsDtos = new ArrayList<>();
        if(itemDetailsDtos != null && !itemDetailsDtos.isEmpty()){
            List<String> itemNames = getItemNames(itemDetailsDtos);
            Map<String, Promotion> promotionMap = getPromotionsMapByNames(itemNames);
            //basket level discount
            for(ItemDetailsDto itemDetailsDto : itemDetailsDtos){
                if(promotionMap.containsKey(itemDetailsDto.getName())){
                    Promotion promotion = promotionMap.get(itemDetailsDto.getName());
                    if(promotion.isActive()){
                        String promotionType = promotion.getPromotionType();
                        IPromotionStrategy promotionCalculator = promotionRegistry.getPromotion(promotionType);
                        if(promotionCalculator!=null){
                            Optional<DiscountDetailsDto> optionalDiscountDto = promotionCalculator.apply(itemDetailsDto.getQuantity(), itemDetailsDto.getUnitPrice(), promotion);
                            optionalDiscountDto.ifPresent(discountDetailsDtos::add);
                        }
                    }
                }
            }
        }
        return discountDetailsDtos;
    }

    private Map<String, Promotion> getPromotionsMapByNames(List<String> itemNames){
        List<Promotion> promotions = promotionRepository.getByProductNames(itemNames);
        return promotions.stream()
                .collect(Collectors
                        .toMap(Promotion::getProductName, promotion -> promotion));
    }

    private List<String> getItemNames(List<ItemDetailsDto> itemDetailsDtos){
        return itemDetailsDtos.stream().map(ItemDetailsDto::getName).toList();
    }

}

package com.demo.service.impl;

import com.demo.dto.DiscountDetailsDto;
import com.demo.dto.ItemDetailsDto;
import com.demo.entity.Promotion;
import com.demo.exception.PromotionServiceException;
import com.demo.promotion.IPromotionStrategy;
import com.demo.promotion.PromotionRegistry;
import com.demo.repository.PromotionRepository;
import com.demo.service.IPromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PromotionServiceImpl implements IPromotionService {

    @Autowired
    private PromotionRegistry promotionRegistry;

    @Autowired
    private PromotionRepository promotionRepository;

    public List<DiscountDetailsDto> calculateDiscount(List<ItemDetailsDto> itemDetailsDtos) {
        try {
            List<DiscountDetailsDto> discountDetailsDtos = new ArrayList<>();
            if(itemDetailsDtos != null && !itemDetailsDtos.isEmpty()){
                List<String> itemNames = getItemNames(itemDetailsDtos);
                Map<String, Promotion> promotionMap = getPromotionsMapByNames(itemNames);
                for(ItemDetailsDto itemDetailsDto : itemDetailsDtos){
                    Optional<DiscountDetailsDto> optionalDiscountDto = applyDiscount(itemDetailsDto, promotionMap);
                    optionalDiscountDto.ifPresent(discountDetailsDtos::add);
                }
            }
            return discountDetailsDtos;
        } catch (Exception ex) {
            log.error("Error occurred while calculating discount details", ex);
            throw new PromotionServiceException("Error in calculating discount details", ex);
        }
    }

    private Optional<DiscountDetailsDto> applyDiscount(ItemDetailsDto itemDetailsDto,Map<String, Promotion> promotionMap){
        Optional<DiscountDetailsDto> optionalDiscountDto = Optional.empty();
        if(promotionMap.containsKey(itemDetailsDto.getName())){
            Promotion promotion = promotionMap.get(itemDetailsDto.getName());
            if(promotion!=null && promotion.isActive()){
                String promotionType = promotion.getPromotionType();
                log.debug("Promotion is getting applied on item={},promotion type={}",itemDetailsDto.getName(),promotionType);
                IPromotionStrategy promotionCalculator = promotionRegistry.getPromotion(promotionType);
                if(promotionCalculator!=null){
                    optionalDiscountDto = promotionCalculator.apply(itemDetailsDto.getQuantity(), itemDetailsDto.getUnitPrice(), promotion);
                }
            }
        }
        return optionalDiscountDto;
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

package com.demo.promotion;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PromotionRegistry {

    private Map<String,IPromotionStrategy> promotionMap;

    @Autowired
    @Qualifier("buyXGetYPromotion")
    private IPromotionStrategy buyXGetYPromotion;

    @Autowired
    @Qualifier("fixedBundlePricePromotion")
    private IPromotionStrategy fixedBundlePricePromotion;

    @PostConstruct
    private void registerPromotions(){
        promotionMap = new HashMap<>();
        promotionMap.put("BUY_X_GET_Y",buyXGetYPromotion);
        promotionMap.put("FIXED_BUNDLE_PRICE",fixedBundlePricePromotion);
    }

    public IPromotionStrategy getPromotion(String promotionType){
        return promotionMap.get(promotionType);
    }
}

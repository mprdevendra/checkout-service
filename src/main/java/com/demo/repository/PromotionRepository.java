package com.demo.repository;

import com.demo.repository.entity.Promotion;
import com.demo.util.JsonReader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PromotionRepository {

    private Map<String, Promotion> promotionMap;

    @PostConstruct
    private void setUp(){
        promotionMap = dbData();
    }

    private Map<String, Promotion> dbData(){
        List<Promotion> promotions = JsonReader.read("data/promotion.json", Promotion.class);
        return promotions.stream()
                .collect(Collectors.toMap(
                        Promotion::getProductCode,
                        promo -> promo));

    }

    public Optional<Promotion> find(String productCode){
        return Optional.ofNullable(promotionMap.get(productCode));
    }
}

package com.demo.repository;

import com.demo.entity.Promotion;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class PromotionRepository {

    private List<Promotion> promotions;

    @PostConstruct
    private void setUp(){
        promotions = dbData();
    }

    private List<Promotion> dbData(){
        //versioning
        //This data is just to test as database connection/configuration is not available in this application.
        return List.of(new Promotion("Bananas", "BUY_X_GET_Y",2,1,null,null,true),
                new Promotion("Oranges","FIXED_BUNDLE_PRICE",null,null,3,new BigDecimal("0.75"),true));
    }

    public Optional<Promotion> findByProductName(String productName){
        return promotions.stream().filter(promotion->promotion.getProductName().equals(productName)).findFirst();
    }

    public List<Promotion> getByProductNames(List<String> productNames){
        return promotions.stream().filter(promotion->
                productNames.contains(promotion.getProductName())).toList();
    }


}

package com.demo.repository;

import com.demo.repository.entity.Promotion;
import com.demo.util.JsonReader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PromotionRepository {

    private List<Promotion> promotions;

    @PostConstruct
    private void setUp(){
        promotions = dbData();
        System.out.println("test");
    }

    private List<Promotion> dbData(){
        return JsonReader.read("data/promotion.json", Promotion.class);
    }

    public Optional<Promotion> findByProductName(String productName){
        return promotions.stream().filter(promotion->promotion.getProductName().equals(productName)).findFirst();
    }

    public List<Promotion> getByProductNames(List<String> productNames){
        return promotions.stream().filter(promotion->
                productNames.contains(promotion.getProductName())).toList();
    }
}

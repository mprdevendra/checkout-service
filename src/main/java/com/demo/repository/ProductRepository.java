package com.demo.repository;

import com.demo.entity.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class ProductRepository {

    private List<Product> productList;

    @PostConstruct
    private void setUpProductData(){
        productList = dbData();
    }

    private List<Product> dbData(){
        //This data is just to test as database connection/configuration is not available in this application.
        return List.of(new Product("Bananas",new BigDecimal("0.50")),
                new Product("Oranges",new BigDecimal("0.30")),
                new Product("Apples",new BigDecimal("0.60")),
                new Product("Lemons",new BigDecimal("0.25")),
                new Product("Peaches",new BigDecimal("0.75")));
    }

    public Optional<Product> findByName(String name){
        return productList.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst();
    }

    public List<Product> getByNames(List<String> names){
        //use map here
        return productList.stream()
                .filter(product -> names.contains(product.getName()))
                .toList();
    }

}

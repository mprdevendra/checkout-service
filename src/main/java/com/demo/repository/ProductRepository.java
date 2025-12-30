package com.demo.repository;

import com.demo.repository.entity.Product;
import com.demo.exception.ProductNotFoundException;
import com.demo.util.JsonReader;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ProductRepository {

    private List<Product> productList;

    @PostConstruct
    private void setUpProductData(){
        productList = dbData();
    }

    private List<Product> dbData(){
        //This data is just to test as database connection/configuration is not available in this application.
        return JsonReader.read("data/product.json", Product.class);
    }

    public Product findByName(String name){
        return productList.stream()
                .filter(product -> product.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with name: " + name
                        ));
    }

    public List<Product> getByNames(List<String> names){
        Map<String, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getName, product -> product));

        List<String> missing = names.stream()
                .filter(name -> !productMap.containsKey(name))
                .toList();

        if (!missing.isEmpty()) {
            throw new ProductNotFoundException(
                    "Product(s) not found: " + String.join(", ", missing)
            );
        }
        return names.stream()
                .map(productMap::get)
                .toList();
    }

}

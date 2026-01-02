package com.demo.repository;

import com.demo.repository.entity.Product;
import com.demo.exception.ProductNotFoundException;
import com.demo.util.JsonReader;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ProductRepository {

    private Map<String, Product> productMap;

    @PostConstruct
    private void setUpProductData(){
        productMap = dbData();
    }

    private Map<String, Product> dbData(){
        //This data is just to test as database connection/configuration is not available in this application.
        List<Product> productList = JsonReader.read("data/product.json", Product.class);
        return productList.stream()
                .collect(Collectors.toMap(
                        Product::getProductCode,
                        product -> product));
    }

    public Product findByProductCode(String productCode) {
        Product product = productMap.get(productCode);
        if (product == null) {
            throw new ProductNotFoundException(
                    "Product not found with name: " + productCode
            );
        }
        return product;
    }

    /*public Map<String, Product> getByProductCode(List<String> productCodes){
        List<String> missing = productCodes.stream()
                .filter(name -> !productMap.containsKey(name))
                .toList();

        if (!missing.isEmpty()) {
            throw new ProductNotFoundException(
                    "Product(s) not found: " + String.join(", ", missing)
            );
        }

        return productCodes.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        productMap::get)
                );
    }*/

}

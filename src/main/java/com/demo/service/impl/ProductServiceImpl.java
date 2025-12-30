package com.demo.service.impl;

import com.demo.repository.entity.Product;
import com.demo.repository.ProductRepository;
import com.demo.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductsByNames(List<String> itemNames){
        log.debug("Fetching products by names: {}", itemNames);
        List<Product> products = productRepository.getByNames(itemNames);
        log.debug("Successfully fetched {} products for names: {}", products.size(), itemNames);
        return products;
    }
}

package com.demo.service.impl;

import com.demo.repository.entity.Product;
import com.demo.repository.ProductRepository;
import com.demo.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product find(String productCode){
        log.info("Finding product with productCode={}", productCode);
        Product product = productRepository.findByProductCode(productCode);
        log.debug("Product found: {}", product);
        return product;
    }
}

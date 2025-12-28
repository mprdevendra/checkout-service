package com.demo.service.impl;

import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import com.demo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductsByNames(List<String> itemNames){
        try{
            return productRepository.getByNames(itemNames);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}

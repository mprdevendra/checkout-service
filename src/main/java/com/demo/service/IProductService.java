package com.demo.service;

import com.demo.repository.entity.Product;

import java.util.List;

public interface IProductService {

    List<Product> getProductsByNames(List<String> itemNames);
}

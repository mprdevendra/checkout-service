package com.demo.service;

import com.demo.entity.Product;

import java.util.List;

public interface IProductService {

    List<Product> getProductsByNames(List<String> itemNames);
}

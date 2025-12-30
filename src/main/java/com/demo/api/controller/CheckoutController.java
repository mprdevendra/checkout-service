package com.demo.api.controller;

import com.demo.api.dto.BasketDto;
import com.demo.exception.CheckoutServiceException;
import com.demo.service.ICheckoutService;
import com.demo.api.dto.Receipt;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
@Slf4j
public class CheckoutController {

    @Autowired
    private ICheckoutService groceryServiceImpl;

    @PostMapping("/basket/calculate")
    public ResponseEntity<?> checkout(@RequestBody @Valid BasketDto basketDto) throws CheckoutServiceException {
        log.info("Received checkout request for BasketId={}", basketDto.getBasketId());
        Receipt receiptResponse = groceryServiceImpl.checkout(basketDto);
        log.info("Returning checkout response for BasketId={}", basketDto.getBasketId());
        return new ResponseEntity<>(receiptResponse, HttpStatus.OK);
    }

}

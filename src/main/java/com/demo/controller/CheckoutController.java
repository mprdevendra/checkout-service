package com.demo.controller;


import com.demo.dto.BasketDto;
import com.demo.exception.InvalidInputException;
import com.demo.service.ICheckoutService;
import com.demo.response.dto.BasketPricingResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    @Autowired
    private ICheckoutService groceryServiceImpl;

    @PostMapping("/basket/calculate")
    public ResponseEntity<?> calculateBasketPrice(@RequestBody @Valid BasketDto basketDto) throws InvalidInputException {
        BasketPricingResponseDto calculatedBasketPrice = groceryServiceImpl.calculateBasketPrice(basketDto);
        String basketPricingDetails = groceryServiceImpl.formatResponse(calculatedBasketPrice);
        return new ResponseEntity<>(basketPricingDetails, HttpStatus.OK);
    }

}

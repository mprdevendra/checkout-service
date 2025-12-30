package com.demo.service.impl;

import com.demo.api.dto.*;
import com.demo.exception.PriceCalculationException;
import com.demo.repository.entity.Product;
import com.demo.exception.CheckoutServiceException;
import com.demo.exception.ProductNotFoundException;
import com.demo.service.ICalculationService;
import com.demo.service.IPriceService;
import com.demo.service.IProductService;
import com.demo.service.IPromotionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceImplTest {

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Mock
    private IProductService productServiceImpl;

    @Mock
    private IPromotionService promotionServiceImpl;

    @Mock
    private ICalculationService calculationServiceImpl;

    @Mock
    private IPriceService priceServiceImpl;

    /*@Test
    void testCalculateBasketPrice_Success() {
        BasketItemDto item = new BasketItemDto("Bananas", 3);
        BasketDto basketDto = new BasketDto(1, List.of(item));

        Product product = new Product("Bananas", new BigDecimal("0.50"));

        ItemDetailsDto itemDetails = new ItemDetailsDto("Apples", 2, new BigDecimal("3.00"), new BigDecimal("1.50"));
        DiscountDto discount = new DiscountDto("Promo", new BigDecimal("0.50"));

        when(priceServiceImpl.itemPrice(List.of("Bananas"))).thenReturn(List.of(product));
        when(promotionServiceImpl.promotion(List.of(itemDetails))).thenReturn(List.of(discount));


        List<ItemDetailsDto> items = priceServiceImpl.itemPrice(basketItems);
        List<DiscountDto> discounts = promotionServiceImpl.promotion(items);
        BigDecimal subTotal = calculationServiceImpl.subTotal(items);
        BigDecimal discountTotal = calculationServiceImpl.discountTotal(discounts);
        BigDecimal total = calculationServiceImpl.total(subTotal, discountTotal);
        Receipt receipt = receiptServiceImpl.receipt(items, discounts, subTotal, discountTotal, total, basketId);

        Receipt response = checkoutService.checkout(basketDto);

        assertNotNull(response);
        assertEquals(1, response.getBasketId());
        assertEquals(new BigDecimal("3.00"), response.getSubTotal());
        assertEquals(new BigDecimal("0.50"), response.getTotalDiscount());
        assertEquals(new BigDecimal("2.50"), response.getTotal());

        verify(productServiceImpl).getProductsByNames(List.of("Apples"));
        verify(promotionServiceImpl).calculateDiscount(anyList());
    }*/

    @Test
    void testCalculateBasketPrice_ProductNotFound() {
        BasketItemDto item = new BasketItemDto("Unknown", 1);
        BasketDto basketDto = new BasketDto(1, List.of(item));

        when(priceServiceImpl.itemPrice(anyList())).thenThrow(new PriceCalculationException("Exception test message"));

        CheckoutServiceException ex = assertThrows(CheckoutServiceException.class,
                () -> checkoutService.checkout(basketDto));

        assertEquals(404, ex.getErrorCode());
        assertTrue(ex.getMessage().contains("Product not found"));
    }
}

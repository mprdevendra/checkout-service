package com.demo.response.formatter;

import com.demo.exception.PrintPriceServiceException;
import com.demo.response.dto.BasketPricingResponseDto;
import org.springframework.stereotype.Component;

public class PrintFormatter {
    public static String format(BasketPricingResponseDto basketPricingResponseDto) {
        try{
            StringBuilder sb = new StringBuilder();

            sb.append("Item          Quantity    Price\n");
            sb.append("--------------------------------\n");

            basketPricingResponseDto.getItems().forEach(ele ->
                    sb.append(String.format(
                            "%-13s %-11d %.2f%n",
                            ele.getName(),
                            ele.getQuantity(),
                            ele.getPrice()
                    ))
            );

            sb.append("--------------------------------\n");
            sb.append(String.format("Subtotal:                  %.2f%n", basketPricingResponseDto.getSubTotal()));
            sb.append("\n");

            sb.append("Discounts:\n");
            basketPricingResponseDto.getDiscounts().forEach(ele ->
                    sb.append(String.format(
                            "%-25s -%.2f%n",
                            ele.getDescription(),
                            ele.getAmount()
                    ))
            );

            sb.append("--------------------------------\n");
            sb.append(String.format("Total Discount:          %.2f%n", basketPricingResponseDto.getTotalDiscount()));
            sb.append("\n");
            sb.append(String.format("Total:                   %.2f%n", basketPricingResponseDto.getTotal()));
            return sb.toString();
        }catch (Exception ex){
            throw new PrintPriceServiceException("Failed to print the basket price : "+ex.getMessage());
        }
    }
}

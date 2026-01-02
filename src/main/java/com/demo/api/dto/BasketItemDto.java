package com.demo.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasketItemDto {

    @NotBlank(message = "Item code must not be empty")
    @NotNull(message = "Item code must not be null")
    private String itemCode;

    @NotNull(message = "Quantity must be value")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}

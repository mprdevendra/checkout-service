package com.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketDto {

    @NotNull(message = "BasketId must not be null")
    @NotEmpty(message = "BasketId must not be empty")
    @Min(value = 1, message = "BasketId must be at least 1")
    private Integer basketId;

    @NotNull(message = "Basket must not be null")
    @NotEmpty(message = "Basket must not be empty")
    @Valid
    private List<BasketItemDto> basketItems;
}

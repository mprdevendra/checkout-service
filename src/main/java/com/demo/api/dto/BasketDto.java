package com.demo.api.dto;

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
    @Min(value = 1, message = "BasketId must be at least 1")
    private Integer basketId;

    @NotNull(message = "Basket item(s) must not be null")
    @NotEmpty(message = "Basket item(s) must not be empty")
    @Valid
    private List<BasketItemDto> basketItems;
}

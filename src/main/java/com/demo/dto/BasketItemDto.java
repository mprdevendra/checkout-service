package com.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
public class BasketItemDto {

    @NotBlank(message = "Item name must not be empty")
    @NotNull(message = "Item name must not be null")
    private String itemName;

    @NotNull(message = "Quantity must be value")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}

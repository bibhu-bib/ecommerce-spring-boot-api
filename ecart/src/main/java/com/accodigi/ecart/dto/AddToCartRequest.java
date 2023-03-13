package com.accodigi.ecart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class AddToCartRequest {
    @NotNull(message = "User id can't be null")
    private Long userId;
    private List<Item> products;
}
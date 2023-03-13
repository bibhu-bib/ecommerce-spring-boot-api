package com.accodigi.ecart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private Long quantity;
    private Long productId;
}

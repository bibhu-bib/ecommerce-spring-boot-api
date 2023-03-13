package com.accodigi.ecart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutRequest {
    private Long userId;

    @NotBlank(message = "Payment mode can't be empty")
    private String paymentMode;

    @NotBlank(message = "WalletId can't be empty")
    private String walletId;

}

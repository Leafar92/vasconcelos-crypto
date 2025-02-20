package com.challenge.vasconcelos.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WalletAssetInfoResponseDTO {
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal value;
}

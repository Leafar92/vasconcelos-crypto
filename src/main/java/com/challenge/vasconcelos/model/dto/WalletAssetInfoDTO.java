package com.challenge.vasconcelos.model.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WalletAssetInfoDTO {
    private String symbol;
    private BigDecimal price;
    private double quantity;

    @JsonIgnore
    private BigDecimal value;

    public WalletAssetInfoDTO(String symbol, BigDecimal price, double quantity) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
    }

    public BigDecimal getValue() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

}

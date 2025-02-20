package com.challenge.vasconcelos.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssetHistory {
    private BigDecimal priceUsd;
    private Long time;
}

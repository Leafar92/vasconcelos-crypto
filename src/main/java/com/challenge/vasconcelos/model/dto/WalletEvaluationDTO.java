package com.challenge.vasconcelos.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WalletEvaluationDTO {
    private BigDecimal total;
    private String bestAsset;
    private BigDecimal bestPerformance;
    private String worstAsset;
    private BigDecimal worstPerformance;
}

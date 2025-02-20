package com.challenge.vasconcelos.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WalletInfoDTO {
    private String email;
    private List<WalletAssetInfoDTO> assets;
    private BigDecimal totalValue;

}

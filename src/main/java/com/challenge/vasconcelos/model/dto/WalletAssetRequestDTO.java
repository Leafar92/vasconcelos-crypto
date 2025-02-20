package com.challenge.vasconcelos.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WalletAssetRequestDTO implements Serializable {

    private static final long serialVersionUID = -7600453122101974845L;

    @NotBlank
    private String symbol;
    @NotBlank
    private BigDecimal price;
    @NotBlank
    private double quantity;
}

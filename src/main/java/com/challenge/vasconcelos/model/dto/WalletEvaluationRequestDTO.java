package com.challenge.vasconcelos.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WalletEvaluationRequestDTO {
    private List<WalletAssetInfoDTO> assets;
}

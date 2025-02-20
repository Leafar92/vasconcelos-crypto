package com.challenge.vasconcelos.service;

import com.challenge.vasconcelos.model.dto.WalletEvaluationDTO;
import com.challenge.vasconcelos.model.dto.WalletEvaluationRequestDTO;

public interface WalletEvaluationService {
    WalletEvaluationDTO evaluateWallet(WalletEvaluationRequestDTO request, String interval);
}

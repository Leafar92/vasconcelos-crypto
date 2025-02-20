package com.challenge.vasconcelos.service;

import com.challenge.vasconcelos.model.Wallet;
import com.challenge.vasconcelos.model.dto.WalletAssetRequestDTO;
import com.challenge.vasconcelos.model.dto.WalletInfoDTO;

public interface WalletService {
    Wallet createWallet(String email);

    void addAssets(WalletAssetRequestDTO request, String email);

    WalletInfoDTO getAllWalletInfo(String email);
}

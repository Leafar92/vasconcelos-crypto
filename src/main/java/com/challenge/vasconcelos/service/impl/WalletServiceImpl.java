package com.challenge.vasconcelos.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.challenge.vasconcelos.model.Wallet;
import com.challenge.vasconcelos.model.WalletAsset;
import com.challenge.vasconcelos.model.dto.WalletAssetInfoDTO;
import com.challenge.vasconcelos.model.dto.WalletAssetRequestDTO;
import com.challenge.vasconcelos.model.dto.WalletInfoDTO;
import com.challenge.vasconcelos.repository.WalletRespository;
import com.challenge.vasconcelos.service.AssetService;
import com.challenge.vasconcelos.service.WalletService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRespository walletRespository;
    private final AssetService assetService;

    public WalletServiceImpl(WalletRespository walletRespository, AssetService assetService) {
        this.walletRespository = walletRespository;
        this.assetService = assetService;
    }

    @Override
    public Wallet createWallet(String email) {
        if (walletRespository.existsByEmail(email)) {
            log.info("A wallet with the email {} already existis", email);
            throw new IllegalArgumentException("A wallet with this email already exists." + email);
        }
        var wallet = new Wallet(email);
        return walletRespository.save(wallet);
    }

    @Override
    @Transactional
    public void addAssets(WalletAssetRequestDTO request, String email) {
        var wallet = getWallet(email);

        var assetByToken = assetService.getAssetBySymbol(request.getSymbol());

        if (assetByToken.getPriceUsd().compareTo(request.getPrice()) != 0) {
            log.info("The price provided {} is different from {}", request.getPrice(), assetByToken.getPriceUsd());
            throw new IllegalArgumentException(
                    String.format("The price provided %s is different from the real price %s", request.getPrice(),
                            assetByToken.getPriceUsd()));
        }
        WalletAsset walletAsset = new WalletAsset(wallet, assetByToken, request.getQuantity());

        wallet.getWalletAssets().add(walletAsset);

        walletRespository.save(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletInfoDTO getAllWalletInfo(String email) {
        var wallet = getWallet(email);
        var assetResponses = wallet.getWalletAssets().stream()
                .map(asset -> new WalletAssetInfoDTO(asset.getAsset().getSymbol(), asset.getAsset().getPriceUsd(),
                        asset.getQuantity()))
                .toList();
        var totalValue = assetResponses.stream().map(WalletAssetInfoDTO::getValue).reduce(BigDecimal.ZERO,
                BigDecimal::add);
        return new WalletInfoDTO(wallet.getEmail(), assetResponses, totalValue);
    }

    private Wallet getWallet(String email) {
        return walletRespository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No valid wallet found for email: " + email));
    }

}

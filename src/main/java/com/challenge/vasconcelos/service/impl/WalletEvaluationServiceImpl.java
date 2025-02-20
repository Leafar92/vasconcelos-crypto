package com.challenge.vasconcelos.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.challenge.vasconcelos.api.feign.client.CoinCapClient;
import com.challenge.vasconcelos.model.ApiResponseHistory;
import com.challenge.vasconcelos.model.dto.WalletAssetInfoDTO;
import com.challenge.vasconcelos.model.dto.WalletEvaluationDTO;
import com.challenge.vasconcelos.model.dto.WalletEvaluationRequestDTO;
import com.challenge.vasconcelos.service.AssetService;
import com.challenge.vasconcelos.service.WalletEvaluationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WalletEvaluationServiceImpl implements WalletEvaluationService {

    private final CoinCapClient coinCapClient;
    private final AssetService assetService;

    public WalletEvaluationServiceImpl(CoinCapClient coinCapClient, AssetService assetService) {
        this.coinCapClient = coinCapClient;
        this.assetService = assetService;
    }

    @Override
    public WalletEvaluationDTO evaluateWallet(WalletEvaluationRequestDTO request, String interval) {
        BigDecimal totalValue = BigDecimal.ZERO;
        String bestAsset = null;
        String worstAsset = null;
        BigDecimal bestPerformance = BigDecimal.ZERO;
        BigDecimal worstPerformance = BigDecimal.ZERO;

        for (WalletAssetInfoDTO asset : request.getAssets()) {
            var assetBySymbol = assetService.getAssetBySymbol(asset.getSymbol());
            var history = coinCapClient.getHistoryAsset(assetBySymbol.getId(), interval);
            BigDecimal pastPrice = getCurrentPrice(history);
            BigDecimal currentPrice = asset.getValue().divide(BigDecimal.valueOf(asset.getQuantity()),
                    RoundingMode.HALF_UP);
            BigDecimal performance = currentPrice.subtract(pastPrice).divide(pastPrice, 5, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            if (performance.compareTo(bestPerformance) > 0) {
                bestPerformance = performance;
                bestAsset = asset.getSymbol();
            }
            if (performance.compareTo(worstPerformance) < 0) {
                worstPerformance = performance;
                worstAsset = asset.getSymbol();
            }

            totalValue = totalValue.add(asset.getValue());
        }

        return new WalletEvaluationDTO(totalValue, bestAsset, bestPerformance, worstAsset, worstPerformance);
    }

    private BigDecimal getCurrentPrice(ApiResponseHistory response) {
        return response.getData().stream().reduce((first, second) -> second).map(entry -> entry.getPriceUsd())
                .orElseThrow(() -> new IllegalArgumentException("No historical data available"));
    }

}

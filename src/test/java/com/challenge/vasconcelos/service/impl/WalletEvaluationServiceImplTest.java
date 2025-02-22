package com.challenge.vasconcelos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.challenge.vasconcelos.BaseSetup;
import com.challenge.vasconcelos.api.feign.client.CoinCapClient;
import com.challenge.vasconcelos.model.ApiResponseHistory;
import com.challenge.vasconcelos.model.Asset;
import com.challenge.vasconcelos.model.AssetHistory;
import com.challenge.vasconcelos.model.dto.WalletAssetInfoDTO;
import com.challenge.vasconcelos.model.dto.WalletEvaluationDTO;
import com.challenge.vasconcelos.model.dto.WalletEvaluationRequestDTO;
import com.challenge.vasconcelos.service.AssetService;

class WalletEvaluationServiceImplTest extends BaseSetup {
    @Mock
    private CoinCapClient coinCapClient;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private WalletEvaluationServiceImpl walletEvaluationService;

    private Asset assetResponseETH;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        assetResponse = JSON_MAPPER.readValue(readTestFile("response-entities/getAssetBTC.json"), Asset.class);
        assetResponseETH = JSON_MAPPER.readValue(readTestFile("response-entities/getAssetETH.json"), Asset.class);
        Mockito.doReturn(assetResponse).when(assetService).getAssetBySymbol("BTC");
        Mockito.doReturn(assetResponseETH).when(assetService).getAssetBySymbol("ETH");
    }

    @Test
    void testEvaluateWalletSuccess() {
        WalletAssetInfoDTO btcAsset = new WalletAssetInfoDTO("BTC", BigDecimal.valueOf(20000), 1);
        WalletAssetInfoDTO ethAsset = new WalletAssetInfoDTO("ETH", BigDecimal.valueOf(5000), 2);

        WalletEvaluationRequestDTO request = new WalletEvaluationRequestDTO(List.of(btcAsset, ethAsset));

        ApiResponseHistory btcHistory = new ApiResponseHistory(
                List.of(new AssetHistory(BigDecimal.valueOf(18000), null)), null);
        ApiResponseHistory ethHistory = new ApiResponseHistory(
                List.of(new AssetHistory(BigDecimal.valueOf(2500), null)), null);

        Mockito.doReturn(btcHistory).when(coinCapClient).getHistoryAsset("bitcoin", "1d");
        Mockito.doReturn(ethHistory).when(coinCapClient).getHistoryAsset("ethereum", "1d");

        WalletEvaluationDTO result = walletEvaluationService.evaluateWallet(request, "1d");

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(30000.0), result.getTotal());
        assertEquals("ETH", result.getBestAsset());
    }

    @Test
    void testEvaluateWalletFailureNoHistory() {
        WalletAssetInfoDTO btcAsset = new WalletAssetInfoDTO("BTC", BigDecimal.valueOf(20000), 1);
        WalletEvaluationRequestDTO request = new WalletEvaluationRequestDTO(List.of(btcAsset));

        ApiResponseHistory btcHistory = new ApiResponseHistory(Collections.emptyList(), null);

        Mockito.doReturn(btcHistory).when(coinCapClient).getHistoryAsset("bitcoin", "1d");

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> walletEvaluationService.evaluateWallet(request, "1d"));
        assertEquals("No historical data available", exception.getMessage());
    }
}

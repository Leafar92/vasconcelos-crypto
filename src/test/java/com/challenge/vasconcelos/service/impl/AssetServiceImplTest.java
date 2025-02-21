package com.challenge.vasconcelos.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;

import com.challenge.vasconcelos.BaseSetup;
import com.challenge.vasconcelos.api.feign.client.CoinCapClient;
import com.challenge.vasconcelos.model.ApiResponse;
import com.challenge.vasconcelos.model.Asset;
import com.challenge.vasconcelos.repository.AssetRepository;

class AssetServiceImplTest extends BaseSetup {
    @Mock
    private CoinCapClient coinCapClient;

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetServiceImpl assetService;

    @Value("${price.update.job.interval}")
    private long updateInterval;
    @Value("${price.update.intervalCalls.api}")
    private long intervalCalls;

    private String symbol = "BTC";

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        assetResponse = JSON_MAPPER.readValue(readTestFile("response-entities/getAssetBTC.json"), Asset.class);
        Mockito.doReturn(Optional.of(assetResponse)).when(assetRepository).findBySymbol(Mockito.any());
    }

    @Test
    void testGetAssetBySymbol_Success() {
        var asset = assetService.getAssetBySymbol(symbol);
        assertThat(asset).isNotNull();
        assertThat(symbol).isEqualTo(asset.getSymbol());
    }

    @Test
    void testGetAssetBySymbol_Failure() {
        doReturn(Optional.empty()).when(assetRepository).findBySymbol(symbol);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> assetService.getAssetBySymbol(symbol));
        assertEquals("There is no token valid for the token provided" + symbol, exception.getMessage());
    }

    @Test
    void testFetchAndSaveAssets() throws Exception {
        doReturn(new ApiResponse(List.of(assetResponse), null)).when(coinCapClient).getAssets(Mockito.anyInt(),
                Mockito.anyInt());
        doReturn(new ApiResponse(List.of(assetResponse), null)).when(coinCapClient).getAssets(Mockito.anyInt(),
                Mockito.anyInt());
        doReturn(new ApiResponse(List.of(assetResponse), null)).when(coinCapClient).getAssets(Mockito.anyInt(),
                Mockito.anyInt());

        when(assetRepository.existsBySymbol(anyString())).thenReturn(false);

        assetService.fetchAndSaveAssets();

        verify(assetRepository, times(18)).save(assetResponse);
    }

    @Test
    void testSaveAssets() {
        var assets = List.of(assetResponse);
        assetService.saveAssets(assets);
        verify(assetRepository, times(1)).save(assetResponse);
    }

}

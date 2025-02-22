package com.challenge.vasconcelos.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.challenge.vasconcelos.BaseSetup;
import com.challenge.vasconcelos.model.Asset;
import com.challenge.vasconcelos.model.Wallet;
import com.challenge.vasconcelos.model.WalletAsset;
import com.challenge.vasconcelos.model.dto.WalletAssetRequestDTO;
import com.challenge.vasconcelos.repository.WalletRespository;
import com.challenge.vasconcelos.service.AssetService;

class WalletServiceImplTest extends BaseSetup {
    @Mock
    private WalletRespository walletRespository;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private WalletServiceImpl walletService;
    private String email;
    private Wallet wallet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        assetResponse = JSON_MAPPER.readValue(readTestFile("response-entities/getAssetBTC.json"), Asset.class);
        email = "test@example.com";
        wallet = new Wallet(email);
    }

    @Test
    void testCreateWalletSuccess() {
        Mockito.doReturn(false).when(walletRespository).existsByEmail(email);
        Mockito.doAnswer(invocation -> invocation.getArgument(0)).when(walletRespository).save(any(Wallet.class));

        var wallet = walletService.createWallet(email);

        assertNotNull(wallet);
        assertEquals(email, wallet.getEmail());
        verify(walletRespository, times(1)).save(wallet);
    }

    @Test
    void testCreateWalletFailureDuplicateEmail() {
        Mockito.doReturn(true).when(walletRespository).existsByEmail(email);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> walletService.createWallet(email));
        assertEquals("A wallet with this email already exists." + email, exception.getMessage());
    }

    @Test
    void testAddAssetsSuccess() {
        WalletAssetRequestDTO request = new WalletAssetRequestDTO("BTC", BigDecimal.valueOf(30000), 1);

        Mockito.doReturn(Optional.of(wallet)).when(walletRespository).findByEmail(email);
        Mockito.doReturn(assetResponse).when(assetService).getAssetBySymbol("BTC");
        Mockito.doAnswer(invocation -> invocation.getArgument(0)).when(walletRespository).save(any(Wallet.class));

        walletService.addAssets(request, email);

        assertEquals(1, wallet.getWalletAssets().size());
        verify(walletRespository, times(1)).save(wallet);
    }

    @Test
    void testAddAssetsFailurePriceMismatch() {
        var asset = Asset.builder().priceUsd(BigDecimal.valueOf(25000)).id("BTC").name("bitcoin").build();
        WalletAssetRequestDTO request = new WalletAssetRequestDTO("BTC", BigDecimal.valueOf(20000), 1);

        Mockito.doReturn(Optional.of(wallet)).when(walletRespository).findByEmail(email);
        Mockito.doReturn(asset).when(assetService).getAssetBySymbol("BTC");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> walletService.addAssets(request, email));
        assertTrue(exception.getMessage().contains("The price provided 20000 is different from the real price 25000"));
    }

    @Test
    void testGetAllWalletInfoSuccess() {
        var walletAsset = new WalletAsset(wallet, assetResponse, 1);
        wallet.getWalletAssets().add(walletAsset);

        Mockito.doReturn(Optional.of(wallet)).when(walletRespository).findByEmail(email);
        var walletInfo = walletService.getAllWalletInfo(email);

        assertNotNull(walletInfo);
        assertEquals(email, walletInfo.getEmail());
        assertEquals(1, walletInfo.getAssets().size());
        assertEquals(BigDecimal.valueOf(30000.0), walletInfo.getTotalValue());
    }

    @Test
    void testGetAllWalletInfoFailureWalletNotFound() {
        String email = "nonexistent@example.com";
        when(walletRespository.findByEmail(email)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> walletService.getAllWalletInfo(email));
        assertEquals("No valid wallet found for email: " + email, exception.getMessage());
    }
}

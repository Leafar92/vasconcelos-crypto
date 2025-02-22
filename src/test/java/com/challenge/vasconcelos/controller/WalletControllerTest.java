package com.challenge.vasconcelos.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.challenge.vasconcelos.model.Wallet;
import com.challenge.vasconcelos.model.dto.*;
import com.challenge.vasconcelos.service.WalletEvaluationService;
import com.challenge.vasconcelos.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(WalletController.class)
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @MockBean
    private WalletEvaluationService walletEvaluationService;

    @Autowired
    private ObjectMapper objectMapper;

    private String email = "test@example.com";

    @Test
    void testCreateWallet() throws Exception {
        var mockWallet = new Wallet();
        Mockito.doReturn(mockWallet).when(walletService).createWallet(email);

        mockMvc.perform(post("/api/v1/wallets").param("email", email)).andExpect(status().isCreated());
    }

    @Test
    void testAddAssetsToWallet() throws Exception {
        var request = new WalletAssetRequestDTO();

        mockMvc.perform(put("/api/v1/wallets/assets/{email}", email).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isNoContent());
    }

    @Test
    void testGetWalletInfo() throws Exception {
        WalletInfoDTO mockInfo = new WalletInfoDTO(email, Collections.emptyList(), BigDecimal.ONE);
        Mockito.doReturn(mockInfo).when(walletService).getAllWalletInfo(email);

        mockMvc.perform(get("/api/v1/wallets/{email}", email)).andExpect(status().isOk());
    }

    @Test
    void testGetEvaluation() throws Exception {
        var request = new WalletEvaluationRequestDTO();
        var interval = "weekly";
        var mockEvaluation = new WalletEvaluationDTO();
        Mockito.doReturn(mockEvaluation).when(walletEvaluationService).evaluateWallet(request, interval);

        mockMvc.perform(post("/api/v1/wallets/evaluate/{interval}", interval).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());
    }

    @Test
    void testHandleIllegalArgumentException() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Invalid data")).when(walletService).createWallet(email);
        mockMvc.perform(post("/api/v1/wallets").param("email", email)).andExpect(status().isBadRequest());
    }

    @Test
    void testHandleGenericException() throws Exception {
        Mockito.doThrow(new RuntimeException("Unexpected error")).when(walletService).createWallet(email);
        mockMvc.perform(post("/api/v1/wallets").param("email", email)).andExpect(status().isInternalServerError());
    }
}

package com.challenge.vasconcelos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.challenge.vasconcelos.model.Wallet;
import com.challenge.vasconcelos.model.dto.*;
import com.challenge.vasconcelos.service.WalletEvaluationService;
import com.challenge.vasconcelos.service.WalletService;

@RestController
public class WalletController extends BaseController {

    private final WalletService walletService;

    private final WalletEvaluationService walletEvaluationService;

    public WalletController(WalletService walletService, WalletEvaluationService walletEvaluationService) {
        this.walletService = walletService;
        this.walletEvaluationService = walletEvaluationService;
    }

    @PostMapping("/api/v1/wallets")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Wallet createWallet(@RequestParam("email") String email) {
        return walletService.createWallet(email);
    }

    @PutMapping("/api/v1/wallets/assets/{email}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void createWallet(@PathVariable("email") String email,
            @RequestBody(required = true) WalletAssetRequestDTO request) {
        walletService.addAssets(request, email);
    }

    @GetMapping("/api/v1/wallets/{email}")
    public WalletInfoDTO getWalletInfo(@PathVariable("email") String email) {
        return walletService.getAllWalletInfo(email);
    }

    @PostMapping("/api/v1/wallets/evaluate/{interval}")
    public WalletEvaluationDTO getEvaluation(@RequestBody WalletEvaluationRequestDTO request,
            @PathVariable("interval") String interval) {
        return walletEvaluationService.evaluateWallet(request, interval);
    }
}

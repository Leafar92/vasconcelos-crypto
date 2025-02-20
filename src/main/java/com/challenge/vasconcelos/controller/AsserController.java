package com.challenge.vasconcelos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.vasconcelos.model.ApiResponse;
import com.challenge.vasconcelos.service.impl.AssetServiceImpl;

@RestController
public class AsserController {
    private final AssetServiceImpl coinCapService;

    public AsserController(AssetServiceImpl coinCapService) {
        this.coinCapService = coinCapService;
    }

    @GetMapping("/v1/assets/{tokenName}")
    public ApiResponse getAssetByToken(@PathVariable("tokenName") String tokenName) {
        return coinCapService.getAssetByTokenExternal(tokenName);
    }
}

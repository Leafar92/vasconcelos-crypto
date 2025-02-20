package com.challenge.vasconcelos.api.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.challenge.vasconcelos.model.ApiResponse;
import com.challenge.vasconcelos.model.ApiResponseHistory;

@FeignClient(name = "coinCapClient", url = "https://api.coincap.io/v2")
public interface CoinCapClient {

    @GetMapping("/assets/{tokenName}")
    public ApiResponse getAssetByToken(@PathVariable("tokenName") String tokenName);

    @GetMapping("/assets")
    public ApiResponse getAssets(@RequestParam("limit") int limit, @RequestParam("offset") int offset);

    @GetMapping("/assets/{tokenName}/history")
    public ApiResponseHistory getHistoryAsset(@PathVariable("tokenName") String tokenName,
            @RequestParam("interval") String interval);
}

package com.challenge.vasconcelos.service;

import com.challenge.vasconcelos.model.ApiResponse;
import com.challenge.vasconcelos.model.Asset;

public interface AssetService {
    ApiResponse getAssetByTokenExternal(String tokenName);

    Asset getAssetBySymbol(String symbol);
}

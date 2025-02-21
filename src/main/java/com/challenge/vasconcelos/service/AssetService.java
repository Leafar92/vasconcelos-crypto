package com.challenge.vasconcelos.service;

import java.util.List;

import com.challenge.vasconcelos.model.Asset;

public interface AssetService {
    Asset getAssetBySymbol(String symbol);

    void saveAssets(List<Asset> assets);

    void fetchAndSaveAssets();
}

package com.challenge.vasconcelos.service.impl;

import java.util.List;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.challenge.vasconcelos.api.feign.client.CoinCapClient;
import com.challenge.vasconcelos.model.Asset;
import com.challenge.vasconcelos.repository.AssetRepository;
import com.challenge.vasconcelos.service.AssetService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetServiceImpl implements AssetService {

    private final CoinCapClient coinCapClient;
    private final ExecutorService executorService;
    private final AssetRepository assetRepository;

    @Value("${price.update.job.interval}")
    private long updateInterval;

    @Value("${price.update.intervalCalls.api}")
    private long intervalCalls;

    public AssetServiceImpl(CoinCapClient coinCapClient, AssetRepository assetRepository) {
        this.coinCapClient = coinCapClient;
        this.executorService = Executors.newFixedThreadPool(3);
        this.assetRepository = assetRepository;
    }

    @Override
    public Asset getAssetBySymbol(String symbol) {
        var assetByToken = assetRepository.findBySymbol(symbol).orElseThrow(
                () -> new IllegalArgumentException("There is no token valid for the token provided" + symbol));
        return assetByToken;
    }

    @Scheduled(fixedDelayString = "${price.update.job.interval}")
    public void fetchAndSaveAssets() {
        int offset = 0;
        int limit = 3;

        while (offset < 50) { // Stop when offset reaches 50 or while (true)
            try {
                final int currentOffset = offset;

                // Create 3 parallel fetch tasks with different offsets
                CompletableFuture<List<Asset>> future1 = CompletableFuture
                        .supplyAsync(() -> coinCapClient.getAssets(currentOffset, limit).getData(), executorService);
                CompletableFuture<List<Asset>> future2 = CompletableFuture.supplyAsync(
                        () -> coinCapClient.getAssets(currentOffset + limit, limit).getData(), executorService);
                CompletableFuture<List<Asset>> future3 = CompletableFuture.supplyAsync(
                        () -> coinCapClient.getAssets(currentOffset + 2 * limit, limit).getData(), executorService);

                // Wait for all requests to complete
                List<Asset> assets1 = future1.get();
                List<Asset> assets2 = future2.get();
                List<Asset> assets3 = future3.get();

                // Stop if there are no more assets to fetch
                // This should be the right condition to stop the method, but it is to much
                // registers to be saved
//                if (assets1.isEmpty() || assets2.isEmpty() || assets3.isEmpty()) {
//                    System.out.println("✅ No more assets to fetch. Stopping.");
//                    break;
//                }

                // Save fetched assets immediately
                saveAssets(assets1);
                saveAssets(assets2);
                saveAssets(assets3);

                // Move to the next batch
                offset += limit * 3; //

                // Sleep to respect API rate limits (configurable)
                TimeUnit.MILLISECONDS.sleep(intervalCalls);

            } catch (Exception e) {
                throw new RuntimeException("Error fetching or saving assets", e);
            }
        }

        // Shutdown thread pool after completing all fetches
        executorService.shutdown();
        log.info("Fetching completed! Offset reached 50.");
    }

    public void saveAssets(List<Asset> assets) {
        for (Asset asset : assets) {
            if (!assetRepository.existsBySymbol(asset.getSymbol())) {
                assetRepository.save(asset);
            }
        }
    }

}

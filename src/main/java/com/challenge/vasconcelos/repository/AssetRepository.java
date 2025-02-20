package com.challenge.vasconcelos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.vasconcelos.model.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String> {
    boolean existsBySymbol(String symbol);

    Optional<Asset> findBySymbol(String symbol);
}

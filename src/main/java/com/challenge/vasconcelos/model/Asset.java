package com.challenge.vasconcelos.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assets")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_number")
    private Long idNumber;

    private String id;

    private String rank;
    private String symbol;
    private String name;

    private BigDecimal supply;

    @JsonProperty("max_supply")
    private BigDecimal maxSupply;

    @Column(name = "market_cap_usd")
    @JsonProperty("marketCapUsd")
    private BigDecimal marketCapUsd;

    @Column(name = "volume_usd_24h")
    @JsonProperty("volumeUsd24Hr")
    private BigDecimal volumeUsd24Hr;

    @Column(name = "price_usd")
    @JsonProperty("priceUsd")
    private BigDecimal priceUsd;

    @Column(name = "change_percent_24h")
    @JsonProperty("changePercent24Hr")
    private BigDecimal changePercent24Hr;

    @Column(name = "vwap_24h")
    @JsonProperty("vwap24Hr")
    private BigDecimal vwap24Hr;

    private String explorer;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<WalletAsset> walletAssets = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Asset asset = (Asset) o;
        return Objects.equals(id, asset.id) && Objects.equals(symbol, asset.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol);
    }

}

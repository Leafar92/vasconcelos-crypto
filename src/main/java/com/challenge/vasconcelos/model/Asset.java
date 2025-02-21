package com.challenge.vasconcelos.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assets")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(name = "max_supply")
    private BigDecimal maxSupply;

    @Column(name = "market_cap_usd")
    private BigDecimal marketCapUsd;

    @Column(name = "volume_usd_24h")
    private BigDecimal volumeUsd24Hr;

    @Column(name = "price_usd")
    private BigDecimal priceUsd;

    @Column(name = "change_percent_24h")
    private BigDecimal changePercent24Hr;

    @Column(name = "vwap_24h")
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

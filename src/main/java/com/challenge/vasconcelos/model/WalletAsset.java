package com.challenge.vasconcelos.model;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "wallet_asset")
@NoArgsConstructor
public class WalletAsset {

    public WalletAsset(Wallet wallet, Asset asset, double quantity) {
        this.wallet = wallet;
        this.asset = asset;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "asset_id_number", nullable = false)
    private Asset asset;

    private double quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        WalletAsset that = (WalletAsset) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

package com.challenge.vasconcelos.model;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wallet")
@Getter
@NoArgsConstructor
public class Wallet {

    public Wallet(String email) {
        this.email = email;
        this.id = UUID.randomUUID();
    }

    @Id
    @Column(nullable = false, updatable = false) // Ensure immutability
    @JsonIgnore
    private UUID id;

    @NotNull
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<WalletAsset> walletAssets = new HashSet<>();

    @PrePersist
    private void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id) && Objects.equals(email, wallet.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}

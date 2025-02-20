package com.challenge.vasconcelos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.vasconcelos.model.Wallet;

@Repository
public interface WalletRespository extends JpaRepository<Wallet, Long> {
    boolean existsByEmail(String email);

    Optional<Wallet> findByEmail(String email);
}

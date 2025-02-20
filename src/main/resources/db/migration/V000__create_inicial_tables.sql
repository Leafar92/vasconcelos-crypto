CREATE TABLE wallet (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT email_check UNIQUE (email)
);

CREATE TABLE assets (
    id_number BIGINT AUTO_INCREMENT PRIMARY KEY,
    id VARCHAR(255) NOT NULL,
    rank VARCHAR(50),
    symbol VARCHAR(50),
    name VARCHAR(255),
    supply DECIMAL(30, 8),
    max_supply DECIMAL(30, 8),
    market_cap_usd DECIMAL(30, 8),
    volume_usd_24h DECIMAL(30, 8),
    price_usd DECIMAL(30, 8),
    change_percent_24h DECIMAL(10, 2),
    vwap_24h DECIMAL(30, 8),
    explorer VARCHAR(255)
);

CREATE TABLE wallet_asset (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id UUID NOT NULL,
    asset_id_number BIGINT NOT NULL,
    quantity DOUBLE NOT NULL,
    CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES wallet(id),
    CONSTRAINT fk_asset FOREIGN KEY (asset_id_number) REFERENCES assets(id_number)
);
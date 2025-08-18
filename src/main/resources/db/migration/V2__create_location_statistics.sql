-- Create location_statistics table
CREATE TABLE location_statistics (
    id BIGSERIAL PRIMARY KEY,
    location_id BIGINT NOT NULL,
    out_of_stock_count INT NOT NULL DEFAULT 0,
    expired_count INT NOT NULL DEFAULT 0,
    near_expiry_count INT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_location_statistics_location
        FOREIGN KEY (location_id) REFERENCES locations(id)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT uk_location_statistics_location_active
        UNIQUE (location_id, is_active)
);

-- Create index for better query performance
CREATE INDEX idx_location_statistics_location_id ON location_statistics(location_id);
CREATE INDEX idx_location_statistics_active ON location_statistics(is_active);
CREATE INDEX idx_location_statistics_last_updated ON location_statistics(last_updated);

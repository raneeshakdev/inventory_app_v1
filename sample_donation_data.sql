-- Sample data for location_donation_stats table
-- This will add donation data for August 2025 (current month/year)
-- Using actual location IDs: 1, 2, 3, 4

INSERT INTO public.location_donation_stats (location_id, total_donation, month, year, week, created_at, updated_at) VALUES
-- Location ID 1 (Bangalore)
(1, 28848.00, 8, 2025, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(1, 15000.00, 8, 2025, 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Location ID 2 (Mangaluru)
(2, 1508.00, 8, 2025, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2500.00, 8, 2025, 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Location ID 3 (Udupi)
(3, 5848.00, 8, 2025, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3200.00, 8, 2025, 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Location ID 4 (Hassan)
(4, 24644.00, 8, 2025, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 12000.00, 8, 2025, 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Check what locations exist in the locations table first
-- SELECT id, name FROM public.locations WHERE id IN (1, 2, 3, 4) ORDER BY id;

-- You can also add data for previous months for testing the monthly endpoint
INSERT INTO public.location_donation_stats (location_id, total_donation, month, year, week, created_at, updated_at) VALUES
-- July 2025 data
(1, 25000.00, 7, 2025, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 8000.00, 7, 2025, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 12000.00, 7, 2025, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 18000.00, 7, 2025, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- June 2025 data
(1, 22000.00, 6, 2025, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 7500.00, 6, 2025, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 11000.00, 6, 2025, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 16000.00, 6, 2025, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

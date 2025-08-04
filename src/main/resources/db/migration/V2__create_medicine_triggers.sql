-- Script: V2__create_medicine_triggers.sql

-- ================================================
-- Cleanup (safe drops if re-running this migration)
-- ================================================
DROP TRIGGER IF EXISTS tr_update_medicine_stock_status ON medicine_purchase_batches;
DROP FUNCTION IF EXISTS update_medicine_stock_status();

DROP TRIGGER IF EXISTS tr_update_batch_quantity_after_distribution ON medicina_distribution_items;
DROP FUNCTION IF EXISTS update_batch_quantity_after_distribution();

DROP TRIGGER IF EXISTS tr_update_distribution_total_items ON medicina_distribution_items;
DROP FUNCTION IF EXISTS update_distribution_total_items();

-- ================================================
-- 1. Trigger: Update medicine stock status after update
-- ================================================
CREATE OR REPLACE FUNCTION update_medicine_stock_status()
RETURNS TRIGGER AS $$
DECLARE
    total_current_stock INT := 0;
    stock_threshold INT := 0;
BEGIN
    SELECT COALESCE(SUM(current_quantity), 0)
    INTO total_current_stock
    FROM medicine_purchase_batches 
    WHERE medicine_id = NEW.medicine_id AND is_active = TRUE;

    SELECT stock_threshold 
    INTO stock_threshold
    FROM medicines 
    WHERE id = NEW.medicine_id;

    UPDATE medicines 
    SET is_out_of_stock = (total_current_stock <= stock_threshold),
        last_modified_at = CURRENT_TIMESTAMP
    WHERE id = NEW.medicine_id;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_update_medicine_stock_status
AFTER UPDATE ON medicine_purchase_batches
FOR EACH ROW
EXECUTE FUNCTION update_medicine_stock_status();

-- ================================================
-- 2. Trigger: Update batch quantity after distribution insert
-- ================================================
CREATE OR REPLACE FUNCTION update_batch_quantity_after_distribution()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE medicine_purchase_batches 
    SET current_quantity = current_quantity - NEW.quantity,
        last_updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.batch_id;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_update_batch_quantity_after_distribution
AFTER INSERT ON medicina_distribution_items
FOR EACH ROW
EXECUTE FUNCTION update_batch_quantity_after_distribution();

-- ================================================
-- 3. Trigger: Update total items in distribution
-- ================================================
CREATE OR REPLACE FUNCTION update_distribution_total_items()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE medicina_distribution 
    SET total_items = (
        SELECT COUNT(*) 
        FROM medicina_distribution_items 
        WHERE distribution_id = NEW.distribution_id
    )
    WHERE id = NEW.distribution_id;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_update_distribution_total_items
AFTER INSERT ON medicina_distribution_items
FOR EACH ROW
EXECUTE FUNCTION update_distribution_total_items();

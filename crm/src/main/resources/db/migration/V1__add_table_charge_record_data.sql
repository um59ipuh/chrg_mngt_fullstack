-- V1__add_table_charge_record_data.sql

CREATE TABLE IF NOT EXISTS cdrs
(
    session_id          INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id          VARCHAR(255)   NOT NULL,
    start_time          TIMESTAMP      NOT NULL,
    end_time            TIMESTAMP      NOT NULL,
    total_cost          DECIMAL(10, 2) NOT NULL
);
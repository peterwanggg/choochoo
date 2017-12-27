CREATE SCHEMA IF NOT EXISTS common;
CREATE TABLE IF NOT EXISTS common.category (
    category_id BIGSERIAL PRIMARY KEY,
    category_name text
);
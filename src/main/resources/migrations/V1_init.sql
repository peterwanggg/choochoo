CREATE SCHEMA IF NOT EXISTS common;

CREATE TABLE IF NOT EXISTS common.category (
    category_id BIGSERIAL PRIMARY KEY,
    category_name text,
    category_type text
);

CREATE TABLE IF NOT EXISTS common.location (
    location_id BIGSERIAL PRIMARY KEY,
    location_name text,
    location_type text,
    api_provider_type text,
    api_provider_id text,
    CONSTRAINT api_id UNIQUE (api_provider_type, api_provider_id)
);

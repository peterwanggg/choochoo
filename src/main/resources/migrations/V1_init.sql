DROP SCHEMA common cascade;

CREATE SCHEMA IF NOT EXISTS common;

CREATE TABLE IF NOT EXISTS common.category (
    category_id BIGSERIAL PRIMARY KEY,
    category_name text NOT NULL,
    category_type text NOT NULL,
    api_provider_type text NOT NULL,
    api_provider_id text NOT NULL,
    CONSTRAINT category_key UNIQUE (category_name, category_type),
    CONSTRAINT category_api_id UNIQUE (api_provider_type, api_provider_id)
);

CREATE TABLE IF NOT EXISTS common.location (
    location_id BIGSERIAL PRIMARY KEY,
    location_name text NOT NULL,
    location_type text NOT NULL,
    api_provider_type text NOT NULL,
    api_provider_id text NOT NULL,
    CONSTRAINT location_api_id UNIQUE (api_provider_type, api_provider_id)
);

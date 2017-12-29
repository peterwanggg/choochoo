--liquibase formatted sql

--changeset pwang:1
--DROP SCHEMA common cascade;
--CREATE SCHEMA common;

CREATE TABLE common.category (
    category_id BIGSERIAL PRIMARY KEY,
    category_name text NOT NULL,
    category_type text NOT NULL,
    api_provider_type text NOT NULL,
    api_provider_id text NOT NULL,
    CONSTRAINT category_key UNIQUE (category_name, category_type),
    CONSTRAINT category_api_key UNIQUE (api_provider_type, api_provider_id)
);

CREATE TABLE common.location (
    location_id BIGSERIAL PRIMARY KEY,
    location_name text NOT NULL,
    location_type text NOT NULL,
    api_provider_type text NOT NULL,
    api_provider_id text NOT NULL,
    CONSTRAINT location_api_key UNIQUE (api_provider_type, api_provider_id)
);

CREATE TABLE common.contestant (
    contestant_id BIGSERIAL PRIMARY KEY,
    category_id BIGSERIAL REFERENCES common.category (category_id),
    contestant_name text NOT NULL,
    image_url text NOT NULL,
    api_provider_type text NOT NULL,
    api_provider_id text NOT NULL,
    CONSTRAINT contestant_api_key UNIQUE (api_provider_type, api_provider_id),
    CONSTRAINT category_contestant_key UNIQUE (category_id, contestant_id)
);


CREATE TABLE common.kings_user (
    kings_user_id BIGSERIAL PRIMARY KEY,
    name text NOT NULL,
    roles text[] NOT NULL default '{}',

    CONSTRAINT name_key UNIQUE (name)
);

CREATE TABLE common.bout (
    bout_id BIGSERIAL PRIMARY KEY,
    category_id BIGSERIAL NOT NULL,
    winner_contestant_id BIGSERIAL NOT NULL,
    loser_contestant_id BIGSERIAL NOT NULL,
    kings_user_id BIGSERIAL NOT NULL REFERENCES common.kings_user,
    create_time TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp NOT NULL,

    FOREIGN KEY (category_id, winner_contestant_id) REFERENCES common.contestant (category_id, contestant_id) ON UPDATE CASCADE,
    FOREIGN KEY (category_id, loser_contestant_id) REFERENCES common.contestant (category_id, contestant_id) ON UPDATE CASCADE

);
CREATE INDEX bout_kings_user_id ON common.bout(kings_user_id);













insert into common.kings_user (name) values ('pete');
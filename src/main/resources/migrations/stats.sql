--liquibase formatted sql

--changeset pwang:1
DROP SCHEMA stats cascade;
CREATE SCHEMA IF NOT EXISTS stats;


CREATE MATERIALIZED VIEW stats.contestant AS
SELECT
    c.contestant_id AS contestant_id,
    c.category_id AS category_id,
    CASE WHEN w.win_count IS NULL THEN 0 ELSE w.win_count END AS win_count,
    CASE WHEN l.lose_count IS NULL THEN 0 ELSE l.lose_count END AS lose_count
FROM
    common.contestant c
LEFT JOIN
    (
    SELECT
        winner_contestant_id,
        COUNT(*) AS win_count
    FROM
        common.bout
    GROUP BY
        winner_contestant_id
    ) AS w
ON
    w.winner_contestant_id = c.contestant_id
LEFT JOIN
    (
    SELECT
        loser_contestant_id,
        COUNT(*) AS lose_count
    FROM
        common.bout
    GROUP BY
        loser_contestant_id
    ) AS l
ON
    l.loser_contestant_id = c.contestant_id;
CREATE UNIQUE INDEX contestant_stats_idx ON stats.contestant (contestant_id);


CREATE TABLE stats.contestant_rank (
    contestant_id BIGSERIAL NOT NULL REFERENCES common.contestant (contestant_id),
    category_id BIGSERIAL NOT NULL REFERENCES common.category (category_id),
    rank INT NOT NULL,
    rank_type text NOT NULL
);
CREATE UNIQUE INDEX contestant_rank_idx ON stats.contestant_rank (contestant_id);




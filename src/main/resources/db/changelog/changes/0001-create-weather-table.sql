--liquibase formatted sql

--changeset searcher-weather:1

CREATE TABLE weather
(
    id            INT                   NOT NULL    AUTO_INCREMENT,
    city          VARCHAR(255)          NOT NULL,
    country       VARCHAR(255)          NOT NULL,
    temperature   NUMERIC(5, 2)         NOT NULL,
    timestamp     TIMESTAMP             NOT NULL,
    PRIMARY KEY (id)
);


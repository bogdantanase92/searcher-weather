--liquibase formatted sql

--changeset searcher-weather:2

CREATE INDEX city_index ON weather(city);
CREATE INDEX timestamp_index ON weather(timestamp);


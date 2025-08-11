CREATE TABLE translation
(
    id       VARCHAR(1000) NOT NULL,
    language VARCHAR(10)   NOT NULL DEFAULT 'en',
    text     TEXT          NOT NULL,
    created_timestamp  TIMESTAMP              DEFAULT CURRENT_TIMESTAMP,
    modified_timestamp TIMESTAMP,
    CONSTRAINT pk_translation PRIMARY KEY (id, language)
);
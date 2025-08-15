CREATE TABLE config
(
    id          VARCHAR(200)  NOT NULL,
    value       VARCHAR(1000) NOT NULL,
    description VARCHAR(1000),
    CONSTRAINT pk_config PRIMARY KEY (id)
);
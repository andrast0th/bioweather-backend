CREATE TABLE device
(
    push_token        VARCHAR(200) NOT NULL,
    user_id           VARCHAR(200) NOT NULL,
    device_info       VARCHAR(1000),
    updated_timestamp TIMESTAMP,
    is_disabled       BOOLEAN      NOT NULL DEFAULT FALSE,
    language          VARCHAR(10)  NOT NULL DEFAULT 'en',
    timezone_offset   INT          NOT NULL DEFAULT 0,
    CONSTRAINT pk_device PRIMARY KEY (push_token)
);
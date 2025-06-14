CREATE TABLE notification_subscription
(
    push_token                        VARCHAR(200)  NOT NULL,
    user_id                           VARCHAR(200)  NOT NULL,
    device_info                       VARCHAR(1000),
    updated_via_subscribe             TIMESTAMP,
    CONSTRAINT pk_notification_subscription PRIMARY KEY (push_token)
);
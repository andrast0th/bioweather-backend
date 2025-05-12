CREATE TABLE notification_subscription
(
    push_token        VARCHAR(200) NOT NULL,
    user_id           VARCHAR(200) NOT NULL,
    device_id         VARCHAR(200) NOT NULL,
    location_id       VARCHAR(200) NOT NULL,
    notification_type VARCHAR(200) NOT NULL,
    CONSTRAINT pk_notification_subscription PRIMARY KEY (push_token)
);
CREATE TABLE notification_subscription
(
    push_token         VARCHAR(200) NOT NULL,
    notification_type  VARCHAR(200) NOT NULL,
    location_id        VARCHAR(200) NOT NULL,
    creation_timestamp TIMESTAMP,
    CONSTRAINT pk_notification_subscription PRIMARY KEY (push_token, notification_type, location_id),
    CONSTRAINT fk_notification_device FOREIGN KEY (push_token) REFERENCES device (push_token)
);
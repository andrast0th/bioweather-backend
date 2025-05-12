ALTER TABLE notification_subscription
    DROP CONSTRAINT IF EXISTS pk_notification_subscription;

ALTER TABLE notification_subscription
    ADD CONSTRAINT pk_notification_subscription PRIMARY KEY (push_token, location_id);
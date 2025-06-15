ALTER TABLE notification_subscription
ADD COLUMN is_disabled BOOLEAN NOT NULL DEFAULT FALSE;

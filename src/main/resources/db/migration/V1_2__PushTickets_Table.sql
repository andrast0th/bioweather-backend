CREATE TABLE push_ticket
(
    id                  VARCHAR(200) NOT NULL,
    push_token          VARCHAR(200) NOT NULL,
    receipt_status     VARCHAR(20),
    receipt_error      VARCHAR(200),
    was_receipt_checked BOOLEAN      NOT NULL DEFAULT FALSE,
    receipt_checked_at  TIMESTAMP,
    ticket_created_at   TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_push_ticket PRIMARY KEY (id),
    CONSTRAINT fk_push_ticket_push_token FOREIGN KEY (push_token) REFERENCES notification_subscription (push_token) ON DELETE CASCADE
);
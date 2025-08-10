CREATE TABLE push_ticket
(
    id                  VARCHAR(200)  NOT NULL,
    push_token          VARCHAR(200)  NOT NULL,
    notification_title  VARCHAR(1500) NOT NULL,
    notification_body   TEXT          NOT NULL,
    notification_type   VARCHAR(20)   NOT NULL,
    location_id         VARCHAR(200)  NULL,
    receipt_status      VARCHAR(20),
    receipt_error       VARCHAR(200),
    was_receipt_checked BOOLEAN       NOT NULL DEFAULT FALSE,
    receipt_checked_at  TIMESTAMP,
    ticket_created_at   TIMESTAMP              DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_push_ticket PRIMARY KEY (id)
);
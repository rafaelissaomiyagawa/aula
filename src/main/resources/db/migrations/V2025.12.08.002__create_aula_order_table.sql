CREATE SEQUENCE aula_order_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE aula_order (
    id BIGINT NOT NULL,
    order_number VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    order_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status VARCHAR(255) NOT NULL,
    total_amount NUMERIC(19, 2) NOT NULL,
    free_shipping BOOLEAN NOT NULL,
    manual_review BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_aula_order_number UNIQUE (order_number)
);

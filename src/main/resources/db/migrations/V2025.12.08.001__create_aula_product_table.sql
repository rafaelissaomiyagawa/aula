CREATE SEQUENCE aula_product_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE aula_product (
    id BIGINT NOT NULL,
    sku VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    active BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_aula_product_sku UNIQUE (sku)
);

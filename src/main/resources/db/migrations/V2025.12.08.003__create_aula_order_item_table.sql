CREATE SEQUENCE aula_order_item_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE aula_order_item (
    id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(19, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_aula_order_item_order FOREIGN KEY (order_id) REFERENCES aula_order (id),
    CONSTRAINT fk_aula_order_item_product FOREIGN KEY (product_id) REFERENCES aula_product (id)
);

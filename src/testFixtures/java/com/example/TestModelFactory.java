package com.example;

import com.example.junit.model.dto.OrderItemRequest;
import com.example.junit.model.dto.OrderRequest;
import com.example.junit.model.entity.Product;
import org.instancio.Instancio;
import org.instancio.Model;

import java.math.BigDecimal;

import static org.instancio.Select.field;

public class TestModelFactory {

    public static final Model<Product> PRODUCT_MODEL = Instancio.of(Product.class)
            .generate(field(Product::getSku), gen -> gen.text().pattern("#C#C#C-#d#d#d#d#d"))
            .generate(field(Product::getPrice), gen -> gen.math().bigDecimal().min(new BigDecimal("10")).max(new BigDecimal("100")))
            .generate(field(Product::getStockQuantity), gen -> gen.ints().min(0))
            .set(field(Product::isActive), true)
            .ignore(field(Product::getId))
            .toModel();

    public static final Model<OrderRequest> ORDER_REQUEST_MODEL = Instancio.of(OrderRequest.class)
            .generate(field(OrderRequest::customerEmail), gen -> gen.net().email())
            .toModel();

    public static final Model<OrderItemRequest> ORDER_ITEM_REQUEST_MODEL = Instancio.of(OrderItemRequest.class)
            .generate(field(OrderItemRequest::productId), gen -> gen.longs().min(1L))
            .generate(field(OrderItemRequest::quantity), gen -> gen.ints().range(1, 10))
            .toModel();

    public static Product createProduct() {
        return Instancio.create(PRODUCT_MODEL);
    }

    public static OrderRequest createOrderRequest() {
        return Instancio.create(ORDER_REQUEST_MODEL);
    }

    public static OrderItemRequest createOrderItemRequest() {
        return Instancio.create(ORDER_ITEM_REQUEST_MODEL);
    }
}

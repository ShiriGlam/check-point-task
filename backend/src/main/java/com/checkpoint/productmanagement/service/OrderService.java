package com.checkpoint.productmanagement.service;

import com.checkpoint.productmanagement.dto.OrderDto;
import com.checkpoint.productmanagement.entity.Order;
import com.checkpoint.productmanagement.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductService productService;
    private final LoggingService loggingService;
    private final List<Order> orders = new ArrayList<>();
    private final AtomicLong orderIdGenerator = new AtomicLong(1);

    public Order processOrder(OrderDto orderDto) {
        // Get the product
        Optional<Product> productOpt = productService.getProductById(orderDto.getProductId());
        if (productOpt.isEmpty()) {
            throw new IllegalArgumentException("Product not found with ID: " + orderDto.getProductId());
        }

        Product product = productOpt.get();
        
        if (product.getQuantity() < orderDto.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + product.getQuantity() + ", Requested: " + orderDto.getQuantity());
        }

        // Create order
        Order order = new Order(
            orderIdGenerator.getAndIncrement(),
            product.getId(),
            product.getName(),
            orderDto.getQuantity(),
            LocalDateTime.now()
        );
        orders.add(order);


        int newQuantity = product.getQuantity() - orderDto.getQuantity();
        product.setQuantity(newQuantity);
        product.setLowStock(newQuantity < 5);
        product.setUpdatedAt(LocalDateTime.now());

        // Log the order
        loggingService.logOperation("ORDER", product.getId(), product.getName(), orderDto.getQuantity());

        return order;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }

    public Optional<Order> getOrderById(Long id) {
        return orders.stream().filter(o -> o.getId().equals(id)).findFirst();
    }
} 
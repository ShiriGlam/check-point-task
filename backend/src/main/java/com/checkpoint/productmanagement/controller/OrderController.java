package com.checkpoint.productmanagement.controller;

import com.checkpoint.productmanagement.dto.OrderDto;
import com.checkpoint.productmanagement.entity.Order;
import com.checkpoint.productmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    

    @PostMapping
    public ResponseEntity<Order> processOrder(@Valid @RequestBody OrderDto orderDto) {
        log.info("POST /api/orders - Processing order for product ID: {}, quantity: {}", 
                orderDto.getProductId(), orderDto.getQuantity());
        
        try {
            Order order = orderService.processOrder(orderDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (IllegalArgumentException e) {
            log.error("Order processing failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("GET /api/orders - Fetching all orders");
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        log.info("GET /api/orders/{} - Fetching order by ID", id);
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 
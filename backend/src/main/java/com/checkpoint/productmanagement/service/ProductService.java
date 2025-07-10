package com.checkpoint.productmanagement.service;

import com.checkpoint.productmanagement.dto.ProductDto;
import com.checkpoint.productmanagement.entity.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private static int operationCounter = 0;

    public Product createProduct(ProductDto productDto) {
        Product product = new Product(
                idGenerator.getAndIncrement(),
                productDto.getName(),
                productDto.getCategory(),
                productDto.getPrice(),
                productDto.getQuantity(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        products.add(product);
        logOperation("CREATE", "Product created with ID: " + product.getId());
        return product;
    }

    public List<Product> getAllProducts() {
        logOperation("READ", "Fetched all products");
        return new ArrayList<>(products);
    }

    public Optional<Product> getProductById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Optional<Product> updateProduct(Long id, ProductDto productDto) {
        Optional<Product> opt = getProductById(id);
        opt.ifPresent(product -> {
            product.setName(productDto.getName());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());
            product.setUpdatedAt(LocalDateTime.now());
            logOperation("UPDATE", "Updated product with ID: " + id);
        });
        return opt;
    }

    public boolean deleteProduct(Long id) {
        boolean removed = products.removeIf(p -> p.getId().equals(id));
        if (removed) {
            logOperation("DELETE", "Deleted product with ID: " + id);
        }
        return removed;
    }

    public List<Product> getLowStockProducts() {
        List<Product> lowStock = products.stream()
                .filter(p -> p.getQuantity() < 5)
                .collect(Collectors.toList());
        logOperation("READ", "Fetched " + lowStock.size() + " low stock products");
        return lowStock;
    }

    public List<Product> searchProductsByName(String name) {
        List<Product> found = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        logOperation("READ", "Searched products by name: " + name + ", found: " + found.size());
        return found;
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> found = products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        logOperation("READ", "Fetched products by category: " + category + ", count: " + found.size());
        return found;
    }

    private void logOperation(String operation, String details) {
        operationCounter++;
        // Add logging here if needed
        if (operationCounter % 5 == 0) {
            // Log rotation point
        }
    }

    public int getOperationCounter() {
        return operationCounter;
    }
} 
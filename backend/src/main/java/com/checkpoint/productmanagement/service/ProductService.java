package com.checkpoint.productmanagement.service;

import com.checkpoint.productmanagement.dto.ProductDto;
import com.checkpoint.productmanagement.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final List<Product> products = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final LoggingService loggingService;

    public Product createProduct(ProductDto productDto) {
        Product product = new Product(
                idGenerator.getAndIncrement(),
                productDto.getName(),
                productDto.getCategory(),
                productDto.getPrice(),
                productDto.getQuantity(),
                productDto.getQuantity() < 5,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        products.add(product);
        loggingService.logOperation("CREATE_PRODUCT", product.getId(), product.getName(), product.getQuantity());
        return product;
    }

    public List<Product> getAllProducts() {
        updateLowStockForExistingProducts();

        return new ArrayList<>(products);
    }

    private void updateLowStockForExistingProducts() {
        products.forEach(product -> {
            if (product.getQuantity() < 5 && !product.isLowStock()) {
                product.setLowStock(true);
            } else if (product.getQuantity() >= 5 && product.isLowStock()) {
                product.setLowStock(false);
            }
        });
    }

    public Optional<Product> getProductById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Optional<Product> updateProduct(Long id, ProductDto productDto) {
        Optional<Product> opt = getProductById(id);
        if (opt.isPresent()) {
            Product product = opt.get();
            product.setName(productDto.getName());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setQuantity(productDto.getQuantity());
            product.setLowStock(productDto.getQuantity() < 5);
            product.setUpdatedAt(LocalDateTime.now());
            loggingService.logOperation("UPDATE_PRODUCT", product.getId(), product.getName(), product.getQuantity());
            return Optional.of(product);
        }
        return Optional.empty();
    }

    public boolean deleteProduct(Long id) {
        Optional<Product> productOpt = getProductById(id);
        boolean removed = products.removeIf(p -> p.getId().equals(id));
        if (removed && productOpt.isPresent()) {
            Product product = productOpt.get();
            loggingService.logOperation("DELETE_PRODUCT", product.getId(), product.getName(), product.getQuantity());
        }
        return removed;
    }

    public List<Product> getLowStockProducts() {
        List<Product> lowStock = products.stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());
        return lowStock;
    }


    public int getOperationCounter() {
        return loggingService.getPendingOperationsCount();
    }
} 
package com.checkpoint.productmanagement.controller;

import com.checkpoint.productmanagement.dto.ProductDto;
import com.checkpoint.productmanagement.entity.Product;
import com.checkpoint.productmanagement.service.CsvImportService;
import com.checkpoint.productmanagement.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*") 
public class ProductController {
    
    private final ProductService productService;
    private final CsvImportService csvImportService;
    
    /**
     * Get all products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("GET /api/products - Fetching all products");
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        log.info("GET /api/products/{} - Fetching product by ID", id);
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
     
    /**
     * Create new product
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("POST /api/products - Creating new product: {}", productDto.getName());
        Product createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    
    /**
     * Update product
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, 
                                              @Valid @RequestBody ProductDto productDto) {
        log.info("PUT /api/products/{} - Updating product", id);
        Optional<Product> updatedProduct = productService.updateProduct(id, productDto);
        return updatedProduct.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Delete product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{} - Deleting product", id);
        boolean deleted = productService.deleteProduct(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    /**
     * Get products with low stock (quantity < 5)
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        log.info("GET /api/products/low-stock - Fetching low stock products");
        List<Product> lowStockProducts = productService.getLowStockProducts();
        return ResponseEntity.ok(lowStockProducts);
    }
    
    /**
     * Search products by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String name) {
        log.info("GET /api/products/search?name={} - Searching products by name", name);
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Get products by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        log.info("GET /api/products/category/{} - Fetching products by category", category);
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    /**
     * Import products from CSV file
     */
    @PostMapping("/import")
    public ResponseEntity<CsvImportService.ImportResult> importProductsFromCsv(
            @RequestParam("file") MultipartFile file) {
        log.info("POST /api/products/import - Importing products from CSV file: {}", 
                file.getOriginalFilename());
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            CsvImportService.ImportResult result = csvImportService.importProductsFromCsv(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error importing CSV: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get operation counter
     */
    @GetMapping("/stats/operations")
    public ResponseEntity<Integer> getOperationCounter() {
        log.info("GET /api/products/stats/operations - Getting operation counter");
        int counter = productService.getOperationCounter();
        return ResponseEntity.ok(counter);
    }
} 
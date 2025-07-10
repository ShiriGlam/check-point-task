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
    
   

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
     

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductDto productDto) {
        Product createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, 
                                              @Valid @RequestBody ProductDto productDto) {
        Optional<Product> updatedProduct = productService.updateProduct(id, productDto);
        return updatedProduct.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        List<Product> lowStockProducts = productService.getLowStockProducts();
        return ResponseEntity.ok(lowStockProducts);
    }
    
    

    @PostMapping("/import")
    public ResponseEntity<CsvImportService.ImportResult> importProductsFromCsv(
            @RequestParam("file") MultipartFile file) {

        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            CsvImportService.ImportResult result = csvImportService.importProductsFromCsv(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    

    @GetMapping("/stats/operations")
    public ResponseEntity<Integer> getOperationCounter() {
        int counter = productService.getOperationCounter();
        return ResponseEntity.ok(counter);
    }
} 
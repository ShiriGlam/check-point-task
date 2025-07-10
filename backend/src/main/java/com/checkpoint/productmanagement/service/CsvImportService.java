package com.checkpoint.productmanagement.service;

import com.checkpoint.productmanagement.dto.ProductDto;
import com.checkpoint.productmanagement.entity.Product;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvImportService {
    
    private final ProductService productService;
    
    /**
     * Import products from CSV file
     */
    public ImportResult importProductsFromCsv(MultipartFile file) {
        log.info("Starting CSV import from file: {}", file.getOriginalFilename());
        
        ImportResult result = new ImportResult();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            // Skip header row
            String[] header = reader.readNext();
            if (header == null || header.length < 4) {
                throw new IllegalArgumentException("CSV file must have at least 4 columns: name, category, price, quantity");
            }
            
            String[] line;
            int lineNumber = 1; // Start from 1 since we skipped header
            
            while ((line = reader.readNext()) != null) {
                lineNumber++;
                try {
                    ProductDto productDto = parseCsvLine(line, lineNumber);
                    Product savedProduct = productService.createProduct(productDto);
                    successCount++;
                    log.info("Imported product: {} (ID: {})", savedProduct.getName(), savedProduct.getId());
                } catch (Exception e) {
                    String error = String.format("Line %d: %s", lineNumber, e.getMessage());
                    errors.add(error);
                    log.error("Error importing line {}: {}", lineNumber, e.getMessage());
                }
            }
            
        } catch (IOException | CsvValidationException e) {
            String error = "Error reading CSV file: " + e.getMessage();
            errors.add(error);
            log.error("CSV import failed: {}", e.getMessage());
        }
        
        result.setSuccessCount(successCount);
        result.setErrorCount(errors.size());
        result.setErrors(errors);
        
        log.info("CSV import completed. Success: {}, Errors: {}", successCount, errors.size());
        return result;
    }
    
    /**
     * Parse a single CSV line into ProductDto
     */
    private ProductDto parseCsvLine(String[] line, int lineNumber) {
        if (line.length < 4) {
            throw new IllegalArgumentException("Line must have at least 4 columns");
        }
        
        String name = line[0].trim();
        String category = line[1].trim();
        String priceStr = line[2].trim();
        String quantityStr = line[3].trim();
        
        // Validate name
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        
        // Validate category
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        
        // Parse and validate price
        BigDecimal price;
        try {
            price = new BigDecimal(priceStr);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be greater than 0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format: " + priceStr);
        }
        
        // Parse and validate quantity
        Integer quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity format: " + quantityStr);
        }
        
        return new ProductDto(null, name, category, price, quantity);
    }
    
    /**
     * Result class for CSV import operations
     */
    public static class ImportResult {
        private int successCount;
        private int errorCount;
        private List<String> errors = new ArrayList<>();
        
        // Getters and setters
        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
        
        public int getErrorCount() { return errorCount; }
        public void setErrorCount(int errorCount) { this.errorCount = errorCount; }
        
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
    }
} 
package com.checkpoint.productmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    
    private Long id;
    
    @NotBlank(message = "שם המוצר הוא שדה חובה")
    @Size(min = 2, max = 255, message = "שם המוצר חייב להיות בין 2 ל-255 תווים")
    private String name;
    
    @NotBlank(message = "קטגוריה היא שדה חובה")
    @Size(min = 2, max = 100, message = "קטגוריה חייבת להיות בין 2 ל-100 תווים")
    private String category;
    
    @NotNull(message = "מחיר הוא שדה חובה")
    @DecimalMin(value = "0.01", message = "מחיר חייב להיות גדול מ-0")
    @Digits(integer = 8, fraction = 2, message = "מחיר חייב להיות בפורמט תקין")
    private BigDecimal price;
    
    @NotNull(message = "כמות היא שדה חובה")
    @Min(value = 0, message = "כמות לא יכולה להיות שלילית")
    @Max(value = 999999, message = "כמות לא יכולה לעלות על 999,999")
    private Integer quantity;
} 
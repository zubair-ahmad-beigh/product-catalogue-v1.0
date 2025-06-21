package com.example.catalogue.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.catalogue.model.ErrorResponse;
import com.example.catalogue.model.Product;
import com.example.catalogue.service.ProductService;

/**
 * REST Controller for Product Catalogue operations
 * Supports multiple API versions with different features:
 * - v1.0: Basic health and products endpoints
 * - v1.1: Adds search functionality
 * - v2.0: Enhanced error handling and validation
 */
@RestController
@RequestMapping("/")
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * Health check endpoint
     * Available in all versions
     * @return Health status
     */
    @GetMapping("health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Service is up");
        response.put("version", "2.0.0");
        response.put("java", "8");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all products
     * Available in all versions
     * @return List of all products
     */
    @GetMapping("products")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    /**
     * Search products by keyword
     * Available in v1.1 and v2.0
     * @param keyword Search keyword
     * @param request HTTP request for error handling
     * @return List of matching products or error response
     */
    @GetMapping("products/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpServletRequest request) {
        
        // v2.0: Enhanced validation
        if (keyword == null || keyword.trim().isEmpty()) {
            ErrorResponse error = ErrorResponse.badRequest(
                "Search keyword is required and cannot be empty", 
                request.getRequestURI()
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        List<Product> results = productService.searchProducts(keyword);
        
        // v2.0: Return 404 if no matches found
        if (results.isEmpty()) {
            ErrorResponse error = ErrorResponse.notFound(
                "No products found matching keyword: " + keyword, 
                request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        return ResponseEntity.ok(results);
    }
    
    /**
     * Get product by ID
     * Available in v2.0
     * @param id Product ID
     * @param request HTTP request for error handling
     * @return Product or error response
     */
    @GetMapping("products/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable Integer id,
            HttpServletRequest request) {
        
        Product product = productService.getProductById(id);
        
        if (product == null) {
            ErrorResponse error = ErrorResponse.notFound(
                "Product not found with ID: " + id, 
                request.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        return ResponseEntity.ok(product);
    }
    
    /**
     * Get product statistics
     * Available in v2.0
     * @return Product statistics
     */
    @GetMapping("products/stats")
    public ResponseEntity<Map<String, Object>> getProductStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productService.getProductCount());
        stats.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Global exception handler for validation errors
     * Available in v2.0
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.internalServerError(
            "An unexpected error occurred: " + ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

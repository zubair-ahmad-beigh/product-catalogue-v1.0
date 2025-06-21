package com.example.catalogue.service;

import com.example.catalogue.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for product operations
 * Manages static product data and provides search functionality
 * Uses Java 8 Streams for filtering operations
 */
@Service
public class ProductService {
    
    // Static product data - in a real application, this would come from a database
    private final List<Product> products;
    
    public ProductService() {
        this.products = initializeProducts();
    }
    
    /**
     * Initialize the static product list
     * @return List of products
     */
    private List<Product> initializeProducts() {
        List<Product> productList = new ArrayList<>();
        
        productList.add(new Product(1, "Time Machine", 999.99));
        productList.add(new Product(2, "Hoverboard", 299.49));
        productList.add(new Product(3, "Flux Capacitor", 599.00));
        productList.add(new Product(4, "DeLorean DMC-12", 25000.00));
        productList.add(new Product(5, "Mr. Fusion", 1500.00));
        productList.add(new Product(6, "Self-Lacing Shoes", 89.99));
        productList.add(new Product(7, "Holographic Projector", 750.00));
        productList.add(new Product(8, "Flying Car", 150000.00));
        productList.add(new Product(9, "Teleporter", 50000.00));
        productList.add(new Product(10, "Robot Butler", 5000.00));
        
        return productList;
    }
    
    /**
     * Get all products
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    /**
     * Search products by keyword (case-insensitive)
     * @param keyword The search keyword
     * @return List of matching products
     */
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = keyword.toLowerCase().trim();
        
        return products.stream()
                .filter(product -> product.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    /**
     * Get product by ID
     * @param id Product ID
     * @return Product if found, null otherwise
     */
    public Product getProductById(Integer id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get total number of products
     * @return Product count
     */
    public int getProductCount() {
        return products.size();
    }
} 
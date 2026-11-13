package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.entity.Product;

import java.util.List;

/**
 * Service encapsulating product catalog management, searching, filtering, and inventory operations.
 */
public class ProductService {

    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getAllActiveProducts() {
        return productDAO.findAllActive();
    }

    public Product getProductById(int productId) {
        if (productId <= 0) return null;
        return productDAO.findById(productId);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.findByCategory(categoryId);
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllActiveProducts();
        }
        return productDAO.searchProducts(keyword.trim());
    }

    public List<Product> filterProducts(Integer categoryId, String brand, Double minPrice, Double maxPrice) {
        if (minPrice != null && minPrice < 0) {
            throw new ValidationException("Minimum price cannot be negative.");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new ValidationException("Maximum price cannot be negative.");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new ValidationException("Minimum price cannot exceed maximum price.");
        }
        return productDAO.filterProducts(categoryId, brand, minPrice, maxPrice);
    }

    public List<String> getAllBrands() {
        return productDAO.findAllBrands();
    }

    /**
     * Adds a new product ensuring inventory business constraints.
     */
    public int addProduct(Product product) {
        if (product == null) {
            throw new ValidationException("Product cannot be null.");
        }
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new ValidationException("Product name is required.");
        }
        if (product.getCategoryId() <= 0) {
            throw new ValidationException("A valid category must be selected.");
        }
        if (product.getPrice() < 0) {
            throw new ValidationException("Product price cannot be negative (Rule: Price >= 0).");
        }
        if (product.getStockQuantity() < 0) {
            throw new ValidationException("Product stock quantity cannot be negative (Rule: Stock >= 0).");
        }

        int generatedId = productDAO.addProduct(product);
        if (generatedId <= 0) {
            throw new ValidationException("Failed to add product into MySQL catalog.");
        }
        return generatedId;
    }

    /**
     * Updates an existing product ensuring business constraints.
     */
    public boolean updateProduct(Product product) {
        if (product == null || product.getProductId() <= 0) {
            throw new ValidationException("Valid product ID required for update.");
        }
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new ValidationException("Product name cannot be empty.");
        }
        if (product.getPrice() < 0) {
            throw new ValidationException("Product price cannot be negative.");
        }
        if (product.getStockQuantity() < 0) {
            throw new ValidationException("Stock quantity cannot be negative.");
        }

        boolean updated = productDAO.updateProduct(product);
        if (!updated) {
            throw new ValidationException("Product update failed or product not found.");
        }
        return true;
    }

    public boolean deleteProduct(int productId) {
        if (productId <= 0) {
            throw new ValidationException("Invalid product ID.");
        }
        return productDAO.deleteProduct(productId);
    }
}

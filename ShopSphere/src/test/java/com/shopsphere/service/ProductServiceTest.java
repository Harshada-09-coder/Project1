package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    private ProductService productService;

    private static class StubProductDAO extends ProductDAO {
        private final List<Product> products = new ArrayList<>();
        private int idCounter = 1;

        @Override
        public int addProduct(Product product) {
            product.setProductId(idCounter++);
            products.add(product);
            return product.getProductId();
        }

        @Override
        public Product findById(int id) {
            for (Product p : products) {
                if (p.getProductId() == id) return p;
            }
            return null;
        }

        @Override
        public boolean updateProduct(Product product) {
            return true;
        }

        @Override
        public List<Product> findAllActive() {
            List<Product> active = new ArrayList<>();
            for (Product p : products) {
                if (p.isActive()) active.add(p);
            }
            return active;
        }
    }

    private StubProductDAO stubDAO;

    @BeforeEach
    void setUp() {
        stubDAO = new StubProductDAO();
        productService = new ProductService(stubDAO);
    }

    @Test
    @DisplayName("Add Product: Rejects null product")
    void testAddProduct_NullProduct_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> productService.addProduct(null));
    }

    @Test
    @DisplayName("Add Product: Rejects empty product name")
    void testAddProduct_EmptyName_ThrowsValidationException() {
        Product p = new Product();
        p.setProductName("  ");
        p.setCategoryId(1);
        p.setPrice(100.0);
        p.setStockQuantity(5);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    @DisplayName("Add Product: Rejects invalid category ID (<= 0)")
    void testAddProduct_InvalidCategory_ThrowsValidationException() {
        Product p = new Product();
        p.setProductName("Gaming Mouse");
        p.setCategoryId(0);
        p.setPrice(1500.0);
        p.setStockQuantity(10);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    @DisplayName("Add Product: Rejects negative price (Rule: Price >= 0)")
    void testAddProduct_NegativePrice_ThrowsValidationException() {
        Product p = new Product();
        p.setProductName("Gaming Mouse");
        p.setCategoryId(1);
        p.setPrice(-49.99);
        p.setStockQuantity(10);
        ValidationException ex = assertThrows(ValidationException.class, () -> productService.addProduct(p));
        assertTrue(ex.getMessage().contains("negative"));
    }

    @Test
    @DisplayName("Add Product: Rejects negative stock quantity (Rule: Stock >= 0)")
    void testAddProduct_NegativeStock_ThrowsValidationException() {
        Product p = new Product();
        p.setProductName("Gaming Mouse");
        p.setCategoryId(1);
        p.setPrice(1500.0);
        p.setStockQuantity(-5);
        ValidationException ex = assertThrows(ValidationException.class, () -> productService.addProduct(p));
        assertTrue(ex.getMessage().contains("stock"));
    }

    @Test
    @DisplayName("Add Product: Successfully adds valid product into catalog")
    void testAddProduct_Success() {
        Product p = new Product();
        p.setProductName("Mechanical Keyboard");
        p.setCategoryId(2);
        p.setPrice(2999.0);
        p.setStockQuantity(25);
        p.setActive(true);

        int id = productService.addProduct(p);
        assertTrue(id > 0);
        assertEquals(1, stubDAO.findAllActive().size());
    }

    @Test
    @DisplayName("Filter Products: Validates price boundaries (min <= max, non-negative)")
    void testFilterProducts_PriceRangeValidation() {
        assertThrows(ValidationException.class, () -> productService.filterProducts(null, null, -10.0, 100.0));
        assertThrows(ValidationException.class, () -> productService.filterProducts(null, null, 10.0, -5.0));
        assertThrows(ValidationException.class, () -> productService.filterProducts(null, null, 500.0, 100.0));
    }
}


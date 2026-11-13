package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceTest {

    private CategoryService categoryService;
    private StubCategoryDAO stubDAO;

    private static class StubCategoryDAO extends CategoryDAO {
        private final Map<String, Category> categories = new HashMap<>();
        private int idCounter = 1;

        @Override
        public Category findByName(String name) {
            return categories.get(name.toLowerCase());
        }

        @Override
        public int addCategory(Category category) {
            category.setCategoryId(idCounter++);
            categories.put(category.getCategoryName().toLowerCase(), category);
            return category.getCategoryId();
        }

        @Override
        public boolean deleteCategory(int id) {
            return categories.values().removeIf(c -> c.getCategoryId() == id);
        }
    }

    @BeforeEach
    void setUp() {
        stubDAO = new StubCategoryDAO();
        categoryService = new CategoryService(stubDAO);
    }

    @Test
    @DisplayName("Add Category: Rejects empty or null name")
    void testAddCategory_EmptyName_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> categoryService.addCategory(null, "Description"));
        assertThrows(ValidationException.class, () -> categoryService.addCategory("  ", "Description"));
    }

    @Test
    @DisplayName("Add Category: Rejects duplicate category name")
    void testAddCategory_DuplicateName_ThrowsValidationException() {
        categoryService.addCategory("Electronics", "Gadgets and tech");
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            categoryService.addCategory("Electronics", "Another description");
        });
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    @DisplayName("Add Category: Successfully adds new category")
    void testAddCategory_Success() {
        int id = categoryService.addCategory("Home & Kitchen", "Home appliances");
        assertTrue(id > 0);
        assertNotNull(categoryService.getCategoryByName("Home & Kitchen"));
    }

    @Test
    @DisplayName("Delete Category: Rejects invalid ID (<= 0)")
    void testDeleteCategory_InvalidId_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> categoryService.deleteCategory(0));
    }
}


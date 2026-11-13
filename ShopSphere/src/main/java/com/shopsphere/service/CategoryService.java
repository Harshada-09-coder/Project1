package com.shopsphere.service;

import com.shopsphere.dao.CategoryDAO;
import com.shopsphere.entity.Category;

import java.util.List;

/**
 * Service encapsulating category management and hierarchy logic.
 */
public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> getAllActiveCategories() {
        return categoryDAO.findAllActive();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    public Category getCategoryById(int categoryId) {
        return categoryDAO.findById(categoryId);
    }

    public Category getCategoryByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        return categoryDAO.findByName(name.trim());
    }

    public int addCategory(String name, String description) {
        return addCategory(new Category(name, description, null));
    }

    public int addCategory(Category category) {
        if (category == null || category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new ValidationException("Category name is required.");
        }
        String name = category.getCategoryName().trim();
        if (categoryDAO.findByName(name) != null) {
            throw new ValidationException("A category named '" + name + "' already exists.");
        }

        category.setCategoryName(name);
        int id = categoryDAO.addCategory(category);
        if (id <= 0) {
            throw new ValidationException("Failed to save category into database.");
        }
        return id;
    }

    public boolean updateCategory(Category category) {
        if (category == null || category.getCategoryId() <= 0) {
            throw new ValidationException("Valid category ID required.");
        }
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty.");
        }
        return categoryDAO.updateCategory(category);
    }

    public boolean deleteCategory(int categoryId) {
        if (categoryId <= 0) {
            throw new ValidationException("Invalid category ID.");
        }
        return categoryDAO.deleteCategory(categoryId);
    }
}

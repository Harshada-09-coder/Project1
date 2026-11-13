package com.shopsphere.service;

import com.shopsphere.dao.AdminDAO;
import com.shopsphere.entity.Admin;

import java.util.List;
import java.util.Map;

/**
 * Service encapsulating administrator operations, authentication, and management analytics.
 */
public class AdminService {

    private final AdminDAO adminDAO;

    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public Admin loginAdmin(String usernameOrEmail, String password) {
        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            throw new ValidationException("Username or email cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty.");
        }

        Admin admin = adminDAO.authenticate(usernameOrEmail.trim(), password);
        if (admin == null) {
            throw new ValidationException("Invalid administrator credentials or account inactive.");
        }

        UserSession.setCurrentAdmin(admin);
        return admin;
    }

    public Map<String, Object> getDashboardStats() {
        return adminDAO.getDashboardStats();
    }

    public List<Admin> getAllAdmins() {
        return adminDAO.findAll();
    }

    public int createAdmin(Admin admin, String password) {
        if (admin == null || admin.getUsername() == null || admin.getUsername().trim().isEmpty()) {
            throw new ValidationException("Admin username is required.");
        }
        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) {
            throw new ValidationException("Admin email is required.");
        }
        if (password == null || password.length() < 4) {
            throw new ValidationException("Password must be at least 4 characters.");
        }

        return adminDAO.createAdmin(admin, password);
    }
}

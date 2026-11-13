package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.entity.User;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Service encapsulating customer business logic, authentication, profile management, and validation.
 */
public class CustomerService {

    private final UserDAO userDAO;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public CustomerService() {
        this.userDAO = new UserDAO();
    }

    public CustomerService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new customer with minimal address details.
     */
    public User registerCustomer(String fullName, String email, String password, String phone, String address) {
        return registerCustomer(fullName, email, password, phone, address, "", "", "", "India");
    }

    /**
     * Registers a new customer after rigorous business validation.
     */
    public User registerCustomer(String fullName, String email, String password, String phone,
                                 String address, String city, String state, String postalCode, String country) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ValidationException("Full name is required.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email address is required.");
        }
        String cleanEmail = email.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(cleanEmail).matches()) {
            throw new ValidationException("Please enter a valid email address (e.g. user@example.com).");
        }
        if (password == null || password.trim().length() < 4) {
            throw new ValidationException("Password must be at least 4 characters long.");
        }

        // Check duplicate email (Rule: Unique customer accounts)
        User existing = userDAO.findByEmail(cleanEmail);
        if (existing != null) {
            throw new ValidationException("An account with this email address already exists. Please log in.");
        }

        User newUser = new User(
                fullName.trim(),
                cleanEmail,
                null, // Password will be hashed in DAO
                phone != null ? phone.trim() : "",
                address != null ? address.trim() : "",
                city != null ? city.trim() : "",
                state != null ? state.trim() : "",
                postalCode != null ? postalCode.trim() : "",
                (country != null && !country.trim().isEmpty()) ? country.trim() : "India"
        );

        int generatedId = userDAO.registerUser(newUser, password);
        if (generatedId <= 0) {
            throw new ValidationException("Failed to register customer account. Database error occurred.");
        }

        newUser.setUserId(generatedId);
        return newUser;
    }

    /**
     * Authenticates customer credentials.
     */
    public User loginCustomer(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email address cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("Password cannot be empty.");
        }

        User user = userDAO.authenticate(email.trim().toLowerCase(), password);
        if (user == null) {
            throw new ValidationException("Invalid email or password. Please verify your credentials.");
        }

        UserSession.setCurrentUser(user);
        return user;
    }

    public User getCustomerById(int userId) {
        return userDAO.findById(userId);
    }

    public User getCustomerByEmail(String email) {
        if (email == null) return null;
        return userDAO.findByEmail(email.trim().toLowerCase());
    }

    public boolean updateProfile(User user) {
        if (user == null || user.getUserId() <= 0) {
            throw new ValidationException("Invalid user ID for profile update.");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new ValidationException("Full name cannot be blank.");
        }
        boolean ok = userDAO.updateUserProfile(user);
        if (ok && UserSession.getCurrentUser() != null && UserSession.getCurrentUser().getUserId() == user.getUserId()) {
            UserSession.setCurrentUser(user);
        }
        return ok;
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.trim().length() < 4) {
            throw new ValidationException("New password must be at least 4 characters long.");
        }
        boolean updated = userDAO.changePassword(userId, oldPassword, newPassword);
        if (!updated) {
            throw new ValidationException("Current password verification failed.");
        }
        return true;
    }

    public List<User> getAllCustomers() {
        return userDAO.findAllUsers();
    }

    public boolean deleteCustomer(int userId) {
        return userDAO.deleteUser(userId);
    }
}

package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerServiceTest {

    private CustomerService customerService;

    // In-memory test stub for UserDAO
    private static class StubUserDAO extends UserDAO {
        private User existingUser;

        @Override
        public User findByEmail(String email) {
            if (existingUser != null && existingUser.getEmail().equalsIgnoreCase(email)) {
                return existingUser;
            }
            return null;
        }

        @Override
        public int registerUser(User user, String plainPassword) {
            user.setUserId(99);
            this.existingUser = user;
            return 99;
        }

        @Override
        public User authenticate(String email, String plainPassword) {
            if (existingUser != null && existingUser.getEmail().equalsIgnoreCase(email) && "ValidPass123".equals(plainPassword)) {
                return existingUser;
            }
            return null;
        }

        @Override
        public boolean updateUserProfile(User user) {
            this.existingUser = user;
            return true;
        }

        @Override
        public boolean changePassword(int userId, String oldPassword, String newPassword) {
            return "ValidPass123".equals(oldPassword);
        }
    }

    private StubUserDAO stubDAO;

    @BeforeEach
    void setUp() {
        stubDAO = new StubUserDAO();
        customerService = new CustomerService(stubDAO);
        UserSession.logout();
    }

    @Test
    @DisplayName("Register Customer: Rejects empty or null name")
    void testRegisterCustomer_EmptyName_ThrowsValidationException() {
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            customerService.registerCustomer("", "user@example.com", "Password123", "9876543210", "123 Main St");
        });
        assertEquals("Full name is required.", ex.getMessage());
    }

    @Test
    @DisplayName("Register Customer: Rejects invalid email format")
    void testRegisterCustomer_InvalidEmail_ThrowsValidationException() {
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            customerService.registerCustomer("Alice Doe", "invalid-email-format", "Password123", "9876543210", "123 Main St");
        });
        assertTrue(ex.getMessage().contains("valid email address"));
    }

    @Test
    @DisplayName("Register Customer: Rejects short password (< 4 chars)")
    void testRegisterCustomer_ShortPassword_ThrowsValidationException() {
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            customerService.registerCustomer("Alice Doe", "alice@example.com", "123", "9876543210", "123 Main St");
        });
        assertTrue(ex.getMessage().contains("at least 4 characters"));
    }

    @Test
    @DisplayName("Register Customer: Successfully registers customer with valid inputs")
    void testRegisterCustomer_Success() {
        User user = customerService.registerCustomer("Alice Doe", "alice@example.com", "ValidPass123", "9876543210", "123 Main St");
        assertNotNull(user);
        assertEquals(99, user.getUserId());
        assertEquals("Alice Doe", user.getFullName());
        assertEquals("alice@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Register Customer: Rejects duplicate email address")
    void testRegisterCustomer_DuplicateEmail_ThrowsValidationException() {
        customerService.registerCustomer("Alice Doe", "alice@example.com", "ValidPass123", "9876543210", "123 Main St");
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            customerService.registerCustomer("Another Alice", "alice@example.com", "ValidPass123", "9876543210", "456 Other St");
        });
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    @DisplayName("Login Customer: Authenticates valid credentials and sets UserSession")
    void testLoginCustomer_Success() {
        customerService.registerCustomer("Alice Doe", "alice@example.com", "ValidPass123", "9876543210", "123 Main St");
        User loggedIn = customerService.loginCustomer("alice@example.com", "ValidPass123");
        assertNotNull(loggedIn);
        assertTrue(UserSession.isLoggedIn());
        assertEquals(loggedIn.getUserId(), UserSession.getCurrentUser().getUserId());
    }

    @Test
    @DisplayName("Login Customer: Rejects incorrect credentials")
    void testLoginCustomer_InvalidCredentials_ThrowsValidationException() {
        customerService.registerCustomer("Alice Doe", "alice@example.com", "ValidPass123", "9876543210", "123 Main St");
        assertThrows(ValidationException.class, () -> {
            customerService.loginCustomer("alice@example.com", "WrongPassword");
        });
    }

    @Test
    @DisplayName("Update Profile: Rejects blank full name")
    void testUpdateProfile_BlankName_ThrowsValidationException() {
        User u = new User();
        u.setUserId(99);
        u.setFullName("");
        assertThrows(ValidationException.class, () -> {
            customerService.updateProfile(u);
        });
    }

    @Test
    @DisplayName("Change Password: Validates current password and enforces minimum length")
    void testChangePassword_Validation() {
        customerService.registerCustomer("Alice Doe", "alice@example.com", "ValidPass123", "9876543210", "123 Main St");
        assertThrows(ValidationException.class, () -> {
            customerService.changePassword(99, "ValidPass123", "12"); // < 4 chars
        });
        assertThrows(ValidationException.class, () -> {
            customerService.changePassword(99, "WrongCurrent", "NewPassword999");
        });
        assertTrue(customerService.changePassword(99, "ValidPass123", "NewPassword999"));
    }
}


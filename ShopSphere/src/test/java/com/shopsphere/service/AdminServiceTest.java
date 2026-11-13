package com.shopsphere.service;

import com.shopsphere.dao.AdminDAO;
import com.shopsphere.entity.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdminServiceTest {

    private AdminService adminService;
    private StubAdminDAO stubAdminDAO;

    private static class StubAdminDAO extends AdminDAO {
        private Admin adminUser;

        @Override
        public Admin authenticate(String usernameOrEmail, String plainPassword) {
            if (adminUser != null &&
                    (adminUser.getUsername().equalsIgnoreCase(usernameOrEmail) || adminUser.getEmail().equalsIgnoreCase(usernameOrEmail))
                    && "Admin@123".equals(plainPassword)) {
                return adminUser;
            }
            return null;
        }

        @Override
        public int createAdmin(Admin admin, String password) {
            admin.setAdminId(1);
            this.adminUser = admin;
            return 1;
        }

        @Override
        public Map<String, Object> getDashboardStats() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", 10);
            stats.put("totalProducts", 25);
            stats.put("totalOrders", 5);
            stats.put("totalRevenue", 15000.0);
            return stats;
        }
    }

    @BeforeEach
    void setUp() {
        stubAdminDAO = new StubAdminDAO();
        adminService = new AdminService(stubAdminDAO);
        UserSession.logout();

        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setUsername("admin");
        admin.setEmail("admin@shopsphere.com");
        admin.setFullName("Master Administrator");
        admin.setActive(true);
        stubAdminDAO.createAdmin(admin, "Admin@123");
    }

    @Test
    @DisplayName("Admin Login: Rejects empty credentials")
    void testLoginAdmin_EmptyCredentials_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> adminService.loginAdmin("", "Admin@123"));
        assertThrows(ValidationException.class, () -> adminService.loginAdmin("admin", ""));
    }

    @Test
    @DisplayName("Admin Login: Rejects invalid password")
    void testLoginAdmin_InvalidPassword_ThrowsValidationException() {
        assertThrows(ValidationException.class, () -> adminService.loginAdmin("admin", "WrongPass"));
    }

    @Test
    @DisplayName("Admin Login: Successfully logs in and sets admin session")
    void testLoginAdmin_Success() {
        Admin loggedIn = adminService.loginAdmin("admin", "Admin@123");
        assertNotNull(loggedIn);
        assertEquals("Master Administrator", loggedIn.getFullName());
        assertTrue(UserSession.isAdmin());
        assertEquals(loggedIn.getAdminId(), UserSession.getCurrentAdmin().getAdminId());
    }

    @Test
    @DisplayName("Admin Stats: Successfully retrieves dashboard metrics")
    void testGetDashboardStats_Success() {
        Map<String, Object> stats = adminService.getDashboardStats();
        assertNotNull(stats);
        assertEquals(10, stats.get("totalUsers"));
        assertEquals(25, stats.get("totalProducts"));
        assertEquals(15000.0, stats.get("totalRevenue"));
    }

    @Test
    @DisplayName("Create Admin: Rejects short password (< 4 chars)")
    void testCreateAdmin_ShortPassword_ThrowsValidationException() {
        Admin newAdmin = new Admin();
        newAdmin.setUsername("manager");
        newAdmin.setEmail("mgr@shopsphere.com");

        assertThrows(ValidationException.class, () -> adminService.createAdmin(newAdmin, "123"));
    }
}


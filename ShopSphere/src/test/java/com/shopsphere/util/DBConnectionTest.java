package com.shopsphere.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DBConnectionTest {

    @Test
    @DisplayName("DBConnection: Loads configuration from db.properties correctly")
    void testConfigurationLoading() {
        String driver = DBConnection.getDriver();
        String url = DBConnection.getUrl();
        String username = DBConnection.getUsername();

        assertNotNull(driver, "JDBC Driver class should be loaded");
        assertEquals("com.mysql.cj.jdbc.Driver", driver);

        assertNotNull(url, "JDBC Connection URL should not be null");
        assertTrue(url.contains("jdbc:mysql://"), "URL should be for MySQL");
        assertTrue(url.contains("shopsphere"), "URL should reference shopsphere database");

        assertNotNull(username, "Database username should not be null");
        assertEquals("root", username);
    }

    @Test
    @DisplayName("DBConnection: closeQuietly handles nulls and exceptions safely")
    void testCloseQuietly() {
        assertDoesNotThrow(() -> DBConnection.closeQuietly((AutoCloseable) null));
        assertDoesNotThrow(() -> DBConnection.closeQuietly(null, null));
    }
}


package com.shopsphere.service;

import com.shopsphere.entity.Admin;
import com.shopsphere.entity.User;

/**
 * Manages the current authenticated session for customers and administrators
 * across Java Swing UI windows.
 */
public class UserSession {

    private static User currentUser;
    private static Admin currentAdmin;

    private UserSession() {
    }

    public static synchronized User getCurrentUser() {
        return currentUser;
    }

    public static synchronized void setCurrentUser(User user) {
        currentUser = user;
    }

    public static synchronized boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public static synchronized boolean isLoggedIn() {
        return isUserLoggedIn();
    }

    public static synchronized int getCurrentUserId() {
        return currentUser != null ? currentUser.getUserId() : 1; // Fallback to 1 for demo if needed
    }

    public static synchronized Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public static synchronized void setCurrentAdmin(Admin admin) {
        currentAdmin = admin;
    }

    public static synchronized boolean isAdminLoggedIn() {
        return currentAdmin != null;
    }

    public static synchronized boolean isAdmin() {
        return isAdminLoggedIn();
    }

    public static synchronized void logout() {
        currentUser = null;
        currentAdmin = null;
    }
}

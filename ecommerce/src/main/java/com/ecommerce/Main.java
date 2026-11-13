package com.ecommerce;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.ecommerce.view.WelcomePage;

public class Main {
    public static void main(String[] args) {
        // Use system look and feel for native font rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Launch UI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            WelcomePage welcomePage = new WelcomePage();
            welcomePage.setVisible(true);
        });
    }
}
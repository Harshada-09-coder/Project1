package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminNavUtils {

    public static final Color COLOR_PRIMARY      = new Color(0x3B, 0x82, 0xF6); // Enterprise Blue
    public static final Color COLOR_PRIMARY_DARK = new Color(0x1D, 0x4E, 0xD8);
    public static final Color COLOR_SIDEBAR_BG   = Color.WHITE;
    public static final Color COLOR_BG           = new Color(0xF3, 0xF6, 0xFA);
    public static final Color COLOR_CARD         = Color.WHITE;
    public static final Color COLOR_TEXT_MAIN    = new Color(0x1E, 0x29, 0x3B);
    public static final Color COLOR_TEXT_MUTED   = new Color(0x64, 0x74, 0x8B);
    public static final Color COLOR_BORDER       = new Color(0xE2, 0xE8, 0xF0);
    public static final Color COLOR_INPUT_BG     = new Color(0xF8, 0xFA, 0xFC);
    public static final Color COLOR_GREEN        = new Color(0x10, 0xB9, 0x81);
    public static final Color COLOR_RED          = new Color(0xEF, 0x44, 0x44);
    public static final Color COLOR_ORANGE       = new Color(0xF5, 0x9E, 0x0B);

    // Common Admin Top Header
    public static JPanel createAdminHeader(JFrame currentFrame, String currentSection) {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(COLOR_CARD);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));

        // Breadcrumb Title (Left)
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel breadcrumb = new JLabel("Home  /  Dashboard  /  " + currentSection);
        breadcrumb.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        breadcrumb.setForeground(COLOR_TEXT_MUTED);

        JLabel title = new JLabel(currentSection);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(COLOR_TEXT_MAIN);

        left.add(breadcrumb);
        left.add(title);

        // Center Search Box
        UIUtils.RoundedPanel searchBox = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        searchBox.setPreferredSize(new Dimension(360, 34));
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 10, 0, 10));

        JLabel sIcon = new JLabel(FontIcon.of(Feather.SEARCH, 13, COLOR_TEXT_MUTED));
        JTextField sInput = new JTextField("Search anything...");
        sInput.setBorder(null);
        sInput.setOpaque(false);
        sInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sInput.setForeground(COLOR_TEXT_MAIN);

        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(sInput, BorderLayout.CENTER);

        // Right Quick Actions (Notifications + Admin Profile)
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JLabel bell = new JLabel(FontIcon.of(Feather.BELL, 16, COLOR_TEXT_MAIN));
        bell.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel adminProfile = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        adminProfile.setOpaque(false);

        UIUtils.RoundedPanel avatar = new UIUtils.RoundedPanel(24, new Color(0xDB, 0xEA, 0xFE));
        avatar.setPreferredSize(new Dimension(32, 32));
        avatar.setLayout(new GridBagLayout());
        avatar.add(new JLabel(FontIcon.of(Feather.USER, 15, COLOR_PRIMARY_DARK)));

        JPanel adminDetails = new JPanel();
        adminDetails.setLayout(new BoxLayout(adminDetails, BoxLayout.Y_AXIS));
        adminDetails.setOpaque(false);

        JLabel adminName = new JLabel("Admin  (Administrator)");
        adminName.setFont(new Font("Segoe UI", Font.BOLD, 11));
        adminName.setForeground(COLOR_TEXT_MAIN);

        JLabel adminEmail = new JLabel("admin@shopsphere.io");
        adminEmail.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        adminEmail.setForeground(COLOR_TEXT_MUTED);

        adminDetails.add(adminName);
        adminDetails.add(adminEmail);

        adminProfile.add(avatar);
        adminProfile.add(adminDetails);

        right.add(bell);
        right.add(adminProfile);

        nav.add(left, BorderLayout.WEST);
        nav.add(searchBox, BorderLayout.CENTER);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    // Common Admin Sidebar
    public static JPanel createAdminSidebar(JFrame currentFrame, String activeItem) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, COLOR_BORDER),
                new EmptyBorder(14, 10, 14, 10)
        ));

        // Brand Logo Header
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        brand.setOpaque(false);

        UIUtils.RoundedPanel logo = new UIUtils.RoundedPanel(10, new Color(0xDB, 0xEA, 0xFE));
        logo.setPreferredSize(new Dimension(32, 32));
        logo.setLayout(new GridBagLayout());
        logo.add(new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, COLOR_PRIMARY_DARK)));

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);

        JLabel appTitle = new JLabel("ShopSphere");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        appTitle.setForeground(COLOR_TEXT_MAIN);

        JLabel subTitle = new JLabel("ADMIN PANEL");
        subTitle.setFont(new Font("Segoe UI", Font.BOLD, 9));
        subTitle.setForeground(COLOR_TEXT_MUTED);

        titles.add(appTitle);
        titles.add(subTitle);
        brand.add(logo);
        brand.add(titles);

        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(20));

        JLabel mainLbl = new JLabel("MAIN MENU");
        mainLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        mainLbl.setForeground(COLOR_TEXT_MUTED);
        sidebar.add(mainLbl);
        sidebar.add(Box.createVerticalStrut(8));

        // Navigation Items
        sidebar.add(createNavItem("Dashboard", Feather.GRID, activeItem.equals("Dashboard"), () -> {
            new AdminDashboardPage().setVisible(true);
            currentFrame.dispose();
        }));
        sidebar.add(createNavItem("Products", Feather.BOX, activeItem.equals("Products"), () -> {
            new AdminProductManagementPage().setVisible(true);
            currentFrame.dispose();
        }));
        sidebar.add(createNavItem("Categories", Feather.LAYERS, activeItem.equals("Categories"), () -> {
            new AdminCategoryManagementPage().setVisible(true);
            currentFrame.dispose();
        }));
        sidebar.add(createNavItem("Orders", Feather.SHOPPING_CART, activeItem.equals("Orders"), () -> {
            new AdminOrderManagementPage().setVisible(true);
            currentFrame.dispose();
        }));
        sidebar.add(createNavItem("Customers", Feather.USERS, activeItem.equals("Customers"), () -> {
            JOptionPane.showMessageDialog(currentFrame, "Customers screen (Phase 2) will open next.");
        }));
        sidebar.add(createNavItem("Inventory", Feather.ARCHIVE, activeItem.equals("Inventory"), () -> {
            JOptionPane.showMessageDialog(currentFrame, "Inventory screen (Phase 2) will open next.");
        }));
        sidebar.add(createNavItem("Reports", Feather.BAR_CHART_2, activeItem.equals("Reports"), () -> {
            JOptionPane.showMessageDialog(currentFrame, "Reports screen (Phase 2) will open next.");
        }));

        sidebar.add(Box.createVerticalStrut(18));
        JLabel otherLbl = new JLabel("OTHER");
        otherLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        otherLbl.setForeground(COLOR_TEXT_MUTED);
        sidebar.add(otherLbl);
        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createNavItem("Settings", Feather.SETTINGS, activeItem.equals("Settings"), () -> {
            JOptionPane.showMessageDialog(currentFrame, "Admin Settings (Phase 2) will open next.");
        }));
        sidebar.add(createNavItem("Logout", Feather.LOG_OUT, false, () -> {
            new WelcomePage().setVisible(true);
            currentFrame.dispose();
        }));

        sidebar.add(Box.createVerticalGlue());

        // Bottom Cluster Health Pill
        UIUtils.RoundedPanel clusterPill = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        clusterPill.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 6));
        JLabel dot = new JLabel("●");
        dot.setForeground(COLOR_GREEN);
        JLabel clusterTxt = new JLabel("<html><b style='font-size:9px;'>Primary Cluster</b><br><span style='font-size:8px; color:#64748B;'>Active • Sync 100%</span></html>");
        clusterPill.add(dot);
        clusterPill.add(clusterTxt);
        sidebar.add(clusterPill);

        return sidebar;
    }

    private static JButton createNavItem(String title, Feather icon, boolean active, Runnable action) {
        JButton btn = new JButton(title, FontIcon.of(icon, 13, active ? Color.WHITE : COLOR_TEXT_MAIN)) {
            @Override
            protected void paintComponent(Graphics g) {
                if (active) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0x60, 0x8E, 0xB5));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 12));
        btn.setForeground(active ? Color.WHITE : COLOR_TEXT_MAIN);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));
        btn.setBorder(new EmptyBorder(6, 10, 6, 10));
        btn.addActionListener(e -> action.run());
        return btn;
    }
}
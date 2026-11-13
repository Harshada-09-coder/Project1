package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminProductManagementPage extends JFrame {

    public AdminProductManagementPage() {
        setTitle("ShopSphere Admin - Manage Products");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Products"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Products"), BorderLayout.WEST);
        main.add(createProductCatalogBody(), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JComponent createProductCatalogBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // 1. Top Header Banner with "+ Add New Product" Button
        body.add(createHeaderActionBar());
        body.add(Box.createVerticalStrut(14));

        // 2. 4 Metric Cards (Total, Active, Out of Stock, Low Stock)
        body.add(createMetricsRow());
        body.add(Box.createVerticalStrut(14));

        // 3. Search & Filter Toolbar
        body.add(createFilterToolbar());
        body.add(Box.createVerticalStrut(14));

        // 4. Products Table Card
        body.add(createProductsTableCard());
        body.add(Box.createVerticalStrut(14));

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createHeaderActionBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel h1 = new JLabel("Manage Products (Catalog Active)");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        h1.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("Manage your store products, pricing, availability, and regional centers.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(h1);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JButton btnExport = new JButton("Export CSV", FontIcon.of(Feather.DOWNLOAD, 12, AdminNavUtils.COLOR_TEXT_MAIN));
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnExport.setBackground(Color.WHITE);

        JButton btnAddProduct = new JButton("+  Add New Product", FontIcon.of(Feather.PLUS, 12, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x60, 0x8E, 0xB5));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnAddProduct.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAddProduct.setForeground(Color.WHITE);
        btnAddProduct.setContentAreaFilled(false);
        btnAddProduct.setBorderPainted(false);
        btnAddProduct.setFocusPainted(false);
        btnAddProduct.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddProduct.addActionListener(e -> {
            new AdminAddProductPage().setVisible(true);
            dispose();
        });

        right.add(btnExport);
        right.add(btnAddProduct);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createMetricsRow() {
        JPanel r = new JPanel(new GridLayout(1, 4, 14, 0));
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

        r.add(createMetricCard("TOTAL PRODUCTS", "356", "Total catalog items", Feather.BOX, AdminNavUtils.COLOR_PRIMARY_DARK));
        r.add(createMetricCard("ACTIVE PRODUCTS", "312", "Live in storefront (87.6%)", Feather.CHECK_CIRCLE, AdminNavUtils.COLOR_GREEN));
        r.add(createMetricCard("OUT OF STOCK", "18", "Needs immediate restock", Feather.ALERT_CIRCLE, AdminNavUtils.COLOR_RED));
        r.add(createMetricCard("LOW STOCK", "26", "Threshold ≤ 10 alert", Feather.ALERT_TRIANGLE, AdminNavUtils.COLOR_ORANGE));

        return r;
    }

    private JPanel createMetricCard(String title, String val, String sub, Feather icon, Color accent) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(12, 14, 12, 14));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JLabel v = new JLabel(val);
        v.setFont(new Font("Segoe UI", Font.BOLD, 20));
        v.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(t);
        left.add(Box.createVerticalStrut(2));
        left.add(v);
        left.add(s);

        p.add(left, BorderLayout.CENTER);
        p.add(new JLabel(FontIcon.of(icon, 18, accent)), BorderLayout.EAST);
        return p;
    }

    private JPanel createFilterToolbar() {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout(10, 0));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 46));

        UIUtils.RoundedPanel sBox = new UIUtils.RoundedPanel(8, AdminNavUtils.COLOR_INPUT_BG, AdminNavUtils.COLOR_BORDER);
        sBox.setPreferredSize(new Dimension(320, 30));
        sBox.setLayout(new BorderLayout());
        sBox.setBorder(new EmptyBorder(0, 8, 0, 8));
        sBox.add(new JLabel(FontIcon.of(Feather.SEARCH, 12, AdminNavUtils.COLOR_TEXT_MUTED)), BorderLayout.WEST);
        JTextField search = new JTextField("Search by product name or SKU...");
        search.setBorder(null);
        search.setOpaque(false);
        sBox.add(search, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(new JComboBox<>(new String[]{"All Categories", "Electronics", "Fashion", "Home & Living"}));
        right.add(new JComboBox<>(new String[]{"All Status", "In Stock", "Low Stock", "Out of Stock"}));
        right.add(new JComboBox<>(new String[]{"Sort: Newest First", "Price: Low to High", "Price: High to Low"}));

        p.add(sBox, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createProductsTableCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        String[] cols = {"PRODUCT", "SKU / ID", "CATEGORY", "PRICE", "STOCK LEVEL", "STATUS", "ACTIONS"};
        Object[][] data = {
                {"Wireless Headphones", "PRD-001", "Electronics", "₹2,499", "45 units (Healthy)", "In Stock", "👁  ✏️  🗑"},
                {"Smart Watch", "PRD-002", "Electronics", "₹3,999", "3 units (Critical)", "Low Stock", "👁  ✏️  🗑"},
                {"Casual Sneakers", "PRD-003", "Fashion", "₹1,799", "28 units (Adequate)", "In Stock", "👁  ✏️  🗑"},
                {"Bluetooth Speaker", "PRD-004", "Electronics", "₹1,299", "0 units (Depleted)", "Out of Stock", "👁  ✏️  🗑"},
                {"Backpack", "PRD-005", "Accessories", "₹899", "12 units (Moderate)", "In Stock", "👁  ✏️  🗑"},
                {"Water Bottle", "PRD-006", "Home & Living", "₹399", "7 units (Low)", "Low Stock", "👁  ✏️  🗑"}
        };

        JTable table = new JTable(data, cols);
        table.setRowHeight(36);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setShowGrid(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(800, 240));

        // Table Bottom Pagination
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(12, 0, 0, 0));
        bottom.add(new JLabel("Showing 1-6 of 356 products"), BorderLayout.WEST);

        JPanel pages = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pages.setOpaque(false);
        pages.add(new JButton("1"));
        pages.add(new JButton("2"));
        pages.add(new JButton("Next ➔"));
        bottom.add(pages, BorderLayout.EAST);

        card.add(scroll, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }
}
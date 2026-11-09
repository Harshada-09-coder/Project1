package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class AdminCategoryManagementPage extends JFrame {

    public AdminCategoryManagementPage() {
        setTitle("ShopSphere Admin - Manage Categories");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Categories"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Categories"), BorderLayout.WEST);
        main.add(createCategoryScrollableBody(), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JComponent createCategoryScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // 1. Top Header Action Banner
        body.add(createHeaderActionBar());
        body.add(Box.createVerticalStrut(14));

        // 2. 4 Metric KPI Cards
        body.add(createMetricsRow());
        body.add(Box.createVerticalStrut(14));

        // 3. Search & Filter Bar
        body.add(createFilterToolbar());
        body.add(Box.createVerticalStrut(14));

        // 4. Categories Table Card
        body.add(createCategoryTableCard());
        body.add(Box.createVerticalStrut(16));

        // 5. Bottom Split: Hierarchy JTree (Left) + Donut Allocation (Right)
        JPanel bottomSplit = new JPanel(new GridBagLayout());
        bottomSplit.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.60;
        gbc.insets = new Insets(0, 0, 0, 16);
        bottomSplit.add(createHierarchyTreeCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.40;
        gbc.insets = new Insets(0, 0, 0, 0);
        bottomSplit.add(createSkuAllocationDonutCard(), gbc);

        body.add(bottomSplit);

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

        JLabel h1 = new JLabel("Manage Categories (Store Taxonomy)");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        h1.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("Organize and manage your store categories and subcategories with inventory alignment.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(h1);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JButton btnAddCategory = new JButton("+ Add New Category", FontIcon.of(Feather.PLUS, 12, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x33, 0x4E, 0x68));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnAddCategory.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAddCategory.setForeground(Color.WHITE);
        btnAddCategory.setContentAreaFilled(false);
        btnAddCategory.setBorderPainted(false);
        btnAddCategory.setFocusPainted(false);
        btnAddCategory.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddCategory.addActionListener(e -> JOptionPane.showMessageDialog(this, "Add Category Dialog: Creates a new taxonomy branch.", "Taxonomy Node", JOptionPane.INFORMATION_MESSAGE));

        right.add(btnAddCategory);
        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createMetricsRow() {
        JPanel r = new JPanel(new GridLayout(1, 4, 14, 0));
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 85));

        r.add(createMetricCard("TOTAL CATEGORIES", "7", "Catalog groups", Feather.LAYERS, AdminNavUtils.COLOR_PRIMARY_DARK));
        r.add(createMetricCard("ACTIVE CATEGORIES", "6", "+1 enabled", Feather.CHECK_CIRCLE, AdminNavUtils.COLOR_GREEN));
        r.add(createMetricCard("TOTAL SUBCATEGORIES", "18", "Sub-tier nodes", Feather.GIT_BRANCH, AdminNavUtils.COLOR_ORANGE));
        r.add(createMetricCard("PRODUCTS ASSIGNED", "356", "Live SKUs", Feather.BOX, new Color(0x63, 0x66, 0xF1)));

        return r;
    }

    private JPanel createMetricCard(String title, String val, String sub, Feather icon, Color accent) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JLabel v = new JLabel(val);
        v.setFont(new Font("Segoe UI", Font.BOLD, 19));
        v.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(t);
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
        JTextField search = new JTextField("Search category name or ID...");
        search.setBorder(null);
        search.setOpaque(false);
        sBox.add(search, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(new JComboBox<>(new String[]{"STATUS: All Status", "Active", "Inactive"}));
        right.add(new JComboBox<>(new String[]{"SORT: A-Z", "Sort: Product Count", "Sort: Newest"}));

        p.add(sBox, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createCategoryTableCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        String[] cols = {"CATEGORY", "ID", "DESCRIPTION", "PRODUCTS COUNT", "SUBCATEGORIES", "STATUS", "DATE CREATED"};
        Object[][] data = {
                {"Electronics", "CAT-001", "Electronic devices and accessories", "120 items", "5 subcategories", "● Active", "10 Oct 2026"},
                {"Fashion", "CAT-002", "Clothing, footwear, and apparel", "85 items", "4 subcategories", "● Active", "12 Oct 2026"},
                {"Home & Living", "CAT-003", "Home essentials and lifestyle products", "46 items", "3 subcategories", "● Active", "14 Oct 2026"},
                {"Beauty & Care", "CAT-004", "Beauty and personal care products", "38 items", "2 subcategories", "● Active", "16 Oct 2026"},
                {"Books", "CAT-005", "Books, notebooks, and stationery", "24 items", "1 subcategory", "● Active", "18 Oct 2026"},
                {"Sports & Fitness", "CAT-006", "Sports equipment and fitness accessories", "25 items", "2 subcategories", "● Inactive", "20 Oct 2026"},
                {"Accessories", "CAT-007", "Everyday accessories and travel gear", "18 items", "1 subcategory", "● Active", "22 Oct 2026"}
        };

        JTable table = new JTable(data, cols);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setShowGrid(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(800, 220));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createHierarchyTreeCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Hierarchy Navigator (Parent Categories & Sub-branches)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Catalog Root (ShopSphere Global)");
        DefaultMutableTreeNode el = new DefaultMutableTreeNode("Electronics (120 items)");
        el.add(new DefaultMutableTreeNode("Smartphones & Tablets (45)"));
        el.add(new DefaultMutableTreeNode("Audio & Headphones (37)"));
        el.add(new DefaultMutableTreeNode("Laptops & Computing (38)"));

        DefaultMutableTreeNode fa = new DefaultMutableTreeNode("Fashion (85 items)");
        fa.add(new DefaultMutableTreeNode("Men's Apparel (40)"));
        fa.add(new DefaultMutableTreeNode("Footwear (45)"));

        root.add(el);
        root.add(fa);
        root.add(new DefaultMutableTreeNode("Home & Living (46 items)"));

        JTree tree = new JTree(root);
        tree.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        card.add(t, BorderLayout.NORTH);
        card.add(new JScrollPane(tree), BorderLayout.CENTER);
        return card;
    }

    private JPanel createSkuAllocationDonutCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("SKU Allocation by Category");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel donut = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = 60, r = 45;

                g2.setStroke(new BasicStroke(14f));
                g2.setColor(new Color(0x3B, 0x82, 0xF6));
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 0, 130);
                g2.setColor(AdminNavUtils.COLOR_GREEN);
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 130, 90);
                g2.setColor(AdminNavUtils.COLOR_ORANGE);
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 220, 80);
                g2.setColor(new Color(0x63, 0x66, 0xF1));
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 300, 60);

                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2.setColor(AdminNavUtils.COLOR_TEXT_MAIN);
                g2.drawString("356", cx - 12, cy + 4);
                g2.dispose();
            }
        };
        donut.setPreferredSize(new Dimension(200, 120));

        JPanel legend = new JPanel(new GridLayout(2, 2, 4, 4));
        legend.setOpaque(false);
        legend.add(new JLabel("● Electronics (34%)"));
        legend.add(new JLabel("● Fashion (24%)"));
        legend.add(new JLabel("● Home & Living (13%)"));
        legend.add(new JLabel("● Others (29%)"));

        card.add(t, BorderLayout.NORTH);
        card.add(donut, BorderLayout.CENTER);
        card.add(legend, BorderLayout.SOUTH);
        return card;
    }
}
package com.shopsphere.view;

import com.shopsphere.entity.Category;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ValidationException;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.util.List;

public class AdminCategoryManagementPage extends JFrame {

    private final CategoryService categoryService = new CategoryService();

    private JLabel lblTotalCategories;
    private JLabel lblActiveCategories;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTree hierarchyTree;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private JTextField txtSearch;

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

        loadCategoryData("");
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

        // 5. Bottom Split: Hierarchy JTree (Left) + Taxonomy Details (Right)
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
        bottomSplit.add(createTaxonomyInfoCard(), gbc);

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

        JLabel sub = new JLabel("Organize store catalog hierarchy, classifications, and category metadata.");
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
        btnAddCategory.addActionListener(e -> showAddCategoryDialog());

        right.add(btnAddCategory);
        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createMetricsRow() {
        JPanel r = new JPanel(new GridLayout(1, 4, 14, 0));
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 85));

        lblTotalCategories = new JLabel("0");
        lblActiveCategories = new JLabel("0");

        r.add(createMetricCard("TOTAL CATEGORIES", lblTotalCategories, "Catalog departments", Feather.LAYERS, AdminNavUtils.COLOR_PRIMARY_DARK));
        r.add(createMetricCard("ACTIVE STATUS", lblActiveCategories, "Enabled in storefront", Feather.CHECK_CIRCLE, AdminNavUtils.COLOR_GREEN));
        r.add(createMetricCard("TAXONOMY STRUCTURE", new JLabel("Hierarchical"), "Category root branches", Feather.GIT_BRANCH, AdminNavUtils.COLOR_ORANGE));
        r.add(createMetricCard("STORE NODES", new JLabel("Synchronized"), "MySQL shopsphere", Feather.DATABASE, new Color(0x63, 0x66, 0xF1)));

        return r;
    }

    private JPanel createMetricCard(String title, JLabel valLbl, String sub, Feather icon, Color accent) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 19));
        valLbl.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(t);
        left.add(valLbl);
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

        txtSearch = new JTextField();
        txtSearch.setToolTipText("Search category name or description...");
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.addActionListener(e -> loadCategoryData(txtSearch.getText().trim()));
        sBox.add(txtSearch, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JButton btnDelete = new JButton("Delete Category", FontIcon.of(Feather.TRASH_2, 12, AdminNavUtils.COLOR_RED));
        btnDelete.addActionListener(e -> handleDeleteCategory());
        right.add(btnDelete);

        p.add(sBox, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createCategoryTableCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        String[] cols = {"ID", "CATEGORY NAME", "DESCRIPTION", "STATUS"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(34);
        categoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        categoryTable.setShowGrid(false);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(categoryTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(800, 240));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createHierarchyTreeCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Category Hierarchy Tree");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        rootNode = new DefaultMutableTreeNode("ShopSphere Taxonomy");
        treeModel = new DefaultTreeModel(rootNode);
        hierarchyTree = new JTree(treeModel);
        hierarchyTree.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hierarchyTree.setRowHeight(24);

        JScrollPane treeScroll = new JScrollPane(hierarchyTree);
        treeScroll.setBorder(BorderFactory.createLineBorder(AdminNavUtils.COLOR_BORDER));
        treeScroll.setPreferredSize(new Dimension(400, 160));

        card.add(t, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        card.add(treeScroll, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createTaxonomyInfoCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Taxonomy Rules & Integration");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel info = new JLabel("<html><body style='width:240px; font-size:11px; line-height:1.5; color:#64748B;'>"
                + "Categories provide top-level organization for the customer catalog.<br><br>"
                + "• Categories cannot have duplicate names.<br>"
                + "• Products must be linked to a valid category.<br>"
                + "• Disabling a category hides associated items in customer storefront."
                + "</body></html>");

        card.add(t);
        card.add(Box.createVerticalStrut(12));
        card.add(info);
        return card;
    }

    private void loadCategoryData(String searchFilter) {
        tableModel.setRowCount(0);
        rootNode.removeAllChildren();

        try {
            List<Category> list = categoryService.getAllCategories();
            int total = list.size();
            int active = 0;
            String term = (searchFilter != null) ? searchFilter.toLowerCase().trim() : "";

            for (Category c : list) {
                if (c.isActive()) active++;

                boolean match = term.isEmpty()
                        || (c.getCategoryName() != null && c.getCategoryName().toLowerCase().contains(term))
                        || (c.getDescription() != null && c.getDescription().toLowerCase().contains(term));

                if (match) {
                    tableModel.addRow(new Object[]{
                            c.getCategoryId(),
                            c.getCategoryName(),
                            (c.getDescription() != null && !c.getDescription().isEmpty()) ? c.getDescription() : "—",
                            c.isActive() ? "Active" : "Inactive"
                    });
                }

                DefaultMutableTreeNode catNode = new DefaultMutableTreeNode(c.getCategoryName() + " (ID: #" + c.getCategoryId() + ")");
                rootNode.add(catNode);
            }

            lblTotalCategories.setText(String.valueOf(total));
            lblActiveCategories.setText(String.valueOf(active));

            treeModel.reload();
            for (int i = 0; i < hierarchyTree.getRowCount(); i++) {
                hierarchyTree.expandRow(i);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddCategoryDialog() {
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
        panel.add(new JLabel("Category Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            try {
                int newId = categoryService.addCategory(name, desc);
                if (newId > 0) {
                    JOptionPane.showMessageDialog(this, "Category '" + name + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCategoryData("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create category.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ValidationException ve) {
                JOptionPane.showMessageDialog(this, ve.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleDeleteCategory() {
        int row = categoryTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int catId = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete category #" + catId + " (" + name + ")?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = categoryService.deleteCategory(catId);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Category deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCategoryData(txtSearch.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete category. Ensure no products are currently assigned to this category.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


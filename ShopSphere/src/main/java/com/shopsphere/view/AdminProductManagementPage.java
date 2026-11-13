package com.shopsphere.view;

import com.shopsphere.entity.Category;
import com.shopsphere.entity.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminProductManagementPage extends JFrame {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    private JLabel lblTotalCount;
    private JLabel lblActiveCount;
    private JLabel lblOutOfStockCount;
    private JLabel lblLowStockCount;

    private JTable productsTable;
    private DefaultTableModel tableModel;
    private JLabel lblTableSummary;
    private JTextField searchInput;
    private JComboBox<String> cmbCategory;

    private final Map<Integer, String> categoryMap = new HashMap<>();
    private final Map<String, Integer> categoryNameToId = new HashMap<>();
    private List<Product> currentProductsList;

    public AdminProductManagementPage() {
        setTitle("ShopSphere Admin - Manage Products");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        loadCategories();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Products"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Products"), BorderLayout.WEST);
        main.add(createProductCatalogBody(), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);

        loadProductData("");
    }

    private void loadCategories() {
        categoryMap.clear();
        categoryNameToId.clear();
        try {
            List<Category> categories = categoryService.getAllCategories();
            for (Category c : categories) {
                categoryMap.put(c.getCategoryId(), c.getCategoryName());
                categoryNameToId.put(c.getCategoryName(), c.getCategoryId());
            }
        } catch (Exception ignored) {}
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

        JLabel sub = new JLabel("Manage store inventory, real-time pricing, stock deductions, and catalog visibility.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(h1);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

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

        right.add(btnAddProduct);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createMetricsRow() {
        JPanel r = new JPanel(new GridLayout(1, 4, 14, 0));
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

        lblTotalCount = new JLabel("0");
        lblActiveCount = new JLabel("0");
        lblOutOfStockCount = new JLabel("0");
        lblLowStockCount = new JLabel("0");

        r.add(createMetricCard("TOTAL PRODUCTS", lblTotalCount, "Total items in catalog", Feather.BOX, AdminNavUtils.COLOR_PRIMARY_DARK));
        r.add(createMetricCard("ACTIVE PRODUCTS", lblActiveCount, "Live in customer storefront", Feather.CHECK_CIRCLE, AdminNavUtils.COLOR_GREEN));
        r.add(createMetricCard("OUT OF STOCK", lblOutOfStockCount, "Needs inventory replenishment", Feather.ALERT_CIRCLE, AdminNavUtils.COLOR_RED));
        r.add(createMetricCard("LOW STOCK", lblLowStockCount, "Threshold ≤ 10 alert", Feather.ALERT_TRIANGLE, AdminNavUtils.COLOR_ORANGE));

        return r;
    }

    private JPanel createMetricCard(String title, JLabel valLbl, String sub, Feather icon, Color accent) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(12, 14, 12, 14));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valLbl.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(t);
        left.add(Box.createVerticalStrut(2));
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

        searchInput = new JTextField();
        searchInput.setToolTipText("Search by product name, brand, or SKU...");
        searchInput.setBorder(null);
        searchInput.setOpaque(false);
        searchInput.addActionListener(e -> applyFilter());
        sBox.add(searchInput, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        cmbCategory = new JComboBox<>();
        cmbCategory.addItem("All Categories");
        for (String catName : categoryNameToId.keySet()) {
            cmbCategory.addItem(catName);
        }
        cmbCategory.addActionListener(e -> applyFilter());

        JButton btnEdit = new JButton("Edit Selected", FontIcon.of(Feather.EDIT_2, 12, AdminNavUtils.COLOR_PRIMARY_DARK));
        btnEdit.addActionListener(e -> handleEditSelected());

        JButton btnDelete = new JButton("Delete", FontIcon.of(Feather.TRASH_2, 12, AdminNavUtils.COLOR_RED));
        btnDelete.addActionListener(e -> handleDeleteSelected());

        right.add(cmbCategory);
        right.add(btnEdit);
        right.add(btnDelete);

        p.add(sBox, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createProductsTableCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        String[] cols = {"ID", "PRODUCT NAME", "SKU", "CATEGORY", "PRICE", "STOCK LEVEL", "STATUS"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        productsTable = new JTable(tableModel);
        productsTable.setRowHeight(34);
        productsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        productsTable.setShowGrid(false);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(productsTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(800, 320));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(12, 0, 0, 0));

        lblTableSummary = new JLabel("Showing 0 products");
        lblTableSummary.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTableSummary.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        bottom.add(lblTableSummary, BorderLayout.WEST);

        card.add(scroll, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private void applyFilter() {
        String term = searchInput.getText().trim();
        loadProductData(term);
    }

    private void loadProductData(String searchFilter) {
        tableModel.setRowCount(0);
        try {
            currentProductsList = productService.getAllActiveProducts();

            int total = currentProductsList.size();
            int active = 0;
            int outOfStock = 0;
            int lowStock = 0;

            String selectedCat = (String) cmbCategory.getSelectedItem();
            Integer filterCatId = (selectedCat != null && !selectedCat.equals("All Categories")) ? categoryNameToId.get(selectedCat) : null;
            String term = (searchFilter != null) ? searchFilter.toLowerCase().trim() : "";

            int shown = 0;
            for (Product p : currentProductsList) {
                if (p.isActive()) active++;
                if (p.getStockQuantity() == 0) outOfStock++;
                else if (p.getStockQuantity() <= 10) lowStock++;

                boolean catMatch = (filterCatId == null || p.getCategoryId() == filterCatId);
                boolean textMatch = term.isEmpty()
                        || (p.getProductName() != null && p.getProductName().toLowerCase().contains(term))
                        || (p.getSku() != null && p.getSku().toLowerCase().contains(term))
                        || (p.getBrand() != null && p.getBrand().toLowerCase().contains(term));

                if (catMatch && textMatch) {
                    String catName = categoryMap.getOrDefault(p.getCategoryId(), "General");
                    String stockStatus = p.getStockQuantity() == 0 ? "Out of Stock" :
                            p.getStockQuantity() <= 10 ? p.getStockQuantity() + " units (Low)" :
                                    p.getStockQuantity() + " units";

                    String statusText = !p.isActive() ? "Inactive" : (p.getStockQuantity() > 0 ? "In Stock" : "Depleted");

                    tableModel.addRow(new Object[]{
                            p.getProductId(),
                            p.getProductName(),
                            (p.getSku() != null) ? p.getSku() : "—",
                            catName,
                            String.format("₹%.2f", p.getPrice()),
                            stockStatus,
                            statusText
                    });
                    shown++;
                }
            }

            lblTotalCount.setText(String.valueOf(total));
            lblActiveCount.setText(String.valueOf(active));
            lblOutOfStockCount.setText(String.valueOf(outOfStock));
            lblLowStockCount.setText(String.valueOf(lowStock));
            lblTableSummary.setText("Showing " + shown + " of " + total + " products in catalog");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEditSelected() {
        int row = productsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(row, 0);
        Product product = productService.getProductById(productId);
        if (product != null) {
            new AdminEditProductPage(product).setVisible(true);
            dispose();
        }
    }

    private void handleDeleteSelected() {
        int row = productsTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product from the table to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to deactivate/delete product #" + productId + " (" + name + ")?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = productService.deleteProduct(productId);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Product deleted from catalog successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadProductData(searchInput.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


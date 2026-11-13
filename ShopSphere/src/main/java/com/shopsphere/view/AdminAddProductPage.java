package com.shopsphere.view;

import com.shopsphere.entity.Category;
import com.shopsphere.entity.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.ValidationException;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAddProductPage extends JFrame {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    private JTextField txtTitle;
    private JTextField txtSku;
    private JTextField txtBrand;
    private JTextField txtPrice;
    private JTextField txtStock;
    private JTextArea txtDescription;
    private JTextField txtImageUrl;
    private JComboBox<String> cmbCategory;

    private JLabel lblPreviewTitle;
    private JLabel lblPreviewPrice;
    private JLabel lblPreviewCategory;

    private final Map<String, Integer> categoryMap = new HashMap<>();

    public AdminAddProductPage() {
        setTitle("ShopSphere Admin - Add New Product");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        loadCategories();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Add Product"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Products"), BorderLayout.WEST);
        main.add(createAddProductScrollableBody(), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void loadCategories() {
        categoryMap.clear();
        try {
            List<Category> list = categoryService.getAllActiveCategories();
            for (Category c : list) {
                categoryMap.put(c.getCategoryName(), c.getCategoryId());
            }
        } catch (Exception ignored) {}
    }

    private JComponent createAddProductScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // Top Header Row with "Back to Products"
        body.add(createTopActionBanner());
        body.add(Box.createVerticalStrut(14));

        // Split Layout: Left Form (62%) | Right Live Preview & Upload (38%)
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Column (Forms)
        gbc.gridx = 0;
        gbc.weightx = 0.62;
        gbc.insets = new Insets(0, 0, 0, 16);
        JPanel leftForms = new JPanel();
        leftForms.setLayout(new BoxLayout(leftForms, BoxLayout.Y_AXIS));
        leftForms.setOpaque(false);

        leftForms.add(createBasicInfoCard());
        leftForms.add(Box.createVerticalStrut(14));
        leftForms.add(createPricingAndInventoryCard());
        leftForms.add(Box.createVerticalStrut(14));

        formGrid.add(leftForms, gbc);

        // Right Column (Images & Live Storefront Preview)
        gbc.gridx = 1;
        gbc.weightx = 0.38;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(false);

        rightCol.add(createLivePreviewCard());
        rightCol.add(Box.createVerticalStrut(14));

        formGrid.add(rightCol, gbc);
        body.add(formGrid);
        body.add(Box.createVerticalStrut(16));

        // Bottom Action Footer Bar
        body.add(createBottomDeploymentBar());
        body.add(Box.createVerticalStrut(16));

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createTopActionBanner() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        JButton btnBack = new JButton("← Back to Products");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            new AdminProductManagementPage().setVisible(true);
            dispose();
        });

        p.add(btnBack, BorderLayout.WEST);
        return p;
    }

    private JPanel createBasicInfoCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Basic Information");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(t);
        card.add(Box.createVerticalStrut(12));

        txtTitle = new JTextField();
        txtTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewTitle.setText(txtTitle.getText().isEmpty() ? "Product Name Preview" : txtTitle.getText());
            }
        });
        card.add(createSimpleInput("Product Title *", txtTitle));
        card.add(Box.createVerticalStrut(10));

        JPanel r2 = new JPanel(new GridLayout(1, 2, 12, 0));
        r2.setOpaque(false);
        txtSku = new JTextField();
        txtBrand = new JTextField();
        r2.add(createSimpleInput("SKU / Product Code", txtSku));
        r2.add(createSimpleInput("Brand Name", txtBrand));
        card.add(r2);
        card.add(Box.createVerticalStrut(10));

        cmbCategory = new JComboBox<>();
        for (String catName : categoryMap.keySet()) {
            cmbCategory.addItem(catName);
        }
        cmbCategory.addActionListener(e -> {
            String selected = (String) cmbCategory.getSelectedItem();
            lblPreviewCategory.setText(selected != null ? selected : "Category");
        });
        card.add(createComboInput("Product Category *", cmbCategory));
        card.add(Box.createVerticalStrut(10));

        JLabel lDesc = new JLabel("Product Description");
        lDesc.setFont(new Font("Segoe UI", Font.BOLD, 11));
        txtDescription = new JTextArea(4, 20);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        descScroll.setBorder(BorderFactory.createLineBorder(AdminNavUtils.COLOR_BORDER));

        card.add(lDesc);
        card.add(Box.createVerticalStrut(4));
        card.add(descScroll);
        card.add(Box.createVerticalStrut(10));

        txtImageUrl = new JTextField();
        card.add(createSimpleInput("Image URL (Optional)", txtImageUrl));

        return card;
    }

    private JPanel createPricingAndInventoryCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Pricing & Inventory Policy");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(t);
        card.add(Box.createVerticalStrut(12));

        JPanel grid = new JPanel(new GridLayout(1, 2, 12, 0));
        grid.setOpaque(false);

        txtPrice = new JTextField("0.00");
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewPrice.setText("₹" + (txtPrice.getText().isEmpty() ? "0.00" : txtPrice.getText()));
            }
        });
        txtStock = new JTextField("10");

        grid.add(createSimpleInput("Selling Price (₹) *", txtPrice));
        grid.add(createSimpleInput("Stock Quantity *", txtStock));

        card.add(grid);
        return card;
    }

    private JPanel createLivePreviewCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Live Storefront Preview");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(t);
        card.add(Box.createVerticalStrut(12));

        UIUtils.RoundedPanel mockCard = new UIUtils.RoundedPanel(12, AdminNavUtils.COLOR_INPUT_BG, AdminNavUtils.COLOR_BORDER);
        mockCard.setLayout(new BoxLayout(mockCard, BoxLayout.Y_AXIS));
        mockCard.setBorder(new EmptyBorder(16, 14, 16, 14));

        JPanel iconBox = new JPanel(new GridBagLayout());
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(80, 80));
        iconBox.add(new JLabel(FontIcon.of(Feather.BOX, 36, AdminNavUtils.COLOR_PRIMARY_DARK)));

        lblPreviewCategory = new JLabel("Electronics");
        lblPreviewCategory.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblPreviewCategory.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        lblPreviewTitle = new JLabel("Product Title Preview");
        lblPreviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPreviewTitle.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        lblPreviewPrice = new JLabel("₹0.00");
        lblPreviewPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPreviewPrice.setForeground(AdminNavUtils.COLOR_PRIMARY_DARK);

        mockCard.add(iconBox);
        mockCard.add(Box.createVerticalStrut(8));
        mockCard.add(lblPreviewCategory);
        mockCard.add(Box.createVerticalStrut(4));
        mockCard.add(lblPreviewTitle);
        mockCard.add(Box.createVerticalStrut(4));
        mockCard.add(lblPreviewPrice);

        card.add(mockCard);
        return card;
    }

    private JPanel createBottomDeploymentBar() {
        UIUtils.RoundedPanel bar = new UIUtils.RoundedPanel(14, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        bar.setLayout(new BorderLayout());
        bar.setBorder(new EmptyBorder(10, 18, 10, 18));
        bar.setMaximumSize(new Dimension(Short.MAX_VALUE, 48));

        JLabel syncNote = new JLabel("● Ready to insert into MySQL catalog");
        syncNote.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        syncNote.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setOpaque(false);

        JButton btnReset = new JButton("Reset Form");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnReset.addActionListener(e -> resetForm());

        JButton btnSave = new JButton("+ Add Product", FontIcon.of(Feather.CHECK, 12, Color.WHITE)) {
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
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setForeground(Color.WHITE);
        btnSave.setContentAreaFilled(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> handleAddProduct());

        btns.add(btnReset);
        btns.add(btnSave);

        bar.add(syncNote, BorderLayout.WEST);
        bar.add(btns, BorderLayout.EAST);
        return bar;
    }

    private void handleAddProduct() {
        try {
            String title = txtTitle.getText().trim();
            String sku = txtSku.getText().trim();
            String brand = txtBrand.getText().trim();
            String desc = txtDescription.getText().trim();
            String img = txtImageUrl.getText().trim();
            String priceStr = txtPrice.getText().trim();
            String stockStr = txtStock.getText().trim();
            String selectedCat = (String) cmbCategory.getSelectedItem();

            if (title.isEmpty()) {
                throw new ValidationException("Product title cannot be empty.");
            }
            if (selectedCat == null || !categoryMap.containsKey(selectedCat)) {
                throw new ValidationException("Please select a valid category.");
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                throw new ValidationException("Invalid price. Please enter a valid decimal number.");
            }

            int stock;
            try {
                stock = Integer.parseInt(stockStr);
            } catch (NumberFormatException e) {
                throw new ValidationException("Invalid stock. Please enter a non-negative integer.");
            }

            int categoryId = categoryMap.get(selectedCat);

            Product product = new Product(
                    categoryId,
                    title,
                    desc,
                    price,
                    stock,
                    sku.isEmpty() ? null : sku,
                    brand.isEmpty() ? null : brand,
                    img.isEmpty() ? null : img,
                    true
            );

            int newId = productService.addProduct(product);
            if (newId > 0) {
                JOptionPane.showMessageDialog(this, "Product #" + newId + " added to catalog successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new AdminProductManagementPage().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product. Check database connection.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        txtTitle.setText("");
        txtSku.setText("");
        txtBrand.setText("");
        txtPrice.setText("0.00");
        txtStock.setText("10");
        txtDescription.setText("");
        txtImageUrl.setText("");
        lblPreviewTitle.setText("Product Title Preview");
        lblPreviewPrice.setText("₹0.00");
    }

    private JPanel createSimpleInput(String label, JTextField tf) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        p.add(l);
        p.add(Box.createVerticalStrut(3));
        p.add(tf);
        return p;
    }

    private JPanel createComboInput(String label, JComboBox<?> cb) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        p.add(l);
        p.add(Box.createVerticalStrut(3));
        p.add(cb);
        return p;
    }
}


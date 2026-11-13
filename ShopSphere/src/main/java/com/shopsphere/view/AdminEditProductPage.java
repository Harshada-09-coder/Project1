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

public class AdminEditProductPage extends JFrame {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    private final Product productToEdit;
    private JTextField txtTitle;
    private JTextField txtSku;
    private JTextField txtBrand;
    private JTextField txtPrice;
    private JTextField txtStock;
    private JTextArea txtDescription;
    private JTextField txtImageUrl;
    private JComboBox<String> cmbCategory;
    private JCheckBox chkActive;

    private JLabel lblPreviewTitle;
    private JLabel lblPreviewPrice;

    private final Map<String, Integer> categoryMap = new HashMap<>();

    public AdminEditProductPage() {
        this(createDefaultProduct());
    }

    private static Product createDefaultProduct() {
        Product p = new Product();
        p.setProductId(1);
        p.setCategoryId(1);
        p.setProductName("Sample Product");
        p.setDescription("Description");
        p.setPrice(999.00);
        p.setStockQuantity(20);
        p.setSku("SKU-001");
        p.setBrand("Generic");
        p.setActive(true);
        return p;
    }

    public AdminEditProductPage(Product product) {
        this.productToEdit = product;
        setTitle("ShopSphere Admin - Edit Product (#" + product.getProductId() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        loadCategories();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Edit Product"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Products"), BorderLayout.WEST);
        main.add(createEditScrollableBody(), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void loadCategories() {
        categoryMap.clear();
        try {
            List<Category> list = categoryService.getAllCategories();
            for (Category c : list) {
                categoryMap.put(c.getCategoryName(), c.getCategoryId());
            }
        } catch (Exception ignored) {}
    }

    private JComponent createEditScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        body.add(createTopBanner());
        body.add(Box.createVerticalStrut(14));

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.62;
        gbc.insets = new Insets(0, 0, 0, 16);
        JPanel leftForms = new JPanel();
        leftForms.setLayout(new BoxLayout(leftForms, BoxLayout.Y_AXIS));
        leftForms.setOpaque(false);

        leftForms.add(createBasicInfoCard());
        leftForms.add(Box.createVerticalStrut(14));
        leftForms.add(createPricingCard());

        formGrid.add(leftForms, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.38;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(false);

        rightCol.add(createLivePreviewCard());

        formGrid.add(rightCol, gbc);
        body.add(formGrid);
        body.add(Box.createVerticalStrut(16));

        body.add(createBottomActionBar());
        body.add(Box.createVerticalStrut(16));

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createTopBanner() {
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

        JLabel idLabel = new JLabel("Editing Product ID: #" + productToEdit.getProductId() + " (" + (productToEdit.getSku() != null ? productToEdit.getSku() : "NO SKU") + ")");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        p.add(btnBack, BorderLayout.WEST);
        p.add(idLabel, BorderLayout.EAST);
        return p;
    }

    private JPanel createBasicInfoCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Product Specification");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(t);
        card.add(Box.createVerticalStrut(12));

        txtTitle = new JTextField(productToEdit.getProductName());
        txtTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewTitle.setText(txtTitle.getText());
            }
        });
        card.add(createFieldBlock("Product Name *", txtTitle));
        card.add(Box.createVerticalStrut(10));

        JPanel r2 = new JPanel(new GridLayout(1, 2, 12, 0));
        r2.setOpaque(false);
        txtSku = new JTextField(productToEdit.getSku() != null ? productToEdit.getSku() : "");
        txtBrand = new JTextField(productToEdit.getBrand() != null ? productToEdit.getBrand() : "");
        r2.add(createFieldBlock("SKU", txtSku));
        r2.add(createFieldBlock("Brand", txtBrand));
        card.add(r2);
        card.add(Box.createVerticalStrut(10));

        cmbCategory = new JComboBox<>();
        String currentCatName = "General";
        for (Map.Entry<String, Integer> entry : categoryMap.entrySet()) {
            cmbCategory.addItem(entry.getKey());
            if (entry.getValue() == productToEdit.getCategoryId()) {
                currentCatName = entry.getKey();
            }
        }
        cmbCategory.setSelectedItem(currentCatName);
        card.add(createComboBlock("Category *", cmbCategory));
        card.add(Box.createVerticalStrut(10));

        JLabel lDesc = new JLabel("Description");
        lDesc.setFont(new Font("Segoe UI", Font.BOLD, 11));
        txtDescription = new JTextArea(productToEdit.getDescription() != null ? productToEdit.getDescription() : "", 4, 20);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        descScroll.setBorder(BorderFactory.createLineBorder(AdminNavUtils.COLOR_BORDER));

        card.add(lDesc);
        card.add(Box.createVerticalStrut(4));
        card.add(descScroll);
        card.add(Box.createVerticalStrut(10));

        txtImageUrl = new JTextField(productToEdit.getImageUrl() != null ? productToEdit.getImageUrl() : "");
        card.add(createFieldBlock("Image URL", txtImageUrl));
        card.add(Box.createVerticalStrut(10));

        chkActive = new JCheckBox("Product is Active and visible in Storefront", productToEdit.isActive());
        chkActive.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkActive.setOpaque(false);
        card.add(chkActive);

        return card;
    }

    private JPanel createPricingCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Pricing & Available Stock");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(t);
        card.add(Box.createVerticalStrut(12));

        JPanel grid = new JPanel(new GridLayout(1, 2, 12, 0));
        grid.setOpaque(false);

        txtPrice = new JTextField(String.valueOf(productToEdit.getPrice()));
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewPrice.setText("₹" + txtPrice.getText());
            }
        });
        txtStock = new JTextField(String.valueOf(productToEdit.getStockQuantity()));

        grid.add(createFieldBlock("Price (₹) *", txtPrice));
        grid.add(createFieldBlock("Stock Quantity *", txtStock));

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

        lblPreviewTitle = new JLabel(productToEdit.getProductName());
        lblPreviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPreviewTitle.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        lblPreviewPrice = new JLabel(String.format("₹%.2f", productToEdit.getPrice()));
        lblPreviewPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPreviewPrice.setForeground(AdminNavUtils.COLOR_PRIMARY_DARK);

        mockCard.add(iconBox);
        mockCard.add(Box.createVerticalStrut(8));
        mockCard.add(lblPreviewTitle);
        mockCard.add(Box.createVerticalStrut(4));
        mockCard.add(lblPreviewPrice);

        card.add(mockCard);
        return card;
    }

    private JPanel createBottomActionBar() {
        UIUtils.RoundedPanel bar = new UIUtils.RoundedPanel(14, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        bar.setLayout(new BorderLayout());
        bar.setBorder(new EmptyBorder(10, 18, 10, 18));
        bar.setMaximumSize(new Dimension(Short.MAX_VALUE, 48));

        JLabel note = new JLabel("● Changes will update the live MySQL catalog");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        note.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> {
            new AdminProductManagementPage().setVisible(true);
            dispose();
        });

        JButton btnSave = new JButton("Save Changes", FontIcon.of(Feather.CHECK, 12, Color.WHITE)) {
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
        btnSave.addActionListener(e -> handleSaveChanges());

        btns.add(btnCancel);
        btns.add(btnSave);

        bar.add(note, BorderLayout.WEST);
        bar.add(btns, BorderLayout.EAST);
        return bar;
    }

    private void handleSaveChanges() {
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

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                throw new ValidationException("Invalid price decimal.");
            }

            int stock;
            try {
                stock = Integer.parseInt(stockStr);
            } catch (NumberFormatException e) {
                throw new ValidationException("Invalid stock integer.");
            }

            Integer catId = categoryMap.get(selectedCat);
            if (catId != null) {
                productToEdit.setCategoryId(catId);
            }
            productToEdit.setProductName(title);
            productToEdit.setSku(sku.isEmpty() ? null : sku);
            productToEdit.setBrand(brand.isEmpty() ? null : brand);
            productToEdit.setDescription(desc);
            productToEdit.setImageUrl(img.isEmpty() ? null : img);
            productToEdit.setPrice(price);
            productToEdit.setStockQuantity(stock);
            productToEdit.setActive(chkActive.isSelected());

            boolean ok = productService.updateProduct(productToEdit);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Product #" + productToEdit.getProductId() + " updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new AdminProductManagementPage().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update product in database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createFieldBlock(String label, JTextField tf) {
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

    private JPanel createComboBlock(String label, JComboBox<?> cb) {
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

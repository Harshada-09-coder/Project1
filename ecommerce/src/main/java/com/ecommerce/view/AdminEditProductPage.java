package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminEditProductPage extends JFrame {

    private JTextField txtTitle;
    private JTextField txtPrice;
    private JTextField txtOriginalPrice;
    private JLabel lblPreviewTitle;
    private JLabel lblPreviewPrice;
    private JLabel lblPreviewOldPrice;

    public AdminEditProductPage() {
        this("Wireless Headphones", "PRD-001", "SoundMax", "Electronics", "2499.00", "2999.00", "45");
    }

    public AdminEditProductPage(String name, String sku, String brand, String category, String price, String mrp, String stock) {
        setTitle("ShopSphere Admin - Edit Product (" + sku + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Edit Product"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Products"), BorderLayout.WEST);
        main.add(createEditScrollableBody(name, sku, brand, category, price, mrp, stock), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JComponent createEditScrollableBody(String name, String sku, String brand, String category, String price, String mrp, String stock) {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // Top Header Banner
        body.add(createTopBanner(sku));
        body.add(Box.createVerticalStrut(14));

        // 2-Column Split: Left Forms | Right Gallery & Live Preview
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Form Sections (62%)
        gbc.gridx = 0;
        gbc.weightx = 0.62;
        gbc.insets = new Insets(0, 0, 0, 16);
        JPanel leftForms = new JPanel();
        leftForms.setLayout(new BoxLayout(leftForms, BoxLayout.Y_AXIS));
        leftForms.setOpaque(false);

        leftForms.add(createBasicInfoCard(name, sku, brand));
        leftForms.add(Box.createVerticalStrut(14));
        leftForms.add(createTaxonomyCard(category));
        leftForms.add(Box.createVerticalStrut(14));
        leftForms.add(createPricingCard(price, mrp));
        leftForms.add(Box.createVerticalStrut(14));
        leftForms.add(createInventoryCard(stock));

        formGrid.add(leftForms, gbc);

        // Right Column (38%)
        gbc.gridx = 1;
        gbc.weightx = 0.38;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(false);

        rightCol.add(createProductGalleryCard());
        rightCol.add(Box.createVerticalStrut(14));
        rightCol.add(createLivePreviewCard(name, price, mrp));
        rightCol.add(Box.createVerticalStrut(14));
        rightCol.add(createPublishingStatusCard());

        formGrid.add(rightCol, gbc);
        body.add(formGrid);
        body.add(Box.createVerticalStrut(16));

        // Bottom Deployment Actions
        body.add(createBottomActionBar());
        body.add(Box.createVerticalStrut(16));

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createTopBanner(String sku) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        JLabel title = new JLabel("Edit Product");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        UIUtils.RoundedPanel skuPill = new UIUtils.RoundedPanel(10, new Color(0xDB, 0xEA, 0xFE));
        skuPill.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
        JLabel skuTxt = new JLabel("SKU: " + sku + " • Active in Store");
        skuTxt.setFont(new Font("Segoe UI", Font.BOLD, 10));
        skuTxt.setForeground(AdminNavUtils.COLOR_PRIMARY_DARK);
        skuPill.add(skuTxt);

        left.add(title);
        left.add(skuPill);

        JButton btnBack = new JButton("← Back to Products", FontIcon.of(Feather.ARROW_LEFT, 12, AdminNavUtils.COLOR_TEXT_MAIN));
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnBack.setBackground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            new AdminProductManagementPage().setVisible(true);
            dispose();
        });

        p.add(left, BorderLayout.WEST);
        p.add(btnBack, BorderLayout.EAST);
        return p;
    }

    private JPanel createBasicInfoCard(String name, String sku, String brand) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Basic Information");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        txtTitle = new JTextField(name);
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewTitle.setText(txtTitle.getText());
            }
        });

        JPanel r1 = new JPanel(new GridLayout(1, 2, 12, 0));
        r1.setOpaque(false);
        JTextField tfSku = new JTextField(sku);
        tfSku.setEditable(false);
        r1.add(createFieldBlock("Internal SKU / ID", tfSku));
        r1.add(createFieldBlock("Brand Name *", new JTextField(brand)));

        JTextArea taDesc = new JTextArea("Premium wireless headphones with high-quality sound, comfortable ear cushions, noise cancellation, and long battery life.", 3, 20);
        taDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        taDesc.setLineWrap(true);

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(createFieldBlock("Product Title *", txtTitle));
        card.add(Box.createVerticalStrut(8));
        card.add(r1);
        card.add(Box.createVerticalStrut(8));
        card.add(new JLabel("Product Description"));
        card.add(Box.createVerticalStrut(4));
        card.add(new JScrollPane(taDesc));

        return card;
    }

    private JPanel createTaxonomyCard(String category) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Classification & Taxonomy");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel r = new JPanel(new GridLayout(1, 2, 12, 0));
        r.setOpaque(false);
        JComboBox<String> cbCat = new JComboBox<>(new String[]{"Electronics", "Fashion", "Home & Living", "Accessories"});
        cbCat.setSelectedItem(category);
        r.add(createComboBlock("Category *", cbCat));
        r.add(createComboBlock("Subcategory *", new JComboBox<>(new String[]{"Audio Accessories", "Wearables", "Gaming"})));

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(r);
        return card;
    }

    private JPanel createPricingCard(String price, String mrp) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Pricing & Taxation");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtPrice = new JTextField(price);
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewPrice.setText("₹" + txtPrice.getText());
            }
        });

        txtOriginalPrice = new JTextField(mrp);
        txtOriginalPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewOldPrice.setText("₹" + txtOriginalPrice.getText());
            }
        });

        JPanel r = new JPanel(new GridLayout(1, 2, 12, 0));
        r.setOpaque(false);
        r.add(createFieldBlock("Selling Price (Checkout ₹) *", txtPrice));
        r.add(createFieldBlock("Original MRP (₹) *", txtOriginalPrice));

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(r);
        return card;
    }

    private JPanel createInventoryCard(String stock) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Inventory & Fulfillment");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel r = new JPanel(new GridLayout(1, 3, 10, 0));
        r.setOpaque(false);
        r.add(createFieldBlock("Available Stock *", new JTextField(stock)));
        r.add(createFieldBlock("Low Stock Alert", new JTextField("10")));
        r.add(createComboBlock("Availability Status", new JComboBox<>(new String[]{"In Stock (Active)", "Out of Stock"})));

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(r);
        return card;
    }

    private JPanel createProductGalleryCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Product Images (3 Loaded)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel gallery = new JPanel(new GridLayout(1, 3, 8, 0));
        gallery.setOpaque(false);

        gallery.add(createThumbBox(Feather.HEADPHONES, "Cover"));
        gallery.add(createThumbBox(Feather.COMPASS, "Angle"));
        gallery.add(createThumbBox(Feather.PACKAGE, "Box"));

        card.add(t);
        card.add(Box.createVerticalStrut(8));
        card.add(gallery);
        return card;
    }

    private JPanel createThumbBox(Feather icon, String tag) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, AdminNavUtils.COLOR_INPUT_BG, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setPreferredSize(new Dimension(75, 65));
        p.add(new JLabel(FontIcon.of(icon, 24, AdminNavUtils.COLOR_PRIMARY_DARK), SwingConstants.CENTER), BorderLayout.CENTER);

        JLabel l = new JLabel(tag, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 9));
        l.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);
        p.add(l, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createLivePreviewCard(String name, String price, String mrp) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Storefront Card Preview (Mockup)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(AdminNavUtils.COLOR_PRIMARY_DARK);

        UIUtils.RoundedPanel mock = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        mock.setLayout(new BoxLayout(mock, BoxLayout.Y_AXIS));
        mock.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel img = new JPanel();
        img.setBackground(AdminNavUtils.COLOR_INPUT_BG);
        img.setPreferredSize(new Dimension(220, 100));
        img.setLayout(new GridBagLayout());
        img.add(new JLabel(FontIcon.of(Feather.HEADPHONES, 38, AdminNavUtils.COLOR_PRIMARY_DARK)));

        lblPreviewTitle = new JLabel(name);
        lblPreviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));

        lblPreviewPrice = new JLabel("₹" + price);
        lblPreviewPrice.setFont(new Font("Segoe UI", Font.BOLD, 13));

        lblPreviewOldPrice = new JLabel("₹" + mrp);
        lblPreviewOldPrice.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblPreviewOldPrice.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JPanel pRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pRow.setOpaque(false);
        pRow.add(lblPreviewPrice);
        pRow.add(lblPreviewOldPrice);

        mock.add(img);
        mock.add(Box.createVerticalStrut(6));
        mock.add(lblPreviewTitle);
        mock.add(pRow);

        card.add(t);
        card.add(Box.createVerticalStrut(8));
        card.add(mock);
        return card;
    }

    private JPanel createPublishingStatusCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Publishing & Visibility");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JRadioButton r1 = new JRadioButton("Active & Live", true);
        JRadioButton r2 = new JRadioButton("Draft (Hidden)");
        JRadioButton r3 = new JRadioButton("Archived");
        ButtonGroup bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2);
        bg.add(r3);

        card.add(t);
        card.add(Box.createVerticalStrut(6));
        card.add(r1);
        card.add(r2);
        card.add(r3);
        return card;
    }

    private JPanel createBottomActionBar() {
        UIUtils.RoundedPanel bar = new UIUtils.RoundedPanel(14, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        bar.setLayout(new BorderLayout());
        bar.setBorder(new EmptyBorder(10, 18, 10, 18));
        bar.setMaximumSize(new Dimension(Short.MAX_VALUE, 48));

        JLabel note = new JLabel("● Ready to synchronize to all operational store nodes");
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
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Product changes saved and synchronized across store nodes!", "Updated", JOptionPane.INFORMATION_MESSAGE);
            new AdminProductManagementPage().setVisible(true);
            dispose();
        });

        btns.add(btnCancel);
        btns.add(btnSave);

        bar.add(note, BorderLayout.WEST);
        bar.add(btns, BorderLayout.EAST);
        return bar;
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
package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminAddProductPage extends JFrame {

    // Dynamic Live Preview Labels
    private JTextField txtTitle;
    private JTextField txtPrice;
    private JTextField txtOriginalPrice;
    private JComboBox<String> cmbCategory;

    private JLabel lblPreviewTitle;
    private JLabel lblPreviewPrice;
    private JLabel lblPreviewOldPrice;

    public AdminAddProductPage() {
        setTitle("ShopSphere Admin - Add New Product");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

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
        leftForms.add(createClassificationCard());
        leftForms.add(Box.createVerticalStrut(14));
        leftForms.add(createPricingCard());
        leftForms.add(Box.createVerticalStrut(14));
        leftForms.add(createInventoryCard());

        formGrid.add(leftForms, gbc);

        // Right Column (Images & Live Storefront Preview)
        gbc.gridx = 1;
        gbc.weightx = 0.38;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(false);

        rightCol.add(createImageUploadCard());
        rightCol.add(Box.createVerticalStrut(14));
        rightCol.add(createLivePreviewCard());
        rightCol.add(Box.createVerticalStrut(14));
        rightCol.add(createPublishingCard());

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

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel title = new JLabel("Add New Product  (Catalog Entry)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("Add a new product to your store by providing its details, pricing, images, and inventory.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(title);
        left.add(sub);

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

    private JPanel createBasicInfoCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Basic Information");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        txtTitle = new JTextField("Wireless Noise-Cancelling Headphones Pro");
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewTitle.setText(txtTitle.getText());
            }
        });

        JPanel r1 = new JPanel(new GridLayout(1, 2, 12, 0));
        r1.setOpaque(false);
        r1.add(createSimpleInput("Product SKU / Code *", new JTextField("PRD-007")));
        r1.add(createSimpleInput("Brand / Manufacturer *", new JTextField("SoundCore Dynamics")));

        JTextArea taDesc = new JTextArea("Engineered with 40mm custom acoustic neodymium drivers for high-fidelity audio.", 3, 20);
        taDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        taDesc.setLineWrap(true);

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(createSimpleInput("Product Title / Name *", txtTitle));
        card.add(Box.createVerticalStrut(8));
        card.add(r1);
        card.add(Box.createVerticalStrut(8));
        card.add(new JLabel("Product Description"));
        card.add(Box.createVerticalStrut(4));
        card.add(new JScrollPane(taDesc));
        return card;
    }

    private JPanel createClassificationCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Product Classification & Taxonomy");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel r = new JPanel(new GridLayout(1, 2, 12, 0));
        r.setOpaque(false);

        cmbCategory = new JComboBox<>(new String[]{"Electronics", "Fashion", "Home & Living", "Accessories"});
        r.add(createComboInput("Primary Category *", cmbCategory));
        r.add(createComboInput("Subcategory *", new JComboBox<>(new String[]{"Audio & Headphones", "Wearables", "Luggage"})));

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(r);
        return card;
    }

    private JPanel createPricingCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Pricing & Financials (INR Base Pricing)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtPrice = new JTextField("2499.00");
        txtPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewPrice.setText("₹" + txtPrice.getText());
            }
        });

        txtOriginalPrice = new JTextField("3999.00");
        txtOriginalPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                lblPreviewOldPrice.setText("₹" + txtOriginalPrice.getText());
            }
        });

        JPanel r1 = new JPanel(new GridLayout(1, 2, 12, 0));
        r1.setOpaque(false);
        r1.add(createSimpleInput("Selling Price (INR ₹) *", txtPrice));
        r1.add(createSimpleInput("Original Price / MRP (INR ₹) *", txtOriginalPrice));

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(r1);
        return card;
    }

    private JPanel createInventoryCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Inventory & Logistics (Stock Control)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel r = new JPanel(new GridLayout(1, 3, 10, 0));
        r.setOpaque(false);
        r.add(createSimpleInput("Stock Quantity *", new JTextField("50")));
        r.add(createSimpleInput("Low Stock Warning", new JTextField("10")));
        r.add(createComboInput("Stock Status", new JComboBox<>(new String[]{"In Stock", "Out of Stock"})));

        card.add(t);
        card.add(Box.createVerticalStrut(10));
        card.add(r);
        return card;
    }

    private JPanel createImageUploadCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Product Images (2 of 8 Loaded)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        UIUtils.RoundedPanel dropZone = new UIUtils.RoundedPanel(12, AdminNavUtils.COLOR_INPUT_BG, AdminNavUtils.COLOR_BORDER);
        dropZone.setLayout(new GridBagLayout());
        dropZone.setPreferredSize(new Dimension(280, 80));
        dropZone.add(new JLabel("Drag & Drop Images here or Browse Files", FontIcon.of(Feather.UPLOAD_CLOUD, 16, AdminNavUtils.COLOR_PRIMARY_DARK), SwingConstants.CENTER));

        card.add(t);
        card.add(Box.createVerticalStrut(8));
        card.add(dropZone);
        return card;
    }

    // Live Card Preview in Realtime
    private JPanel createLivePreviewCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Live Storefront Preview (Customer View)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(AdminNavUtils.COLOR_PRIMARY_DARK);

        UIUtils.RoundedPanel mockCard = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        mockCard.setLayout(new BoxLayout(mockCard, BoxLayout.Y_AXIS));
        mockCard.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel imgBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AdminNavUtils.COLOR_INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        imgBox.setPreferredSize(new Dimension(220, 110));
        imgBox.setLayout(new GridBagLayout());
        imgBox.add(new JLabel(FontIcon.of(Feather.HEADPHONES, 36, AdminNavUtils.COLOR_PRIMARY_DARK)));

        lblPreviewTitle = new JLabel("Wireless Headphones Pro");
        lblPreviewTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));

        lblPreviewPrice = new JLabel("₹2,499.00");
        lblPreviewPrice.setFont(new Font("Segoe UI", Font.BOLD, 13));

        lblPreviewOldPrice = new JLabel("₹3,999.00");
        lblPreviewOldPrice.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblPreviewOldPrice.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JPanel pRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pRow.setOpaque(false);
        pRow.add(lblPreviewPrice);
        pRow.add(lblPreviewOldPrice);

        mockCard.add(imgBox);
        mockCard.add(Box.createVerticalStrut(6));
        mockCard.add(lblPreviewTitle);
        mockCard.add(pRow);

        card.add(t);
        card.add(Box.createVerticalStrut(8));
        card.add(mockCard);
        return card;
    }

    private JPanel createPublishingCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Visibility & Publishing");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JRadioButton r1 = new JRadioButton("Active & Published", true);
        JRadioButton r2 = new JRadioButton("Draft Only");
        ButtonGroup bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2);

        JCheckBox chkFeatured = new JCheckBox("Mark as Featured Product (Homepage)", true);

        card.add(t);
        card.add(Box.createVerticalStrut(6));
        card.add(r1);
        card.add(r2);
        card.add(chkFeatured);
        return card;
    }

    private JPanel createBottomDeploymentBar() {
        UIUtils.RoundedPanel bar = new UIUtils.RoundedPanel(14, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        bar.setLayout(new BorderLayout());
        bar.setBorder(new EmptyBorder(10, 18, 10, 18));
        bar.setMaximumSize(new Dimension(Short.MAX_VALUE, 48));

        JLabel syncNote = new JLabel("● Ready for Deployment • Draft changes synchronized locally");
        syncNote.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        syncNote.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setOpaque(false);

        JButton btnReset = new JButton("Reset Form");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 11));

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
        btnSave.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Product added to catalog successfully!", "Catalog Synchronized", JOptionPane.INFORMATION_MESSAGE);
            new AdminProductManagementPage().setVisible(true);
            dispose();
        });

        btns.add(btnReset);
        btns.add(btnSave);

        bar.add(syncNote, BorderLayout.WEST);
        bar.add(btns, BorderLayout.EAST);
        return bar;
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
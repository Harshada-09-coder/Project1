package com.shopsphere.view;

import com.shopsphere.entity.Category;
import com.shopsphere.entity.Product;
import com.shopsphere.entity.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.UserSession;
import com.shopsphere.service.ValidationException;
import com.shopsphere.util.ImageUtils;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ProductDetailsPage extends JFrame {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final CartService cartService = new CartService();

    private Product product;
    private int quantity = 1;
    private JLabel lblQty;

    public ProductDetailsPage() {
        this(1);
    }

    public ProductDetailsPage(Product product) {
        this(product != null ? product.getProductId() : 1);
    }

    public ProductDetailsPage(int productId) {
        this.product = productService.getProductById(productId);
        if (this.product == null) {
            List<Product> all = productService.getAllActiveProducts();
            if (!all.isEmpty()) {
                this.product = all.get(0);
            }
        }

        String title = (product != null) ? product.getProductName() : "Product Details";
        setTitle("ShopSphere - " + title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.min(1440, (int) (scr.width * 0.94));
        int h = Math.min(1024, (int) (scr.height * 0.92));
        setSize(w, h);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIUtils.COLOR_BG);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel mainLayout = new JPanel(new BorderLayout(16, 0));
        mainLayout.setOpaque(false);
        mainLayout.setBorder(new EmptyBorder(12, 20, 14, 20));

        mainLayout.add(createSidebar(), BorderLayout.WEST);
        mainLayout.add(createScrollableDetailContent(), BorderLayout.CENTER);

        root.add(mainLayout, BorderLayout.CENTER);
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createHeader() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(UIUtils.COLOR_CARD);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtils.COLOR_BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("Product Details");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        brand.add(title);
        brand.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnCatalog = new JButton("← Back to Catalog", FontIcon.of(Feather.ARROW_LEFT, 13, UIUtils.COLOR_TEXT_MAIN));
        btnCatalog.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCatalog.setContentAreaFilled(false);
        btnCatalog.setBorderPainted(false);
        btnCatalog.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCatalog.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

        JButton btnCart = new JButton("View Cart", FontIcon.of(Feather.SHOPPING_BAG, 13, UIUtils.COLOR_TEXT_MAIN));
        btnCart.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCart.setContentAreaFilled(false);
        btnCart.setBorderPainted(false);
        btnCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCart.addActionListener(e -> {
            new MyCartPage().setVisible(true);
            dispose();
        });

        right.add(btnCatalog);
        right.add(btnCart);

        nav.add(brand, BorderLayout.WEST);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtils.COLOR_CARD);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("EXPLORE CATEGORIES");
        title.setFont(new Font("Segoe UI", Font.BOLD, 11));
        title.setForeground(UIUtils.COLOR_TEXT_MUTED);
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(12));

        try {
            List<Category> cats = categoryService.getAllActiveCategories();
            for (Category c : cats) {
                JButton btn = new JButton(c.getCategoryName());
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.setHorizontalAlignment(SwingConstants.LEFT);
                btn.addActionListener(e -> {
                    new ProductListingPage(c.getCategoryName()).setVisible(true);
                    dispose();
                });
                sidebar.add(btn);
                sidebar.add(Box.createVerticalStrut(4));
            }
        } catch (Exception ignored) {}

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JComponent createScrollableDetailContent() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10, 10, 20, 10));

        if (product == null) {
            JLabel err = new JLabel("Product details unavailable.");
            err.setFont(new Font("Segoe UI", Font.BOLD, 16));
            body.add(err);
            return body;
        }

        // Top 2-Column Split: Big Image Left + Details Right
        JPanel hero = new JPanel(new GridBagLayout());
        hero.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Big Icon / Image Box (45%)
        gbc.gridx = 0;
        gbc.weightx = 0.45;
        gbc.insets = new Insets(0, 0, 0, 20);

        UIUtils.RoundedPanel imageCard = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        imageCard.setLayout(new BorderLayout());
        imageCard.setPreferredSize(new Dimension(380, 420));
        imageCard.setBorder(new EmptyBorder(16, 16, 16, 16));

        ImageIcon heroIcon = ImageUtils.getProductIcon(product, 310, 260);
        JLabel mainImgLabel = new JLabel(heroIcon != null ? heroIcon : FontIcon.of(Feather.BOX, 100, UIUtils.COLOR_PRIMARY_DARK));
        mainImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageCard.add(mainImgLabel, BorderLayout.CENTER);

        JPanel thumbRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        thumbRow.setOpaque(false);
        List<ImageIcon> thumbs = ImageUtils.getProductGalleryThumbnails(product, 56, 56);
        for (int i = 0; i < thumbs.size(); i++) {
            ImageIcon tIcon = thumbs.get(i);
            JButton tBtn = new JButton(tIcon);
            tBtn.setPreferredSize(new Dimension(64, 64));
            tBtn.setBackground(Color.WHITE);
            tBtn.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1));
            tBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            final int idx = i;
            tBtn.addActionListener(ev -> {
                String[] paths = {"/images/headphones.png", "/images/headphones_side.png", "/images/headphones_earcup.png", "/images/headphones_case.png"};
                String p = (idx < paths.length) ? paths[idx] : ImageUtils.resolveProductResource(product);
                ImageIcon full = ImageUtils.loadImage(p, 310, 260);
                if (full != null) {
                    mainImgLabel.setIcon(full);
                }
            });
            thumbRow.add(tBtn);
        }
        imageCard.add(thumbRow, BorderLayout.SOUTH);
        hero.add(imageCard, gbc);

        // Details Panel (55%)
        gbc.gridx = 1;
        gbc.weightx = 0.55;
        gbc.insets = new Insets(0, 0, 0, 0);

        UIUtils.RoundedPanel infoCard = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        infoCard.setLayout(new BoxLayout(infoCard, BoxLayout.Y_AXIS));
        infoCard.setBorder(new EmptyBorder(20, 24, 20, 24));

        Category cat = categoryService.getCategoryById(product.getCategoryId());
        String catName = (cat != null) ? cat.getCategoryName() : "General";

        JLabel lblCat = new JLabel(catName.toUpperCase());
        lblCat.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblCat.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        JLabel lblName = new JLabel(product.getProductName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblName.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel lblBrand = new JLabel("Brand: " + (product.getBrand() != null ? product.getBrand() : "Authentic Original"));
        lblBrand.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBrand.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblPrice = new JLabel(String.format("₹%,.2f", product.getPrice()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblPrice.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        boolean inStock = product.getStockQuantity() > 0;
        JLabel lblStock = new JLabel(inStock ? "● In Stock (" + product.getStockQuantity() + " units available)" : "● Currently Out of Stock");
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStock.setForeground(inStock ? UIUtils.COLOR_GREEN : UIUtils.COLOR_RED);

        infoCard.add(lblCat);
        infoCard.add(Box.createVerticalStrut(4));
        infoCard.add(lblName);
        infoCard.add(lblBrand);
        infoCard.add(Box.createVerticalStrut(14));
        infoCard.add(lblPrice);
        infoCard.add(Box.createVerticalStrut(6));
        infoCard.add(lblStock);
        infoCard.add(Box.createVerticalStrut(16));

        // Quantity Selector
        JPanel qtyRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        qtyRow.setOpaque(false);
        JLabel lq = new JLabel("Quantity:");
        lq.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JButton btnMinus = new JButton("-");
        lblQty = new JLabel("1");
        lblQty.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton btnPlus = new JButton("+");

        btnMinus.addActionListener(e -> {
            if (quantity > 1) {
                quantity--;
                lblQty.setText(String.valueOf(quantity));
            }
        });

        btnPlus.addActionListener(e -> {
            if (quantity < product.getStockQuantity()) {
                quantity++;
                lblQty.setText(String.valueOf(quantity));
            } else {
                JOptionPane.showMessageDialog(this, "Maximum available stock reached.", "Stock Limit", JOptionPane.WARNING_MESSAGE);
            }
        });

        qtyRow.add(lq);
        qtyRow.add(btnMinus);
        qtyRow.add(lblQty);
        qtyRow.add(btnPlus);
        infoCard.add(qtyRow);
        infoCard.add(Box.createVerticalStrut(20));

        // Action Buttons Row: Add to Cart + Buy Now
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        btnRow.setOpaque(false);

        JButton btnAddToCart = new JButton("Add to Cart", FontIcon.of(Feather.SHOPPING_BAG, 14, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(inStock ? new Color(0x60, 0x8E, 0xB5) : Color.GRAY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setContentAreaFilled(false);
        btnAddToCart.setBorderPainted(false);
        btnAddToCart.setEnabled(inStock);
        btnAddToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddToCart.addActionListener(e -> handleAddToCart(false));

        JButton btnBuyNow = new JButton("Buy Now", FontIcon.of(Feather.CREDIT_CARD, 14, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(inStock ? new Color(0x10, 0xB9, 0x81) : Color.GRAY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnBuyNow.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuyNow.setForeground(Color.WHITE);
        btnBuyNow.setContentAreaFilled(false);
        btnBuyNow.setBorderPainted(false);
        btnBuyNow.setEnabled(inStock);
        btnBuyNow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuyNow.addActionListener(e -> handleAddToCart(true));

        btnRow.add(btnAddToCart);
        btnRow.add(btnBuyNow);
        infoCard.add(btnRow);

        hero.add(infoCard, gbc);
        body.add(hero);
        body.add(Box.createVerticalStrut(20));

        // Description Details Card
        UIUtils.RoundedPanel descCard = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        descCard.setLayout(new BoxLayout(descCard, BoxLayout.Y_AXIS));
        descCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel descTitle = new JLabel("Product Description & Details");
        descTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        descTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel descText = new JLabel("<html><body style='width:700px; font-size:12px; line-height:1.6; color:#475569;'>"
                + (product.getDescription() != null && !product.getDescription().isEmpty() ? product.getDescription() : "No extended description provided for this product.")
                + "</body></html>");

        descCard.add(descTitle);
        descCard.add(Box.createVerticalStrut(8));
        descCard.add(descText);
        body.add(descCard);
        body.add(Box.createVerticalStrut(20));

        JLabel recoTitle = new JLabel("You May Also Like");
        recoTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recoTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        body.add(recoTitle);
        body.add(Box.createVerticalStrut(10));

        JPanel recoRow = new JPanel(new GridLayout(1, 3, 14, 0));
        recoRow.setOpaque(false);
        recoRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 160));

        String[][] recos = {
            {"Smart Fitness Watch", "₹2,499.00", "/images/smartwatch.png"},
            {"Portable Bluetooth Speaker", "₹1,899.00", "/images/speaker.png"},
            {"Desk Organizer & Dock", "₹1,299.00", "/images/charging_dock.png"}
        };
        for (String[] r : recos) {
            UIUtils.RoundedPanel rCard = new UIUtils.RoundedPanel(12, Color.WHITE, UIUtils.COLOR_BORDER);
            rCard.setLayout(new BorderLayout());
            rCard.setBorder(new EmptyBorder(10, 10, 10, 10));

            ImageIcon rIcon = ImageUtils.loadImage(r[2], 90, 75);
            JLabel rImg = new JLabel(rIcon != null ? rIcon : FontIcon.of(Feather.BOX, 24, UIUtils.COLOR_PRIMARY));
            rImg.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel rInfo = new JPanel();
            rInfo.setLayout(new BoxLayout(rInfo, BoxLayout.Y_AXIS));
            rInfo.setOpaque(false);

            JLabel rName = new JLabel(r[0]);
            rName.setFont(new Font("Segoe UI", Font.BOLD, 11));
            rName.setForeground(UIUtils.COLOR_TEXT_MAIN);
            JLabel rPrice = new JLabel(r[1]);
            rPrice.setFont(new Font("Segoe UI", Font.BOLD, 12));
            rPrice.setForeground(UIUtils.COLOR_PRIMARY_DARK);

            rInfo.add(rName);
            rInfo.add(Box.createVerticalStrut(2));
            rInfo.add(rPrice);

            rCard.add(rImg, BorderLayout.CENTER);
            rCard.add(rInfo, BorderLayout.SOUTH);
            recoRow.add(rCard);
        }
        body.add(recoRow);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private void handleAddToCart(boolean proceedToCheckout) {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in to add products to your cart.", "Login Required", JOptionPane.INFORMATION_MESSAGE);
            new CustomerLoginPage().setVisible(true);
            dispose();
            return;
        }

        try {
            boolean ok = cartService.addToCart(currentUser.getUserId(), product.getProductId(), quantity);
            if (ok) {
                if (proceedToCheckout) {
                    new CheckoutPage().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Added " + quantity + " units of '" + product.getProductName() + "' to your cart!", "Cart Updated", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Cart Warning", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to add to cart: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

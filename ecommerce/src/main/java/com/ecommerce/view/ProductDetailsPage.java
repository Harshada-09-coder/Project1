package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductDetailsPage extends JFrame {

    // Palette Configuration
    public static final Color COLOR_PRIMARY      = new Color(0x8D, 0xB4, 0xD6);
    public static final Color COLOR_PRIMARY_DARK = new Color(0x65, 0x94, 0xBD);
    public static final Color COLOR_LIGHT_BLUE   = new Color(0xD6, 0xE6, 0xF2);
    public static final Color COLOR_BACKGROUND   = new Color(0xF2, 0xF7, 0xFB);
    public static final Color COLOR_CARD         = Color.WHITE;
    public static final Color COLOR_MAIN_TEXT    = new Color(0x24, 0x34, 0x4D);
    public static final Color COLOR_SECONDARY    = new Color(0x6B, 0x72, 0x80);
    public static final Color COLOR_BORDER       = new Color(0xE1, 0xEA, 0xF2);
    public static final Color COLOR_INPUT_BG     = new Color(0xF4, 0xF7, 0xFB);
    public static final Color COLOR_STAR_YELLOW  = new Color(0xFA, 0xB0, 0x05);
    public static final Color COLOR_GREEN        = new Color(0x10, 0xB9, 0x81);

    // Dynamic Product State
    private String productName;
    private String category;
    private int price;
    private int originalPrice;
    private String discount;
    private Feather currentIcon = Feather.HEADPHONES;

    // Interactive UI controls
    private int quantity = 1;
    private final int MAX_QUANTITY = 5;
    private JLabel lblQuantity;
    private JLabel mainProductIconLabel;
    private JLabel lblMainAngle;
    private boolean isWishlisted = false;
    private JButton btnWishlist;

    // Tab content panel
    private JPanel tabContentContainer;
    private CardLayout tabCardLayout;
    private JButton btnTabDesc;
    private JButton btnTabSpecs;
    private JButton btnTabReviews;

    public ProductDetailsPage() {
        this("Wireless Noise-Cancelling Headphones", "Electronics", 1499, 2499, "40% OFF", Feather.HEADPHONES);
    }

    public ProductDetailsPage(String productName, String category, int price, int originalPrice, String discount, Feather icon) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discount = discount;
        if (icon != null) this.currentIcon = icon;

        setTitle("ShopSphere - " + productName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.min(1440, (int) (scr.width * 0.94));
        int h = Math.min(1024, (int) (scr.height * 0.92));
        setSize(w, h);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BACKGROUND);

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

    // ==========================================
    // 1. TOP HEADER (Reused standard format)
    // ==========================================
    private JPanel createHeader() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(COLOR_CARD);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));

        // Brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        UIUtils.RoundedPanel logo = new UIUtils.RoundedPanel(12, COLOR_LIGHT_BLUE);
        logo.setPreferredSize(new Dimension(34, 34));
        logo.setLayout(new GridBagLayout());
        logo.add(new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, COLOR_PRIMARY_DARK)));

        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(COLOR_MAIN_TEXT);

        JLabel sub = new JLabel("Your Everyday Shopping Destination");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_SECONDARY);

        brand.add(logo);
        brand.add(title);
        brand.add(sub);

        // Center Search Box
        UIUtils.RoundedPanel searchBox = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        searchBox.setPreferredSize(new Dimension(360, 36));
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 12, 0, 12));

        JLabel sIcon = new JLabel(FontIcon.of(Feather.SEARCH, 14, COLOR_SECONDARY));
        JTextField topSearch = new JTextField("Search products, categories, or brands...");
        topSearch.setBorder(null);
        topSearch.setOpaque(false);
        topSearch.setForeground(COLOR_SECONDARY);
        topSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(topSearch, BorderLayout.CENTER);

        // Right Quick Actions
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnWishlistNav = createIconButton(Feather.HEART, "Wishlist", () -> showPlaceholder("Wishlist"));
        JButton btnCartNav = createIconButton(Feather.SHOPPING_BAG, "Cart", () -> { new MyCartPage().setVisible(true);
        dispose();
});       
        JButton btnAccount = createIconButton(Feather.USER, "My Account", () -> {
        new MyAccountPage().setVisible(true);
        dispose();
    });

        right.add(btnWishlistNav);
        right.add(btnCartNav);
        right.add(btnAccount);

        nav.add(brand, BorderLayout.WEST);
        nav.add(searchBox, BorderLayout.CENTER);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JButton createIconButton(Feather icon, String text, Runnable action) {
        JButton btn = new JButton(text, FontIcon.of(icon, 14, COLOR_MAIN_TEXT));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(COLOR_MAIN_TEXT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // ==========================================
    // 2. SIDEBAR
    // ==========================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(new EmptyBorder(8, 4, 10, 12));

        JLabel catLbl = new JLabel("CATEGORIES");
        catLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catLbl.setForeground(COLOR_SECONDARY);

        sidebar.add(catLbl);
        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createSidebarBtn("All Products", Feather.GRID, true, () -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        }));
        sidebar.add(createSidebarBtn("Electronics", Feather.TV, false, () -> openCategory("Electronics")));
        sidebar.add(createSidebarBtn("Fashion", Feather.TAG, false, () -> openCategory("Fashion")));
        sidebar.add(createSidebarBtn("Home & Living", Feather.HOME, false, () -> openCategory("Home & Living")));
        sidebar.add(createSidebarBtn("Beauty & Care", Feather.SUN, false, () -> openCategory("Beauty & Personal Care")));
        sidebar.add(createSidebarBtn("Books & Stationery", Feather.BOOK, false, () -> openCategory("Books & Stationery")));
        sidebar.add(createSidebarBtn("Sports & Fitness", Feather.ACTIVITY, false, () -> openCategory("Sports & Fitness")));
        sidebar.add(createSidebarBtn("Accessories", Feather.PACKAGE, false, () -> openCategory("Accessories")));

        sidebar.add(Box.createVerticalStrut(20));

        JLabel myShopLbl = new JLabel("MY SHOPPING");
        myShopLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        myShopLbl.setForeground(COLOR_SECONDARY);

        sidebar.add(myShopLbl);
        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createSidebarBtn("My Cart", Feather.SHOPPING_CART, false, () -> { new MyCartPage().setVisible(true);
        dispose();
        }));
        sidebar.add(createSidebarBtn("My Orders", Feather.ARCHIVE, false, () -> { new MyOrdersPage().setVisible(true);
        dispose();
        }));
        sidebar.add(createSidebarBtn("My Account", Feather.USER, false, () -> { new MyAccountPage().setVisible(true);
       dispose();
       }));

        sidebar.add(createSidebarBtn("Logout", Feather.LOG_OUT, false, () -> {
            new WelcomePage().setVisible(true);
            dispose();
        }));

        return sidebar;
    }

    private void openCategory(String catName) {
        new ProductListingPage(catName).setVisible(true);
        dispose();
    }

    private JButton createSidebarBtn(String title, Feather icon, boolean active, Runnable onClick) {
        JButton btn = new JButton(title, FontIcon.of(icon, 13, active ? Color.WHITE : COLOR_MAIN_TEXT)) {
            @Override
            protected void paintComponent(Graphics g) {
                if (active) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(COLOR_PRIMARY_DARK);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 12));
        btn.setForeground(active ? Color.WHITE : COLOR_MAIN_TEXT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));
        btn.setBorder(new EmptyBorder(6, 10, 6, 10));
        btn.addActionListener(e -> onClick.run());
        return btn;
    }

    // ==========================================
    // 3. MAIN PRODUCT DETAILS CONTENT
    // ==========================================
    private JComponent createScrollableDetailContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Top Breadcrumb & "Back to Products" Row
        content.add(createTopNavigationRow());
        content.add(Box.createVerticalStrut(14));

        // Two-Column Split (Left: Image & Thumbnails, Right: Info, Price, Actions)
        content.add(createTwoColumnProductPanel());
        content.add(Box.createVerticalStrut(24));

        // Description / Specs / Reviews Tabs
        content.add(createProductTabsSection());
        content.add(Box.createVerticalStrut(24));

        // Related Products ("You May Also Like")
        content.add(createRelatedProductsSection());
        content.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // Breadcrumb + Back Button
    private JPanel createTopNavigationRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));

        // Breadcrumbs: Home / All Products / Category / Product Name
        JPanel breadcrumbs = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        breadcrumbs.setOpaque(false);

        JLabel linkHome = createBreadcrumbLink("Home", () -> {
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });
        JLabel linkAll = createBreadcrumbLink("All Products", () -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });
        JLabel linkCat = createBreadcrumbLink(category, () -> {
            new ProductListingPage(category).setVisible(true);
            dispose();
        });
        JLabel lblCurrent = new JLabel(productName);
        lblCurrent.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCurrent.setForeground(COLOR_MAIN_TEXT);

        breadcrumbs.add(linkHome);
        breadcrumbs.add(new JLabel("/"));
        breadcrumbs.add(linkAll);
        breadcrumbs.add(new JLabel("/"));
        breadcrumbs.add(linkCat);
        breadcrumbs.add(new JLabel("/"));
        breadcrumbs.add(lblCurrent);

        // Back button (Right)
        JButton btnBack = new JButton("← Back to Products", FontIcon.of(Feather.ARROW_LEFT, 12, COLOR_MAIN_TEXT));
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setForeground(COLOR_MAIN_TEXT);
        btnBack.setBackground(COLOR_CARD);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                new EmptyBorder(6, 12, 6, 12)
        ));
        btnBack.addActionListener(e -> {
            new ProductListingPage(category).setVisible(true);
            dispose();
        });

        row.add(breadcrumbs, BorderLayout.WEST);
        row.add(btnBack, BorderLayout.EAST);
        return row;
    }

    private JLabel createBreadcrumbLink(String text, Runnable action) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(COLOR_PRIMARY_DARK);
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
        return lbl;
    }

    // ==========================================
    // 4. TWO-COLUMN PRODUCT DETAILS
    // ==========================================
    private JPanel createTwoColumnProductPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 490));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Column (Images & Thumbnails)
        gbc.gridx = 0;
        gbc.weightx = 0.44;
        gbc.insets = new Insets(0, 0, 0, 18);
        panel.add(createLeftImageShowcase(), gbc);

        // Right Column (Product Information & Actions)
        gbc.gridx = 1;
        gbc.weightx = 0.56;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(createRightProductInfo(), gbc);

        return panel;
    }

    // 4A. Left: Product Image Box and Thumbnails
    private JPanel createLeftImageShowcase() {
        UIUtils.RoundedPanel container = new UIUtils.RoundedPanel(18, COLOR_CARD, COLOR_BORDER);
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Big Main Image Canvas
        JPanel mainCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background soft gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(0xEE, 0xF5, 0xFB), getWidth(), getHeight(), new Color(0xDE, 0xEB, 0xF7));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
            }
        };
        mainCanvas.setPreferredSize(new Dimension(360, 270));
        mainCanvas.setOpaque(false);
        mainCanvas.setLayout(new GridBagLayout());

        mainProductIconLabel = new JLabel(FontIcon.of(currentIcon, 90, COLOR_PRIMARY_DARK));
        mainCanvas.add(mainProductIconLabel);

        // Subtitle badge on main image
        JPanel photoCountPill = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        photoCountPill.setBackground(new Color(255, 255, 255, 210));
        photoCountPill.setBorder(new EmptyBorder(4, 8, 4, 8));
        lblMainAngle = new JLabel("Studio View (4 Angles)");
        lblMainAngle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblMainAngle.setForeground(COLOR_MAIN_TEXT);
        photoCountPill.add(lblMainAngle);

        JPanel mainCanvasWrapper = new JPanel(new BorderLayout());
        mainCanvasWrapper.setOpaque(false);
        mainCanvasWrapper.add(mainCanvas, BorderLayout.CENTER);
        mainCanvasWrapper.add(photoCountPill, BorderLayout.SOUTH);

        // Thumbnails Row below
        JPanel thumbnailsRow = new JPanel(new GridLayout(1, 4, 10, 0));
        thumbnailsRow.setOpaque(false);
        thumbnailsRow.setBorder(new EmptyBorder(12, 0, 0, 0));

        thumbnailsRow.add(createThumbnail(currentIcon, "Front View"));
        thumbnailsRow.add(createThumbnail(Feather.COMPASS, "Side Angle"));
        thumbnailsRow.add(createThumbnail(Feather.PACKAGE, "Accessories"));
        thumbnailsRow.add(createThumbnail(Feather.SHIELD, "Ear Cushions"));

        // Bottom Trust badges
        JPanel trustRow = new JPanel(new GridLayout(1, 2, 8, 0));
        trustRow.setOpaque(false);
        trustRow.setBorder(new EmptyBorder(12, 0, 0, 0));

        trustRow.add(createMiniTrustPill("🛡 1-Year Official Warranty"));
        trustRow.add(createMiniTrustPill("🔄 7-Day Easy Replacement"));

        container.add(mainCanvasWrapper, BorderLayout.CENTER);
        container.add(thumbnailsRow, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(container, BorderLayout.CENTER);
        wrapper.add(trustRow, BorderLayout.SOUTH);

        return wrapper;
    }

    private JPanel createThumbnail(Feather icon, String angleName) {
        UIUtils.RoundedPanel thumb = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        thumb.setPreferredSize(new Dimension(65, 60));
        thumb.setLayout(new GridBagLayout());
        thumb.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel ic = new JLabel(FontIcon.of(icon, 22, COLOR_PRIMARY_DARK));
        thumb.add(ic);

        thumb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainProductIconLabel.setIcon(FontIcon.of(icon, 90, COLOR_PRIMARY_DARK));
                lblMainAngle.setText(angleName + " Active");
            }
        });

        return thumb;
    }

    private JPanel createMiniTrustPill(String text) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, COLOR_CARD, COLOR_BORDER);
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 6));
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(COLOR_SECONDARY);
        p.add(l);
        return p;
    }

    // 4B. Right: Product Information & Purchase Section
    private JPanel createRightProductInfo() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(18, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(22, 26, 22, 26));

        // Category Tag & Stock Status Row
        JPanel topTagRow = new JPanel(new BorderLayout());
        topTagRow.setOpaque(false);

        JPanel leftTags = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftTags.setOpaque(false);
        JLabel catBadge = new JLabel(" " + category.toUpperCase() + " ");
        catBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catBadge.setForeground(COLOR_PRIMARY_DARK);
        catBadge.setOpaque(true);
        catBadge.setBackground(COLOR_INPUT_BG);

        JLabel modelTag = new JLabel("• Model: SP-NC850");
        modelTag.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        modelTag.setForeground(COLOR_SECONDARY);

        leftTags.add(catBadge);
        leftTags.add(modelTag);

        JLabel inStockBadge = new JLabel("● In Stock (28 units remaining)");
        inStockBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        inStockBadge.setForeground(COLOR_GREEN);

        topTagRow.add(leftTags, BorderLayout.WEST);
        topTagRow.add(inStockBadge, BorderLayout.EAST);

        // Product Title
        JLabel titleLbl = new JLabel("<html><body style='width: 480px;'>" + productName + "</body></html>");
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLbl.setForeground(COLOR_MAIN_TEXT);
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Rating & Verification
        JPanel ratingRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        ratingRow.setOpaque(false);
        ratingRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel star = new JLabel(FontIcon.of(Feather.STAR, 13, COLOR_STAR_YELLOW));
        JLabel score = new JLabel("4.8  (342 customer reviews)");
        score.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        score.setForeground(COLOR_SECONDARY);

        JLabel verified = new JLabel("• 🛡 ShopSphere Verified Merchant");
        verified.setFont(new Font("Segoe UI", Font.BOLD, 11));
        verified.setForeground(COLOR_PRIMARY_DARK);

        ratingRow.add(star);
        ratingRow.add(score);
        ratingRow.add(verified);

        // Price Row
        JPanel priceRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        priceRow.setOpaque(false);
        priceRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel currentPriceLbl = new JLabel("₹" + String.format("%,d", price));
        currentPriceLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        currentPriceLbl.setForeground(COLOR_MAIN_TEXT);

        JLabel originalPriceLbl = new JLabel("<html><strike>₹" + String.format("%,d", originalPrice) + "</strike></html>");
        originalPriceLbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        originalPriceLbl.setForeground(COLOR_SECONDARY);

        JLabel discountBadge = new JLabel(" " + discount + " ");
        discountBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        discountBadge.setForeground(Color.WHITE);
        discountBadge.setOpaque(true);
        discountBadge.setBackground(COLOR_PRIMARY_DARK);

        priceRow.add(currentPriceLbl);
        priceRow.add(originalPriceLbl);
        priceRow.add(discountBadge);

        JLabel taxNote = new JLabel("Inclusive of all taxes • Free express shipping on this desktop order");
        taxNote.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        taxNote.setForeground(COLOR_SECONDARY);
        taxNote.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Short Description
        JLabel descLbl = new JLabel("<html><body style='width: 480px; line-height: 1.45; color: #6B7280; font-size: 11px;'>"
                + "Enjoy immersive acoustic sound, comfortable all-day listening, and seamless "
                + "multi-device wireless pairing. Powered by advanced hybrid active noise cancellation (-35dB) "
                + "and custom-engineered 40mm dynamic drivers."
                + "</body></html>");
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Bullet Specs Row
        JPanel bulletSpecs = new JPanel(new GridLayout(2, 2, 6, 6));
        bulletSpecs.setOpaque(false);
        bulletSpecs.setAlignmentX(Component.LEFT_ALIGNMENT);
        bulletSpecs.setMaximumSize(new Dimension(Short.MAX_VALUE, 46));

        bulletSpecs.add(createBulletItem("Bluetooth 5.3 (15m range)"));
        bulletSpecs.add(createBulletItem("Hybrid ANC (-35dB)"));
        bulletSpecs.add(createBulletItem("Ultra-plush memory foam"));
        bulletSpecs.add(createBulletItem("Up to 40h playback"));

        // Interactive Quantity + Add to Cart + Buy Now + Wishlist Row
        JPanel actionRow = createPurchaseActionRow();
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Security / Delivery Info Note
        JPanel noteRow = new JPanel(new GridLayout(1, 3, 10, 0));
        noteRow.setOpaque(false);
        noteRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        noteRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));

        noteRow.add(createFeatureMicro("🔒 Secure Checkout", "256-bit SSL encrypted"));
        noteRow.add(createFeatureMicro("🚚 Express Dispatch", "Ships within 24 hours"));
        noteRow.add(createFeatureMicro("📡 Live Telemetry", "Real-time desktop tracking"));

        // Assembly
        card.add(topTagRow);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(4));
        card.add(ratingRow);
        card.add(Box.createVerticalStrut(12));
        card.add(priceRow);
        card.add(taxNote);
        card.add(Box.createVerticalStrut(10));
        card.add(descLbl);
        card.add(Box.createVerticalStrut(12));
        card.add(bulletSpecs);
        card.add(Box.createVerticalStrut(18));
        card.add(actionRow);
        card.add(Box.createVerticalStrut(16));
        card.add(noteRow);

        return card;
    }

    private JLabel createBulletItem(String text) {
        JLabel l = new JLabel("✔  " + text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(COLOR_MAIN_TEXT);
        return l;
    }

    private JPanel createFeatureMicro(String title, String sub) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(4, 8, 4, 8));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));
        t.setForeground(COLOR_MAIN_TEXT);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        s.setForeground(COLOR_SECONDARY);

        p.add(t);
        p.add(s);
        return p;
    }

    // Purchase Quantity & Buttons
    private JPanel createPurchaseActionRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 46));

        // 1. Quantity Selector: [-] 1 [+]
        UIUtils.RoundedPanel qtyBox = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        qtyBox.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 4));

        JButton btnMinus = createQtyButton("−");
        lblQuantity = new JLabel("1");
        lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblQuantity.setForeground(COLOR_MAIN_TEXT);
        lblQuantity.setPreferredSize(new Dimension(20, 24));
        lblQuantity.setHorizontalAlignment(SwingConstants.CENTER);
        JButton btnPlus = createQtyButton("+");

        btnMinus.addActionListener(e -> {
            if (quantity > 1) {
                quantity--;
                lblQuantity.setText(String.valueOf(quantity));
            } else {
                JOptionPane.showMessageDialog(this, "Minimum order quantity is 1.", "Quantity", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPlus.addActionListener(e -> {
            if (quantity < MAX_QUANTITY) {
                quantity++;
                lblQuantity.setText(String.valueOf(quantity));
            } else {
                JOptionPane.showMessageDialog(this, "Sample order limit reached: Maximum " + MAX_QUANTITY + " units per customer.", "Quantity Limit", JOptionPane.WARNING_MESSAGE);
            }
        });

        qtyBox.add(btnMinus);
        qtyBox.add(lblQuantity);
        qtyBox.add(btnPlus);

        // 2. Add to Cart Button
        JButton btnAddToCart = new JButton("Add to Cart", FontIcon.of(Feather.SHOPPING_BAG, 14, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_PRIMARY_DARK.darker() : COLOR_PRIMARY_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setContentAreaFilled(false);
        btnAddToCart.setBorderPainted(false);
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddToCart.setPreferredSize(new Dimension(150, 40));
        btnAddToCart.addActionListener(e -> {
            int choice = JOptionPane.showOptionDialog(this,
            quantity + " × \"" + productName + "\" added to your basket!\nWould you like to view your cart now?",
            "Added to Cart",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"View Cart", "Continue Shopping"},
            "View Cart");
    if (choice == JOptionPane.YES_OPTION) {
        new MyCartPage().setVisible(true);
        dispose();
    }
});

        // 3. Buy Now Button
        JButton btnBuyNow = new JButton("Buy Now") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_INPUT_BG.darker() : COLOR_LIGHT_BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnBuyNow.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuyNow.setForeground(COLOR_MAIN_TEXT);
        btnBuyNow.setContentAreaFilled(false);
        btnBuyNow.setBorderPainted(false);
        btnBuyNow.setFocusPainted(false);
        btnBuyNow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuyNow.setPreferredSize(new Dimension(100, 40));
        btnBuyNow.addActionListener(e -> {
            showPlaceholder("Checkout & Order Settlement");
        });

        // 4. Wishlist Button
        btnWishlist = new JButton(FontIcon.of(Feather.HEART, 16, COLOR_SECONDARY));
        btnWishlist.setPreferredSize(new Dimension(42, 40));
        btnWishlist.setBackground(COLOR_CARD);
        btnWishlist.setFocusPainted(false);
        btnWishlist.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnWishlist.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                new EmptyBorder(4, 4, 4, 4)
        ));
        btnWishlist.addActionListener(e -> {
            isWishlisted = !isWishlisted;
            btnWishlist.setIcon(FontIcon.of(Feather.HEART, 16, isWishlisted ? new Color(0xEF, 0x44, 0x44) : COLOR_SECONDARY));
            JOptionPane.showMessageDialog(this,
                    isWishlisted ? "Saved to your Wishlist!" : "Removed from Wishlist.",
                    "Wishlist Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        row.add(qtyBox);
        row.add(btnAddToCart);
        row.add(btnBuyNow);
        row.add(btnWishlist);
        return row;
    }

    private JButton createQtyButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(COLOR_MAIN_TEXT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(28, 28));
        return btn;
    }

    // ==========================================
    // 5. DESCRIPTION / SPECS / REVIEWS TABS
    // ==========================================
    private JPanel createProductTabsSection() {
        UIUtils.RoundedPanel container = new UIUtils.RoundedPanel(18, COLOR_CARD, COLOR_BORDER);
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(16, 24, 20, 24));

        // Tab Navigation Header
        JPanel tabButtonsBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        tabButtonsBar.setOpaque(false);
        tabButtonsBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(0, 0, 10, 0)
        ));

        btnTabDesc    = createTabButton("Description", true);
        btnTabSpecs   = createTabButton("Specifications", false);
        btnTabReviews = createTabButton("Reviews (342)", false);

        tabButtonsBar.add(btnTabDesc);
        tabButtonsBar.add(btnTabSpecs);
        tabButtonsBar.add(btnTabReviews);

        // Tab Content (CardLayout)
        tabCardLayout = new CardLayout();
        tabContentContainer = new JPanel(tabCardLayout);
        tabContentContainer.setOpaque(false);
        tabContentContainer.setBorder(new EmptyBorder(16, 0, 0, 0));

        tabContentContainer.add(createDescriptionTabContent(), "DESC");
        tabContentContainer.add(createSpecificationsTabContent(), "SPECS");
        tabContentContainer.add(createReviewsTabContent(), "REVIEWS");

        btnTabDesc.addActionListener(e -> switchTab("DESC", btnTabDesc));
        btnTabSpecs.addActionListener(e -> switchTab("SPECS", btnTabSpecs));
        btnTabReviews.addActionListener(e -> switchTab("REVIEWS", btnTabReviews));

        container.add(tabButtonsBar, BorderLayout.NORTH);
        container.add(tabContentContainer, BorderLayout.CENTER);
        return container;
    }

    private JButton createTabButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(active ? COLOR_PRIMARY_DARK : COLOR_SECONDARY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void switchTab(String cardName, JButton selectedBtn) {
        tabCardLayout.show(tabContentContainer, cardName);
        btnTabDesc.setForeground(COLOR_SECONDARY);
        btnTabSpecs.setForeground(COLOR_SECONDARY);
        btnTabReviews.setForeground(COLOR_SECONDARY);
        selectedBtn.setForeground(COLOR_PRIMARY_DARK);
    }

    // Tab 1: Description + 3 Feature Highlights (As shown in screenshot)
    private JPanel createDescriptionTabContent() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        // 3 Feature Highlight Cards Row
        JPanel highlightRow = new JPanel(new GridLayout(1, 3, 14, 0));
        highlightRow.setOpaque(false);

        highlightRow.add(createFeatureHighlightCard(
                Feather.VOLUME_2,
                "40mm Custom Drivers",
                "Mylar diaphragms tuned with high-purity copper voice coils deliver punchy sub-bass, crystal-clear mids, and non-fatiguing highs."
        ));

        highlightRow.add(createFeatureHighlightCard(
                Feather.RADIO,
                "Ambient Transparency",
                "Switch between full ANC isolation and crystal Ambient Mode with a simple tap, allowing natural conversations and awareness."
        ));

        highlightRow.add(createFeatureHighlightCard(
                Feather.MIC,
                "Beamforming Microphones",
                "Quad-mic ENC array filters out 95% of background room noises, keyboard clicking, and wind for pristine call clarity."
        ));

        p.add(highlightRow);
        return p;
    }

    private JPanel createFeatureHighlightCard(Feather icon, String title, String body) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(12, COLOR_INPUT_BG, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 14, 14, 14));

        UIUtils.RoundedPanel iconBox = new UIUtils.RoundedPanel(10, COLOR_LIGHT_BLUE);
        iconBox.setPreferredSize(new Dimension(32, 32));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(new JLabel(FontIcon.of(icon, 16, COLOR_PRIMARY_DARK)));
        iconBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(COLOR_MAIN_TEXT);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel b = new JLabel("<html><body style='width: 240px; color:#6B7280; font-size:10px; line-height: 1.4;'>"
                + body + "</body></html>");
        b.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconBox);
        card.add(Box.createVerticalStrut(8));
        card.add(t);
        card.add(Box.createVerticalStrut(4));
        card.add(b);
        return card;
    }

    // Tab 2: Specifications
    private JPanel createSpecificationsTabContent() {
        JPanel p = new JPanel(new GridLayout(4, 2, 12, 10));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(4, 8, 4, 8));

        p.add(createSpecRow("Product Category", category));
        p.add(createSpecRow("Connectivity", "Bluetooth 5.3 + 3.5mm Aux"));
        p.add(createSpecRow("Battery Capacity", "600 mAh (Fast Charge: 10m = 5 hrs)"));
        p.add(createSpecRow("Active Noise Cancellation", "Hybrid ANC up to -35dB"));
        p.add(createSpecRow("Frequency Response", "20 Hz - 40,000 Hz"));
        p.add(createSpecRow("Weight", "248 grams (Ultra-lightweight)"));
        p.add(createSpecRow("Microphone System", "4-Mic Array with ENC Voice Isolation"));
        p.add(createSpecRow("Included Accessories", "Carrying Case, USB-C Cable, 3.5mm Jack"));

        return p;
    }

    private JPanel createSpecRow(String key, String val) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        JLabel k = new JLabel(key);
        k.setFont(new Font("Segoe UI", Font.BOLD, 11));
        k.setForeground(COLOR_SECONDARY);

        JLabel v = new JLabel(val);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        v.setForeground(COLOR_MAIN_TEXT);

        r.add(k, BorderLayout.WEST);
        r.add(v, BorderLayout.EAST);
        return r;
    }

    // Tab 3: Reviews
    private JPanel createReviewsTabContent() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        p.add(createReviewItem("Rahul S. ★★★★★", "Superb sound quality! Noise cancellation isolates keyboard and AC sound easily. Very comfortable."));
        p.add(Box.createVerticalStrut(8));
        p.add(createReviewItem("Sneha P. ★★★★★", "Battery lasts more than 4 days of continuous college study sessions. Worth every rupee!"));
        return p;
    }

    private JPanel createReviewItem(String author, String comment) {
        UIUtils.RoundedPanel r = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG);
        r.setLayout(new BoxLayout(r, BoxLayout.Y_AXIS));
        r.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel a = new JLabel(author);
        a.setFont(new Font("Segoe UI", Font.BOLD, 11));
        a.setForeground(COLOR_MAIN_TEXT);

        JLabel c = new JLabel(comment);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        c.setForeground(COLOR_SECONDARY);

        r.add(a);
        r.add(Box.createVerticalStrut(2));
        r.add(c);
        return r;
    }

    // ==========================================
    // 6. "YOU MAY ALSO LIKE" RELATED PRODUCTS
    // ==========================================
    private JPanel createRelatedProductsSection() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JLabel sectionTitle = new JLabel("You May Also Like");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(COLOR_MAIN_TEXT);

        JPanel cardsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        cardsRow.setOpaque(false);
        cardsRow.setBorder(new EmptyBorder(10, 0, 0, 0));

        cardsRow.add(createRelatedProductCard("Smart Watch", "Electronics", 2499, Feather.WATCH));
        cardsRow.add(createRelatedProductCard("Wireless Earbuds", "Electronics", 999, Feather.HEADPHONES));
        cardsRow.add(createRelatedProductCard("Portable Bluetooth Speaker", "Electronics", 1299, Feather.RADIO));

        container.add(sectionTitle);
        container.add(cardsRow);
        return container;
    }

    private JPanel createRelatedProductCard(String name, String cat, int itemPrice, Feather icon) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        // Center Placeholder Graphic
        JPanel graphic = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        graphic.setPreferredSize(new Dimension(140, 90));
        graphic.setLayout(new GridBagLayout());
        graphic.add(new JLabel(FontIcon.of(icon, 34, COLOR_PRIMARY_DARK)));

        // Bottom Details
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(8, 0, 0, 0));

        JPanel textGroup = new JPanel();
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
        textGroup.setOpaque(false);

        JLabel title = new JLabel(name);
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(COLOR_MAIN_TEXT);

        JLabel pr = new JLabel("₹" + String.format("%,d", itemPrice));
        pr.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pr.setForeground(COLOR_MAIN_TEXT);

        textGroup.add(title);
        textGroup.add(pr);

        JButton btnView = new JButton("View Details");
        btnView.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnView.setForeground(COLOR_PRIMARY_DARK);
        btnView.setBackground(COLOR_INPUT_BG);
        btnView.setFocusPainted(false);
        btnView.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnView.addActionListener(e -> {
            new ProductDetailsPage(name, cat, itemPrice, (int) (itemPrice * 1.4), "30% OFF", icon).setVisible(true);
            dispose();
        });

        bottom.add(textGroup, BorderLayout.WEST);
        bottom.add(btnView, BorderLayout.EAST);

        card.add(graphic, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private void showPlaceholder(String msg) {
        JOptionPane.showMessageDialog(this,
                msg + " screen is being integrated in the next project step.",
                "ShopSphere Navigation", JOptionPane.INFORMATION_MESSAGE);
    }
}
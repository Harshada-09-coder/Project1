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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CustomerDashboardPage extends JFrame {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final CartService cartService = new CartService();

    public CustomerDashboardPage() {
        setTitle("ShopSphere - Customer Catalog & Shopping");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1380, (int)(scr.width * 0.92)), Math.min(880, (int)(scr.height * 0.90)));
        setMinimumSize(new Dimension(1080, 700));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIUtils.COLOR_BG);

        // Custom Dashboard Top Navbar
        root.add(createDashboardNavbar(), BorderLayout.NORTH);

        // Center: Sidebar + Main Catalog area
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.setBorder(new EmptyBorder(10, 16, 10, 16));

        centerContainer.add(createSidebar(), BorderLayout.WEST);
        centerContainer.add(createMainCatalogScroll(), BorderLayout.CENTER);

        root.add(centerContainer, BorderLayout.CENTER);
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // 1. Dashboard Navbar with Search & Account
    private JPanel createDashboardNavbar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(UIUtils.COLOR_CARD);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtils.COLOR_BORDER),
                new EmptyBorder(10, 20, 10, 20)
        ));

        // Brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);
        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);

        User currentUser = UserSession.getCurrentUser();
        String welcomeText = (currentUser != null) ? "Hello, " + currentUser.getFullName() : "Your Everyday Shopping Destination";
        JLabel sub = new JLabel(welcomeText);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        brand.add(title);
        brand.add(sub);

        // Center Search Box
        UIUtils.RoundedPanel searchBox = new UIUtils.RoundedPanel(12, UIUtils.COLOR_INPUT_BG, UIUtils.COLOR_BORDER);
        searchBox.setPreferredSize(new Dimension(380, 36));
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 12, 0, 12));

        JLabel sIcon = new JLabel(FontIcon.of(Feather.SEARCH, 14, UIUtils.COLOR_TEXT_MUTED));
        JTextField sInput = new JTextField();
        sInput.setToolTipText("Search products, categories, or brands...");
        sInput.setBorder(null);
        sInput.setOpaque(false);
        sInput.setForeground(UIUtils.COLOR_TEXT_MAIN);
        sInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sInput.addActionListener(e -> {
            String kw = sInput.getText().trim();
            if (!kw.isEmpty()) {
                new ProductListingPage(kw).setVisible(true);
                dispose();
            }
        });

        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(sInput, BorderLayout.CENTER);

        // Right Quick Actions
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JLabel cartIcon = new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, UIUtils.COLOR_TEXT_MAIN));
        cartIcon.setToolTipText("View My Cart");
        cartIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cartIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MyCartPage().setVisible(true);
                CustomerDashboardPage.this.dispose();
            }
        });

        JButton btnAccount = new JButton("My Account", FontIcon.of(Feather.USER, 14, UIUtils.COLOR_TEXT_MAIN));
        btnAccount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAccount.setContentAreaFilled(false);
        btnAccount.setBorderPainted(false);
        btnAccount.setFocusPainted(false);
        btnAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAccount.addActionListener(e -> {
            new MyAccountPage().setVisible(true);
            CustomerDashboardPage.this.dispose();
        });

        right.add(cartIcon);
        right.add(btnAccount);

        nav.add(brand, BorderLayout.WEST);
        nav.add(searchBox, BorderLayout.CENTER);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    // 2. Sidebar Navigation
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setBorder(new EmptyBorder(10, 8, 10, 16));

        JLabel catLbl = new JLabel("CATEGORIES");
        catLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catLbl.setForeground(UIUtils.COLOR_TEXT_MUTED);

        sidebar.add(catLbl);
        sidebar.add(Box.createVerticalStrut(10));

        JButton btnAllProducts = createSidebarItem("All Products", Feather.GRID, true);
        btnAllProducts.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });
        sidebar.add(btnAllProducts);

        // Populate active categories dynamically from database/service
        try {
            List<Category> categories = categoryService.getAllActiveCategories();
            for (Category c : categories) {
                sidebar.add(createCategorySidebarItem(c.getCategoryName(), getIconForCategory(c.getCategoryName())));
            }
        } catch (Exception ex) {
            sidebar.add(createCategorySidebarItem("Electronics", Feather.TV));
            sidebar.add(createCategorySidebarItem("Fashion", Feather.TAG));
            sidebar.add(createCategorySidebarItem("Home & Living", Feather.HOME));
            sidebar.add(createCategorySidebarItem("Sports & Fitness", Feather.ACTIVITY));
            sidebar.add(createCategorySidebarItem("Books", Feather.BOOK));
        }

        sidebar.add(Box.createVerticalStrut(20));

        JLabel shopLbl = new JLabel("MY SHOPPING");
        shopLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        shopLbl.setForeground(UIUtils.COLOR_TEXT_MUTED);

        sidebar.add(shopLbl);
        sidebar.add(Box.createVerticalStrut(10));

        JButton btnCartSidebar = createSidebarItem("My Cart", Feather.SHOPPING_CART, false);
        btnCartSidebar.addActionListener(e -> {
            new MyCartPage().setVisible(true);
            CustomerDashboardPage.this.dispose();
        });
        sidebar.add(btnCartSidebar);

        JButton btnOrders = createSidebarItem("My Orders", Feather.PACKAGE, false);
        btnOrders.addActionListener(e -> {
            new MyOrdersPage().setVisible(true);
            CustomerDashboardPage.this.dispose();
        });
        sidebar.add(btnOrders);

        JButton btnMyAccount = createSidebarItem("My Account", Feather.USER, false);
        btnMyAccount.addActionListener(e -> {
            new MyAccountPage().setVisible(true);
            CustomerDashboardPage.this.dispose();
        });
        sidebar.add(btnMyAccount);

        // Logout Action
        JButton btnLogout = createSidebarItem("Logout", Feather.LOG_OUT, false);
        btnLogout.addActionListener(e -> {
            UserSession.logout();
            new WelcomePage().setVisible(true);
            dispose();
        });
        sidebar.add(btnLogout);

        return sidebar;
    }

    private JButton createCategorySidebarItem(String catName, Feather icon) {
        JButton btn = createSidebarItem(catName, icon, false);
        btn.addActionListener(e -> {
            new ProductListingPage(catName).setVisible(true);
            CustomerDashboardPage.this.dispose();
        });
        return btn;
    }

    private JButton createSidebarItem(String title, Feather icon, boolean active) {
        JButton btn = new JButton(title, FontIcon.of(icon, 14, active ? Color.WHITE : UIUtils.COLOR_TEXT_MAIN)) {
            @Override
            protected void paintComponent(Graphics g) {
                if (active) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(UIUtils.COLOR_PRIMARY);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 12));
        btn.setForeground(active ? Color.WHITE : UIUtils.COLOR_TEXT_MAIN);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));
        btn.setBorder(new EmptyBorder(6, 10, 6, 10));
        return btn;
    }

    // 3. Main Catalog View
    private JComponent createMainCatalogScroll() {
        JPanel catalog = new JPanel();
        catalog.setLayout(new BoxLayout(catalog, BoxLayout.Y_AXIS));
        catalog.setOpaque(false);
        catalog.setBorder(new EmptyBorder(6, 10, 16, 10));

        // Hero Banner
        catalog.add(createHeroBanner());
        catalog.add(Box.createVerticalStrut(20));

        // Shop by Category Row
        catalog.add(createSectionTitle("Shop by Category", "Verified Departments"));
        catalog.add(Box.createVerticalStrut(10));
        catalog.add(createCategoryCardsRow());
        catalog.add(Box.createVerticalStrut(24));

        // Featured Products
        catalog.add(createSectionTitle("Featured Products", "Live from MySQL Catalog"));
        catalog.add(Box.createVerticalStrut(10));
        catalog.add(createProductCardsRow());

        JScrollPane scroll = new JScrollPane(catalog);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createHeroBanner() {
        UIUtils.RoundedPanel banner = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        banner.setLayout(new BorderLayout());
        banner.setBorder(new EmptyBorder(20, 24, 20, 24));
        banner.setMaximumSize(new Dimension(Short.MAX_VALUE, 160));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel tag = new JLabel("✦ CURATED SEASONAL OFFERS");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        tag.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        JLabel h1 = new JLabel("Discover Your Next Favorite");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        h1.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel p = new JLabel("<html><body style='color:#6B7280; font-size:11px;'>"
                + "Explore quality products for your everyday needs with seamless desktop cart synchronization."
                + "</body></html>");

        JButton btnExplore = new JButton("Explore Products ➔");
        btnExplore.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExplore.setForeground(Color.WHITE);
        btnExplore.setBackground(UIUtils.COLOR_PRIMARY);
        btnExplore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExplore.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

        left.add(tag);
        left.add(Box.createVerticalStrut(4));
        left.add(h1);
        left.add(Box.createVerticalStrut(4));
        left.add(p);
        left.add(Box.createVerticalStrut(12));
        left.add(btnExplore);

        banner.add(left, BorderLayout.WEST);

        ImageIcon heroImg = ImageUtils.getHeroBanner(280, 140);
        if (heroImg != null) {
            JLabel heroLabel = new JLabel(heroImg);
            heroLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            banner.add(heroLabel, BorderLayout.CENTER);
        }

        return banner;
    }

    private JPanel createSectionTitle(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 26));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        s.setForeground(UIUtils.COLOR_TEXT_MUTED);

        p.add(t, BorderLayout.WEST);
        p.add(s, BorderLayout.EAST);
        return p;
    }

    private JPanel createCategoryCardsRow() {
        JPanel row = new JPanel(new GridLayout(1, 5, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));

        try {
            List<Category> list = categoryService.getAllActiveCategories();
            int count = 0;
            for (Category c : list) {
                if (count++ >= 5) break;
                row.add(createCategoryCard(c.getCategoryName(), "Active", getIconForCategory(c.getCategoryName())));
            }
        } catch (Exception ignored) {
        }

        if (row.getComponentCount() == 0) {
            row.add(createCategoryCard("Electronics", "Active", Feather.TV));
            row.add(createCategoryCard("Fashion", "Active", Feather.TAG));
            row.add(createCategoryCard("Home & Living", "Active", Feather.HOME));
            row.add(createCategoryCard("Sports & Fitness", "Active", Feather.ACTIVITY));
            row.add(createCategoryCard("Books", "Active", Feather.BOOK));
        }

        return row;
    }

    private JPanel createCategoryCard(String name, String count, Feather icon) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(12, Color.WHITE, UIUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(12, 10, 12, 10));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ProductListingPage(name).setVisible(true);
                CustomerDashboardPage.this.dispose();
            }
        });

        JLabel ic = new JLabel(FontIcon.of(icon, 18, UIUtils.COLOR_PRIMARY_DARK));
        ic.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nm = new JLabel(name);
        nm.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nm.setForeground(UIUtils.COLOR_TEXT_MAIN);
        nm.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel cn = new JLabel(count);
        cn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        cn.setForeground(UIUtils.COLOR_TEXT_MUTED);
        cn.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(ic);
        card.add(Box.createVerticalStrut(6));
        card.add(nm);
        card.add(Box.createVerticalStrut(2));
        card.add(cn);

        return card;
    }

    private JPanel createProductCardsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 260));

        List<Product> products = null;
        try {
            products = productService.getAllActiveProducts();
        } catch (Exception ignored) {
        }

        // Image resource slots to cycle through for visual variety when DB products share the same name
        String[] diverseImages = {
            "/images/headphones.png",
            "/images/smartwatch.png",
            "/images/backpack.png",
            "/images/sneakers.png"
        };

        if (products != null && !products.isEmpty()) {
            int displayed = 0;
            for (Product p : products) {
                if (displayed >= 4) break;
                String oldPrice = "₹" + String.format("%.2f", p.getPrice() * 1.25);
                String currentPrice = "₹" + String.format("%.2f", p.getPrice());
                // Force a unique image per slot so featured section never looks identical
                row.add(createLiveProductCard(p, currentPrice, oldPrice, "20% OFF",
                        getIconForProduct(p), diverseImages[displayed]));
                displayed++;
            }
            // Pad to 4 slots with dummy cards if fewer than 4 live products
            while (displayed < 4) {
                String[] dummyNames = {"Wireless Headphones", "Smart Fitness Watch", "Water-Resistant Backpack", "LED Table Lamp"};
                String[] dummyPrices = {"₹1,499.00", "₹2,499.00", "₹999.00", "₹799.00"};
                String[] dummyOld   = {"₹2,499.00", "₹3,999.00", "₹1,699.00", "₹1,299.00"};
                String[] dummyDisc  = {"40% OFF", "37% OFF", "41% OFF", "38% OFF"};
                Feather[] dummyIcons = {Feather.HEADPHONES, Feather.WATCH, Feather.PACKAGE, Feather.SUN};
                row.add(createDummyProductCard(dummyNames[displayed], dummyPrices[displayed],
                        dummyOld[displayed], dummyDisc[displayed], dummyIcons[displayed],
                        diverseImages[displayed]));
                displayed++;
            }
        } else {
            row.add(createDummyProductCard("Wireless Headphones", "₹1,499.00", "₹2,499.00", "40% OFF", Feather.HEADPHONES, diverseImages[0]));
            row.add(createDummyProductCard("Smart Fitness Watch", "₹2,499.00", "₹3,999.00", "37% OFF", Feather.WATCH, diverseImages[1]));
            row.add(createDummyProductCard("Water-Resistant Backpack", "₹999.00", "₹1,699.00", "41% OFF", Feather.PACKAGE, diverseImages[2]));
            row.add(createDummyProductCard("Minimalist LED Table Lamp", "₹799.00", "₹1,299.00", "38% OFF", Feather.SUN, diverseImages[3]));
        }

        return row;
    }

    private JPanel createLiveProductCard(Product product, String price, String oldPrice, String discount, Feather icon, String overrideImagePath) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, Color.WHITE, UIUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top discount badge
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel badge = new JLabel(" " + discount + " ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(UIUtils.COLOR_PRIMARY_DARK);

        JLabel stockStatus = new JLabel(product.getStockQuantity() > 0 ? "Stock: " + product.getStockQuantity() : "Out of Stock");
        stockStatus.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        stockStatus.setForeground(product.getStockQuantity() > 0 ? UIUtils.COLOR_GREEN : UIUtils.COLOR_DANGER);

        top.add(badge, BorderLayout.WEST);
        top.add(stockStatus, BorderLayout.EAST);

        // Center Product Graphic
        JPanel centerGraphic = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIUtils.COLOR_INPUT_BG);
                g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 10, 10);
                g2.dispose();
            }
        };
        centerGraphic.setOpaque(false);
        centerGraphic.setLayout(new GridBagLayout());
        ImageIcon pIcon = overrideImagePath != null
                ? ImageUtils.loadImage(overrideImagePath, 120, 100)
                : ImageUtils.getProductIcon(product, 120, 100);
        if (pIcon == null) pIcon = ImageUtils.getProductIcon(product, 120, 100);
        if (pIcon != null) {
            centerGraphic.add(new JLabel(pIcon));
        } else {
            centerGraphic.add(new JLabel(FontIcon.of(icon, 36, UIUtils.COLOR_PRIMARY)));
        }
        centerGraphic.setCursor(new Cursor(Cursor.HAND_CURSOR));
        centerGraphic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ProductDetailsPage(product).setVisible(true);
                CustomerDashboardPage.this.dispose();
            }
        });

        // Bottom Info & Button
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setOpaque(false);

        String shortName = product.getProductName().length() > 22 ? product.getProductName().substring(0, 20) + "…" : product.getProductName();
        JLabel t = new JLabel(shortName);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JPanel priceRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        priceRow.setOpaque(false);
        JLabel p = new JLabel(price);
        p.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel op = new JLabel("<html><strike>" + oldPrice + "</strike></html>");
        op.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        op.setForeground(UIUtils.COLOR_TEXT_MUTED);

        priceRow.add(p);
        priceRow.add(op);

        JButton btnAdd = new JButton("Add to Cart", FontIcon.of(Feather.SHOPPING_CART, 11, UIUtils.COLOR_PRIMARY_DARK));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> {
            int userId = UserSession.getCurrentUserId();
            try {
                cartService.addToCart(userId, product.getProductId(), 1);
                JOptionPane.showMessageDialog(CustomerDashboardPage.this,
                        "Added '" + product.getProductName() + "' to your shopping cart!",
                        "Cart Updated", JOptionPane.INFORMATION_MESSAGE);
            } catch (ValidationException ve) {
                JOptionPane.showMessageDialog(CustomerDashboardPage.this, ve.getMessage(), "Cart Warning", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CustomerDashboardPage.this, "Could not add to cart: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottom.add(t);
        bottom.add(Box.createVerticalStrut(4));
        bottom.add(priceRow);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(btnAdd);

        card.add(top, BorderLayout.NORTH);
        card.add(centerGraphic, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createDummyProductCard(String title, String price, String oldPrice, String discount, Feather icon, String overrideImagePath) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, Color.WHITE, UIUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel badge = new JLabel(" " + discount + " ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 9));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(UIUtils.COLOR_PRIMARY_DARK);
        top.add(badge, BorderLayout.WEST);

        JPanel centerGraphic = new JPanel();
        centerGraphic.setOpaque(false);
        centerGraphic.setLayout(new GridBagLayout());
        Product dummyP = new Product();
        dummyP.setProductName(title);
        ImageIcon pIcon = overrideImagePath != null
                ? ImageUtils.loadImage(overrideImagePath, 120, 100)
                : ImageUtils.getProductIcon(dummyP, 120, 100);
        if (pIcon == null) pIcon = ImageUtils.getProductIcon(dummyP, 120, 100);
        if (pIcon != null) {
            centerGraphic.add(new JLabel(pIcon));
        } else {
            centerGraphic.add(new JLabel(FontIcon.of(icon, 36, UIUtils.COLOR_PRIMARY)));
        }

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JPanel priceRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        priceRow.setOpaque(false);
        JLabel p = new JLabel(price);
        p.setFont(new Font("Segoe UI", Font.BOLD, 13));
        p.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel op = new JLabel("<html><strike>" + oldPrice + "</strike></html>");
        op.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        op.setForeground(UIUtils.COLOR_TEXT_MUTED);

        priceRow.add(p);
        priceRow.add(op);

        JButton btnAdd = new JButton("Add to Cart", FontIcon.of(Feather.SHOPPING_CART, 11, UIUtils.COLOR_PRIMARY_DARK));
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAdd.addActionListener(e -> JOptionPane.showMessageDialog(this, title + " added to cart!"));

        bottom.add(t);
        bottom.add(Box.createVerticalStrut(4));
        bottom.add(priceRow);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(btnAdd);

        card.add(top, BorderLayout.NORTH);
        card.add(centerGraphic, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private Feather getIconForCategory(String categoryName) {
        if (categoryName == null) return Feather.BOX;
        String lower = categoryName.toLowerCase();
        if (lower.contains("elect")) return Feather.TV;
        if (lower.contains("fash")) return Feather.TAG;
        if (lower.contains("home") || lower.contains("kitchen")) return Feather.HOME;
        if (lower.contains("sport") || lower.contains("fit")) return Feather.ACTIVITY;
        if (lower.contains("book")) return Feather.BOOK;
        return Feather.PACKAGE;
    }

    private Feather getIconForProduct(Product product) {
        if (product == null || product.getProductName() == null) return Feather.PACKAGE;
        String name = product.getProductName().toLowerCase();
        if (name.contains("headphone") || name.contains("earbud") || name.contains("audio")) return Feather.HEADPHONES;
        if (name.contains("watch")) return Feather.WATCH;
        if (name.contains("phone") || name.contains("galaxy") || name.contains("iphone")) return Feather.SMARTPHONE;
        if (name.contains("macbook") || name.contains("laptop")) return Feather.MONITOR;
        if (name.contains("shoe") || name.contains("sneaker")) return Feather.TAG;
        if (name.contains("jacket") || name.contains("shirt")) return Feather.TAG;
        if (name.contains("book")) return Feather.BOOK;
        return Feather.PACKAGE;
    }
}


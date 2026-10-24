package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProductListingPage extends JFrame {

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

    // In-memory Sample Products Data Model
    public static class ProductItem {
        public final String id;
        public final String name;
        public final String category;
        public final int price;
        public final int originalPrice;
        public final String discount;
        public final double rating;
        public final int reviewCount;
        public final Feather icon;

        public ProductItem(String id, String name, String category, int price, int originalPrice,
                           String discount, double rating, int reviewCount, Feather icon) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
            this.originalPrice = originalPrice;
            this.discount = discount;
            this.rating = rating;
            this.reviewCount = reviewCount;
            this.icon = icon;
        }
    }

    private final List<ProductItem> allProducts = new ArrayList<>();
    private final List<ProductItem> filteredProducts = new ArrayList<>();

    // UI Interactive Controls
    private JTextField txtSearch;
    private JComboBox<String> cmbCategory;
    private JTextField txtMinPrice;
    private JTextField txtMaxPrice;
    private JComboBox<String> cmbSort;
    private JLabel lblProductCount;
    private JPanel gridCardsPanel;

    public ProductListingPage() {
        this("All Categories");
    }

    public ProductListingPage(String initialCategory) {
        setTitle("ShopSphere - All Products");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Responsive Dimensions
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.min(1440, (int) (scr.width * 0.94));
        int h = Math.min(1024, (int) (scr.height * 0.92));
        setSize(w, h);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        initSampleData();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BACKGROUND);

        // 1. Shared Header
        root.add(createCustomerHeader(), BorderLayout.NORTH);

        // 2. Center Content Split: Sidebar (West) + Product Catalog Area (Center)
        JPanel mainArea = new JPanel(new BorderLayout(16, 0));
        mainArea.setOpaque(false);
        mainArea.setBorder(new EmptyBorder(12, 20, 14, 20));

        mainArea.add(createSidebar(), BorderLayout.WEST);
        mainArea.add(createMainContentArea(), BorderLayout.CENTER);

        root.add(mainArea, BorderLayout.CENTER);

        // 3. Shared Footer
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);

        // Apply initial category if passed from Dashboard
        if (initialCategory != null && !initialCategory.equals("All Categories")) {
            cmbCategory.setSelectedItem(initialCategory);
        } else {
            applyFilters();
        }
    }

    private void initSampleData() {
        allProducts.clear();
        allProducts.add(new ProductItem("P1", "Wireless Headphones", "Electronics", 1499, 2499, "40% OFF", 4.8, 342, Feather.HEADPHONES));
        allProducts.add(new ProductItem("P2", "Smart Watch", "Electronics", 2499, 3999, "37% OFF", 4.9, 518, Feather.WATCH));
        allProducts.add(new ProductItem("P3", "Casual Sneakers", "Fashion", 1799, 2999, "40% OFF", 4.6, 210, Feather.TAG));
        allProducts.add(new ProductItem("P4", "Travel Backpack", "Accessories", 999, 1699, "41% OFF", 4.7, 189, Feather.PACKAGE));
        allProducts.add(new ProductItem("P5", "Desk Lamp", "Home & Living", 799, 1299, "38% OFF", 4.5, 276, Feather.SUN));
        allProducts.add(new ProductItem("P6", "Cotton T-Shirt", "Fashion", 599, 999, "40% OFF", 4.4, 430, Feather.TAG));
        allProducts.add(new ProductItem("P7", "Notebook Set", "Books & Stationery", 299, 499, "40% OFF", 4.8, 150, Feather.BOOK));
        allProducts.add(new ProductItem("P8", "Fitness Yoga Mat", "Sports & Fitness", 899, 1499, "40% OFF", 4.7, 320, Feather.ACTIVITY));
        allProducts.add(new ProductItem("P9", "Gaming Mouse", "Electronics", 1299, 1999, "35% OFF", 4.8, 290, Feather.RADIO));
        allProducts.add(new ProductItem("P10", "Denim Jacket", "Fashion", 2199, 3499, "37% OFF", 4.5, 115, Feather.TAG));
        allProducts.add(new ProductItem("P11", "Ceramic Coffee Mug", "Home & Living", 399, 599, "33% OFF", 4.9, 412, Feather.COFFEE));
        allProducts.add(new ProductItem("P12", "Sunscreen Lotion SPF50", "Beauty & Personal Care", 449, 699, "36% OFF", 4.7, 198, Feather.SUN));
        
        filteredProducts.addAll(allProducts);
    }

    // ==========================================
    // 1. TOP HEADER (Reused ShopSphere Design)
    // ==========================================
    private JPanel createCustomerHeader() {
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

        // Center Quick Search
        UIUtils.RoundedPanel searchBox = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        searchBox.setPreferredSize(new Dimension(360, 36));
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 12, 0, 12));

        JLabel sIcon = new JLabel(FontIcon.of(Feather.SEARCH, 14, COLOR_SECONDARY));
        JTextField topSearch = new JTextField();
        topSearch.setText("Search catalog...");
        topSearch.setBorder(null);
        topSearch.setOpaque(false);
        topSearch.setForeground(COLOR_SECONDARY);
        topSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(topSearch, BorderLayout.CENTER);

        // Right Action Items
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnWishlist = createIconButton(Feather.HEART, "Wishlist (0)");
        JButton btnCart = createIconButton(Feather.SHOPPING_BAG, "Cart");
        btnCart.addActionListener(e -> { new MyCartPage().setVisible(true);
        dispose();
        });
        JButton btnAccount = createIconButton(Feather.USER, "My Account");
        btnAccount.addActionListener(e -> { new MyAccountPage().setVisible(true);
        dispose();
        });
        btnWishlist.addActionListener(e -> showPlaceholder("Wishlist"));
        btnCart.addActionListener(e -> showPlaceholder("My Cart"));
        btnAccount.addActionListener(e -> showPlaceholder("My Account"));

        right.add(btnWishlist);
        right.add(btnCart);
        right.add(btnAccount);

        nav.add(brand, BorderLayout.WEST);
        nav.add(searchBox, BorderLayout.CENTER);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JButton createIconButton(Feather icon, String text) {
        JButton btn = new JButton(text, FontIcon.of(icon, 14, COLOR_MAIN_TEXT));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(COLOR_MAIN_TEXT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        JLabel catLbl = new JLabel("SHOP");
        catLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catLbl.setForeground(COLOR_SECONDARY);

        sidebar.add(catLbl);
        sidebar.add(Box.createVerticalStrut(8));

        // Sidebar Shop items
        sidebar.add(createSidebarBtn("All Products", Feather.GRID, true, () -> {
            cmbCategory.setSelectedItem("All Categories");
        }));
        sidebar.add(createSidebarBtn("Electronics", Feather.TV, false, () -> cmbCategory.setSelectedItem("Electronics")));
        sidebar.add(createSidebarBtn("Fashion", Feather.TAG, false, () -> cmbCategory.setSelectedItem("Fashion")));
        sidebar.add(createSidebarBtn("Home & Living", Feather.HOME, false, () -> cmbCategory.setSelectedItem("Home & Living")));
        sidebar.add(createSidebarBtn("Beauty & Care", Feather.SUN, false, () -> cmbCategory.setSelectedItem("Beauty & Personal Care")));
        sidebar.add(createSidebarBtn("Books & Stationery", Feather.BOOK, false, () -> cmbCategory.setSelectedItem("Books & Stationery")));
        sidebar.add(createSidebarBtn("Sports & Fitness", Feather.ACTIVITY, false, () -> cmbCategory.setSelectedItem("Sports & Fitness")));
        sidebar.add(createSidebarBtn("Accessories", Feather.PACKAGE, false, () -> cmbCategory.setSelectedItem("Accessories")));

        sidebar.add(Box.createVerticalStrut(20));

        JLabel myShopLbl = new JLabel("MY SHOPPING");
        myShopLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        myShopLbl.setForeground(COLOR_SECONDARY);

        sidebar.add(myShopLbl);
        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createSidebarBtn("My Cart", Feather.SHOPPING_CART, false, () -> {
        new MyCartPage().setVisible(true);
         dispose();
        }));       
        sidebar.add(createSidebarBtn("My Orders", Feather.ARCHIVE, false, () -> {
        new MyOrdersPage().setVisible(true);
        dispose();
        }));
        sidebar.add(createSidebarBtn("My Account", Feather.USER, false, () -> { new MyAccountPage().setVisible(true);
        dispose();
        }));
        // Logout
        sidebar.add(createSidebarBtn("Logout", Feather.LOG_OUT, false, () -> {
            new WelcomePage().setVisible(true);
            dispose();
        }));

        return sidebar;
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
    // 3. MAIN CATALOG AREA (Breadcrumbs, Filters, Grid)
    // ==========================================
    private JComponent createMainContentArea() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);

        // Header Title & Breadcrumb Card
        mainContent.add(createPageTitleBanner());
        mainContent.add(Box.createVerticalStrut(12));

        // Filter Bar Card
        mainContent.add(createFilterAndSortToolbar());
        mainContent.add(Box.createVerticalStrut(14));

        // Products Grid
        gridCardsPanel = new JPanel(new GridLayout(0, 4, 14, 14));
        gridCardsPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(gridCardsPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        mainContent.add(scroll);
        return mainContent;
    }

    private JPanel createPageTitleBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setOpaque(false);
        banner.setMaximumSize(new Dimension(Short.MAX_VALUE, 58));

        // Left Titles
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        // Breadcrumb
        JPanel breadcrumbs = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        breadcrumbs.setOpaque(false);

        JLabel homeLink = new JLabel("Home");
        homeLink.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        homeLink.setForeground(COLOR_PRIMARY_DARK);
        homeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CustomerDashboardPage().setVisible(true);
                dispose();
            }
        });

        JLabel slash = new JLabel("/");
        slash.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        slash.setForeground(COLOR_SECONDARY);

        JLabel current = new JLabel("All Products");
        current.setFont(new Font("Segoe UI", Font.BOLD, 11));
        current.setForeground(COLOR_MAIN_TEXT);

        breadcrumbs.add(homeLink);
        breadcrumbs.add(slash);
        breadcrumbs.add(current);

        // Page title
        JLabel title = new JLabel("All Products");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(COLOR_MAIN_TEXT);

        JLabel sub = new JLabel("Explore our collection and find products you love.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_SECONDARY);

        left.add(breadcrumbs);
        left.add(Box.createVerticalStrut(2));
        left.add(title);
        left.add(sub);

        // Right Product Counter Badge
        lblProductCount = new JLabel("Showing 12 products");
        lblProductCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblProductCount.setForeground(COLOR_SECONDARY);

        banner.add(left, BorderLayout.WEST);
        banner.add(lblProductCount, BorderLayout.EAST);

        return banner;
    }

    // ==========================================
    // 4. FILTER & SORT TOOLBAR
    // ==========================================
    private JPanel createFilterAndSortToolbar() {
        UIUtils.RoundedPanel panel = new UIUtils.RoundedPanel(14, COLOR_CARD, COLOR_BORDER);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 10));
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 58));

        // 1. Search Box
        UIUtils.RoundedPanel searchBox = new UIUtils.RoundedPanel(8, COLOR_INPUT_BG, COLOR_BORDER);
        searchBox.setPreferredSize(new Dimension(200, 32));
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 8, 0, 8));

        JLabel icon = new JLabel(FontIcon.of(Feather.SEARCH, 13, COLOR_SECONDARY));
        txtSearch = new JTextField();
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtSearch.setToolTipText("Search by product name...");
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { applyFilters(); }
            public void removeUpdate(DocumentEvent e) { applyFilters(); }
            public void changedUpdate(DocumentEvent e) { applyFilters(); }
        });

        searchBox.add(icon, BorderLayout.WEST);
        searchBox.add(txtSearch, BorderLayout.CENTER);

        // 2. Category Dropdown
        String[] categories = {
                "All Categories", "Electronics", "Fashion", "Home & Living",
                "Beauty & Personal Care", "Books & Stationery", "Sports & Fitness", "Accessories"
        };
        cmbCategory = new JComboBox<>(categories);
        cmbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cmbCategory.setPreferredSize(new Dimension(150, 32));
        cmbCategory.setBackground(Color.WHITE);
        cmbCategory.addActionListener(e -> applyFilters());

        // 3. Price Filter (Min / Max)
        txtMinPrice = new JTextField(4);
        txtMinPrice.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtMinPrice.setToolTipText("Min Price");
        txtMinPrice.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                new EmptyBorder(3, 6, 3, 6)
        ));

        txtMaxPrice = new JTextField(4);
        txtMaxPrice.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtMaxPrice.setToolTipText("Max Price");
        txtMaxPrice.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                new EmptyBorder(3, 6, 3, 6)
        ));

        JButton btnPriceFilter = new JButton("Go");
        btnPriceFilter.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnPriceFilter.setBackground(COLOR_LIGHT_BLUE);
        btnPriceFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPriceFilter.addActionListener(e -> applyFilters());

        JPanel priceGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        priceGroup.setOpaque(false);
        JLabel lblPrice = new JLabel("Price: ₹");
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblPrice.setForeground(COLOR_SECONDARY);
        JLabel lblDash = new JLabel("-");
        priceGroup.add(lblPrice);
        priceGroup.add(txtMinPrice);
        priceGroup.add(lblDash);
        priceGroup.add(txtMaxPrice);
        priceGroup.add(btnPriceFilter);

        // 4. Sort Dropdown
        String[] sortOptions = {"Featured", "Price: Low to High", "Price: High to Low", "Name: A to Z"};
        cmbSort = new JComboBox<>(sortOptions);
        cmbSort.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cmbSort.setPreferredSize(new Dimension(140, 32));
        cmbSort.setBackground(Color.WHITE);
        cmbSort.addActionListener(e -> applyFilters());

        // 5. Clear Filters Button
        JButton btnClear = new JButton("Clear Filters", FontIcon.of(Feather.X_CIRCLE, 12, COLOR_SECONDARY));
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnClear.setForeground(COLOR_SECONDARY);
        btnClear.setContentAreaFilled(false);
        btnClear.setBorderPainted(false);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.addActionListener(e -> resetFilters());

        panel.add(new JLabel("Search:"));
        panel.add(searchBox);
        panel.add(Box.createHorizontalStrut(6));
        panel.add(new JLabel("Category:"));
        panel.add(cmbCategory);
        panel.add(Box.createHorizontalStrut(6));
        panel.add(priceGroup);
        panel.add(Box.createHorizontalStrut(6));
        panel.add(new JLabel("Sort By:"));
        panel.add(cmbSort);
        panel.add(btnClear);

        return panel;
    }

    // ==========================================
    // 5. FILTER & SORT LOGIC (In-Memory)
    // ==========================================
    private void applyFilters() {
        String keyword = txtSearch != null ? txtSearch.getText().trim().toLowerCase() : "";
        String selectedCat = cmbCategory != null ? (String) cmbCategory.getSelectedItem() : "All Categories";

        int minP = 0;
        int maxP = Integer.MAX_VALUE;

        try {
            if (txtMinPrice != null && !txtMinPrice.getText().trim().isEmpty()) {
                minP = Integer.parseInt(txtMinPrice.getText().trim());
            }
            if (txtMaxPrice != null && !txtMaxPrice.getText().trim().isEmpty()) {
                maxP = Integer.parseInt(txtMaxPrice.getText().trim());
            }
        } catch (NumberFormatException ignored) {}

        final int finalMin = minP;
        final int finalMax = maxP;

        // Filter Stream
        filteredProducts.clear();
        filteredProducts.addAll(allProducts.stream()
                .filter(p -> keyword.isEmpty() || p.name.toLowerCase().contains(keyword))
                .filter(p -> selectedCat.equals("All Categories") || p.category.equalsIgnoreCase(selectedCat))
                .filter(p -> p.price >= finalMin && p.price <= finalMax)
                .collect(Collectors.toList()));

        // Sorting
        String sort = cmbSort != null ? (String) cmbSort.getSelectedItem() : "Featured";
        if ("Price: Low to High".equals(sort)) {
            filteredProducts.sort(Comparator.comparingInt(a -> a.price));
        } else if ("Price: High to Low".equals(sort)) {
            filteredProducts.sort((a, b) -> Integer.compare(b.price, a.price));
        } else if ("Name: A to Z".equals(sort)) {
            filteredProducts.sort(Comparator.comparing(a -> a.name));
        }

        renderProductGrid();
    }

    private void resetFilters() {
        if (txtSearch != null) txtSearch.setText("");
        if (cmbCategory != null) cmbCategory.setSelectedIndex(0);
        if (txtMinPrice != null) txtMinPrice.setText("");
        if (txtMaxPrice != null) txtMaxPrice.setText("");
        if (cmbSort != null) cmbSort.setSelectedIndex(0);
        applyFilters();
    }

    private void renderProductGrid() {
        gridCardsPanel.removeAll();
        lblProductCount.setText("Showing " + filteredProducts.size() + " products");

        if (filteredProducts.isEmpty()) {
            JPanel emptyNotice = new JPanel(new GridBagLayout());
            emptyNotice.setOpaque(false);
            JLabel lbl = new JLabel("No products match your filters. Try clearing filters.");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(COLOR_SECONDARY);
            emptyNotice.add(lbl);
            gridCardsPanel.setLayout(new BorderLayout());
            gridCardsPanel.add(emptyNotice, BorderLayout.CENTER);
        } else {
            gridCardsPanel.setLayout(new GridLayout(0, 4, 14, 14));
            for (ProductItem item : filteredProducts) {
                gridCardsPanel.add(createProductCard(item));
            }
        }

        gridCardsPanel.revalidate();
        gridCardsPanel.repaint();
    }

    // ==========================================
    // 6. PRODUCT CARD COMPONENT
    // ==========================================
    private JPanel createProductCard(ProductItem item) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top Row: Discount Tag + Heart Icon
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel discountBadge = new JLabel(" " + item.discount + " ");
        discountBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        discountBadge.setForeground(Color.WHITE);
        discountBadge.setOpaque(true);
        discountBadge.setBackground(COLOR_PRIMARY_DARK);

        JLabel heartIcon = new JLabel(FontIcon.of(Feather.HEART, 14, COLOR_SECONDARY));
        heartIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        heartIcon.addMouseListener(new MouseAdapter() {
            private boolean liked = false;
            @Override
            public void mouseClicked(MouseEvent e) {
                liked = !liked;
                heartIcon.setIcon(FontIcon.of(Feather.HEART, 14, liked ? new Color(0xEF, 0x44, 0x44) : COLOR_SECONDARY));
            }
        });

        top.add(discountBadge, BorderLayout.WEST);
        top.add(heartIcon, BorderLayout.EAST);

        // Center: Vector / Placeholder Illustration Area
        JPanel imageCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        imageCanvas.setPreferredSize(new Dimension(160, 110));
        imageCanvas.setLayout(new GridBagLayout());
        imageCanvas.add(new JLabel(FontIcon.of(item.icon, 40, COLOR_PRIMARY_DARK)));
        imageCanvas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        imageCanvas.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        new ProductDetailsPage(item.name, item.category, item.price, item.originalPrice, item.discount, item.icon).setVisible(true);
        ProductListingPage.this.dispose();
    }
});

        // Bottom Details Area
        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);
        details.setBorder(new EmptyBorder(8, 0, 0, 0));

        JLabel catLbl = new JLabel(item.category.toUpperCase());
        catLbl.setFont(new Font("Segoe UI", Font.BOLD, 9));
        catLbl.setForeground(COLOR_SECONDARY);

        JLabel nameLbl = new JLabel(item.name);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLbl.setForeground(COLOR_MAIN_TEXT);

        // Rating row
        JPanel ratingRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        ratingRow.setOpaque(false);
        JLabel star = new JLabel(FontIcon.of(Feather.STAR, 11, COLOR_STAR_YELLOW));
        JLabel score = new JLabel(item.rating + " (" + item.reviewCount + ")");
        score.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        score.setForeground(COLOR_SECONDARY);
        ratingRow.add(star);
        ratingRow.add(score);

        // Price Row
        JPanel priceRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        priceRow.setOpaque(false);
        JLabel pMain = new JLabel("₹" + String.format("%,d", item.price));
        pMain.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pMain.setForeground(COLOR_MAIN_TEXT);

        JLabel pOld = new JLabel("<html><strike>₹" + String.format("%,d", item.originalPrice) + "</strike></html>");
        pOld.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        pOld.setForeground(COLOR_SECONDARY);

        priceRow.add(pMain);
        priceRow.add(pOld);

        // Action Buttons Row: View Details + Add to Cart
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 6, 0));
        btnRow.setOpaque(false);

        JButton btnDetails = new JButton("Details");
        btnDetails.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnDetails.setForeground(COLOR_MAIN_TEXT);
        btnDetails.setBackground(COLOR_INPUT_BG);
        btnDetails.setFocusPainted(false);
        btnDetails.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDetails.addActionListener(e -> {
    new ProductDetailsPage(item.name, item.category, item.price, item.originalPrice, item.discount, item.icon).setVisible(true);
    ProductListingPage.this.dispose();
});
        JButton btnAddToCart = new JButton("Add", FontIcon.of(Feather.SHOPPING_CART, 11, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_PRIMARY_DARK.darker() : COLOR_PRIMARY_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setContentAreaFilled(false);
        btnAddToCart.setBorderPainted(false);
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddToCart.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    item.name + " (₹" + item.price + ") has been added to your cart!",
                    "Added to Cart", JOptionPane.INFORMATION_MESSAGE);
        });

        btnRow.add(btnDetails);
        btnRow.add(btnAddToCart);

        details.add(catLbl);
        details.add(nameLbl);
        details.add(Box.createVerticalStrut(2));
        details.add(ratingRow);
        details.add(Box.createVerticalStrut(4));
        details.add(priceRow);
        details.add(Box.createVerticalStrut(8));
        details.add(btnRow);

        card.add(top, BorderLayout.NORTH);
        card.add(imageCanvas, BorderLayout.CENTER);
        card.add(details, BorderLayout.SOUTH);

        return card;
    }

    private void showPlaceholder(String screenName) {
        JOptionPane.showMessageDialog(this,
                screenName + " screen will be connected cleanly in the next step.",
                "ShopSphere Navigation", JOptionPane.INFORMATION_MESSAGE);
    }
}
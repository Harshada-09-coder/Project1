package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerDashboardPage extends JFrame {

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

        JLabel sub = new JLabel("Your Everyday Shopping Destination");
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
        JTextField sInput = new JTextField("Search products, categories, or brands...");
        sInput.setBorder(null);
        sInput.setOpaque(false);
        sInput.setForeground(UIUtils.COLOR_TEXT_MUTED);
        sInput.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(sInput, BorderLayout.CENTER);

        // Right Quick Actions
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JLabel wishIcon = new JLabel(FontIcon.of(Feather.HEART, 16, UIUtils.COLOR_TEXT_MAIN));
        wishIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        wishIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(CustomerDashboardPage.this, "Wishlist will be shown here.", "Wishlist", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel cartIcon = new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, UIUtils.COLOR_TEXT_MAIN));
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

        right.add(wishIcon);
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

        sidebar.add(createCategorySidebarItem("Electronics", Feather.TV));
        sidebar.add(createCategorySidebarItem("Fashion", Feather.TAG));
        sidebar.add(createCategorySidebarItem("Home & Living", Feather.HOME));
        sidebar.add(createCategorySidebarItem("Beauty & Care", Feather.SUN));
        sidebar.add(createCategorySidebarItem("Books & Stationery", Feather.BOOK));
        sidebar.add(createCategorySidebarItem("Sports & Fitness", Feather.ACTIVITY));

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
        catalog.add(createSectionTitle("Shop by Category", "6 Active Departments"));
        catalog.add(Box.createVerticalStrut(10));
        catalog.add(createCategoryCardsRow());
        catalog.add(Box.createVerticalStrut(24));

        // Featured Products
        catalog.add(createSectionTitle("Featured Products", "Handpicked for you"));
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
        JPanel row = new JPanel(new GridLayout(1, 6, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 100));

        row.add(createCategoryCard("Electronics", "1.2k+ items", Feather.TV));
        row.add(createCategoryCard("Fashion", "3.4k+ items", Feather.TAG));
        row.add(createCategoryCard("Home & Living", "890+ items", Feather.HOME));
        row.add(createCategoryCard("Beauty & Care", "640+ items", Feather.SUN));
        row.add(createCategoryCard("Books & Stationery", "1.5k+ items", Feather.BOOK));
        row.add(createCategoryCard("Sports & Fitness", "420+ items", Feather.ACTIVITY));

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

        row.add(createProductCard("Wireless Headphones", "₹1,499", "₹2,499", "40% OFF", Feather.HEADPHONES));
        row.add(createProductCard("Smart Fitness Watch", "₹2,499", "₹3,999", "37% OFF", Feather.WATCH));
        row.add(createProductCard("Water-Resistant Backpack", "₹999", "₹1,699", "41% OFF", Feather.PACKAGE));
        row.add(createProductCard("Minimalist LED Table Lamp", "₹799", "₹1,299", "38% OFF", Feather.SUN));

        return row;
    }

    private JPanel createProductCard(String title, String price, String oldPrice, String discount, Feather icon) {
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

        JLabel heart = new JLabel(FontIcon.of(Feather.HEART, 14, UIUtils.COLOR_TEXT_MUTED));
        top.add(badge, BorderLayout.WEST);
        top.add(heart, BorderLayout.EAST);

        // Center Product Graphic Placeholder
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
        centerGraphic.add(new JLabel(FontIcon.of(icon, 36, UIUtils.COLOR_PRIMARY)));
        centerGraphic.setCursor(new Cursor(Cursor.HAND_CURSOR));
        centerGraphic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ProductDetailsPage(title, "Electronics", 1499, 2499, discount, icon).setVisible(true);
                CustomerDashboardPage.this.dispose();
            }
        });

        // Bottom Info & Button
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
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
}
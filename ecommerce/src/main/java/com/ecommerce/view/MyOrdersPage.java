package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class MyOrdersPage extends JFrame {

    // Palette Configuration
    public static final Color COLOR_PRIMARY      = new Color(0x78, 0xA5, 0xCE);
    public static final Color COLOR_PRIMARY_DARK = new Color(0x5F, 0x8E, 0xB8);
    public static final Color COLOR_LIGHT_BLUE   = new Color(0xD7, 0xE7, 0xF5);
    public static final Color COLOR_BACKGROUND   = new Color(0xF3, 0xF6, 0xFA);
    public static final Color COLOR_CARD         = Color.WHITE;
    public static final Color COLOR_TEXT_MAIN    = new Color(0x1F, 0x2D, 0x42);
    public static final Color COLOR_SECONDARY    = new Color(0x71, 0x7E, 0x93);
    public static final Color COLOR_BORDER       = new Color(0xE3, 0xEC, 0xF4);
    public static final Color COLOR_INPUT_BG     = new Color(0xF7, 0xFA, 0xFD);
    public static final Color COLOR_GREEN        = new Color(0x10, 0xB9, 0x81);
    public static final Color COLOR_RED          = new Color(0xEF, 0x44, 0x44);

    // In-memory Data Models
    public static class OrderItem {
        public final String name;
        public final int quantity;
        public final String details;
        public final Feather icon;

        public OrderItem(String name, int quantity, String details, Feather icon) {
            this.name = name;
            this.quantity = quantity;
            this.details = details;
            this.icon = icon;
        }
    }

    public static class OrderRecord {
        public final String orderId;
        public final Date orderDate;
        public final String dateDisplay;
        public final List<OrderItem> items;
        public final int totalAmount;
        public final String paymentMethod;
        public String status;
        public final String statusNote;

        public OrderRecord(String orderId, Date orderDate, String dateDisplay, List<OrderItem> items,
                           int totalAmount, String paymentMethod, String status, String statusNote) {
            this.orderId = orderId;
            this.orderDate = orderDate;
            this.dateDisplay = dateDisplay;
            this.items = items;
            this.totalAmount = totalAmount;
            this.paymentMethod = paymentMethod;
            this.status = status;
            this.statusNote = statusNote;
        }
    }

    private final List<OrderRecord> allOrders = new ArrayList<>();
    private final List<OrderRecord> displayedOrders = new ArrayList<>();

    // Interactive Controls
    private JTextField txtSearch;
    private JComboBox<String> cmbStatus;
    private JComboBox<String> cmbSort;
    private JPanel ordersContainer;
    private JPanel emptyStatePanel;
    private JLabel lblFooterCount;

    // Summary Metric Labels
    private JLabel lblSummaryTotal;
    private JLabel lblSummaryDelivered;
    private JLabel lblSummaryInProgress;
    private JLabel lblSummaryCancelled;

    public MyOrdersPage() {
        setTitle("ShopSphere - My Orders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int targetW = Math.min(1380, (int) (scr.width * 0.94));
        int targetH = Math.min(920, (int) (scr.height * 0.92));
        setSize(targetW, targetH);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        initSampleOrders();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BACKGROUND);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel mainLayout = new JPanel(new BorderLayout(16, 0));
        mainLayout.setOpaque(false);
        mainLayout.setBorder(new EmptyBorder(10, 20, 12, 20));

        mainLayout.add(createSidebar(), BorderLayout.WEST);
        mainLayout.add(createScrollableBody(), BorderLayout.CENTER);

        root.add(mainLayout, BorderLayout.CENTER);
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);
        applyFilters();
    }

    private void initSampleOrders() {
        allOrders.clear();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.ENGLISH);

        // Order 1 (Processing)
        cal.set(2026, Calendar.OCTOBER, 20, 10, 42);
        allOrders.add(new OrderRecord(
                "SS-20261020-1042",
                cal.getTime(),
                "20 October 2026, 10:42 AM",
                Arrays.asList(
                        new OrderItem("Wireless Over-Ear Headphones", 1, "Electronics • Audio", Feather.HEADPHONES),
                        new OrderItem("Smart Fitness Watch Pro", 1, "Wearables • Fitness", Feather.WATCH)
                ),
                3998,
                "Cash on Delivery (Post-paid)",
                "Processing",
                "Order Confirmed"
        ));

        // Order 2 (Delivered)
        cal.set(2026, Calendar.OCTOBER, 12, 14, 20);
        allOrders.add(new OrderRecord(
                "SS-20261012-0831",
                cal.getTime(),
                "12 October 2026",
                Collections.singletonList(
                        new OrderItem("Urban Commuter Travel Backpack", 1, "Fashion • Luggage & Bags", Feather.PACKAGE)
                ),
                999,
                "UPI (Verified)",
                "Delivered",
                "Delivered on 15 Oct"
        ));

        // Order 3 (Shipped)
        cal.set(2026, Calendar.SEPTEMBER, 28, 9, 15);
        allOrders.add(new OrderRecord(
                "SS-20260928-0715",
                cal.getTime(),
                "28 September 2026",
                Arrays.asList(
                        new OrderItem("Portable Bluetooth Speaker", 1, "Electronics • Audio", Feather.RADIO),
                        new OrderItem("65W GaN USB-C Fast Charger", 2, "Accessories • Power", Feather.ZAP)
                ),
                2198,
                "UPI (Verified)",
                "Shipped",
                "Delivered on 02 Oct"
        ));

        // Order 4 (Cancelled)
        cal.set(2026, Calendar.SEPTEMBER, 15, 18, 50);
        allOrders.add(new OrderRecord(
                "SS-20260915-0520",
                cal.getTime(),
                "15 September 2026",
                Collections.singletonList(
                        new OrderItem("Casual Retro Runner Sneakers", 1, "Footwear • Men's 9.5", Feather.TAG)
                ),
                1799,
                "Cash on Delivery",
                "Cancelled",
                "Cancelled by User"
        ));

        // Order 5 (Delivered)
        cal.set(2026, Calendar.AUGUST, 24, 11, 30);
        allOrders.add(new OrderRecord(
                "SS-20260824-0319",
                cal.getTime(),
                "24 August 2026",
                Collections.singletonList(
                        new OrderItem("Minimalist Nordic LED Table Lamp", 1, "Home & Living • Lighting", Feather.SUN)
                ),
                799,
                "Card (Debit)",
                "Delivered",
                "Delivered on 28 Aug"
        ));

        displayedOrders.addAll(allOrders);
    }

    // ==========================================
    // 1. TOP HEADER
    // ==========================================
    private JPanel createHeader() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(COLOR_CARD);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        UIUtils.RoundedPanel logo = new UIUtils.RoundedPanel(10, COLOR_LIGHT_BLUE);
        logo.setPreferredSize(new Dimension(32, 32));
        logo.setLayout(new GridBagLayout());
        logo.add(new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, COLOR_PRIMARY_DARK)));

        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("Your Everyday Shopping Destination");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_SECONDARY);

        brand.add(logo);
        brand.add(title);
        brand.add(sub);

        UIUtils.RoundedPanel searchBox = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        searchBox.setPreferredSize(new Dimension(360, 34));
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 12, 0, 12));

        JLabel sIcon = new JLabel(FontIcon.of(Feather.SEARCH, 13, COLOR_SECONDARY));
        JTextField topSearch = new JTextField("Search products, categories, or brands...");
        topSearch.setBorder(null);
        topSearch.setOpaque(false);
        topSearch.setForeground(COLOR_SECONDARY);
        topSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(topSearch, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnWishlist = createHeaderButton(Feather.HEART, "Wishlist (0)", () -> showPlaceholder("Wishlist"));
        JButton btnCart     = createHeaderButton(Feather.SHOPPING_BAG, "Cart (3)", () -> {
            new MyCartPage().setVisible(true);
            dispose();
        });
        JButton btnAccount  = createHeaderButton(Feather.USER, "My Account", () -> {
            new MyAccountPage().setVisible(true);
            dispose();
        });

        right.add(btnWishlist);
        right.add(btnCart);
        right.add(btnAccount);

        nav.add(brand, BorderLayout.WEST);
        nav.add(searchBox, BorderLayout.CENTER);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JButton createHeaderButton(Feather icon, String text, Runnable action) {
        JButton btn = new JButton(text, FontIcon.of(icon, 14, COLOR_TEXT_MAIN));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(COLOR_TEXT_MAIN);
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
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setBorder(new EmptyBorder(8, 4, 10, 12));

        JLabel catLbl = new JLabel("CATEGORIES");
        catLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catLbl.setForeground(COLOR_SECONDARY);

        sidebar.add(catLbl);
        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createSidebarBtn("All Products", Feather.GRID, false, () -> {
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

        sidebar.add(createSidebarBtn("My Cart", Feather.SHOPPING_CART, false, () -> {
            new MyCartPage().setVisible(true);
            dispose();
        }));

        // Highlight My Orders as ACTIVE
        sidebar.add(createSidebarBtn("My Orders", Feather.ARCHIVE, true, () -> {}));
        sidebar.add(createSidebarBtn("My Account", Feather.USER, false, () -> {
            new MyAccountPage().setVisible(true);
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
        JButton btn = new JButton(title, FontIcon.of(icon, 13, active ? Color.WHITE : COLOR_TEXT_MAIN)) {
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
        btn.setForeground(active ? Color.WHITE : COLOR_TEXT_MAIN);
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
    // 3. MAIN BODY
    // ==========================================
    private JComponent createScrollableBody() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        content.add(createPageHeaderBanner());
        content.add(Box.createVerticalStrut(12));

        content.add(createFilterToolbar());
        content.add(Box.createVerticalStrut(14));

        ordersContainer = new JPanel();
        ordersContainer.setLayout(new BoxLayout(ordersContainer, BoxLayout.Y_AXIS));
        ordersContainer.setOpaque(false);

        emptyStatePanel = createEmptyStatePanel();
        emptyStatePanel.setVisible(false);

        content.add(ordersContainer);
        content.add(emptyStatePanel);
        content.add(Box.createVerticalStrut(12));

        content.add(createFooterControls());
        content.add(Box.createVerticalStrut(16));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createPageHeaderBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setOpaque(false);
        banner.setMaximumSize(new Dimension(Short.MAX_VALUE, 58));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JPanel crumbs = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        crumbs.setOpaque(false);

        JLabel home = createBreadcrumbLink("Home", () -> {
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });
        JLabel shop = createBreadcrumbLink("My Shopping", () -> {});
        JLabel current = new JLabel("My Orders");
        current.setFont(new Font("Segoe UI", Font.BOLD, 11));
        current.setForeground(COLOR_TEXT_MAIN);

        crumbs.add(home);
        crumbs.add(new JLabel("/"));
        crumbs.add(shop);
        crumbs.add(new JLabel("/"));
        crumbs.add(current);

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleRow.setOpaque(false);

        JLabel title = new JLabel("My Orders");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(COLOR_TEXT_MAIN);

        UIUtils.RoundedPanel badgeCount = new UIUtils.RoundedPanel(10, COLOR_LIGHT_BLUE);
        badgeCount.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
        lblSummaryTotal = new JLabel(allOrders.size() + " Total");
        lblSummaryTotal.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblSummaryTotal.setForeground(COLOR_PRIMARY_DARK);
        badgeCount.add(lblSummaryTotal);

        titleRow.add(title);
        titleRow.add(badgeCount);

        JLabel sub = new JLabel("Track your purchases, review invoices, and manage post-order deliveries.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_SECONDARY);

        left.add(crumbs);
        left.add(Box.createVerticalStrut(2));
        left.add(titleRow);
        left.add(sub);

        JPanel summaryPills = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        summaryPills.setOpaque(false);

        lblSummaryDelivered = new JLabel();
        lblSummaryInProgress = new JLabel();
        lblSummaryCancelled = new JLabel();

        summaryPills.add(createSummaryPill("SUMMARY", allOrders.size(), COLOR_TEXT_MAIN, COLOR_CARD));
        summaryPills.add(createSummaryPill("● Delivered:", getCountByStatus("Delivered"), COLOR_GREEN, COLOR_CARD));
        summaryPills.add(createSummaryPill("● In Progress:", getCountByStatus("Processing") + getCountByStatus("Shipped"), COLOR_PRIMARY_DARK, COLOR_CARD));
        summaryPills.add(createSummaryPill("● Cancelled:", getCountByStatus("Cancelled"), COLOR_RED, COLOR_CARD));

        banner.add(left, BorderLayout.WEST);
        banner.add(summaryPills, BorderLayout.EAST);
        return banner;
    }

    private JPanel createSummaryPill(String prefix, int count, Color fgColor, Color bgColor) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, bgColor, COLOR_BORDER);
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

        JLabel l = new JLabel(prefix + " " + count);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(fgColor);
        p.add(l);
        return p;
    }

    private JLabel createBreadcrumbLink(String text, Runnable action) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(COLOR_PRIMARY_DARK);
        l.setCursor(new Cursor(Cursor.HAND_CURSOR));
        l.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
        return l;
    }

    // ==========================================
    // 4. SEARCH & FILTER TOOLBAR
    // ==========================================
    private JPanel createFilterToolbar() {
        UIUtils.RoundedPanel panel = new UIUtils.RoundedPanel(12, COLOR_CARD, COLOR_BORDER);
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 48));

        UIUtils.RoundedPanel searchBox = new UIUtils.RoundedPanel(8, COLOR_INPUT_BG, COLOR_BORDER);
        searchBox.setPreferredSize(new Dimension(340, 32));
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 10, 0, 10));

        JLabel sIcon = new JLabel(FontIcon.of(Feather.SEARCH, 13, COLOR_SECONDARY));
        txtSearch = new JTextField();
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearch.setToolTipText("Search by order ID or product name...");

        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(txtSearch, BorderLayout.CENTER);

        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightControls.setOpaque(false);

        String[] statuses = {"All Orders", "Processing", "Shipped", "Delivered", "Cancelled"};
        cmbStatus = new JComboBox<>(statuses);
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cmbStatus.setPreferredSize(new Dimension(120, 30));
        cmbStatus.setBackground(Color.WHITE);

        String[] sorts = {"Newest First", "Oldest First"};
        cmbSort = new JComboBox<>(sorts);
        cmbSort.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cmbSort.setPreferredSize(new Dimension(110, 30));
        cmbSort.setBackground(Color.WHITE);

        JButton btnApply = new JButton("Apply Filters", FontIcon.of(Feather.FILTER, 11, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_PRIMARY_DARK.darker() : COLOR_PRIMARY_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnApply.setForeground(Color.WHITE);
        btnApply.setContentAreaFilled(false);
        btnApply.setBorderPainted(false);
        btnApply.setFocusPainted(false);
        btnApply.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnApply.setPreferredSize(new Dimension(115, 30));
        btnApply.addActionListener(e -> applyFilters());

        JButton btnClear = new JButton("Clear Filters", FontIcon.of(Feather.REFRESH_CW, 11, COLOR_SECONDARY));
        btnClear.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnClear.setForeground(COLOR_SECONDARY);
        btnClear.setContentAreaFilled(false);
        btnClear.setBorderPainted(false);
        btnClear.setFocusPainted(false);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.addActionListener(e -> resetFilters());

        rightControls.add(cmbStatus);
        rightControls.add(cmbSort);
        rightControls.add(btnApply);
        rightControls.add(btnClear);

        panel.add(searchBox, BorderLayout.WEST);
        panel.add(rightControls, BorderLayout.EAST);
        return panel;
    }

    // ==========================================
    // 5. FILTERING LOGIC
    // ==========================================
    private void applyFilters() {
        String keyword = txtSearch != null ? txtSearch.getText().trim().toLowerCase() : "";
        String statusFilter = cmbStatus != null ? (String) cmbStatus.getSelectedItem() : "All Orders";
        String sortOption = cmbSort != null ? (String) cmbSort.getSelectedItem() : "Newest First";

        displayedOrders.clear();
        displayedOrders.addAll(allOrders.stream()
                .filter(order -> {
                    if (keyword.isEmpty()) return true;
                    boolean matchId = order.orderId.toLowerCase().contains(keyword);
                    boolean matchItem = order.items.stream().anyMatch(it -> it.name.toLowerCase().contains(keyword));
                    return matchId || matchItem;
                })
                .filter(order -> {
                    if (statusFilter == null || statusFilter.equals("All Orders")) return true;
                    return order.status.equalsIgnoreCase(statusFilter);
                })
                .sorted((o1, o2) -> {
                    if ("Oldest First".equals(sortOption)) {
                        return o1.orderDate.compareTo(o2.orderDate);
                    }
                    return o2.orderDate.compareTo(o1.orderDate);
                })
                .collect(Collectors.toList()));

        renderOrdersList();
    }

    private void resetFilters() {
        if (txtSearch != null) txtSearch.setText("");
        if (cmbStatus != null) cmbStatus.setSelectedIndex(0);
        if (cmbSort != null) cmbSort.setSelectedIndex(0);
        applyFilters();
    }

    private void renderOrdersList() {
        ordersContainer.removeAll();

        if (displayedOrders.isEmpty()) {
            ordersContainer.setVisible(false);
            emptyStatePanel.setVisible(true);
        } else {
            ordersContainer.setVisible(true);
            emptyStatePanel.setVisible(false);

            for (OrderRecord order : displayedOrders) {
                ordersContainer.add(createOrderCard(order));
                ordersContainer.add(Box.createVerticalStrut(12));
            }
        }

        if (lblFooterCount != null) {
            lblFooterCount.setText("Showing " + displayedOrders.size() + " of " + allOrders.size() + " recorded customer orders");
        }

        ordersContainer.revalidate();
        ordersContainer.repaint();
    }

    // ==========================================
    // 6. ORDER CARD
    // ==========================================
    private JPanel createOrderCard(OrderRecord order) {
        Color statusColor = getStatusColor(order.status);
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(14, 18, 14, 18));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 160));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JPanel leftMeta = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftMeta.setOpaque(false);

        JLabel lblId = new JLabel("ORDER ID: " + order.orderId);
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblId.setForeground(COLOR_TEXT_MAIN);

        JLabel btnCopy = new JLabel(FontIcon.of(Feather.COPY, 12, COLOR_SECONDARY));
        btnCopy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCopy.setToolTipText("Copy Order ID");
        btnCopy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(order.orderId), null);
                JOptionPane.showMessageDialog(MyOrdersPage.this, "Copied Order ID: " + order.orderId, "Clipboard", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel dot = new JLabel("•");
        dot.setForeground(COLOR_SECONDARY);

        JLabel lblDate = new JLabel("Placed on " + order.dateDisplay, FontIcon.of(Feather.CALENDAR, 12, COLOR_SECONDARY), SwingConstants.LEFT);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDate.setForeground(COLOR_SECONDARY);

        leftMeta.add(lblId);
        leftMeta.add(btnCopy);
        leftMeta.add(dot);
        leftMeta.add(lblDate);

        UIUtils.RoundedPanel statusPill = new UIUtils.RoundedPanel(12, getStatusBgColor(order.status), statusColor);
        statusPill.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 3));
        JLabel lblStatus = new JLabel("●  " + (order.statusNote != null ? order.statusNote : order.status));
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblStatus.setForeground(statusColor);
        statusPill.add(lblStatus);

        topRow.add(leftMeta, BorderLayout.WEST);
        topRow.add(statusPill, BorderLayout.EAST);

        JPanel centerRow = new JPanel(new BorderLayout(16, 0));
        centerRow.setOpaque(false);
        centerRow.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel itemsStack = new JPanel();
        itemsStack.setLayout(new BoxLayout(itemsStack, BoxLayout.Y_AXIS));
        itemsStack.setOpaque(false);

        for (OrderItem it : order.items) {
            itemsStack.add(createItemRow(it));
            itemsStack.add(Box.createVerticalStrut(6));
        }

        JPanel rightInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightInfo.setOpaque(false);

        JPanel priceBox = new JPanel();
        priceBox.setLayout(new BoxLayout(priceBox, BoxLayout.Y_AXIS));
        priceBox.setOpaque(false);

        JLabel lTotal = new JLabel("TOTAL AMOUNT");
        lTotal.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lTotal.setForeground(COLOR_SECONDARY);

        JLabel pTotal = new JLabel("₹" + String.format("%,d", order.totalAmount));
        pTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pTotal.setForeground(COLOR_TEXT_MAIN);

        JLabel lPay = new JLabel(order.paymentMethod, FontIcon.of(Feather.CREDIT_CARD, 10, COLOR_SECONDARY), SwingConstants.LEFT);
        lPay.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lPay.setForeground(COLOR_SECONDARY);

        priceBox.add(lTotal);
        priceBox.add(pTotal);
        priceBox.add(lPay);

        JPanel btnsGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        btnsGroup.setOpaque(false);

        JButton btnViewDetails = new JButton("View Details");
        btnViewDetails.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnViewDetails.setForeground(COLOR_TEXT_MAIN);
        btnViewDetails.setBackground(COLOR_INPUT_BG);
        btnViewDetails.setFocusPainted(false);
        btnViewDetails.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewDetails.addActionListener(e -> showOrderDetailsDialog(order));

        btnsGroup.add(createContextActionButton(order));
        btnsGroup.add(btnViewDetails);

        rightInfo.add(priceBox);
        rightInfo.add(btnsGroup);

        centerRow.add(itemsStack, BorderLayout.CENTER);
        centerRow.add(rightInfo, BorderLayout.EAST);

        card.add(topRow, BorderLayout.NORTH);
        card.add(centerRow, BorderLayout.CENTER);

        return card;
    }

    private JPanel createItemRow(OrderItem item) {
        JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        r.setOpaque(false);

        UIUtils.RoundedPanel icBox = new UIUtils.RoundedPanel(8, COLOR_INPUT_BG, COLOR_BORDER);
        icBox.setPreferredSize(new Dimension(32, 32));
        icBox.setLayout(new GridBagLayout());
        icBox.add(new JLabel(FontIcon.of(item.icon, 14, COLOR_PRIMARY_DARK)));

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);

        JLabel nameLbl = new JLabel(item.name);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLbl.setForeground(COLOR_TEXT_MAIN);

        JLabel subLbl = new JLabel("×" + item.quantity + "  •  " + item.details);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        subLbl.setForeground(COLOR_SECONDARY);

        txt.add(nameLbl);
        txt.add(subLbl);

        r.add(icBox);
        r.add(txt);
        return r;
    }

    private JButton createContextActionButton(OrderRecord order) {
        JButton btn;
        if ("Processing".equalsIgnoreCase(order.status) || "Shipped".equalsIgnoreCase(order.status)) {
            btn = new JButton("Track Order", FontIcon.of(Feather.MAP_PIN, 11, Color.WHITE)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(COLOR_PRIMARY_DARK);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };
            btn.setForeground(Color.WHITE);
            btn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                    "Live Telemetry: Order #" + order.orderId + " is on schedule with Courier BlueDart.",
                    "Courier Telemetry", JOptionPane.INFORMATION_MESSAGE));
        } else if ("Cancelled".equalsIgnoreCase(order.status)) {
            btn = new JButton("Reorder", FontIcon.of(Feather.REFRESH_CW, 11, COLOR_TEXT_MAIN));
            btn.setBackground(COLOR_LIGHT_BLUE);
            btn.addActionListener(e -> {
                new ProductListingPage("All Categories").setVisible(true);
                dispose();
            });
        } else {
            btn = new JButton("Buy Again", FontIcon.of(Feather.REPEAT, 11, COLOR_PRIMARY_DARK));
            btn.setBackground(COLOR_LIGHT_BLUE);
            btn.addActionListener(e -> {
                new ProductListingPage("All Categories").setVisible(true);
                dispose();
            });
        }

        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        return btn;
    }

    private void showOrderDetailsDialog(OrderRecord order) {
        JDialog dialog = new JDialog(this, "Order Details - " + order.orderId, true);
        dialog.setSize(520, 480);
        dialog.setLocationRelativeTo(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(COLOR_CARD);
        p.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Order #" + order.orderId);
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel dt = new JLabel("Placed on: " + order.dateDisplay + " | Status: " + order.status);
        dt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dt.setForeground(COLOR_SECONDARY);

        p.add(title);
        p.add(dt);
        p.add(Box.createVerticalStrut(14));

        JPanel prodBox = new JPanel();
        prodBox.setLayout(new BoxLayout(prodBox, BoxLayout.Y_AXIS));
        prodBox.setOpaque(false);
        prodBox.setBorder(BorderFactory.createTitledBorder("Items"));

        for (OrderItem it : order.items) {
            JLabel itm = new JLabel("• " + it.name + " (Qty: " + it.quantity + ") - " + it.details);
            itm.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            prodBox.add(itm);
            prodBox.add(Box.createVerticalStrut(4));
        }

        p.add(prodBox);
        p.add(Box.createVerticalStrut(12));

        p.add(new JLabel("Payment Mode: " + order.paymentMethod));
        p.add(Box.createVerticalStrut(4));
        p.add(new JLabel("Total Paid/Payable: ₹" + String.format("%,d", order.totalAmount)));
        p.add(Box.createVerticalStrut(4));
        p.add(new JLabel("Delivery Address: Arjun Mehta, Flat 402, Royal Palms, Indiranagar, Bengaluru - 560038"));
        p.add(Box.createVerticalStrut(18));

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionRow.setOpaque(false);

        if ("Processing".equalsIgnoreCase(order.status)) {
            JButton btnCancel = new JButton("Cancel Order");
            btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnCancel.setForeground(COLOR_RED);
            btnCancel.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "Are you sure you want to cancel order " + order.orderId + "?\n(This is a local UI state cancellation demo)",
                        "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    order.status = "Cancelled";
                    dialog.dispose();
                    updateHeaderCounts();
                    applyFilters();
                    JOptionPane.showMessageDialog(this, "Order " + order.orderId + " has been marked as Cancelled.", "Order Cancelled", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            actionRow.add(btnCancel);
        }

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnClose.addActionListener(e -> dialog.dispose());
        actionRow.add(btnClose);

        p.add(actionRow);
        dialog.setContentPane(p);
        dialog.setVisible(true);
    }

    private void updateHeaderCounts() {
        if (lblSummaryTotal != null) lblSummaryTotal.setText(allOrders.size() + " Total");
    }

    private JPanel createEmptyStatePanel() {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(14, COLOR_CARD, COLOR_BORDER);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(50, 30, 50, 30));
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 260));

        JLabel ic = new JLabel(FontIcon.of(Feather.INBOX, 46, COLOR_PRIMARY));
        ic.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel h1 = new JLabel("No orders found");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        h1.setForeground(COLOR_TEXT_MAIN);
        h1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel desc = new JLabel("We couldn't find any orders matching your search or filters.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(COLOR_SECONDARY);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.setOpaque(false);

        JButton btnClr = new JButton("Clear Filters");
        btnClr.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClr.addActionListener(e -> resetFilters());

        JButton btnShop = new JButton("Continue Shopping");
        btnShop.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnShop.setForeground(Color.WHITE);
        btnShop.setBackground(COLOR_PRIMARY_DARK);
        btnShop.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

        btnRow.add(btnClr);
        btnRow.add(btnShop);

        p.add(ic);
        p.add(Box.createVerticalStrut(10));
        p.add(h1);
        p.add(Box.createVerticalStrut(4));
        p.add(desc);
        p.add(Box.createVerticalStrut(16));
        p.add(btnRow);

        return p;
    }

    private JPanel createFooterControls() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setMaximumSize(new Dimension(Short.MAX_VALUE, 36));

        lblFooterCount = new JLabel("Showing " + displayedOrders.size() + " of " + allOrders.size() + " recorded customer orders");
        lblFooterCount.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblFooterCount.setForeground(COLOR_SECONDARY);

        JPanel pages = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        pages.setOpaque(false);

        JButton btnPrev = new JButton("← Previous");
        btnPrev.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnPrev.setEnabled(false);

        JButton btnP1 = new JButton("1");
        btnP1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnP1.setForeground(Color.WHITE);
        btnP1.setBackground(COLOR_PRIMARY_DARK);

        JButton btnP2 = new JButton("2");
        btnP2.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        JButton btnNext = new JButton("Next →");
        btnNext.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        pages.add(btnPrev);
        pages.add(btnP1);
        pages.add(btnP2);
        pages.add(btnNext);

        footer.add(lblFooterCount, BorderLayout.WEST);
        footer.add(pages, BorderLayout.EAST);
        return footer;
    }

    private Color getStatusColor(String status) {
        if ("Delivered".equalsIgnoreCase(status)) return COLOR_GREEN;
        if ("Cancelled".equalsIgnoreCase(status)) return COLOR_RED;
        return COLOR_PRIMARY_DARK;
    }

    private Color getStatusBgColor(String status) {
        if ("Delivered".equalsIgnoreCase(status)) return new Color(0xDC, 0xFC, 0xE7);
        if ("Cancelled".equalsIgnoreCase(status)) return new Color(0xFE, 0xE2, 0xE2);
        return COLOR_LIGHT_BLUE;
    }

    private int getCountByStatus(String status) {
        return (int) allOrders.stream().filter(o -> o.status.equalsIgnoreCase(status)).count();
    }

    private void showPlaceholder(String name) {
        JOptionPane.showMessageDialog(this,
                name + " screen will be connected cleanly in the next step.",
                "ShopSphere Navigation", JOptionPane.INFORMATION_MESSAGE);
    }
}
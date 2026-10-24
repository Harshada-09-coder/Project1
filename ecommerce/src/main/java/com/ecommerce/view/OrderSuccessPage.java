package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OrderSuccessPage extends JFrame {

    // ShopSphere Color Palette
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

    // Sample Order Metadata
    private final String orderId = "SS-20261020-1042";

    public OrderSuccessPage() {
        setTitle("ShopSphere - Order Confirmation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int targetW = Math.min(1380, (int) (scr.width * 0.94));
        int targetH = Math.min(900, (int) (scr.height * 0.92));
        setSize(targetW, targetH);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BACKGROUND);

        // Header
        root.add(createHeader(), BorderLayout.NORTH);

        // Center split layout (Sidebar + Main Confirmation Body)
        JPanel mainLayout = new JPanel(new BorderLayout(16, 0));
        mainLayout.setOpaque(false);
        mainLayout.setBorder(new EmptyBorder(10, 20, 12, 20));

        mainLayout.add(createSidebar(), BorderLayout.WEST);
        mainLayout.add(createScrollableBody(), BorderLayout.CENTER);

        root.add(mainLayout, BorderLayout.CENTER);
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);
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

        // Center Search Box
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

        // Right Quick Actions
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnWishlist = createHeaderButton(Feather.HEART, "Wishlist (0)", () -> showPlaceholder("Wishlist"));
        JButton btnCart     = createHeaderButton(Feather.SHOPPING_BAG, "Cart (0)", () -> {
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
        sidebar.add(createSidebarBtn("My Orders", Feather.ARCHIVE, false, () -> {
            new MyOrdersPage().setVisible(true);
            dispose();
        }));
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

        content.add(createBreadcrumbRow());
        content.add(Box.createVerticalStrut(10));
        content.add(createHeroSuccessCard());
        content.add(Box.createVerticalStrut(12));
        content.add(createFulfillmentTrackerCard());
        content.add(Box.createVerticalStrut(14));

        JPanel columnsRow = new JPanel(new GridBagLayout());
        columnsRow.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Column
        gbc.gridx = 0;
        gbc.weightx = 0.62;
        gbc.insets = new Insets(0, 0, 0, 16);
        columnsRow.add(createOrderConfirmationDetailsCard(), gbc);

        // Right Column
        gbc.gridx = 1;
        gbc.weightx = 0.38;
        gbc.insets = new Insets(0, 0, 0, 0);
        columnsRow.add(createOrderSummaryCard(), gbc);

        content.add(columnsRow);
        content.add(Box.createVerticalStrut(16));

        content.add(createBottomActionBar());
        content.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createBreadcrumbRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));

        JPanel crumbs = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        crumbs.setOpaque(false);

        JLabel home = createBreadcrumbLink("Home", () -> {
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });
        JLabel chk = createBreadcrumbLink("Checkout", () -> {
            new CheckoutPage().setVisible(true);
            dispose();
        });
        JLabel curr = new JLabel("Order Confirmation");
        curr.setFont(new Font("Segoe UI", Font.BOLD, 11));
        curr.setForeground(COLOR_TEXT_MAIN);

        crumbs.add(home);
        crumbs.add(new JLabel("/"));
        crumbs.add(chk);
        crumbs.add(new JLabel("/"));
        crumbs.add(curr);

        UIUtils.RoundedPanel syncPill = new UIUtils.RoundedPanel(12, COLOR_INPUT_BG, COLOR_BORDER);
        syncPill.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 3));
        JLabel syncText = new JLabel("● SESSION RECORDED • SYNC ACK #4892");
        syncText.setFont(new Font("Segoe UI", Font.BOLD, 10));
        syncText.setForeground(COLOR_PRIMARY_DARK);
        syncPill.add(syncText);

        row.add(crumbs, BorderLayout.WEST);
        row.add(syncPill, BorderLayout.EAST);
        return row;
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

    private JPanel createHeroSuccessCard() {
        UIUtils.RoundedPanel banner = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        banner.setLayout(new BorderLayout(16, 0));
        banner.setBorder(new EmptyBorder(18, 22, 18, 22));
        banner.setMaximumSize(new Dimension(Short.MAX_VALUE, 115));

        UIUtils.RoundedPanel avatarBox = new UIUtils.RoundedPanel(16, COLOR_LIGHT_BLUE);
        avatarBox.setPreferredSize(new Dimension(54, 54));
        avatarBox.setLayout(new GridBagLayout());
        avatarBox.add(new JLabel(FontIcon.of(Feather.CHECK_CIRCLE, 28, COLOR_PRIMARY_DARK)));

        JPanel textGroup = new JPanel();
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
        textGroup.setOpaque(false);

        JPanel tagRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tagRow.setOpaque(false);

        JLabel authTag = new JLabel(" TRANSACTION AUTHORIZED ");
        authTag.setFont(new Font("Segoe UI", Font.BOLD, 9));
        authTag.setForeground(COLOR_PRIMARY_DARK);
        authTag.setOpaque(true);
        authTag.setBackground(COLOR_INPUT_BG);

        JLabel receiptTag = new JLabel("Instant Receipt #SS-9921");
        receiptTag.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        receiptTag.setForeground(COLOR_SECONDARY);

        tagRow.add(authTag);
        tagRow.add(receiptTag);

        JLabel h1 = new JLabel("Order Placed Successfully!");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        h1.setForeground(COLOR_TEXT_MAIN);

        JLabel desc = new JLabel("<html><body style='color:#717E93; font-size:11px;'>"
                + "Thank you for shopping with ShopSphere. Your order has been securely acknowledged by our fulfillment cluster and is now queued for immediate dispatch."
                + "</body></html>");

        textGroup.add(tagRow);
        textGroup.add(Box.createVerticalStrut(3));
        textGroup.add(h1);
        textGroup.add(Box.createVerticalStrut(2));
        textGroup.add(desc);

        JPanel rightArt = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(COLOR_INPUT_BG);
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                g2.setColor(new Color(0xB8, 0xD4, 0xEB));
                g2.fillRoundRect(w / 2 - 30, h / 2 - 10, 36, 40, 6, 6);
                g2.setColor(new Color(0x94, 0xC0, 0xE4));
                g2.fillRoundRect(w / 2 + 5, h / 2, 34, 30, 6, 6);
                g2.dispose();
            }
        };
        rightArt.setPreferredSize(new Dimension(110, 75));
        rightArt.setOpaque(false);

        banner.add(avatarBox, BorderLayout.WEST);
        banner.add(textGroup, BorderLayout.CENTER);
        banner.add(rightArt, BorderLayout.EAST);
        return banner;
    }

    private JPanel createFulfillmentTrackerCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new GridLayout(1, 4, 10, 0));
        card.setBorder(new EmptyBorder(12, 16, 12, 16));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));

        card.add(createStepBlock(Feather.CHECK_CIRCLE, "Order Placed", "Today, 10:42 AM", true, false));
        card.add(createStepBlock(Feather.REFRESH_CW, "Processing", "Allocating Inventory", false, true));
        card.add(createStepBlock(Feather.TRUCK, "Shipped", "Est. Tomorrow", false, false));
        card.add(createStepBlock(Feather.PACKAGE, "Delivered", "Est. 23 Oct", false, false));

        return card;
    }

    private JPanel createStepBlock(Feather icon, String title, String sub, boolean isDone, boolean isCurrent) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
        p.setOpaque(false);

        Color circleBg = isDone ? COLOR_PRIMARY_DARK : (isCurrent ? COLOR_LIGHT_BLUE : COLOR_INPUT_BG);
        Color iconCol = isDone ? Color.WHITE : (isCurrent ? COLOR_PRIMARY_DARK : COLOR_SECONDARY);

        UIUtils.RoundedPanel circle = new UIUtils.RoundedPanel(18, circleBg);
        circle.setPreferredSize(new Dimension(28, 28));
        circle.setLayout(new GridBagLayout());
        circle.add(new JLabel(FontIcon.of(icon, 13, iconCol)));

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 11));
        t.setForeground(COLOR_TEXT_MAIN);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(COLOR_SECONDARY);

        text.add(t);
        text.add(s);

        p.add(circle);
        p.add(text);
        return p;
    }

    private JPanel createOrderConfirmationDetailsCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setOpaque(false);
        cardHeader.setMaximumSize(new Dimension(Short.MAX_VALUE, 26));

        JLabel title = new JLabel("Order Confirmation", FontIcon.of(Feather.FILE_TEXT, 14, COLOR_PRIMARY_DARK), SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);

        UIUtils.RoundedPanel copyBadge = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        copyBadge.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 3));
        copyBadge.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblId = new JLabel("ORDER ID: " + orderId);
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblId.setForeground(COLOR_PRIMARY_DARK);

        JLabel copyIc = new JLabel(FontIcon.of(Feather.COPY, 11, COLOR_SECONDARY));
        copyBadge.add(lblId);
        copyBadge.add(copyIc);

        copyBadge.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(orderId), null);
                JOptionPane.showMessageDialog(OrderSuccessPage.this, "Order ID copied to clipboard: " + orderId, "Copied", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        cardHeader.add(title, BorderLayout.WEST);
        cardHeader.add(copyBadge, BorderLayout.EAST);

        JPanel r1 = new JPanel(new GridLayout(1, 2, 12, 0));
        r1.setOpaque(false);
        r1.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));
        r1.add(createInfoBox("PLACEMENT DATE", "20 October 2026, 10:42 AM", null));
        r1.add(createInfoBox("WORKFLOW STATUS", "● Confirmed & Queued", COLOR_GREEN));

        JPanel r2 = new JPanel(new GridLayout(1, 2, 12, 0));
        r2.setOpaque(false);
        r2.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));
        r2.add(createInfoBox("PAYMENT PROTOCOL", "Cash on Delivery (Post-paid)", null));
        r2.add(createAddressBox("DELIVERY CONSIGNEE & ADDRESS", "Arjun Mehta (Default Node)\nFlat 402, Royal Palms Residency,\n14th Main Road, Indiranagar, Bengaluru - 560038"));

        JPanel r3 = new JPanel(new GridLayout(1, 2, 12, 0));
        r3.setOpaque(false);
        r3.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));
        r3.add(createInfoBox("FULFILLMENT ESTIMATE", "Expected dispatch within 24 hours", null));
        r3.add(createInfoBox("DISPATCH COURIER", "BlueDart Express (Track ID Assigned)", null));

        UIUtils.RoundedPanel noticePill = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        noticePill.setLayout(new BorderLayout());
        noticePill.setBorder(new EmptyBorder(8, 12, 8, 12));
        noticePill.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));

        JLabel lNotice = new JLabel("Confirmation summary and invoice token dispatched to arjun.mehta@enterprise.org",
                FontIcon.of(Feather.MAIL, 13, COLOR_SECONDARY), SwingConstants.LEFT);
        lNotice.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lNotice.setForeground(COLOR_SECONDARY);

        JButton btnResend = new JButton("Resend");
        btnResend.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnResend.setForeground(COLOR_PRIMARY_DARK);
        btnResend.setContentAreaFilled(false);
        btnResend.setBorderPainted(false);
        btnResend.setFocusPainted(false);
        btnResend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnResend.addActionListener(e -> JOptionPane.showMessageDialog(this, "Order confirmation resent to registered email address.", "Sent", JOptionPane.INFORMATION_MESSAGE));

        noticePill.add(lNotice, BorderLayout.WEST);
        noticePill.add(btnResend, BorderLayout.EAST);

        card.add(cardHeader);
        card.add(Box.createVerticalStrut(10));
        card.add(r1);
        card.add(Box.createVerticalStrut(8));
        card.add(r2);
        card.add(Box.createVerticalStrut(8));
        card.add(r3);
        card.add(Box.createVerticalStrut(10));
        card.add(noticePill);

        return card;
    }

    private JPanel createInfoBox(String tag, String value, Color overrideValColor) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel t = new JLabel(tag);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(COLOR_SECONDARY);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 11));
        v.setForeground(overrideValColor != null ? overrideValColor : COLOR_TEXT_MAIN);

        p.add(t);
        p.add(Box.createVerticalStrut(4));
        p.add(v);
        return p;
    }

    private JPanel createAddressBox(String tag, String multilineAddr) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel t = new JLabel(tag);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(COLOR_SECONDARY);

        JLabel v = new JLabel("<html><body style='line-height:1.3; color:#1F2D42; font-size:10px;'>"
                + multilineAddr.replace("\n", "<br>") + "</body></html>");

        p.add(t);
        p.add(Box.createVerticalStrut(3));
        p.add(v);
        return p;
    }

    private JPanel createOrderSummaryCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel h = new JPanel(new BorderLayout());
        h.setOpaque(false);
        h.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));

        JLabel title = new JLabel("Order Summary");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel countBadge = new JLabel("3 items confirmed");
        countBadge.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        countBadge.setForeground(COLOR_SECONDARY);

        h.add(title, BorderLayout.WEST);
        h.add(countBadge, BorderLayout.EAST);

        JPanel items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        items.setOpaque(false);
        items.setBorder(new EmptyBorder(8, 0, 8, 0));

        items.add(createSummaryRowItem("Wireless Headphones", "Midnight Black", 1, 1499, Feather.HEADPHONES));
        items.add(Box.createVerticalStrut(8));
        items.add(createSummaryRowItem("Casual Sneakers", "Slate Gray", 1, 1799, Feather.TAG));
        items.add(Box.createVerticalStrut(8));
        items.add(createSummaryRowItem("Travel Backpack", "Dark Navy", 2, 999, Feather.PACKAGE));

        JLabel lblSub = new JLabel("₹5,296");
        JLabel lblShip = new JLabel("Free");
        lblShip.setForeground(COLOR_GREEN);
        JLabel lblDis = new JLabel("-₹0");
        JLabel lblTot = new JLabel("₹5,296");
        lblTot.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTot.setForeground(COLOR_TEXT_MAIN);

        card.add(h);
        card.add(Box.createVerticalStrut(4));
        card.add(items);

        JSeparator s1 = new JSeparator();
        s1.setForeground(COLOR_BORDER);
        card.add(s1);
        card.add(Box.createVerticalStrut(8));

        card.add(createPriceRow("Items Subtotal", lblSub));
        card.add(Box.createVerticalStrut(4));
        card.add(createPriceRow("Standard Logistics & Shipping", lblShip));
        card.add(Box.createVerticalStrut(4));
        card.add(createPriceRow("Special Promotional Coupon", lblDis));
        card.add(Box.createVerticalStrut(8));

        JSeparator s2 = new JSeparator();
        s2.setForeground(COLOR_BORDER);
        card.add(s2);
        card.add(Box.createVerticalStrut(8));

        card.add(createPriceRow("Total Payable (Post-paid)", lblTot));
        card.add(Box.createVerticalStrut(14));

        UIUtils.RoundedPanel buyerPill = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        buyerPill.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 6));
        buyerPill.add(new JLabel(FontIcon.of(Feather.SHIELD, 13, COLOR_GREEN)));
        JLabel txtProtect = new JLabel("ShopSphere Buyer Protection Guaranteed");
        txtProtect.setFont(new Font("Segoe UI", Font.BOLD, 10));
        txtProtect.setForeground(COLOR_SECONDARY);
        buyerPill.add(txtProtect);

        card.add(buyerPill);
        return card;
    }

    private JPanel createSummaryRowItem(String name, String variant, int qty, int unitPrice, Feather icon) {
        JPanel r = new JPanel(new BorderLayout(8, 0));
        r.setOpaque(false);

        UIUtils.RoundedPanel icBox = new UIUtils.RoundedPanel(8, COLOR_INPUT_BG);
        icBox.setPreferredSize(new Dimension(28, 28));
        icBox.setLayout(new GridBagLayout());
        icBox.add(new JLabel(FontIcon.of(icon, 13, COLOR_PRIMARY_DARK)));

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);

        JLabel n = new JLabel(name);
        n.setFont(new Font("Segoe UI", Font.BOLD, 11));
        n.setForeground(COLOR_TEXT_MAIN);

        JLabel q = new JLabel("Qty: " + qty + " • " + variant);
        q.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        q.setForeground(COLOR_SECONDARY);

        txt.add(n);
        txt.add(q);

        JLabel price = new JLabel("₹" + String.format("%,d", qty * unitPrice));
        price.setFont(new Font("Segoe UI", Font.BOLD, 12));
        price.setForeground(COLOR_TEXT_MAIN);

        r.add(icBox, BorderLayout.WEST);
        r.add(txt, BorderLayout.CENTER);
        r.add(price, BorderLayout.EAST);
        return r;
    }

    private JPanel createPriceRow(String label, JLabel val) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(COLOR_SECONDARY);

        val.setFont(val.getFont().deriveFont(11.5f));
        r.add(l, BorderLayout.WEST);
        r.add(val, BorderLayout.EAST);
        return r;
    }

    private JPanel createBottomActionBar() {
        UIUtils.RoundedPanel bar = new UIUtils.RoundedPanel(14, COLOR_CARD, COLOR_BORDER);
        bar.setLayout(new BorderLayout());
        bar.setBorder(new EmptyBorder(12, 18, 12, 18));
        bar.setMaximumSize(new Dimension(Short.MAX_VALUE, 54));

        JPanel leftBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftBtns.setOpaque(false);

        JButton btnViewOrders = new JButton("View My Orders", FontIcon.of(Feather.ARCHIVE, 12, Color.WHITE)) {
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
        btnViewOrders.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnViewOrders.setForeground(Color.WHITE);
        btnViewOrders.setContentAreaFilled(false);
        btnViewOrders.setBorderPainted(false);
        btnViewOrders.setFocusPainted(false);
        btnViewOrders.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewOrders.setPreferredSize(new Dimension(145, 34));
        btnViewOrders.addActionListener(e -> {
            new MyOrdersPage().setVisible(true);
            OrderSuccessPage.this.dispose();
        });

        JButton btnContinueShopping = new JButton("Continue Shopping", FontIcon.of(Feather.SHOPPING_BAG, 12, COLOR_TEXT_MAIN));
        btnContinueShopping.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnContinueShopping.setForeground(COLOR_TEXT_MAIN);
        btnContinueShopping.setBackground(COLOR_INPUT_BG);
        btnContinueShopping.setFocusPainted(false);
        btnContinueShopping.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContinueShopping.setPreferredSize(new Dimension(160, 34));
        btnContinueShopping.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

        leftBtns.add(btnViewOrders);
        leftBtns.add(btnContinueShopping);

        JPanel rightHelp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        rightHelp.setOpaque(false);

        JLabel needHelp = new JLabel("Need help?");
        needHelp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        needHelp.setForeground(COLOR_SECONDARY);

        JLabel contactSupport = new JLabel("Contact Support ↗");
        contactSupport.setFont(new Font("Segoe UI", Font.BOLD, 11));
        contactSupport.setForeground(COLOR_PRIMARY_DARK);
        contactSupport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contactSupport.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(OrderSuccessPage.this,
                        "Support Ticket Gateway: Help desk is available 24/7 at support@shopsphere.enterprise",
                        "Customer Support", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        rightHelp.add(needHelp);
        rightHelp.add(contactSupport);

        bar.add(leftBtns, BorderLayout.WEST);
        bar.add(rightHelp, BorderLayout.EAST);
        return bar;
    }

    private void showPlaceholder(String name) {
        JOptionPane.showMessageDialog(this,
                name + " screen will be connected cleanly in the next step.",
                "ShopSphere Navigation", JOptionPane.INFORMATION_MESSAGE);
    }
}
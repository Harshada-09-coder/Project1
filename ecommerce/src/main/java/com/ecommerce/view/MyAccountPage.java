package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MyAccountPage extends JFrame {

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
    public static final Color COLOR_ORANGE       = new Color(0xF5, 0x9E, 0x0B);

    // Profile In-Memory State
    private String fullName = "Rahul Sharma";
    private String dob = "15 August 2004";
    private String age = "Age 21";
    private String email = "rahul.sharma@example.com";
    private String mobile = "+91 98765 43210";
    private boolean notificationsEnabled = true;

    // Security In-Memory State
    private String maskedPassword = "••••••••••••••••";
    private boolean twoFactorEnabled = true;

    // Address In-Memory Model
    public static class AddressRecord {
        public String title;
        public String recipient;
        public String line1;
        public String line2;
        public String city;
        public String state;
        public String pin;
        public String country;
        public String instructions;
        public String tag;

        public AddressRecord(String title, String recipient, String line1, String line2,
                             String city, String state, String pin, String country,
                             String instructions, String tag) {
            this.title = title;
            this.recipient = recipient;
            this.line1 = line1;
            this.line2 = line2;
            this.city = city;
            this.state = state;
            this.pin = pin;
            this.country = country;
            this.instructions = instructions;
            this.tag = tag;
        }
    }

    private AddressRecord defaultAddress;
    private final List<AddressRecord> additionalAddresses = new ArrayList<>();

    // Dynamic UI References
    private JLabel lblProfileName;
    private JLabel lblProfileEmail;
    private JLabel lblLegalName;
    private JLabel lblDob;
    private JLabel lblEmailVal;
    private JLabel lblMobileVal;
    private JButton btnNotificationToggle;
    private JLabel lbl2faBadge;

    private JPanel defaultAddressContentPanel;
    private JPanel additionalAddressContainer;

    public MyAccountPage() {
        setTitle("ShopSphere - My Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int targetW = Math.min(1380, (int) (scr.width * 0.94));
        int targetH = Math.min(920, (int) (scr.height * 0.92));
        setSize(targetW, targetH);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        initSampleAddresses();

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
    }

    private void initSampleAddresses() {
        defaultAddress = new AddressRecord(
                "Primary Residential",
                "Rahul Sharma",
                "24 Green Park Road, Near Orchid Avenue",
                "Kothrud",
                "Pune",
                "Maharashtra",
                "411001",
                "India",
                "Leave package with apartment security desk if recipient unavailable during daytime.",
                "Default Shipping"
        );

        additionalAddresses.clear();
        additionalAddresses.add(new AddressRecord(
                "Tech Park Office Hub (Work)",
                "Rahul Sharma",
                "Tower B, Cyber City, Magarpatta",
                "Hadapsar",
                "Pune",
                "Maharashtra",
                "411028",
                "India",
                "Deliver between 9 AM and 6 PM to the 4th-floor reception desk.",
                "Work"
        ));
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
        JButton btnCart     = createHeaderButton(Feather.SHOPPING_BAG, "Cart (3)", () -> {
            new MyCartPage().setVisible(true);
            dispose();
        });
        JButton btnAccount  = createHeaderButton(Feather.USER, "My Account", () -> {});

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

        // Highlight My Account as ACTIVE
        sidebar.add(createSidebarBtn("My Account", Feather.USER, true, () -> {}));

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

        // Breadcrumb + Heading
        content.add(createHeaderBanner());
        content.add(Box.createVerticalStrut(10));

        // Customer Profile Overview Card
        content.add(createProfileOverviewCard());
        content.add(Box.createVerticalStrut(14));

        // Two-Column Grid: Left (Personal Info + Security) | Right (Addresses)
        JPanel columnsRow = new JPanel(new GridBagLayout());
        columnsRow.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Column (52%)
        gbc.gridx = 0;
        gbc.weightx = 0.52;
        gbc.insets = new Insets(0, 0, 0, 16);
        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setOpaque(false);
        leftCol.add(createPersonalInfoCard());
        leftCol.add(Box.createVerticalStrut(14));
        leftCol.add(createAccountSecurityCard());
        columnsRow.add(leftCol, gbc);

        // Right Column (48%)
        gbc.gridx = 1;
        gbc.weightx = 0.48;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(false);
        rightCol.add(createDefaultAddressCard());
        rightCol.add(Box.createVerticalStrut(14));
        rightCol.add(createAdditionalAddressesCard());
        columnsRow.add(rightCol, gbc);

        content.add(columnsRow);
        content.add(Box.createVerticalStrut(16));

        // Full-Width Bottom: Account Activity Summary
        content.add(createAccountActivitySummaryCard());
        content.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createHeaderBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setOpaque(false);
        banner.setMaximumSize(new Dimension(Short.MAX_VALUE, 48));

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
        JLabel current = new JLabel("My Account");
        current.setFont(new Font("Segoe UI", Font.BOLD, 11));
        current.setForeground(COLOR_TEXT_MAIN);

        crumbs.add(home);
        crumbs.add(new JLabel("/"));
        crumbs.add(shop);
        crumbs.add(new JLabel("/"));
        crumbs.add(current);

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleRow.setOpaque(false);

        JLabel title = new JLabel("My Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("Manage your personal identity, contact credentials, and regional delivery preferences.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_SECONDARY);

        titleRow.add(title);
        titleRow.add(Box.createHorizontalStrut(6));
        titleRow.add(sub);

        left.add(crumbs);
        left.add(Box.createVerticalStrut(2));
        left.add(titleRow);

        // Right Sync Pill
        UIUtils.RoundedPanel syncPill = new UIUtils.RoundedPanel(12, COLOR_INPUT_BG, COLOR_BORDER);
        syncPill.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 3));
        JLabel syncText = new JLabel("● Synchronized • Node #IN-MH-02");
        syncText.setFont(new Font("Segoe UI", Font.BOLD, 10));
        syncText.setForeground(COLOR_PRIMARY_DARK);
        syncPill.add(syncText);

        banner.add(left, BorderLayout.WEST);
        banner.add(syncPill, BorderLayout.EAST);
        return banner;
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
    // 4. PROFILE OVERVIEW CARD
    // ==========================================
    private JPanel createProfileOverviewCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BorderLayout(18, 0));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 95));

        // Left Avatar "RS"
        JPanel avatarWrapper = new JPanel(null);
        avatarWrapper.setPreferredSize(new Dimension(60, 60));
        avatarWrapper.setOpaque(false);

        UIUtils.RoundedPanel avatarCircle = new UIUtils.RoundedPanel(60, COLOR_PRIMARY_DARK);
        avatarCircle.setBounds(0, 0, 56, 56);
        avatarCircle.setLayout(new GridBagLayout());
        JLabel initials = new JLabel("RS");
        initials.setFont(new Font("Segoe UI", Font.BOLD, 20));
        initials.setForeground(Color.WHITE);
        avatarCircle.add(initials);

        // Green Online Dot
        UIUtils.RoundedPanel onlineDot = new UIUtils.RoundedPanel(14, COLOR_GREEN, Color.WHITE);
        onlineDot.setBounds(40, 40, 14, 14);

        avatarWrapper.add(onlineDot);
        avatarWrapper.add(avatarCircle);

        // Center Details
        JPanel centerInfo = new JPanel();
        centerInfo.setLayout(new BoxLayout(centerInfo, BoxLayout.Y_AXIS));
        centerInfo.setOpaque(false);

        JPanel nameRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        nameRow.setOpaque(false);

        lblProfileName = new JLabel(fullName);
        lblProfileName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblProfileName.setForeground(COLOR_TEXT_MAIN);

        nameRow.add(lblProfileName);
        nameRow.add(createMiniTag("✔ Active Customer", COLOR_PRIMARY_DARK, COLOR_INPUT_BG));
        nameRow.add(createMiniTag("🏆 Tier: Tier-1 Prime", new Color(0x63, 0x66, 0xF1), COLOR_LIGHT_BLUE));

        JPanel subRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        subRow.setOpaque(false);

        lblProfileEmail = new JLabel(email, FontIcon.of(Feather.MAIL, 12, COLOR_SECONDARY), SwingConstants.LEFT);
        lblProfileEmail.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblProfileEmail.setForeground(COLOR_SECONDARY);

        JLabel lblMember = new JLabel("Member since: January 2026", FontIcon.of(Feather.CALENDAR, 12, COLOR_SECONDARY), SwingConstants.LEFT);
        lblMember.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMember.setForeground(COLOR_SECONDARY);

        JLabel lblHub = new JLabel("Pune Regional Hub", FontIcon.of(Feather.MAP_PIN, 12, COLOR_SECONDARY), SwingConstants.LEFT);
        lblHub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblHub.setForeground(COLOR_SECONDARY);

        subRow.add(lblProfileEmail);
        subRow.add(new JLabel("•"));
        subRow.add(lblMember);
        subRow.add(new JLabel("•"));
        subRow.add(lblHub);

        centerInfo.add(nameRow);
        centerInfo.add(Box.createVerticalStrut(2));
        centerInfo.add(subRow);

        // Right Actions (Export Data & Edit Profile)
        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        rightActions.setOpaque(false);

        JButton btnExport = new JButton("Export Data", FontIcon.of(Feather.DOWNLOAD, 12, COLOR_TEXT_MAIN));
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnExport.setForeground(COLOR_TEXT_MAIN);
        btnExport.setBackground(COLOR_INPUT_BG);
        btnExport.setFocusPainted(false);
        btnExport.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExport.addActionListener(e -> exportProfileData());

        JButton btnEditProfile = new JButton("Edit Profile", FontIcon.of(Feather.EDIT_2, 12, Color.WHITE)) {
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
        btnEditProfile.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnEditProfile.setForeground(Color.WHITE);
        btnEditProfile.setContentAreaFilled(false);
        btnEditProfile.setBorderPainted(false);
        btnEditProfile.setFocusPainted(false);
        btnEditProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditProfile.addActionListener(e -> openEditPersonalDetailsDialog());

        rightActions.add(btnExport);
        rightActions.add(btnEditProfile);

        card.add(avatarWrapper, BorderLayout.WEST);
        card.add(centerInfo, BorderLayout.CENTER);
        card.add(rightActions, BorderLayout.EAST);
        return card;
    }

    private JPanel createMiniTag(String text, Color fg, Color bg) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, bg, COLOR_BORDER);
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l.setForeground(fg);
        p.add(l);
        return p;
    }

    // ==========================================
    // 5. PERSONAL INFORMATION CARD
    // ==========================================
    private JPanel createPersonalInfoCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Short.MAX_VALUE, 26));

        JPanel hTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        hTitle.setOpaque(false);
        hTitle.add(new JLabel(FontIcon.of(Feather.USER, 15, COLOR_PRIMARY_DARK)));
        JLabel t = new JLabel("Personal Information");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(COLOR_TEXT_MAIN);
        hTitle.add(t);

        JButton btnEdit = new JButton("Edit Details", FontIcon.of(Feather.EDIT, 11, COLOR_PRIMARY_DARK));
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnEdit.setForeground(COLOR_PRIMARY_DARK);
        btnEdit.setContentAreaFilled(false);
        btnEdit.setBorderPainted(false);
        btnEdit.setFocusPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.addActionListener(e -> openEditPersonalDetailsDialog());

        header.add(hTitle, BorderLayout.WEST);
        header.add(btnEdit, BorderLayout.EAST);

        JLabel sub = new JLabel("Validated customer record & identification");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(COLOR_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 2x2 Field Grid
        JPanel grid = new JPanel(new GridLayout(2, 2, 14, 10));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(10, 0, 10, 0));

        lblLegalName = new JLabel(fullName);
        lblDob = new JLabel(dob + "  " + age);
        lblEmailVal = new JLabel(email);
        lblMobileVal = new JLabel(mobile);

        grid.add(createFieldBlock("LEGAL FULL NAME", lblLegalName, "Primary account holder", null));
        grid.add(createFieldBlock("DATE OF BIRTH", lblDob, "Verified via National ID", null));
        grid.add(createFieldBlock("EMAIL ADDRESS", lblEmailVal, "Verified for order invoicing", Feather.CHECK_CIRCLE));
        grid.add(createFieldBlock("PRIMARY MOBILE", lblMobileVal, "OTP & Delivery alerts active", Feather.CHECK_CIRCLE));

        // WhatsApp / SMS Notification Row
        UIUtils.RoundedPanel notifRow = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        notifRow.setLayout(new BorderLayout());
        notifRow.setBorder(new EmptyBorder(8, 12, 8, 12));
        notifRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 38));

        JLabel notifText = new JLabel("Transactional notifications enabled for WhatsApp & SMS",
                FontIcon.of(Feather.BELL, 12, COLOR_PRIMARY_DARK), SwingConstants.LEFT);
        notifText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        notifText.setForeground(COLOR_TEXT_MAIN);

        btnNotificationToggle = new JButton(notificationsEnabled ? "ON" : "OFF");
        btnNotificationToggle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        btnNotificationToggle.setForeground(Color.WHITE);
        btnNotificationToggle.setBackground(notificationsEnabled ? COLOR_PRIMARY_DARK : COLOR_SECONDARY);
        btnNotificationToggle.setFocusPainted(false);
        btnNotificationToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnNotificationToggle.setPreferredSize(new Dimension(48, 22));
        btnNotificationToggle.addActionListener(e -> {
            notificationsEnabled = !notificationsEnabled;
            btnNotificationToggle.setText(notificationsEnabled ? "ON" : "OFF");
            btnNotificationToggle.setBackground(notificationsEnabled ? COLOR_PRIMARY_DARK : COLOR_SECONDARY);
            JOptionPane.showMessageDialog(this,
                    "Transactional notifications are now " + (notificationsEnabled ? "ENABLED" : "PAUSED") + ".",
                    "Notification Preference", JOptionPane.INFORMATION_MESSAGE);
        });

        notifRow.add(notifText, BorderLayout.WEST);
        notifRow.add(btnNotificationToggle, BorderLayout.EAST);

        card.add(header);
        card.add(sub);
        card.add(grid);
        card.add(notifRow);
        return card;
    }

    private JPanel createFieldBlock(String tag, JLabel valLabel, String subtext, Feather optionalIcon) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel t = new JLabel(tag);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(COLOR_SECONDARY);

        JPanel vRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        vRow.setOpaque(false);
        valLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        valLabel.setForeground(COLOR_TEXT_MAIN);
        vRow.add(valLabel);

        if (optionalIcon != null) {
            vRow.add(new JLabel(FontIcon.of(optionalIcon, 12, COLOR_GREEN)));
        }

        JLabel s = new JLabel(subtext);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(COLOR_SECONDARY);

        p.add(t);
        p.add(Box.createVerticalStrut(2));
        p.add(vRow);
        p.add(Box.createVerticalStrut(2));
        p.add(s);
        return p;
    }

    // ==========================================
    // 6. ACCOUNT SECURITY CARD
    // ==========================================
    private JPanel createAccountSecurityCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));

        JPanel hTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        hTitle.setOpaque(false);
        hTitle.add(new JLabel(FontIcon.of(Feather.LOCK, 15, COLOR_PRIMARY_DARK)));
        JLabel t = new JLabel("Account Security");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(COLOR_TEXT_MAIN);
        hTitle.add(t);

        UIUtils.RoundedPanel scorePill = new UIUtils.RoundedPanel(12, COLOR_INPUT_BG, COLOR_BORDER);
        scorePill.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
        JLabel score = new JLabel("● Security Score: High");
        score.setFont(new Font("Segoe UI", Font.BOLD, 10));
        score.setForeground(COLOR_GREEN);
        scorePill.add(score);

        header.add(hTitle, BorderLayout.WEST);
        header.add(scorePill, BorderLayout.EAST);

        JLabel sub = new JLabel("Authentication credentials & active security protocols");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(COLOR_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password Row
        UIUtils.RoundedPanel pwdCard = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        pwdCard.setLayout(new BorderLayout());
        pwdCard.setBorder(new EmptyBorder(10, 14, 10, 14));
        pwdCard.setMaximumSize(new Dimension(Short.MAX_VALUE, 65));

        JPanel pwdTexts = new JPanel();
        pwdTexts.setLayout(new BoxLayout(pwdTexts, BoxLayout.Y_AXIS));
        pwdTexts.setOpaque(false);

        JLabel lPwd = new JLabel("ACCOUNT MASTER PASSWORD");
        lPwd.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lPwd.setForeground(COLOR_SECONDARY);

        JPanel pRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pRow.setOpaque(false);
        JLabel dots = new JLabel(maskedPassword);
        dots.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dots.setForeground(COLOR_TEXT_MAIN);

        JLabel charTag = new JLabel(" 16 Chars ");
        charTag.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        charTag.setOpaque(true);
        charTag.setBackground(COLOR_LIGHT_BLUE);

        pRow.add(dots);
        pRow.add(charTag);

        JLabel subPwd = new JLabel("Your password is securely protected • Last changed 45 days ago");
        subPwd.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        subPwd.setForeground(COLOR_SECONDARY);

        pwdTexts.add(lPwd);
        pwdTexts.add(pRow);
        pwdTexts.add(subPwd);

        JButton btnChangePwd = new JButton("Change Password", FontIcon.of(Feather.KEY, 11, COLOR_PRIMARY_DARK));
        btnChangePwd.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnChangePwd.setBackground(Color.WHITE);
        btnChangePwd.setFocusPainted(false);
        btnChangePwd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChangePwd.addActionListener(e -> openChangePasswordDialog());

        pwdCard.add(pwdTexts, BorderLayout.CENTER);
        pwdCard.add(btnChangePwd, BorderLayout.EAST);

        // 2FA & Active Sessions (Split Row)
        JPanel subRow = new JPanel(new GridLayout(1, 2, 12, 0));
        subRow.setOpaque(false);
        subRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 68));

        // 2FA Block
        UIUtils.RoundedPanel twoFaBox = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        twoFaBox.setLayout(new BorderLayout());
        twoFaBox.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel twoFaTxt = new JPanel();
        twoFaTxt.setLayout(new BoxLayout(twoFaTxt, BoxLayout.Y_AXIS));
        twoFaTxt.setOpaque(false);

        JLabel t1 = new JLabel("Two-Factor Authentication (2FA)");
        t1.setFont(new Font("Segoe UI", Font.BOLD, 11));
        t1.setForeground(COLOR_TEXT_MAIN);

        JLabel t2 = new JLabel("Authenticator & SMS OTP");
        t2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        t2.setForeground(COLOR_SECONDARY);

        twoFaTxt.add(t1);
        twoFaTxt.add(t2);

        lbl2faBadge = new JLabel(twoFactorEnabled ? "Enabled" : "Disabled");
        lbl2faBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl2faBadge.setForeground(twoFactorEnabled ? COLOR_GREEN : COLOR_SECONDARY);
        lbl2faBadge.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl2faBadge.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                twoFactorEnabled = !twoFactorEnabled;
                lbl2faBadge.setText(twoFactorEnabled ? "Enabled" : "Disabled");
                lbl2faBadge.setForeground(twoFactorEnabled ? COLOR_GREEN : COLOR_SECONDARY);
                JOptionPane.showMessageDialog(MyAccountPage.this,
                        "Two-Factor Authentication is now " + (twoFactorEnabled ? "ENABLED" : "DISABLED") + ".",
                        "Security Setting", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        twoFaBox.add(twoFaTxt, BorderLayout.CENTER);
        twoFaBox.add(lbl2faBadge, BorderLayout.EAST);

        // Active Session Block
        UIUtils.RoundedPanel sessBox = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        sessBox.setLayout(new BorderLayout());
        sessBox.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel sessTxt = new JPanel();
        sessTxt.setLayout(new BoxLayout(sessTxt, BoxLayout.Y_AXIS));
        sessTxt.setOpaque(false);

        JLabel s1 = new JLabel("Active Logged-in Sessions");
        s1.setFont(new Font("Segoe UI", Font.BOLD, 11));
        s1.setForeground(COLOR_TEXT_MAIN);

        JLabel s2 = new JLabel("ShopSphere Client (v2.4)");
        s2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s2.setForeground(COLOR_SECONDARY);

        sessTxt.add(s1);
        sessTxt.add(s2);

        JLabel sBadge = new JLabel("1 Active Node");
        sBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        sBadge.setForeground(COLOR_PRIMARY_DARK);

        sessBox.add(sessTxt, BorderLayout.CENTER);
        sessBox.add(sBadge, BorderLayout.EAST);

        subRow.add(twoFaBox);
        subRow.add(sessBox);

        card.add(header);
        card.add(sub);
        card.add(Box.createVerticalStrut(10));
        card.add(pwdCard);
        card.add(Box.createVerticalStrut(10));
        card.add(subRow);

        return card;
    }

    // ==========================================
    // 7. DEFAULT DELIVERY ADDRESS CARD
    // ==========================================
    private JPanel createDefaultAddressCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Short.MAX_VALUE, 26));

        JPanel hTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        hTitle.setOpaque(false);
        hTitle.add(new JLabel(FontIcon.of(Feather.TRUCK, 15, COLOR_PRIMARY_DARK)));
        JLabel t = new JLabel("Default Delivery Address");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(COLOR_TEXT_MAIN);
        hTitle.add(t);

        JButton btnManage = new JButton("Manage", FontIcon.of(Feather.SETTINGS, 11, COLOR_PRIMARY_DARK));
        btnManage.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnManage.setForeground(COLOR_PRIMARY_DARK);
        btnManage.setContentAreaFilled(false);
        btnManage.setBorderPainted(false);
        btnManage.setFocusPainted(false);
        btnManage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnManage.addActionListener(e -> openManageDefaultAddressDialog());

        header.add(hTitle, BorderLayout.WEST);
        header.add(btnManage, BorderLayout.EAST);

        JLabel sub = new JLabel("Primary destination for 1-click orders");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(COLOR_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Address Display Box
        defaultAddressContentPanel = new JPanel();
        defaultAddressContentPanel.setLayout(new BoxLayout(defaultAddressContentPanel, BoxLayout.Y_AXIS));
        defaultAddressContentPanel.setOpaque(false);

        renderDefaultAddressContent();

        card.add(header);
        card.add(sub);
        card.add(Box.createVerticalStrut(8));
        card.add(defaultAddressContentPanel);

        return card;
    }

    private void renderDefaultAddressContent() {
        defaultAddressContentPanel.removeAll();

        UIUtils.RoundedPanel inner = new UIUtils.RoundedPanel(12, COLOR_INPUT_BG, COLOR_BORDER);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(10, 14, 10, 14));

        JPanel topTagRow = new JPanel(new BorderLayout());
        topTagRow.setOpaque(false);

        JLabel titleBadge = new JLabel(" 🏠 " + defaultAddress.title + " ");
        titleBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleBadge.setForeground(COLOR_PRIMARY_DARK);
        titleBadge.setOpaque(true);
        titleBadge.setBackground(COLOR_LIGHT_BLUE);

        JLabel verifyBadge = new JLabel("✔ Verified Dispatch Point");
        verifyBadge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        verifyBadge.setForeground(COLOR_GREEN);

        topTagRow.add(titleBadge, BorderLayout.WEST);
        topTagRow.add(verifyBadge, BorderLayout.EAST);

        JLabel nameRow = new JLabel("<html><b>" + defaultAddress.recipient + "</b> <span style='color:#717E93; font-size:10px;'>Default Shipping</span></html>");
        nameRow.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameRow.setForeground(COLOR_TEXT_MAIN);

        JLabel addrRow1 = new JLabel(defaultAddress.line1 + ", " + defaultAddress.line2);
        addrRow1.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        addrRow1.setForeground(COLOR_TEXT_MAIN);

        JLabel addrRow2 = new JLabel(defaultAddress.city + ", " + defaultAddress.state + " • PIN: " + defaultAddress.pin + " • " + defaultAddress.country);
        addrRow2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        addrRow2.setForeground(COLOR_SECONDARY);

        // Instruction Pill
        UIUtils.RoundedPanel instBox = new UIUtils.RoundedPanel(8, Color.WHITE, COLOR_BORDER);
        instBox.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 4));
        JLabel instIcon = new JLabel(FontIcon.of(Feather.INFO, 12, COLOR_PRIMARY_DARK));
        JLabel instTxt = new JLabel("<html><body style='width:320px; font-size:10px; color:#717E93;'><b>Delivery Instructions:</b> "
                + defaultAddress.instructions + "</body></html>");
        instBox.add(instIcon);
        instBox.add(instTxt);

        inner.add(topTagRow);
        inner.add(Box.createVerticalStrut(6));
        inner.add(nameRow);
        inner.add(Box.createVerticalStrut(3));
        inner.add(addrRow1);
        inner.add(addrRow2);
        inner.add(Box.createVerticalStrut(8));
        inner.add(instBox);

        defaultAddressContentPanel.add(inner);
        defaultAddressContentPanel.revalidate();
        defaultAddressContentPanel.repaint();
    }

    // ==========================================
    // 8. ADDITIONAL SAVED ADDRESSES
    // ==========================================
    private JPanel createAdditionalAddressesCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));

        JLabel title = new JLabel("Additional Addresses on File (" + additionalAddresses.size() + ")");
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));
        title.setForeground(COLOR_TEXT_MAIN);

        header.add(title, BorderLayout.WEST);

        additionalAddressContainer = new JPanel();
        additionalAddressContainer.setLayout(new BoxLayout(additionalAddressContainer, BoxLayout.Y_AXIS));
        additionalAddressContainer.setOpaque(false);

        renderAdditionalAddresses();

        // Add New Address Button
        JButton btnAddNew = new JButton("+  Add New Shipping Address", FontIcon.of(Feather.PLUS, 12, COLOR_PRIMARY_DARK));
        btnAddNew.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAddNew.setForeground(COLOR_PRIMARY_DARK);
        btnAddNew.setBackground(COLOR_LIGHT_BLUE);
        btnAddNew.setFocusPainted(false);
        btnAddNew.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddNew.setMaximumSize(new Dimension(Short.MAX_VALUE, 34));
        btnAddNew.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddNew.addActionListener(e -> openAddNewAddressDialog());

        card.add(header);
        card.add(Box.createVerticalStrut(8));
        card.add(additionalAddressContainer);
        card.add(Box.createVerticalStrut(10));
        card.add(btnAddNew);

        return card;
    }

    private void renderAdditionalAddresses() {
        additionalAddressContainer.removeAll();

        for (int i = 0; i < additionalAddresses.size(); i++) {
            AddressRecord addr = additionalAddresses.get(i);
            final int index = i;

            UIUtils.RoundedPanel row = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
            row.setLayout(new BorderLayout());
            row.setBorder(new EmptyBorder(8, 12, 8, 12));
            row.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

            JPanel txt = new JPanel();
            txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
            txt.setOpaque(false);

            JLabel t = new JLabel(addr.title);
            t.setFont(new Font("Segoe UI", Font.BOLD, 11));
            t.setForeground(COLOR_TEXT_MAIN);

            JLabel a = new JLabel(addr.line1 + ", " + addr.city);
            a.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            a.setForeground(COLOR_SECONDARY);

            txt.add(t);
            txt.add(a);

            JButton btnSetDefault = new JButton("Set Default");
            btnSetDefault.setFont(new Font("Segoe UI", Font.BOLD, 10));
            btnSetDefault.setForeground(COLOR_PRIMARY_DARK);
            btnSetDefault.setContentAreaFilled(false);
            btnSetDefault.setBorderPainted(false);
            btnSetDefault.setFocusPainted(false);
            btnSetDefault.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnSetDefault.addActionListener(e -> {
                // Swap default with this address
                AddressRecord oldDefault = defaultAddress;
                oldDefault.title = oldDefault.title.replace(" (Default)", "") + " (Secondary)";
                defaultAddress = addr;
                additionalAddresses.set(index, oldDefault);
                renderDefaultAddressContent();
                renderAdditionalAddresses();
                JOptionPane.showMessageDialog(this,
                        "\"" + addr.title + "\" has been set as your primary delivery address.",
                        "Default Address Updated", JOptionPane.INFORMATION_MESSAGE);
            });

            row.add(txt, BorderLayout.CENTER);
            row.add(btnSetDefault, BorderLayout.EAST);

            additionalAddressContainer.add(row);
            additionalAddressContainer.add(Box.createVerticalStrut(6));
        }

        additionalAddressContainer.revalidate();
        additionalAddressContainer.repaint();
    }

    // ==========================================
    // 9. ACCOUNT ACTIVITY SUMMARY (Bottom Row)
    // ==========================================
    private JPanel createAccountActivitySummaryCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Short.MAX_VALUE, 26));

        JPanel hTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        hTitle.setOpaque(false);
        hTitle.add(new JLabel(FontIcon.of(Feather.BAR_CHART_2, 15, COLOR_PRIMARY_DARK)));
        JLabel t = new JLabel("Account Activity Summary");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("• Lifetime purchasing telemetry & order dispatch status");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(COLOR_SECONDARY);

        hTitle.add(t);
        hTitle.add(sub);

        JButton btnViewOrders = new JButton("View All Orders ➔");
        btnViewOrders.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnViewOrders.setForeground(COLOR_PRIMARY_DARK);
        btnViewOrders.setContentAreaFilled(false);
        btnViewOrders.setBorderPainted(false);
        btnViewOrders.setFocusPainted(false);
        btnViewOrders.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewOrders.addActionListener(e -> {
            new MyOrdersPage().setVisible(true);
            dispose();
        });

        header.add(hTitle, BorderLayout.WEST);
        header.add(btnViewOrders, BorderLayout.EAST);

        // 3 Telemetry Cards Row
        JPanel telemetryRow = new JPanel(new GridLayout(1, 3, 14, 0));
        telemetryRow.setOpaque(false);
        telemetryRow.setBorder(new EmptyBorder(10, 0, 0, 0));

        telemetryRow.add(createActivityBlock("TOTAL ORDERS PLACED", "12", "↗ 100% Fulfilled Ratio",
                "Total spent: ₹34,490 across 8 categories", Feather.PACKAGE, COLOR_PRIMARY_DARK));

        telemetryRow.add(createActivityBlock("PENDING / IN-TRANSIT", "2", "Expected Tomorrow",
                "Order #SS-98211 & #SS-98304 on dispatch route", Feather.CLOCK, COLOR_ORANGE));

        telemetryRow.add(createActivityBlock("COMPLETED ORDERS", "10", "Delivered & Signed",
                "All items verified with zero return disputes", Feather.CHECK_CIRCLE, COLOR_GREEN));

        card.add(header);
        card.add(telemetryRow);
        return card;
    }

    private JPanel createActivityBlock(String title, String bigNum, String tag, String subtext, Feather icon, Color accentColor) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, COLOR_INPUT_BG, COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(COLOR_SECONDARY);

        JPanel numRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        numRow.setOpaque(false);
        JLabel n = new JLabel(bigNum);
        n.setFont(new Font("Segoe UI", Font.BOLD, 22));
        n.setForeground(COLOR_TEXT_MAIN);

        JLabel tg = new JLabel(" " + tag + " ");
        tg.setFont(new Font("Segoe UI", Font.BOLD, 9));
        tg.setForeground(accentColor);
        tg.setOpaque(true);
        tg.setBackground(COLOR_CARD);

        numRow.add(n);
        numRow.add(tg);

        JLabel s = new JLabel(subtext);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(COLOR_SECONDARY);

        left.add(t);
        left.add(Box.createVerticalStrut(2));
        left.add(numRow);
        left.add(Box.createVerticalStrut(2));
        left.add(s);

        UIUtils.RoundedPanel icBox = new UIUtils.RoundedPanel(10, COLOR_CARD, COLOR_BORDER);
        icBox.setPreferredSize(new Dimension(36, 36));
        icBox.setLayout(new GridBagLayout());
        icBox.add(new JLabel(FontIcon.of(icon, 16, accentColor)));

        p.add(left, BorderLayout.CENTER);
        p.add(icBox, BorderLayout.EAST);
        return p;
    }

    // ==========================================
    // 10. DIALOGS & DEMO ACTIONS
    // ==========================================

    private void openEditPersonalDetailsDialog() {
        JDialog dlg = new JDialog(this, "Edit Personal Information", true);
        dlg.setSize(440, 360);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(COLOR_CARD);
        form.setBorder(new EmptyBorder(18, 20, 18, 20));

        JTextField tfName = new JTextField(fullName);
        JTextField tfDob  = new JTextField(dob);
        JTextField tfEmail = new JTextField(email);
        JTextField tfPhone = new JTextField(mobile);

        form.add(createDialogField("Legal Full Name *", tfName));
        form.add(Box.createVerticalStrut(8));
        form.add(createDialogField("Date of Birth *", tfDob));
        form.add(Box.createVerticalStrut(8));
        form.add(createDialogField("Email Address *", tfEmail));
        form.add(Box.createVerticalStrut(8));
        form.add(createDialogField("Primary Mobile *", tfPhone));
        form.add(Box.createVerticalStrut(16));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dlg.dispose());

        JButton btnSave = new JButton("Save Changes");
        btnSave.setBackground(COLOR_PRIMARY_DARK);
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String n = tfName.getText().trim();
            String d = tfDob.getText().trim();
            String em = tfEmail.getText().trim();
            String ph = tfPhone.getText().trim();

            if (n.isEmpty() || d.isEmpty() || em.isEmpty() || ph.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", em)) {
                JOptionPane.showMessageDialog(dlg, "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Update in-memory state
            fullName = n;
            dob = d;
            email = em;
            mobile = ph;

            lblProfileName.setText(fullName);
            lblProfileEmail.setText(email);
            lblLegalName.setText(fullName);
            lblDob.setText(dob + "  " + age);
            lblEmailVal.setText(email);
            lblMobileVal.setText(mobile);

            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Profile information updated successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        });

        btnRow.add(btnCancel);
        btnRow.add(btnSave);
        form.add(btnRow);

        dlg.setContentPane(form);
        dlg.setVisible(true);
    }

    private void openChangePasswordDialog() {
        JDialog dlg = new JDialog(this, "Change Password", true);
        dlg.setSize(400, 300);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(COLOR_CARD);
        p.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPasswordField pfCurrent = new JPasswordField();
        JPasswordField pfNew = new JPasswordField();
        JPasswordField pfConfirm = new JPasswordField();

        p.add(createDialogField("Current Password *", pfCurrent));
        p.add(Box.createVerticalStrut(8));
        p.add(createDialogField("New Password *", pfNew));
        p.add(Box.createVerticalStrut(8));
        p.add(createDialogField("Confirm New Password *", pfConfirm));
        p.add(Box.createVerticalStrut(16));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dlg.dispose());

        JButton btnSave = new JButton("Save Changes");
        btnSave.setBackground(COLOR_PRIMARY_DARK);
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            String pCur = new String(pfCurrent.getPassword());
            String pNew = new String(pfNew.getPassword());
            String pConf = new String(pfConfirm.getPassword());

            if (pCur.isEmpty() || pNew.isEmpty() || pConf.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All password fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!pNew.equals(pConf)) {
                JOptionPane.showMessageDialog(dlg, "New Password and Confirm Password do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (pNew.length() < 6) {
                JOptionPane.showMessageDialog(dlg, "Password must be at least 6 characters.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Password changed successfully! (Demo State)", "Security Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        btnRow.add(btnCancel);
        btnRow.add(btnSave);
        p.add(btnRow);

        dlg.setContentPane(p);
        dlg.setVisible(true);
    }

    private void openManageDefaultAddressDialog() {
        JDialog dlg = new JDialog(this, "Manage Default Address", true);
        dlg.setSize(460, 420);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(COLOR_CARD);
        p.setBorder(new EmptyBorder(18, 20, 18, 20));

        JTextField tfRecipient = new JTextField(defaultAddress.recipient);
        JTextField tfLine1 = new JTextField(defaultAddress.line1);
        JTextField tfLine2 = new JTextField(defaultAddress.line2);
        JTextField tfCity = new JTextField(defaultAddress.city);
        JTextField tfState = new JTextField(defaultAddress.state);
        JTextField tfPin = new JTextField(defaultAddress.pin);
        JTextField tfInst = new JTextField(defaultAddress.instructions);

        p.add(createDialogField("Recipient Name *", tfRecipient));
        p.add(Box.createVerticalStrut(6));
        p.add(createDialogField("Street Address (Line 1) *", tfLine1));
        p.add(Box.createVerticalStrut(6));
        p.add(createDialogField("Area / Landmark (Line 2)", tfLine2));
        p.add(Box.createVerticalStrut(6));

        JPanel rowLoc = new JPanel(new GridLayout(1, 3, 8, 0));
        rowLoc.setOpaque(false);
        rowLoc.add(createDialogField("City *", tfCity));
        rowLoc.add(createDialogField("State *", tfState));
        rowLoc.add(createDialogField("PIN *", tfPin));
        p.add(rowLoc);

        p.add(Box.createVerticalStrut(6));
        p.add(createDialogField("Delivery Instructions", tfInst));
        p.add(Box.createVerticalStrut(14));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dlg.dispose());

        JButton btnSave = new JButton("Save Address");
        btnSave.setBackground(COLOR_PRIMARY_DARK);
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            if (tfRecipient.getText().trim().isEmpty() || tfLine1.getText().trim().isEmpty() ||
                tfCity.getText().trim().isEmpty() || tfPin.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Please fill in all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!Pattern.matches("^\\d{6}$", tfPin.getText().trim())) {
                JOptionPane.showMessageDialog(dlg, "PIN code must be exactly 6 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            defaultAddress.recipient = tfRecipient.getText().trim();
            defaultAddress.line1 = tfLine1.getText().trim();
            defaultAddress.line2 = tfLine2.getText().trim();
            defaultAddress.city = tfCity.getText().trim();
            defaultAddress.state = tfState.getText().trim();
            defaultAddress.pin = tfPin.getText().trim();
            defaultAddress.instructions = tfInst.getText().trim();

            renderDefaultAddressContent();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Default delivery address updated!", "Updated", JOptionPane.INFORMATION_MESSAGE);
        });

        btnRow.add(btnCancel);
        btnRow.add(btnSave);
        p.add(btnRow);

        dlg.setContentPane(p);
        dlg.setVisible(true);
    }

    private void openAddNewAddressDialog() {
        JDialog dlg = new JDialog(this, "Add New Shipping Address", true);
        dlg.setSize(440, 380);
        dlg.setLocationRelativeTo(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(COLOR_CARD);
        p.setBorder(new EmptyBorder(18, 20, 18, 20));

        JTextField tfTitle = new JTextField("Vacation Home");
        JTextField tfRecipient = new JTextField(fullName);
        JTextField tfLine = new JTextField("Flat 12, Pine Crest Road");
        JTextField tfCity = new JTextField("Lonavala");
        JTextField tfState = new JTextField("Maharashtra");
        JTextField tfPin = new JTextField("410401");

        p.add(createDialogField("Address Label / Title *", tfTitle));
        p.add(Box.createVerticalStrut(6));
        p.add(createDialogField("Recipient Full Name *", tfRecipient));
        p.add(Box.createVerticalStrut(6));
        p.add(createDialogField("Street Address *", tfLine));
        p.add(Box.createVerticalStrut(6));

        JPanel rowLoc = new JPanel(new GridLayout(1, 3, 8, 0));
        rowLoc.setOpaque(false);
        rowLoc.add(createDialogField("City *", tfCity));
        rowLoc.add(createDialogField("State *", tfState));
        rowLoc.add(createDialogField("PIN (6-digits) *", tfPin));
        p.add(rowLoc);

        p.add(Box.createVerticalStrut(14));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dlg.dispose());

        JButton btnSave = new JButton("Save Address");
        btnSave.setBackground(COLOR_PRIMARY_DARK);
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            if (tfTitle.getText().trim().isEmpty() || tfLine.getText().trim().isEmpty() ||
                tfCity.getText().trim().isEmpty() || tfPin.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!Pattern.matches("^\\d{6}$", tfPin.getText().trim())) {
                JOptionPane.showMessageDialog(dlg, "PIN code must be exactly 6 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            additionalAddresses.add(new AddressRecord(
                    tfTitle.getText().trim(),
                    tfRecipient.getText().trim(),
                    tfLine.getText().trim(),
                    "",
                    tfCity.getText().trim(),
                    tfState.getText().trim(),
                    tfPin.getText().trim(),
                    "India",
                    "Deliver during daytime.",
                    "Secondary"
            ));

            renderAdditionalAddresses();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "New shipping address added to your profile!", "Address Added", JOptionPane.INFORMATION_MESSAGE);
        });

        btnRow.add(btnCancel);
        btnRow.add(btnSave);
        p.add(btnRow);

        dlg.setContentPane(p);
        dlg.setVisible(true);
    }

    private JPanel createDialogField(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(COLOR_TEXT_MAIN);

        p.add(l);
        p.add(Box.createVerticalStrut(3));
        p.add(field);
        return p;
    }

    private void exportProfileData() {
        String data = "========================================\n"
                    + "   SHOPSPHERE CUSTOMER PROFILE TELEMETRY\n"
                    + "========================================\n\n"
                    + "Customer Name: " + fullName + "\n"
                    + "Date of Birth: " + dob + " (" + age + ")\n"
                    + "Email: " + email + "\n"
                    + "Mobile: " + mobile + "\n"
                    + "Membership Tier: Tier-1 Prime\n"
                    + "Member Since: January 2026\n"
                    + "Two-Factor Auth: " + (twoFactorEnabled ? "Active" : "Disabled") + "\n\n"
                    + "DEFAULT DELIVERY ADDRESS:\n"
                    + defaultAddress.recipient + "\n"
                    + defaultAddress.line1 + ", " + defaultAddress.line2 + "\n"
                    + defaultAddress.city + ", " + defaultAddress.state + " - " + defaultAddress.pin + "\n"
                    + defaultAddress.country + "\n\n"
                    + "Export Timestamp: " + new java.util.Date() + "\n";

        File file = new File("ShopSphere_Customer_Export.txt");
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(data);
            JOptionPane.showMessageDialog(this,
                    "Profile telemetry data exported successfully!\nSaved to: " + file.getAbsolutePath(),
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Export Dialog: " + data, "Profile Export Preview", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showPlaceholder(String name) {
        JOptionPane.showMessageDialog(this,
                name + " screen will be connected cleanly in the next step.",
                "ShopSphere Navigation", JOptionPane.INFORMATION_MESSAGE);
    }
}
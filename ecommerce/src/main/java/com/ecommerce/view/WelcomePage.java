package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class WelcomePage extends JFrame {

    // Stitch Design Color Palette
    public static final Color COLOR_PRIMARY_BLUE = new Color(0x75, 0xA2, 0xC9); // #75A2C9 Soft Slate Blue
    public static final Color COLOR_PRIMARY_HOVER = new Color(0x60, 0x8E, 0xB5);
    public static final Color COLOR_LIGHT_BLUE   = new Color(0xD6, 0xE6, 0xF2); // #D6E6F2
    public static final Color COLOR_BACKGROUND   = new Color(0xF2, 0xF7, 0xFB); // #F2F7FB
    public static final Color COLOR_CARD_BG      = Color.WHITE;                 // #FFFFFF
    public static final Color COLOR_MAIN_TEXT    = new Color(0x24, 0x34, 0x4D); // #24344D
    public static final Color COLOR_SECONDARY    = new Color(0x6B, 0x72, 0x80); // #6B7280
    public static final Color COLOR_BORDER       = new Color(0xE1, 0xEA, 0xF2); // #E1EAF2
    public static final Color COLOR_ACCENT_GREEN = new Color(0x10, 0xB9, 0x81); // Emerald Green
    public static final Color COLOR_BADGE_BG     = new Color(0xEB, 0xF3, 0xFA);

    public WelcomePage() {
        setTitle("ShopSphere - Enterprise Desktop Gateway");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Responsive Screen Sizing (स्क्रीन साईझनुसार आपोआप ॲडजस्ट होईल)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int targetWidth = Math.min(1366, (int) (screenSize.width * 0.90));
        int targetHeight = Math.min(880, (int) (screenSize.height * 0.90));
        setSize(targetWidth, targetHeight);
        setMinimumSize(new Dimension(1024, 680));
        setLocationRelativeTo(null); // Center on screen

        // Root Background
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BACKGROUND);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createScrollableBody(), BorderLayout.CENTER);
        rootPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    // ==========================================
    // 1. HEADER NAVBAR
    // ==========================================
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 32, 10, 32));

        // Brand (Left)
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        brandPanel.setOpaque(false);

        RoundedPanel logoIcon = new RoundedPanel(36, COLOR_LIGHT_BLUE);
        logoIcon.setPreferredSize(new Dimension(38, 38));
        logoIcon.setLayout(new GridBagLayout());
        
        FontIcon brandIcon = FontIcon.of(Feather.SHOPPING_BAG, 18, COLOR_PRIMARY_BLUE.darker());
        logoIcon.add(new JLabel(brandIcon));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);

        JLabel appTitle = new JLabel("ShopSphere");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        appTitle.setForeground(COLOR_MAIN_TEXT);

        JLabel appSubtitle = new JLabel("Your Everyday Shopping Destination");
        appSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        appSubtitle.setForeground(COLOR_SECONDARY);

        titleBox.add(appTitle);
        titleBox.add(appSubtitle);

        brandPanel.add(logoIcon);
        brandPanel.add(titleBox);

        // Header Right: Status and Indicators
        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightBox.setOpaque(false);

        JPanel statusPill = createPillBadge("Master Node: Online (99.8%)", COLOR_ACCENT_GREEN, COLOR_CARD_BG, true, Feather.ACTIVITY);
        rightBox.add(statusPill);

        JPanel decorDots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 8));
        decorDots.setOpaque(false);
        decorDots.add(createDot(new Color(0x93, 0xC5, 0xFD)));
        decorDots.add(createDot(new Color(0xBF, 0xDB, 0xFE)));
        decorDots.add(createDot(new Color(0xFE, 0xD7, 0xAA)));
        rightBox.add(decorDots);

        header.add(brandPanel, BorderLayout.WEST);
        header.add(rightBox, BorderLayout.EAST);
        return header;
    }

    // ==========================================
    // 2. SCROLLABLE BODY (Fits laptop screens perfectly)
    // ==========================================
    private JComponent createScrollableBody() {
        JPanel bodyContainer = new JPanel();
        bodyContainer.setLayout(new BoxLayout(bodyContainer, BoxLayout.Y_AXIS));
        bodyContainer.setOpaque(false);
        bodyContainer.setBorder(new EmptyBorder(10, 36, 16, 36));

        // Hero Row
        JPanel heroRow = new JPanel(new GridBagLayout());
        heroRow.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Hero
        gbc.gridx = 0;
        gbc.weightx = 0.58;
        gbc.insets = new Insets(0, 0, 0, 20);
        heroRow.add(createHeroLeftContent(), gbc);

        // Right Hero Showcase Card
        gbc.gridx = 1;
        gbc.weightx = 0.42;
        gbc.insets = new Insets(0, 0, 0, 0);
        heroRow.add(createHeroRightCard(), gbc);

        bodyContainer.add(heroRow);
        bodyContainer.add(Box.createVerticalStrut(26));
        bodyContainer.add(createFeatureCardsRow());

        JScrollPane scrollPane = new JScrollPane(bodyContainer);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    // ==========================================
    // 2A. HERO LEFT
    // ==========================================
    private JPanel createHeroLeftContent() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JPanel welcomeTag = createPillBadge("Welcome to ShopSphere Enterprise", COLOR_PRIMARY_BLUE, COLOR_BADGE_BG, true, Feather.INFO);
        welcomeTag.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel mainHeading = new JLabel("<html><body style='width: 480px;'>"
                + "Everything You Love, All in One Place."
                + "</body></html>");
        mainHeading.setFont(new Font("Segoe UI", Font.BOLD, 36));
        mainHeading.setForeground(COLOR_MAIN_TEXT);
        mainHeading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subDescription = new JLabel("<html><body style='width: 450px; line-height: 1.5; color: #6B7280;'>"
                + "Discover curated products across dozens of categories, experience an effortless "
                + "catalog browser, and track your global dispatches with genuine peace of mind."
                + "</body></html>");
        subDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Buttons with Ikonli Vector Icons
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCustomerLogin = createPrimaryButton("Customer Login", Feather.LOG_IN);
        JButton btnCreateAccount = createOutlineButton("Create Account", Feather.USER_PLUS);
        JButton btnAdminLogin    = createGhostButton("Administrator Access & ERP Portal", Feather.SHIELD);

        btnCustomerLogin.addActionListener(e -> onCustomerLoginClicked());
        btnCreateAccount.addActionListener(e -> onCreateAccountClicked());
        btnAdminLogin.addActionListener(e -> onAdminLoginClicked());

        buttonRow.add(btnCustomerLogin);
        buttonRow.add(btnCreateAccount);
        buttonRow.add(btnAdminLogin);

        // Metrics
        JPanel statsRow = createStatsRow();
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(welcomeTag);
        leftPanel.add(Box.createVerticalStrut(14));
        leftPanel.add(mainHeading);
        leftPanel.add(Box.createVerticalStrut(12));
        leftPanel.add(subDescription);
        leftPanel.add(Box.createVerticalStrut(24));
        leftPanel.add(buttonRow);
        leftPanel.add(Box.createVerticalStrut(32));
        leftPanel.add(statsRow);

        return leftPanel;
    }

    // ==========================================
    // 2B. HERO RIGHT CARD
    // ==========================================
    private JPanel createHeroRightCard() {
        RoundedPanel card = new RoundedPanel(22, COLOR_CARD_BG, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(420, 290));

        JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 14));
        cardHeader.setOpaque(false);
        cardHeader.add(createPillBadge("Real-time Cart Sync", COLOR_ACCENT_GREEN, COLOR_BADGE_BG, true, Feather.REFRESH_CW));

        // Center Artistic Canvas
        JPanel showcaseCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Soft pastel showcase background
                GradientPaint gp = new GradientPaint(0, 0, new Color(0xEE, 0xF5, 0xFB), w, h, new Color(0xD8, 0xEA, 0xF7));
                g2.setPaint(gp);
                g2.fillRoundRect(16, 4, w - 32, h - 16, 16, 16);

                // Modern 3D/Gift Box Vectors
                g2.setColor(new Color(0xB8, 0xD4, 0xEB));
                g2.fillRoundRect(w / 2 - 75, h / 2 - 35, 70, 80, 8, 8);
                g2.setColor(new Color(0x95, 0xC1, 0xE4));
                g2.fillRoundRect(w / 2 + 5, h / 2 - 15, 75, 70, 8, 8);
                g2.setColor(new Color(0xC7, 0xDF, 0xF2));
                g2.fillRoundRect(w / 2 - 20, h / 2 - 65, 55, 55, 8, 8);

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.2f));
                g2.drawArc(w / 2 - 58, h / 2 - 48, 32, 26, 0, 180);
                g2.drawArc(w / 2 + 28, h / 2 - 28, 30, 24, 0, 180);

                g2.dispose();
            }
        };
        showcaseCanvas.setOpaque(false);
        showcaseCanvas.setLayout(new BorderLayout());

        // Floating Bottom Badge
        JPanel bottomOverlay = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 14));
        bottomOverlay.setOpaque(false);

        RoundedPanel dispatchPill = new RoundedPanel(12, COLOR_CARD_BG, COLOR_BORDER);
        dispatchPill.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));

        FontIcon truckIcon = FontIcon.of(Feather.TRUCK, 18, COLOR_PRIMARY_BLUE);
        dispatchPill.add(new JLabel(truckIcon));

        JPanel dispatchTexts = new JPanel();
        dispatchTexts.setLayout(new BoxLayout(dispatchTexts, BoxLayout.Y_AXIS));
        dispatchTexts.setOpaque(false);

        JLabel l1 = new JLabel("Express Dispatch Active");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l1.setForeground(COLOR_MAIN_TEXT);

        JLabel l2 = new JLabel("Next slot departs in 14m");
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        l2.setForeground(COLOR_SECONDARY);

        dispatchTexts.add(l1);
        dispatchTexts.add(l2);

        dispatchPill.add(dispatchTexts);
        bottomOverlay.add(dispatchPill);

        showcaseCanvas.add(bottomOverlay, BorderLayout.SOUTH);

        card.add(cardHeader, BorderLayout.NORTH);
        card.add(showcaseCanvas, BorderLayout.CENTER);

        return card;
    }

    // ==========================================
    // 2C. METRICS STATS
    // ==========================================
    private JPanel createStatsRow() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 32, 0));
        statsPanel.setOpaque(false);

        statsPanel.add(createStatBlock("14,200+", "Active", "Verified Catalog Items", true));
        statsPanel.add(createStatBlock("99.8%", "SLA", "Dispatch Reliability", false));
        statsPanel.add(createStatBlock("24 / 7", "", "Swing Client Sync", false));

        return statsPanel;
    }

    private JPanel createStatBlock(String bigNumber, String tag, String subtext, boolean isGreenTag) {
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setOpaque(false);

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        topRow.setOpaque(false);

        JLabel numLabel = new JLabel(bigNumber);
        numLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        numLabel.setForeground(COLOR_MAIN_TEXT);
        topRow.add(numLabel);

        if (!tag.isEmpty()) {
            JLabel tagLabel = new JLabel(tag);
            tagLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            tagLabel.setForeground(isGreenTag ? COLOR_ACCENT_GREEN : COLOR_PRIMARY_BLUE);
            topRow.add(tagLabel);
        }

        JLabel descLabel = new JLabel(subtext);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(COLOR_SECONDARY);

        block.add(topRow);
        block.add(Box.createVerticalStrut(3));
        block.add(descLabel);

        return block;
    }

    // ==========================================
    // 3. THREE FEATURE CARDS
    // ==========================================
    private JPanel createFeatureCardsRow() {
        JPanel cardsRow = new JPanel(new GridLayout(1, 3, 18, 0));
        cardsRow.setOpaque(false);

        cardsRow.add(createFeatureCard(
                Feather.COMPASS,
                "Explore Products",
                "MULTI-CATEGORY",
                "Instant",
                "Effortlessly search and filter across department inventories with real-time stock indicators and detailed technical specs.",
                "Browse Catalog ➔",
                "24+ Categories"
        ));

        cardsRow.add(createFeatureCard(
                Feather.SHOPPING_BAG,
                "Easy Shopping",
                "1-CLICK CHECKOUT",
                "Buffered",
                "Stage, bundle, and save preferred items in persistent desktop sessions. Review totals with transparent pricing breakdowns.",
                "Basket Manager ➔",
                "Zero Data Loss"
        ));

        cardsRow.add(createFeatureCard(
                Feather.PACKAGE,
                "Track Orders",
                "LIVE TELEMETRY",
                "GPS Ready",
                "Live waypoint telemetry, courier updates, and automated digital invoices ready for direct download or local printer routing.",
                "Shipment Radar ➔",
                "Automated PDF"
        ));

        return cardsRow;
    }

    private JPanel createFeatureCard(Feather featherIcon, String title, String subtitle, String pillText,
                                     String description, String actionText, String footerHint) {
        RoundedPanel card = new RoundedPanel(16, COLOR_CARD_BG, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 18, 16, 18));

        // Top Row
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftHeader.setOpaque(false);

        RoundedPanel iconBox = new RoundedPanel(10, COLOR_BADGE_BG);
        iconBox.setPreferredSize(new Dimension(34, 34));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(new JLabel(FontIcon.of(featherIcon, 16, COLOR_MAIN_TEXT)));

        JPanel titleGroup = new JPanel();
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.setOpaque(false);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setForeground(COLOR_MAIN_TEXT);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        subLbl.setForeground(COLOR_SECONDARY);

        titleGroup.add(titleLbl);
        titleGroup.add(subLbl);

        leftHeader.add(iconBox);
        leftHeader.add(titleGroup);

        JPanel pillTag = createPillBadge(pillText, COLOR_SECONDARY, COLOR_BADGE_BG, false, null);

        topRow.add(leftHeader, BorderLayout.WEST);
        topRow.add(pillTag, BorderLayout.EAST);

        // Description
        JLabel descLbl = new JLabel("<html><body style='width: 240px; line-height: 1.4; color: #6B7280;'>"
                + description + "</body></html>");
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLbl.setBorder(new EmptyBorder(12, 0, 14, 0));

        // Footer Row
        JPanel footerRow = new JPanel(new BorderLayout());
        footerRow.setOpaque(false);

        JLabel actionLbl = new JLabel(actionText);
        actionLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionLbl.setForeground(COLOR_MAIN_TEXT);
        actionLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel hintLbl = new JLabel(footerHint);
        hintLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hintLbl.setForeground(COLOR_SECONDARY);

        footerRow.add(actionLbl, BorderLayout.WEST);
        footerRow.add(hintLbl, BorderLayout.EAST);

        card.add(topRow, BorderLayout.NORTH);
        card.add(descLbl, BorderLayout.CENTER);
        card.add(footerRow, BorderLayout.SOUTH);

        return card;
    }

    // ==========================================
    // 4. FOOTER
    // ==========================================
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 32, 12, 32));

        JPanel leftStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftStatus.setOpaque(false);

        JLabel dot = new JLabel("●");
        dot.setForeground(COLOR_ACCENT_GREEN);
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel statusText = new JLabel("ShopSphere Enterprise Gateway  •  Connected to Master Node (Cluster US-East-1)");
        statusText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusText.setForeground(COLOR_SECONDARY);

        leftStatus.add(dot);
        leftStatus.add(statusText);

        JLabel rightText = new JLabel("© 2026 ShopSphere. All rights reserved.  •  Privacy Policy  •  Terms of Service");
        rightText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rightText.setForeground(COLOR_SECONDARY);

        footer.add(leftStatus, BorderLayout.WEST);
        footer.add(rightText, BorderLayout.EAST);

        return footer;
    }

    // ==========================================
    // 5. NAVIGATION HOOKS
    // ==========================================
    private void onCustomerLoginClicked() {
        new CustomerLoginPage().setVisible(true);
        this.dispose();
    }

    private void onCreateAccountClicked() {
         new CustomerRegistrationPage().setVisible(true);
         this.dispose();
    }

    private void onAdminLoginClicked() {
         JOptionPane.showMessageDialog(this,
            "Admin Login / ERP Portal screen can be connected next!",
            "Admin Access", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==========================================
    // 6. BUTTONS WITH IKONLI SUPPORT
    // ==========================================
    private JButton createPrimaryButton(String text, Feather icon) {
        JButton button = new JButton(text, FontIcon.of(icon, 15, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_PRIMARY_HOVER : COLOR_PRIMARY_BLUE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        configureButton(button, Color.WHITE);
        return button;
    }

    private JButton createOutlineButton(String text, Feather icon) {
        JButton button = new JButton(text, FontIcon.of(icon, 15, COLOR_MAIN_TEXT)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_BADGE_BG : COLOR_CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(COLOR_BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        configureButton(button, COLOR_MAIN_TEXT);
        return button;
    }

    private JButton createGhostButton(String text, Feather icon) {
        JButton button = new JButton(text, FontIcon.of(icon, 15, COLOR_SECONDARY)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(COLOR_BADGE_BG);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        configureButton(button, COLOR_SECONDARY);
        return button;
    }

    private void configureButton(JButton button, Color fg) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(fg);
        button.setIconTextGap(8);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(9, 14, 9, 14));
    }

    private JPanel createPillBadge(String text, Color textColor, Color bgColor, boolean roundedFull, Feather optionalIcon) {
        RoundedPanel badge = new RoundedPanel(roundedFull ? 20 : 8, bgColor, COLOR_BORDER);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));

        if (optionalIcon != null) {
            JLabel ic = new JLabel(FontIcon.of(optionalIcon, 12, textColor));
            badge.add(ic);
        }

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(textColor);
        badge.add(lbl);
        return badge;
    }

    private JComponent createDot(Color color) {
        return new JComponent() {
            { setPreferredSize(new Dimension(8, 8)); }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(0, 0, 8, 8);
                g2.dispose();
            }
        };
    }

    // ==========================================
    // 7. ROUNDED CONTAINER HELPER
    // ==========================================
    public static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color backgroundColor;
        private Color borderColor = null;

        public RoundedPanel(int radius, Color bg) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bg;
            setOpaque(false);
        }

        public RoundedPanel(int radius, Color bg, Color border) {
            this(radius, bg);
            this.borderColor = border;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
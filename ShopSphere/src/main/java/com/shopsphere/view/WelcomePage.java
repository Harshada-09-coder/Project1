package com.shopsphere.view;

import com.shopsphere.util.ImageUtils;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class WelcomePage extends JFrame {

    public static final Color COLOR_PRIMARY_BLUE = new Color(0x75, 0xA2, 0xC9);
    public static final Color COLOR_PRIMARY_HOVER = new Color(0x60, 0x8E, 0xB5);
    public static final Color COLOR_LIGHT_BLUE   = new Color(0xD6, 0xE6, 0xF2);
    public static final Color COLOR_BACKGROUND   = new Color(0xF2, 0xF7, 0xFB);
    public static final Color COLOR_CARD_BG      = Color.WHITE;
    public static final Color COLOR_MAIN_TEXT    = new Color(0x24, 0x34, 0x4D);
    public static final Color COLOR_SECONDARY    = new Color(0x6B, 0x72, 0x80);
    public static final Color COLOR_BORDER       = new Color(0xE1, 0xEA, 0xF2);
    public static final Color COLOR_ACCENT_GREEN = new Color(0x10, 0xB9, 0x81);
    public static final Color COLOR_BADGE_BG     = new Color(0xEB, 0xF3, 0xFA);

    public WelcomePage() {
        setTitle("ShopSphere - Enterprise Desktop Gateway");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int targetWidth = Math.min(1366, (int) (screenSize.width * 0.90));
        int targetHeight = Math.min(880, (int) (screenSize.height * 0.90));
        setSize(targetWidth, targetHeight);
        setMinimumSize(new Dimension(1024, 680));
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(COLOR_BACKGROUND);

        rootPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        rootPanel.add(createScrollableBody(), BorderLayout.CENTER);
        rootPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        setContentPane(rootPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new WelcomePage().setVisible(true);
        });
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 32, 10, 32));

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

        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightBox.setOpaque(false);

        JPanel statusPill = createPillBadge("Master Node: Online (99.8%)", COLOR_ACCENT_GREEN, COLOR_CARD_BG, true, Feather.ACTIVITY);
        rightBox.add(statusPill);

        header.add(brandPanel, BorderLayout.WEST);
        header.add(rightBox, BorderLayout.EAST);
        return header;
    }

    private JComponent createScrollableBody() {
        JPanel bodyContainer = new JPanel();
        bodyContainer.setLayout(new BoxLayout(bodyContainer, BoxLayout.Y_AXIS));
        bodyContainer.setOpaque(false);
        bodyContainer.setBorder(new EmptyBorder(10, 36, 16, 36));

        JPanel heroRow = new JPanel(new GridBagLayout());
        heroRow.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.58;
        gbc.insets = new Insets(0, 0, 0, 20);
        heroRow.add(createHeroLeftContent(), gbc);

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
                + "catalog browser, and track your dispatches with complete peace of mind."
                + "</body></html>");
        subDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

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

    private JPanel createHeroRightCard() {
        RoundedPanel card = new RoundedPanel(22, COLOR_CARD_BG, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(420, 290));

        JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 14));
        cardHeader.setOpaque(false);
        cardHeader.add(createPillBadge("Real-time Cart Sync", COLOR_ACCENT_GREEN, COLOR_BADGE_BG, true, Feather.REFRESH_CW));

        JPanel showcaseCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(0, 0, new Color(0xEE, 0xF5, 0xFB), w, h, new Color(0xD8, 0xEA, 0xF7));
                g2.setPaint(gp);
                g2.fillRoundRect(16, 4, w - 32, h - 16, 16, 16);
                g2.dispose();
            }
        };
        showcaseCanvas.setOpaque(false);
        showcaseCanvas.setLayout(new BorderLayout());

        ImageIcon heroIcon = ImageUtils.getHeroBanner(360, 180);
        if (heroIcon != null) {
            JLabel bannerLabel = new JLabel(heroIcon);
            bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            showcaseCanvas.add(bannerLabel, BorderLayout.CENTER);
        }

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

        JLabel l2 = new JLabel("Fast & Reliable Delivery");
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

    private JPanel createStatsRow() {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 32, 0));
        statsPanel.setOpaque(false);

        statsPanel.add(createStatBlock("1,000+", "Active", "Verified Catalog Items", true));
        statsPanel.add(createStatBlock("99.8%", "SLA", "Dispatch Reliability", false));
        statsPanel.add(createStatBlock("24 / 7", "", "Swing Client Sync", false));

        return statsPanel;
    }

    private JPanel createStatBlock(String bigNumber, String tag, String subtext, boolean isGreenTag) {
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setOpaque(false);

        JPanel numRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        numRow.setOpaque(false);

        JLabel numLbl = new JLabel(bigNumber);
        numLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        numLbl.setForeground(COLOR_MAIN_TEXT);
        numRow.add(numLbl);

        if (!tag.isEmpty()) {
            JPanel tagPill = createPillBadge(tag, isGreenTag ? COLOR_ACCENT_GREEN : COLOR_PRIMARY_BLUE, COLOR_BADGE_BG, true, null);
            numRow.add(tagPill);
        }

        JLabel subLbl = new JLabel(subtext);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLbl.setForeground(COLOR_SECONDARY);

        block.add(numRow);
        block.add(Box.createVerticalStrut(2));
        block.add(subLbl);
        return block;
    }

    private JPanel createFeatureCardsRow() {
        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);

        row.add(createFeatureCard("Instant Catalog Access", "Explore thousands of products with live stock tracking and filters.", Feather.COMPASS, "Browse", () -> onCustomerLoginClicked()));
        row.add(createFeatureCard("Secure Shopping Cart", "Syncs cart items directly to MySQL with transaction protection.", Feather.SHOPPING_BAG, "Order", () -> onCustomerLoginClicked()));
        row.add(createFeatureCard("Admin Inventory ERP", "Manage products, categories, stock limits, and orders easily.", Feather.SHIELD, "Manage", () -> onAdminLoginClicked()));

        return row;
    }

    private JPanel createFeatureCard(String title, String desc, Feather icon, String actionText, Runnable action) {
        RoundedPanel card = new RoundedPanel(16, COLOR_CARD_BG, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);

        RoundedPanel iconBox = new RoundedPanel(12, COLOR_BADGE_BG);
        iconBox.setPreferredSize(new Dimension(36, 36));
        iconBox.setLayout(new GridBagLayout());
        iconBox.add(new JLabel(FontIcon.of(icon, 16, COLOR_PRIMARY_BLUE.darker())));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLbl.setForeground(COLOR_MAIN_TEXT);

        JPanel titleWithIcon = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleWithIcon.setOpaque(false);
        titleWithIcon.add(iconBox);
        titleWithIcon.add(titleLbl);

        topRow.add(titleWithIcon, BorderLayout.WEST);

        JLabel descLbl = new JLabel("<html><body style='width:220px; line-height:1.4; color:#6B7280; font-size:11px;'>" + desc + "</body></html>");
        descLbl.setBorder(new EmptyBorder(8, 0, 8, 0));

        JPanel footerRow = new JPanel(new BorderLayout());
        footerRow.setOpaque(false);

        JButton actionBtn = new JButton(actionText + " ➔");
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        actionBtn.setForeground(COLOR_PRIMARY_BLUE.darker());
        actionBtn.setContentAreaFilled(false);
        actionBtn.setBorderPainted(false);
        actionBtn.setFocusPainted(false);
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionBtn.addActionListener(e -> action.run());

        footerRow.add(actionBtn, BorderLayout.WEST);

        card.add(topRow, BorderLayout.NORTH);
        card.add(descLbl, BorderLayout.CENTER);
        card.add(footerRow, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createFooterPanel() {
        return UIUtils.createCommonFooter();
    }

    private void onCustomerLoginClicked() {
        new CustomerLoginPage().setVisible(true);
        this.dispose();
    }

    private void onCreateAccountClicked() {
        new CustomerRegistrationPage().setVisible(true);
        this.dispose();
    }

    private void onAdminLoginClicked() {
        new AdminLoginPage().setVisible(true);
        this.dispose();
    }

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

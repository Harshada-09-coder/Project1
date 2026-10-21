package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class CustomerLoginPage extends JFrame {

    public static final Color COLOR_PRIMARY      = new Color(0x78, 0xA5, 0xCE); // #78A5CE
    public static final Color COLOR_PRIMARY_DARK = new Color(0x5F, 0x8E, 0xB8);
    public static final Color COLOR_BG           = new Color(0xF3, 0xF6, 0xFA);
    public static final Color COLOR_CARD         = Color.WHITE;
    public static final Color COLOR_TEXT_MAIN    = new Color(0x1F, 0x2D, 0x42);
    public static final Color COLOR_TEXT_MUTED   = new Color(0x71, 0x7E, 0x93);
    public static final Color COLOR_BORDER       = new Color(0xE3, 0xEC, 0xF4);
    public static final Color COLOR_INPUT_BG     = new Color(0xF4, 0xF7, 0xFB);
    public static final Color COLOR_PILL_BG      = new Color(0xEB, 0xF3, 0xFA);
    public static final Color COLOR_GREEN        = new Color(0x10, 0xB9, 0x81);

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private boolean showPassword = false;

    public CustomerLoginPage() {
        setTitle("ShopSphere - Customer Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 840);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BG);

        // Header
        root.add(createHeader(), BorderLayout.NORTH);

        // Center Wrapper - Card Layout horizontally centered (Stitch Style)
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(10, 30, 10, 30));

        JPanel splitContainer = new JPanel(new GridLayout(1, 2, 28, 0));
        splitContainer.setOpaque(false);
        splitContainer.setPreferredSize(new Dimension(1060, 580)); // Fixed proportion

        splitContainer.add(createLeftShowcaseCard());
        splitContainer.add(createRightLoginFormCard());

        centerWrapper.add(splitContainer);
        root.add(centerWrapper, BorderLayout.CENTER);

        // Footer
        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ==========================================================
    // TOP HEADER
    // ==========================================================
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 40, 8, 40));

        // Brand (Left)
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        RoundedPanel logoBox = new RoundedPanel(12, new Color(0xD7, 0xE7, 0xF5));
        logoBox.setPreferredSize(new Dimension(34, 34));
        logoBox.setLayout(new GridBagLayout());
        logoBox.add(new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, COLOR_PRIMARY_DARK)));

        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel divider = new JLabel("|");
        divider.setForeground(new Color(0xD0, 0xDC, 0xEA));
        divider.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel sub = new JLabel("Your Everyday Shopping Destination");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(COLOR_TEXT_MUTED);

        brand.add(logoBox);
        brand.add(title);
        brand.add(divider);
        brand.add(sub);

        // Right Status Pill
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        RoundedPanel pill = new RoundedPanel(20, COLOR_CARD, COLOR_BORDER);
        pill.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel dot = new JLabel("●");
        dot.setForeground(COLOR_GREEN);
        dot.setFont(new Font("Segoe UI", Font.BOLD, 11));
        JLabel statusText = new JLabel("Master Node: Online (99.8%)");
        statusText.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusText.setForeground(COLOR_TEXT_MAIN);
        pill.add(dot);
        pill.add(statusText);

        right.add(pill);
        header.add(brand, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    // ==========================================================
    // LEFT CARD: BRANDING & 3D BAGS ART
    // ==========================================================
    private JPanel createLeftShowcaseCard() {
        RoundedPanel card = new RoundedPanel(24, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(26, 30, 26, 30));

        // Top info
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

        // Back Link
        JButton btnBack = new JButton("← Back to Home");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setForeground(COLOR_TEXT_MUTED);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBack.setBorder(new EmptyBorder(0, 0, 8, 0));
        btnBack.addActionListener(e -> {
            new WelcomePage().setVisible(true);
            dispose();
        });

        // Tag
        RoundedPanel tagPill = new RoundedPanel(12, COLOR_PILL_BG);
        tagPill.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
        tagPill.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel tag = new JLabel("CUSTOMER ACCESS GATEWAY");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        tag.setForeground(COLOR_PRIMARY_DARK);
        tagPill.add(tag);

        JLabel h1 = new JLabel("Welcome Back!");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 28));
        h1.setForeground(COLOR_TEXT_MAIN);
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("<html><body style='width:380px; color:#717E93; line-height:1.45; font-size:11px;'>"
                + "Log in to continue shopping, explore products, and manage your orders with automated desktop synchronization."
                + "</body></html>");
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 3 Compact Badges
        JPanel pillsRow = new JPanel(new GridLayout(1, 3, 10, 0));
        pillsRow.setOpaque(false);
        pillsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        pillsRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));

        pillsRow.add(createMiniBadge("Personalized Feed", Feather.CHECK_CIRCLE));
        pillsRow.add(createMiniBadge("Saved Cart & Wishlist", Feather.SHOPPING_BAG));
        pillsRow.add(createMiniBadge("Real-Time Tracking", Feather.TRUCK));

        top.add(btnBack);
        top.add(tagPill);
        top.add(Box.createVerticalStrut(10));
        top.add(h1);
        top.add(Box.createVerticalStrut(6));
        top.add(desc);
        top.add(Box.createVerticalStrut(16));
        top.add(pillsRow);

        // Center 3D Bags Illustration Box
        JPanel artContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Soft pastel studio backdrop
                GradientPaint bgGrad = new GradientPaint(0, 0, new Color(0xEE, 0xF5, 0xFB), w, h, new Color(0xD8, 0xE9, 0xF7));
                g2.setPaint(bgGrad);
                g2.fillRoundRect(0, 0, w, h, 18, 18);

                // Shadows below gifts
                g2.setColor(new Color(0xC7, 0xDA, 0xEC));
                g2.fillOval(w / 2 - 130, h - 35, 260, 22);

                // Big Shopping Bag (Left)
                int bagX = w / 2 - 110;
                int bagY = h / 2 - 60;
                g2.setColor(new Color(0xA9, 0xCE, 0xEB)); // Main Blue Bag
                g2.fillRoundRect(bagX, bagY, 110, 130, 14, 14);

                // Bag Handles
                g2.setColor(new Color(0x8D, 0xB6, 0xD8));
                g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(bagX + 22, bagY - 30, 66, 55, 0, 180);

                // Checkmark circle badge on bag
                g2.setColor(Color.WHITE);
                g2.fillOval(bagX + 35, bagY + 45, 40, 40);
                g2.setColor(new Color(0xA9, 0xCE, 0xEB));
                g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(bagX + 48, bagY + 65, bagX + 54, bagY + 72);
                g2.drawLine(bagX + 54, bagY + 72, bagX + 64, bagY + 58);

                // Stacked Gift Boxes (Right)
                int boxX = w / 2 + 15;
                // Bottom Box (White)
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(boxX, bagY + 65, 85, 65, 10, 10);
                g2.setColor(new Color(0xD8, 0xE8, 0xF6));
                g2.fillRect(boxX + 37, bagY + 65, 12, 65); // Ribbon

                // Middle Box (Slate Blue)
                g2.setColor(new Color(0x76, 0x9F, 0xC4));
                g2.fillRoundRect(boxX + 5, bagY + 15, 75, 52, 10, 10);
                g2.setColor(new Color(0x9E, 0xC3, 0xE5));
                g2.fillRect(boxX + 36, bagY + 15, 12, 52); // Ribbon

                // Top Box (Light Pastel Blue)
                g2.setColor(new Color(0xC7, 0xE2, 0xF7));
                g2.fillRoundRect(boxX + 16, bagY - 25, 54, 42, 8, 8);
                g2.setColor(Color.WHITE);
                g2.fillRect(boxX + 38, bagY - 25, 9, 42);

                g2.dispose();
            }
        };
        artContainer.setOpaque(false);
        artContainer.setPreferredSize(new Dimension(420, 240));

        card.add(top, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(16), BorderLayout.CENTER);
        card.add(artContainer, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createMiniBadge(String text, Feather icon) {
        RoundedPanel badge = new RoundedPanel(12, COLOR_INPUT_BG, COLOR_BORDER);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 8));

        JLabel ic = new JLabel(FontIcon.of(icon, 13, COLOR_PRIMARY_DARK));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(COLOR_TEXT_MAIN);

        badge.add(ic);
        badge.add(lbl);
        return badge;
    }

    // ==========================================================
    // RIGHT CARD: LOGIN FORM
    // ==========================================================
    private JPanel createRightLoginFormCard() {
        RoundedPanel card = new RoundedPanel(24, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(34, 42, 30, 42));

        JLabel title = new JLabel("Customer Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Enter your credentials to access your account.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Fields
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();

        JPanel fieldEmail = createInputField("Email Address", "Registered account", txtEmail, Feather.MAIL, false);
        JPanel fieldPass  = createInputField("Password", "Required", txtPassword, Feather.LOCK, true);

        // Remember Me & Forgot Password
        JPanel optionsRow = new JPanel(new BorderLayout());
        optionsRow.setOpaque(false);
        optionsRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 26));
        optionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JCheckBox chkRemember = new JCheckBox("Remember Me");
        chkRemember.setOpaque(false);
        chkRemember.setFocusPainted(false);
        chkRemember.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkRemember.setForeground(COLOR_TEXT_MUTED);

        JLabel lblForgot = new JLabel("Forgot Password?");
        lblForgot.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblForgot.setForeground(COLOR_PRIMARY_DARK);
        lblForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        optionsRow.add(chkRemember, BorderLayout.WEST);
        optionsRow.add(lblForgot, BorderLayout.EAST);

        // Login Button
        JButton btnLogin = new JButton("Log In ➔") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_PRIMARY_DARK : COLOR_PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> {
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });

        // Bottom Nav Link
        JPanel bottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        bottomNav.setOpaque(false);
        bottomNav.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNoAcc = new JLabel("Don't have an account?");
        lblNoAcc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNoAcc.setForeground(COLOR_TEXT_MUTED);

        JLabel lblCreate = new JLabel("Create Account");
        lblCreate.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCreate.setForeground(COLOR_PRIMARY_DARK);
        lblCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCreate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CustomerRegistrationPage().setVisible(true);
                dispose();
            }
        });

        bottomNav.add(lblNoAcc);
        bottomNav.add(lblCreate);

        // Assembly
        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(28));
        card.add(fieldEmail);
        card.add(Box.createVerticalStrut(16));
        card.add(fieldPass);
        card.add(Box.createVerticalStrut(10));
        card.add(optionsRow);
        card.add(Box.createVerticalStrut(28));
        card.add(btnLogin);
        card.add(Box.createVerticalGlue());
        card.add(bottomNav);

        return card;
    }

    private JPanel createInputField(String label, String rightHint, JTextField input, Feather icon, boolean isPassword) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.setMaximumSize(new Dimension(Short.MAX_VALUE, 66));
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(COLOR_TEXT_MAIN);

        JLabel r = new JLabel(rightHint);
        r.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        r.setForeground(COLOR_TEXT_MUTED);

        top.add(l, BorderLayout.WEST);
        top.add(r, BorderLayout.EAST);

        RoundedPanel box = new RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        box.setLayout(new BorderLayout());
        box.setBorder(new EmptyBorder(6, 12, 6, 12));

        JLabel ic = new JLabel(FontIcon.of(icon, 14, COLOR_TEXT_MUTED));
        input.setOpaque(false);
        input.setBorder(new EmptyBorder(0, 10, 0, 0));
        input.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        input.setForeground(COLOR_TEXT_MAIN);

        box.add(ic, BorderLayout.WEST);
        box.add(input, BorderLayout.CENTER);

        if (isPassword) {
            JLabel eyeIcon = new JLabel(FontIcon.of(Feather.EYE, 14, COLOR_TEXT_MUTED));
            eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            eyeIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPassword = !showPassword;
                    ((JPasswordField) input).setEchoChar(showPassword ? (char) 0 : '•');
                    eyeIcon.setIcon(FontIcon.of(showPassword ? Feather.EYE_OFF : Feather.EYE, 14, COLOR_TEXT_MUTED));
                }
            });
            box.add(eyeIcon, BorderLayout.EAST);
        }

        wrap.add(top);
        wrap.add(Box.createVerticalStrut(4));
        wrap.add(box);
        return wrap;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 40, 14, 40));

        JLabel left = new JLabel("●  ShopSphere Enterprise Gateway — Connected to Master Node (Cluster US-East-1)");
        left.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        left.setForeground(COLOR_TEXT_MUTED);

        JLabel right = new JLabel("© 2026 ShopSphere. All rights reserved.   •   Privacy Policy   •   Terms of Service");
        right.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        right.setForeground(COLOR_TEXT_MUTED);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    public static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color bg;
        private Color border = null;

        public RoundedPanel(int radius, Color bg) {
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }

        public RoundedPanel(int radius, Color bg, Color border) {
            this(radius, bg);
            this.border = border;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            if (border != null) {
                g2.setColor(border);
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, getHeight() - 1, radius, radius));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class CustomerRegistrationPage extends JFrame {

    public static final Color COLOR_PRIMARY      = new Color(0x78, 0xA5, 0xCE);
    public static final Color COLOR_PRIMARY_DARK = new Color(0x5F, 0x8E, 0xB8);
    public static final Color COLOR_BG           = new Color(0xF3, 0xF6, 0xFA);
    public static final Color COLOR_CARD         = Color.WHITE;
    public static final Color COLOR_TEXT_MAIN    = new Color(0x1F, 0x2D, 0x42);
    public static final Color COLOR_TEXT_MUTED   = new Color(0x71, 0x7E, 0x93);
    public static final Color COLOR_BORDER       = new Color(0xE3, 0xEC, 0xF4);
    public static final Color COLOR_INPUT_BG     = new Color(0xF4, 0xF7, 0xFB);
    public static final Color COLOR_PILL_BG      = new Color(0xEB, 0xF3, 0xFA);
    public static final Color COLOR_GREEN        = new Color(0x10, 0xB9, 0x81);

    public CustomerRegistrationPage() {
        setTitle("ShopSphere - Create Your Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 840);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BG);

        // Header
        root.add(createHeader(), BorderLayout.NORTH);

        // Center Content fixed proportional grid
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(10, 30, 10, 30));

        JPanel splitContainer = new JPanel(new GridLayout(1, 2, 28, 0));
        splitContainer.setOpaque(false);
        splitContainer.setPreferredSize(new Dimension(1060, 600));

        splitContainer.add(createLeftShowcaseCard());
        splitContainer.add(createRightRegisterFormCard());

        centerWrapper.add(splitContainer);
        root.add(centerWrapper, BorderLayout.CENTER);

        // Footer
        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 40, 8, 40));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        CustomerLoginPage.RoundedPanel logoBox = new CustomerLoginPage.RoundedPanel(12, new Color(0xD7, 0xE7, 0xF5));
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

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        CustomerLoginPage.RoundedPanel pill = new CustomerLoginPage.RoundedPanel(20, COLOR_CARD, COLOR_BORDER);
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

    private JPanel createLeftShowcaseCard() {
        CustomerLoginPage.RoundedPanel card = new CustomerLoginPage.RoundedPanel(24, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(26, 30, 22, 30));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

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

        CustomerLoginPage.RoundedPanel tagPill = new CustomerLoginPage.RoundedPanel(12, COLOR_PILL_BG);
        tagPill.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
        tagPill.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel tag = new JLabel("NEW CUSTOMER GATEWAY");
        tag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        tag.setForeground(COLOR_PRIMARY_DARK);
        tagPill.add(tag);

        JLabel h1 = new JLabel("Join the ShopSphere Community");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 23));
        h1.setForeground(COLOR_TEXT_MAIN);
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("<html><body style='width:380px; color:#717E93; line-height:1.4; font-size:11px;'>"
                + "Create your account to discover products, manage your cart, and keep track of your orders with automated desktop synchronization."
                + "</body></html>");
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pillsRow = new JPanel(new GridLayout(1, 3, 10, 0));
        pillsRow.setOpaque(false);
        pillsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        pillsRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));

        pillsRow.add(createMiniBadge("Personalized Feed", Feather.CHECK_CIRCLE));
        pillsRow.add(createMiniBadge("Fast Cart Express", Feather.SHOPPING_BAG));
        pillsRow.add(createMiniBadge("Real-Time Tracking", Feather.TRUCK));

        top.add(btnBack);
        top.add(tagPill);
        top.add(Box.createVerticalStrut(8));
        top.add(h1);
        top.add(Box.createVerticalStrut(4));
        top.add(desc);
        top.add(Box.createVerticalStrut(14));
        top.add(pillsRow);

        // Center 3D Gift Art
        JPanel artContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                GradientPaint bgGrad = new GradientPaint(0, 0, new Color(0xEE, 0xF5, 0xFB), w, h, new Color(0xD8, 0xE9, 0xF7));
                g2.setPaint(bgGrad);
                g2.fillRoundRect(0, 0, w, h, 18, 18);

                g2.setColor(new Color(0xC7, 0xDA, 0xEC));
                g2.fillOval(w / 2 - 120, h - 30, 240, 20);

                int bagX = w / 2 - 105;
                int bagY = h / 2 - 50;

                // White Shopping Bag
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(bagX, bagY, 100, 115, 14, 14);

                // Handle
                g2.setColor(new Color(0xBD, 0xD4, 0xEA));
                g2.setStroke(new BasicStroke(3.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(bagX + 20, bagY - 26, 60, 48, 0, 180);

                // Check circle
                g2.setColor(new Color(0xBD, 0xD4, 0xEA));
                g2.fillOval(bagX + 32, bagY + 40, 36, 36);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(bagX + 43, bagY + 58, bagX + 49, bagY + 64);
                g2.drawLine(bagX + 49, bagY + 64, bagX + 59, bagY + 52);

                // Stacked Gift Boxes (Right)
                int boxX = w / 2 + 10;
                // Bottom Box (Pastel Blue)
                g2.setColor(new Color(0xB5, 0xD4, 0xEE));
                g2.fillRoundRect(boxX, bagY + 55, 75, 60, 10, 10);

                // Middle Box (Slate Blue)
                g2.setColor(new Color(0x76, 0x9F, 0xC4));
                g2.fillRoundRect(boxX + 5, bagY + 10, 66, 46, 8, 8);

                // Top Box (Pale Cyan)
                g2.setColor(new Color(0xD3, 0xE7, 0xF8));
                g2.fillRoundRect(boxX + 14, bagY - 25, 48, 36, 6, 6);

                g2.dispose();
            }
        };
        artContainer.setOpaque(false);
        artContainer.setPreferredSize(new Dimension(420, 210));

        // Bottom TLS Pill
        JPanel tlsRow = new JPanel(new BorderLayout());
        tlsRow.setOpaque(false);
        tlsRow.setBorder(new EmptyBorder(10, 4, 0, 4));

        JLabel l1 = new JLabel("🛡  End-to-End Encrypted Session");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l1.setForeground(COLOR_TEXT_MUTED);

        JLabel l2 = new JLabel("TLS 1.3 Active");
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        l2.setForeground(COLOR_TEXT_MUTED);

        tlsRow.add(l1, BorderLayout.WEST);
        tlsRow.add(l2, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);
        card.add(artContainer, BorderLayout.CENTER);
        card.add(tlsRow, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createMiniBadge(String text, Feather icon) {
        CustomerLoginPage.RoundedPanel badge = new CustomerLoginPage.RoundedPanel(12, COLOR_INPUT_BG, COLOR_BORDER);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 8));

        JLabel ic = new JLabel(FontIcon.of(icon, 13, COLOR_PRIMARY_DARK));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(COLOR_TEXT_MAIN);

        badge.add(ic);
        badge.add(lbl);
        return badge;
    }

    private JPanel createRightRegisterFormCard() {
        CustomerLoginPage.RoundedPanel card = new CustomerLoginPage.RoundedPanel(24, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(26, 38, 22, 38));

        // Top Row: Title + Step 1 of 1
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setOpaque(false);
        topHeader.setMaximumSize(new Dimension(Short.MAX_VALUE, 28));
        topHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel step = new JLabel("● Step 1 of 1");
        step.setFont(new Font("Segoe UI", Font.BOLD, 11));
        step.setForeground(COLOR_PRIMARY_DARK);

        topHeader.add(title, BorderLayout.WEST);
        topHeader.add(step, BorderLayout.EAST);

        JLabel sub = new JLabel("Enter your details to get started with instant access.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(COLOR_TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Inputs
        JPanel fName  = createCompactField("Full Name", "Required", new JTextField(), Feather.USER, false);
        JPanel fEmail = createCompactField("Email Address", "Work or personal", new JTextField(), Feather.MAIL, false);
        JPanel fPhone = createCompactField("Phone Number", "SMS Updates", new JTextField(), Feather.PHONE, false);
        JPanel fPass  = createCompactField("Password", "Min. 8 characters", new JPasswordField(), Feather.LOCK, true);
        JPanel fConf  = createCompactField("Confirm Password", "Matches entry", new JPasswordField(), Feather.CHECK, true);

        // Submit Button
        JButton btnSubmit = new JButton("Create Account") {
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
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setContentAreaFilled(false);
        btnSubmit.setBorderPainted(false);
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        btnSubmit.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSubmit.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Account created successfully! Redirecting to Dashboard...", "Success", JOptionPane.INFORMATION_MESSAGE);
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });

        // Bottom link to Login
        JPanel bottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        bottomNav.setOpaque(false);
        bottomNav.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHave = new JLabel("Already have an account?");
        lblHave.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblHave.setForeground(COLOR_TEXT_MUTED);

        JLabel lblLogin = new JLabel("Log In");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblLogin.setForeground(COLOR_PRIMARY_DARK);
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CustomerLoginPage().setVisible(true);
                dispose();
            }
        });

        bottomNav.add(lblHave);
        bottomNav.add(lblLogin);

        card.add(topHeader);
        card.add(Box.createVerticalStrut(2));
        card.add(sub);
        card.add(Box.createVerticalStrut(14));
        card.add(fName);
        card.add(Box.createVerticalStrut(8));
        card.add(fEmail);
        card.add(Box.createVerticalStrut(8));
        card.add(fPhone);
        card.add(Box.createVerticalStrut(8));
        card.add(fPass);
        card.add(Box.createVerticalStrut(8));
        card.add(fConf);
        card.add(Box.createVerticalStrut(16));
        card.add(btnSubmit);
        card.add(Box.createVerticalStrut(10));
        card.add(bottomNav);

        return card;
    }

    private JPanel createCompactField(String label, String rightHint, JTextField input, Feather icon, boolean isPassword) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setOpaque(false);
        wrap.setMaximumSize(new Dimension(Short.MAX_VALUE, 52));
        wrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(COLOR_TEXT_MAIN);

        JLabel r = new JLabel(rightHint);
        r.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        r.setForeground(COLOR_TEXT_MUTED);

        top.add(l, BorderLayout.WEST);
        top.add(r, BorderLayout.EAST);

        CustomerLoginPage.RoundedPanel box = new CustomerLoginPage.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        box.setLayout(new BorderLayout());
        box.setBorder(new EmptyBorder(4, 10, 4, 10));

        JLabel ic = new JLabel(FontIcon.of(icon, 13, COLOR_TEXT_MUTED));
        input.setOpaque(false);
        input.setBorder(new EmptyBorder(0, 8, 0, 0));
        input.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        input.setForeground(COLOR_TEXT_MAIN);

        box.add(ic, BorderLayout.WEST);
        box.add(input, BorderLayout.CENTER);

        if (isPassword) {
            JLabel eye = new JLabel(FontIcon.of(Feather.EYE, 13, COLOR_TEXT_MUTED));
            eye.setCursor(new Cursor(Cursor.HAND_CURSOR));
            eye.addMouseListener(new MouseAdapter() {
                private boolean show = false;
                @Override
                public void mouseClicked(MouseEvent e) {
                    show = !show;
                    ((JPasswordField) input).setEchoChar(show ? (char) 0 : '•');
                    eye.setIcon(FontIcon.of(show ? Feather.EYE_OFF : Feather.EYE, 13, COLOR_TEXT_MUTED));
                }
            });
            box.add(eye, BorderLayout.EAST);
        }

        wrap.add(top);
        wrap.add(Box.createVerticalStrut(2));
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
}
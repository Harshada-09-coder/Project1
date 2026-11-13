package com.shopsphere.view;

import com.shopsphere.entity.User;
import com.shopsphere.service.CustomerService;
import com.shopsphere.service.ValidationException;
import com.shopsphere.util.ImageUtils;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class CustomerLoginPage extends JFrame {

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

    private final CustomerService customerService = new CustomerService();
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

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(10, 30, 10, 30));

        JPanel splitContainer = new JPanel(new GridLayout(1, 2, 28, 0));
        splitContainer.setOpaque(false);
        splitContainer.setPreferredSize(new Dimension(1060, 580));

        splitContainer.add(createLeftShowcaseCard());
        splitContainer.add(createRightLoginFormCard());

        centerWrapper.add(splitContainer);
        root.add(centerWrapper, BorderLayout.CENTER);

        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(16, 40, 8, 40));

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

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        RoundedPanel pill = new RoundedPanel(20, COLOR_CARD, COLOR_BORDER);
        pill.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel dot = new JLabel("●");
        dot.setForeground(COLOR_GREEN);
        dot.setFont(new Font("Segoe UI", Font.BOLD, 11));
        JLabel statusText = new JLabel("MySQL Database: Active");
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
        RoundedPanel card = new RoundedPanel(24, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(26, 30, 26, 30));

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

        RoundedPanel tagPill = new RoundedPanel(12, COLOR_PILL_BG);
        tagPill.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
        tagPill.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagText = new JLabel("SECURE CUSTOMER ACCESS");
        tagText.setFont(new Font("Segoe UI", Font.BOLD, 10));
        tagText.setForeground(COLOR_PRIMARY_DARK);
        tagPill.add(tagText);

        JLabel heading = new JLabel("<html><body style='width: 380px;'>"
                + "Welcome back to your favorite store."
                + "</body></html>");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(COLOR_TEXT_MAIN);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHead = new JLabel("<html><body style='width: 360px; color:#717E93; line-height: 1.45; font-size:12px;'>"
                + "Sign in to access your synchronized cart, track past deliveries, and manage account preferences."
                + "</body></html>");
        subHead.setAlignmentX(Component.LEFT_ALIGNMENT);

        top.add(btnBack);
        top.add(tagPill);
        top.add(Box.createVerticalStrut(10));
        top.add(heading);
        top.add(Box.createVerticalStrut(6));
        top.add(subHead);

        // Center Graphic
        JPanel centerGraphic = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(0, 0, new Color(0xEE, 0xF5, 0xFB), w, h, new Color(0xD8, 0xEA, 0xF7));
                g2.setPaint(gp);
                g2.fillRoundRect(10, 10, w - 20, h - 20, 18, 18);
                g2.dispose();
            }
        };
        centerGraphic.setOpaque(false);
        centerGraphic.setLayout(new BorderLayout());

        ImageIcon banner = ImageUtils.getHeroBanner(280, 200);
        if (banner != null) {
            JLabel bannerLbl = new JLabel(banner);
            bannerLbl.setHorizontalAlignment(SwingConstants.CENTER);
            centerGraphic.add(bannerLbl, BorderLayout.CENTER);
        }

        card.add(top, BorderLayout.NORTH);
        card.add(centerGraphic, BorderLayout.CENTER);

        return card;
    }

    private JPanel createRightLoginFormCard() {
        RoundedPanel card = new RoundedPanel(24, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(32, 40, 32, 40));

        JLabel title = new JLabel("Customer Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Enter your registered email and password to continue.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtEmail = new JTextField();
        txtPassword = new JPasswordField();

        JPanel fieldEmail = createInputField("Email Address", "Registered Email", txtEmail, Feather.MAIL, false);
        JPanel fieldPass  = createInputField("Password", "Case sensitive", txtPassword, Feather.LOCK, true);

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
        btnLogin.addActionListener(e -> handleLogin());

        // Pressing Enter in password triggers login
        txtPassword.addActionListener(e -> handleLogin());

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

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(28));
        card.add(fieldEmail);
        card.add(Box.createVerticalStrut(16));
        card.add(fieldPass);
        card.add(Box.createVerticalStrut(24));
        card.add(btnLogin);
        card.add(Box.createVerticalGlue());
        card.add(bottomNav);

        return card;
    }

    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String pass = new String(txtPassword.getPassword());

        try {
            User user = customerService.loginCustomer(email, pass);
            JOptionPane.showMessageDialog(this,
                    "Welcome back, " + user.getFullName() + "!",
                    "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            new CustomerDashboardPage().setVisible(true);
            dispose();
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
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
        return UIUtils.createCommonFooter();
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

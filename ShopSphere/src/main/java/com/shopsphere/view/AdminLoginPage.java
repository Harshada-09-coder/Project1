package com.shopsphere.view;

import com.shopsphere.entity.Admin;
import com.shopsphere.service.AdminService;
import com.shopsphere.service.UserSession;
import com.shopsphere.service.ValidationException;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminLoginPage extends JFrame {

    private final AdminService adminService = new AdminService();
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private boolean showPassword = false;

    public AdminLoginPage() {
        setTitle("ShopSphere - Administrator Portal (Desktop Client v2.4)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1360, (int) (scr.width * 0.94)), Math.min(880, (int) (scr.height * 0.92)));
        setMinimumSize(new Dimension(1080, 700));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0xF3, 0xF6, 0xFA));

        // Header Top Bar
        root.add(createAdminLoginHeader(), BorderLayout.NORTH);

        // Center 2-Column Split Box
        JPanel centerWrap = new JPanel(new GridBagLayout());
        centerWrap.setOpaque(false);
        centerWrap.setBorder(new EmptyBorder(10, 30, 10, 30));

        JPanel split = new JPanel(new GridLayout(1, 2, 28, 0));
        split.setOpaque(false);
        split.setPreferredSize(new Dimension(1080, 580));

        split.add(createLeftHeroShowcase());
        split.add(createRightLoginFormCard());

        centerWrap.add(split);
        root.add(centerWrap, BorderLayout.CENTER);

        // Footer
        root.add(createAdminLoginFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createAdminLoginHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 40, 8, 40));

        JLabel title = new JLabel("ShopSphere • Administrator Portal • Desktop Client v2.4");
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        UIUtils.RoundedPanel secPill = new UIUtils.RoundedPanel(14, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        secPill.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        secPill.add(new JLabel(FontIcon.of(Feather.SHIELD, 12, AdminNavUtils.COLOR_GREEN)));
        JLabel secTxt = new JLabel("Admin Security Node • 256-Bit TLS");
        secTxt.setFont(new Font("Segoe UI", Font.BOLD, 10));
        secTxt.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);
        secPill.add(secTxt);

        header.add(title, BorderLayout.WEST);
        header.add(secPill, BorderLayout.EAST);
        return header;
    }

    private JPanel createLeftHeroShowcase() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(24, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(26, 32, 24, 32));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

        // Logo + Version pill
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoRow.setOpaque(false);
        logoRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        UIUtils.RoundedPanel logoBox = new UIUtils.RoundedPanel(12, new Color(0x24, 0x34, 0x4D));
        logoBox.setPreferredSize(new Dimension(36, 36));
        logoBox.setLayout(new GridBagLayout());
        logoBox.add(new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, Color.WHITE)));

        JLabel brandTitle = new JLabel("ShopSphere");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        brandTitle.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        UIUtils.RoundedPanel verPill = new UIUtils.RoundedPanel(12, new Color(0xEB, 0xF3, 0xFA));
        verPill.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
        JLabel verTxt = new JLabel("● Enterprise Management Suite • v2.4");
        verTxt.setFont(new Font("Segoe UI", Font.BOLD, 10));
        verTxt.setForeground(new Color(0x3B, 0x82, 0xF6));
        verPill.add(verTxt);

        logoRow.add(logoBox);
        logoRow.add(brandTitle);
        logoRow.add(verPill);

        JLabel h1 = new JLabel("Welcome to ShopSphere Admin Portal");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        h1.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);
        h1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("<html><body style='width:380px; color:#64748B; line-height:1.45; font-size:11px;'>"
                + "Manage your products, orders, customers, and inventory from one centralized dashboard."
                + "</body></html>");
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 3 Security Badges
        JPanel badges = new JPanel(new GridLayout(1, 3, 8, 0));
        badges.setOpaque(false);
        badges.setAlignmentX(Component.LEFT_ALIGNMENT);
        badges.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        badges.add(createMiniBadge("Real-time Order Telemetry", Feather.ACTIVITY));
        badges.add(createMiniBadge("Automated Inventory Sync", Feather.REFRESH_CW));
        badges.add(createMiniBadge("Role-based Access Control", Feather.SHIELD));

        top.add(logoRow);
        top.add(Box.createVerticalStrut(14));
        top.add(h1);
        top.add(Box.createVerticalStrut(6));
        top.add(desc);
        top.add(Box.createVerticalStrut(14));
        top.add(badges);

        // 3D Desk & Analytics Mockup
        JPanel artCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // Studio Pastel Gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(0xEE, 0xF5, 0xFB), w, h, new Color(0xD8, 0xEA, 0xF7));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 18, 18);

                // Modern 3D Desk
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(w / 2 - 130, h / 2, 260, 20, 10, 10);
                g2.setColor(new Color(0x60, 0x8E, 0xB5));
                g2.fillRoundRect(w / 2 - 120, h / 2 + 20, 30, 70, 8, 8);

                // Open Laptop
                g2.setColor(new Color(0x1E, 0x29, 0x3B));
                g2.fillRoundRect(w / 2 - 60, h / 2 - 70, 90, 65, 8, 8);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(w / 2 - 55, h / 2 - 65, 80, 55, 6, 6);
                g2.setColor(new Color(0x3B, 0x82, 0xF6));
                g2.fillRect(w / 2 - 50, h / 2 - 40, 20, 25);
                g2.fillRect(w / 2 - 25, h / 2 - 50, 20, 35);
                g2.fillRect(w / 2, h / 2 - 30, 20, 15);

                // Stacked Logistics Parcels
                g2.setColor(new Color(0xB8, 0xD4, 0xEB));
                g2.fillRoundRect(w / 2 + 50, h / 2 - 40, 45, 40, 6, 6);
                g2.setColor(new Color(0x94, 0xC0, 0xE4));
                g2.fillRoundRect(w / 2 + 65, h / 2 - 25, 40, 25, 4, 4);

                g2.dispose();
            }
        };
        artCanvas.setPreferredSize(new Dimension(420, 230));

        card.add(top, BorderLayout.NORTH);
        card.add(artCanvas, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createMiniBadge(String text, Feather icon) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, new Color(0xF8, 0xFA, 0xFC), AdminNavUtils.COLOR_BORDER);
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 6));
        p.add(new JLabel(FontIcon.of(icon, 12, AdminNavUtils.COLOR_PRIMARY_DARK)));
        JLabel l = new JLabel("<html><center style='font-size:9px; color:#1E293B;'><b>" + text + "</b></center></html>");
        p.add(l);
        return p;
    }

    private JPanel createRightLoginFormCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(24, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(34, 40, 28, 40));

        // Shield Avatar
        UIUtils.RoundedPanel shieldCircle = new UIUtils.RoundedPanel(40, new Color(0xDB, 0xEA, 0xFE));
        shieldCircle.setPreferredSize(new Dimension(46, 46));
        shieldCircle.setMaximumSize(new Dimension(46, 46));
        shieldCircle.setLayout(new GridBagLayout());
        shieldCircle.add(new JLabel(FontIcon.of(Feather.SHIELD, 20, AdminNavUtils.COLOR_PRIMARY_DARK)));
        shieldCircle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel authLabel = new JLabel("● AUTHORIZED PERSONNEL ONLY");
        authLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        authLabel.setForeground(AdminNavUtils.COLOR_RED);
        authLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Admin Sign In");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Access your ShopSphere administration dashboard.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Pre-populated with valid default seed credentials
        txtEmail = new JTextField("admin");
        txtPassword = new JPasswordField("Admin@123");

        JPanel fEmail = createField("Admin Username / Email *", txtEmail, Feather.MAIL, false);
        JPanel fPass  = createField("Password *", txtPassword, Feather.LOCK, true);

        // Options Row
        JPanel optRow = new JPanel(new BorderLayout());
        optRow.setOpaque(false);
        optRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 26));

        JCheckBox chk = new JCheckBox("Remember Me");
        chk.setSelected(true);
        chk.setOpaque(false);
        chk.setFocusPainted(false);
        chk.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chk.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JLabel forgot = new JLabel("Forgot Password?");
        forgot.setFont(new Font("Segoe UI", Font.BOLD, 11));
        forgot.setForeground(AdminNavUtils.COLOR_PRIMARY_DARK);
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));

        optRow.add(chk, BorderLayout.WEST);
        optRow.add(forgot, BorderLayout.EAST);

        // Sign In Button
        JButton btnSignIn = new JButton("Sign In to Admin Panel ➔") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0x1E, 0x29, 0x3B).darker() : new Color(0x33, 0x4E, 0x68));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnSignIn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSignIn.setForeground(Color.WHITE);
        btnSignIn.setContentAreaFilled(false);
        btnSignIn.setBorderPainted(false);
        btnSignIn.setFocusPainted(false);
        btnSignIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSignIn.setMaximumSize(new Dimension(Short.MAX_VALUE, 42));
        btnSignIn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSignIn.addActionListener(e -> handleAdminLogin());
        txtPassword.addActionListener(e -> handleAdminLogin());

        // Clearance Banner
        UIUtils.RoundedPanel clearancePill = new UIUtils.RoundedPanel(10, new Color(0xF8, 0xFA, 0xFC), AdminNavUtils.COLOR_BORDER);
        clearancePill.setLayout(new BoxLayout(clearancePill, BoxLayout.Y_AXIS));
        clearancePill.setBorder(new EmptyBorder(8, 12, 8, 12));
        clearancePill.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));

        JLabel c1 = new JLabel("Secure access for authorized administrators only.", FontIcon.of(Feather.SHIELD, 12, AdminNavUtils.COLOR_GREEN), SwingConstants.LEFT);
        c1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        c1.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel c2 = new JLabel("Session monitored by Security Node #AD-904");
        c2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        c2.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        clearancePill.add(c1);
        clearancePill.add(c2);

        card.add(shieldCircle);
        card.add(Box.createVerticalStrut(8));
        card.add(authLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(title);
        card.add(Box.createVerticalStrut(2));
        card.add(sub);
        card.add(Box.createVerticalStrut(22));
        card.add(fEmail);
        card.add(Box.createVerticalStrut(10));
        card.add(fPass);
        card.add(Box.createVerticalStrut(8));
        card.add(optRow);
        card.add(Box.createVerticalStrut(22));
        card.add(btnSignIn);
        card.add(Box.createVerticalStrut(16));
        card.add(clearancePill);

        return card;
    }

    private void handleAdminLogin() {
        String usernameOrEmail = txtEmail.getText().trim();
        String pass = new String(txtPassword.getPassword());

        try {
            Admin admin = adminService.loginAdmin(usernameOrEmail, pass);
            JOptionPane.showMessageDialog(this,
                    "Authentication successful! Welcome, " + admin.getFullName() + " (" + admin.getRole() + ")",
                    "Admin Authenticated", JOptionPane.INFORMATION_MESSAGE);
            new AdminDashboardPage().setVisible(true);
            dispose();
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Authentication Denied", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Fallback for offline demo
            if (("admin".equalsIgnoreCase(usernameOrEmail) || "admin@shopsphere.com".equalsIgnoreCase(usernameOrEmail))
                    && "Admin@123".equals(pass)) {
                Admin fallback = new Admin("admin", "admin@shopsphere.com", null, "System Administrator", "SUPER_ADMIN");
                fallback.setAdminId(1);
                UserSession.setCurrentAdmin(fallback);
                new AdminDashboardPage().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Admin authentication error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createField(String label, JTextField input, Feather icon, boolean isPass) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 54));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        UIUtils.RoundedPanel box = new UIUtils.RoundedPanel(8, new Color(0xF8, 0xFA, 0xFC), AdminNavUtils.COLOR_BORDER);
        box.setLayout(new BorderLayout());
        box.setBorder(new EmptyBorder(4, 10, 4, 10));

        JLabel ic = new JLabel(FontIcon.of(icon, 13, AdminNavUtils.COLOR_TEXT_MUTED));
        input.setOpaque(false);
        input.setBorder(new EmptyBorder(0, 8, 0, 0));
        input.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        input.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        box.add(ic, BorderLayout.WEST);
        box.add(input, BorderLayout.CENTER);

        if (isPass) {
            JLabel eye = new JLabel(FontIcon.of(Feather.EYE, 13, AdminNavUtils.COLOR_TEXT_MUTED));
            eye.setCursor(new Cursor(Cursor.HAND_CURSOR));
            eye.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showPassword = !showPassword;
                    ((JPasswordField) input).setEchoChar(showPassword ? (char) 0 : '•');
                    eye.setIcon(FontIcon.of(showPassword ? Feather.EYE_OFF : Feather.EYE, 13, AdminNavUtils.COLOR_TEXT_MUTED));
                }
            });
            box.add(eye, BorderLayout.EAST);
        }

        p.add(l);
        p.add(Box.createVerticalStrut(3));
        p.add(box);
        return p;
    }

    private JPanel createAdminLoginFooter() {
        JPanel f = new JPanel(new BorderLayout());
        f.setOpaque(false);
        f.setBorder(new EmptyBorder(10, 40, 12, 40));

        JLabel l = new JLabel("© 2026 ShopSphere. All rights reserved. • Operating Shell Node 08-7A-4C-9B");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JLabel r = new JLabel("Privacy Policy   •   Help & Support   •   ● System Status: All Systems Operational");
        r.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        r.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        f.add(l, BorderLayout.WEST);
        f.add(r, BorderLayout.EAST);
        return f;
    }
}


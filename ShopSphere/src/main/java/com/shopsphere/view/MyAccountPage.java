package com.shopsphere.view;

import com.shopsphere.entity.User;
import com.shopsphere.service.CustomerService;
import com.shopsphere.service.UserSession;
import com.shopsphere.service.ValidationException;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class MyAccountPage extends JFrame {

    private final CustomerService customerService = new CustomerService();

    private JTextField txtFullName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;
    private JTextField txtCity;
    private JTextField txtState;
    private JTextField txtPin;

    private JPasswordField txtOldPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;

    public MyAccountPage() {
        setTitle("ShopSphere - My Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int targetW = Math.min(1380, (int) (scr.width * 0.94));
        int targetH = Math.min(920, (int) (scr.height * 0.92));
        setSize(targetW, targetH);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in to view your profile.", "Login Required", JOptionPane.INFORMATION_MESSAGE);
            new CustomerLoginPage().setVisible(true);
            dispose();
            return;
        }

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIUtils.COLOR_BG);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel mainLayout = new JPanel(new BorderLayout(16, 0));
        mainLayout.setOpaque(false);
        mainLayout.setBorder(new EmptyBorder(12, 20, 14, 20));

        mainLayout.add(createSidebar(), BorderLayout.WEST);
        mainLayout.add(createAccountScrollArea(currentUser), BorderLayout.CENTER);

        root.add(mainLayout, BorderLayout.CENTER);
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createHeader() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(UIUtils.COLOR_CARD);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtils.COLOR_BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("Customer Account Settings");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        brand.add(title);
        brand.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnHome = new JButton("Home", FontIcon.of(Feather.HOME, 13, UIUtils.COLOR_TEXT_MAIN));
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.addActionListener(e -> {
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });

        JButton btnCatalog = new JButton("Store Catalog", FontIcon.of(Feather.GRID, 13, UIUtils.COLOR_TEXT_MAIN));
        btnCatalog.setContentAreaFilled(false);
        btnCatalog.setBorderPainted(false);
        btnCatalog.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCatalog.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

        right.add(btnHome);
        right.add(btnCatalog);

        nav.add(brand, BorderLayout.WEST);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtils.COLOR_CARD);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel title = new JLabel("CUSTOMER MENU");
        title.setFont(new Font("Segoe UI", Font.BOLD, 11));
        title.setForeground(UIUtils.COLOR_TEXT_MUTED);
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(14));

        sidebar.add(createNavBtn("Store Catalog", Feather.GRID, () -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        }));
        sidebar.add(Box.createVerticalStrut(6));

        sidebar.add(createNavBtn("My Shopping Cart", Feather.SHOPPING_BAG, () -> {
            new MyCartPage().setVisible(true);
            dispose();
        }));
        sidebar.add(Box.createVerticalStrut(6));

        sidebar.add(createNavBtn("My Orders", Feather.PACKAGE, () -> {
            new MyOrdersPage().setVisible(true);
            dispose();
        }));
        sidebar.add(Box.createVerticalStrut(16));

        sidebar.add(createNavBtn("Logout", Feather.LOG_OUT, () -> {
            UserSession.logout();
            new WelcomePage().setVisible(true);
            dispose();
        }));

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton createNavBtn(String text, Feather icon, Runnable action) {
        JButton btn = new JButton(text, FontIcon.of(icon, 13, UIUtils.COLOR_TEXT_MAIN));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JComponent createAccountScrollArea(User user) {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(10, 10, 20, 10));

        // 1. Profile Information Card
        UIUtils.RoundedPanel profileCard = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel pTitle = new JLabel("Personal Information & Address");
        pTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        pTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        profileCard.add(pTitle);
        profileCard.add(Box.createVerticalStrut(14));

        txtFullName = new JTextField(user.getFullName() != null ? user.getFullName() : "");
        txtEmail = new JTextField(user.getEmail() != null ? user.getEmail() : "");
        txtEmail.setEditable(false);
        txtEmail.setBackground(UIUtils.COLOR_INPUT_BG);

        JPanel row1 = new JPanel(new GridLayout(1, 2, 12, 0));
        row1.setOpaque(false);
        row1.add(createField("Full Name *", txtFullName));
        row1.add(createField("Email Address (Registered ID)", txtEmail));
        profileCard.add(row1);
        profileCard.add(Box.createVerticalStrut(10));

        txtPhone = new JTextField(user.getPhone() != null ? user.getPhone() : "");
        txtAddress = new JTextField(user.getAddress() != null ? user.getAddress() : "");

        JPanel row2 = new JPanel(new GridLayout(1, 2, 12, 0));
        row2.setOpaque(false);
        row2.add(createField("Mobile Phone", txtPhone));
        row2.add(createField("Street Address", txtAddress));
        profileCard.add(row2);
        profileCard.add(Box.createVerticalStrut(10));

        txtCity = new JTextField(user.getCity() != null ? user.getCity() : "");
        txtState = new JTextField(user.getState() != null ? user.getState() : "");
        txtPin = new JTextField(user.getPostalCode() != null ? user.getPostalCode() : "");

        JPanel row3 = new JPanel(new GridLayout(1, 3, 12, 0));
        row3.setOpaque(false);
        row3.add(createField("City", txtCity));
        row3.add(createField("State", txtState));
        row3.add(createField("PIN / Postal Code", txtPin));
        profileCard.add(row3);
        profileCard.add(Box.createVerticalStrut(16));

        JButton btnUpdateProfile = new JButton("Update Profile Details", FontIcon.of(Feather.SAVE, 12, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x60, 0x8E, 0xB5));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnUpdateProfile.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnUpdateProfile.setForeground(Color.WHITE);
        btnUpdateProfile.setContentAreaFilled(false);
        btnUpdateProfile.setBorderPainted(false);
        btnUpdateProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdateProfile.addActionListener(e -> handleUpdateProfile(user));

        profileCard.add(btnUpdateProfile);
        body.add(profileCard);
        body.add(Box.createVerticalStrut(16));

        // 2. Change Password Card
        UIUtils.RoundedPanel secCard = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        secCard.setLayout(new BoxLayout(secCard, BoxLayout.Y_AXIS));
        secCard.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel sTitle = new JLabel("Security & Change Password");
        sTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        secCard.add(sTitle);
        secCard.add(Box.createVerticalStrut(14));

        txtOldPassword = new JPasswordField();
        txtNewPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        JPanel pwRow = new JPanel(new GridLayout(1, 3, 12, 0));
        pwRow.setOpaque(false);
        pwRow.add(createField("Current Password *", txtOldPassword));
        pwRow.add(createField("New Password *", txtNewPassword));
        pwRow.add(createField("Confirm New Password *", txtConfirmPassword));
        secCard.add(pwRow);
        secCard.add(Box.createVerticalStrut(16));

        JButton btnChangePassword = new JButton("Update Password", FontIcon.of(Feather.KEY, 12, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x33, 0x4E, 0x68));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnChangePassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnChangePassword.setForeground(Color.WHITE);
        btnChangePassword.setContentAreaFilled(false);
        btnChangePassword.setBorderPainted(false);
        btnChangePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChangePassword.addActionListener(e -> handleChangePassword(user));

        secCard.add(btnChangePassword);
        body.add(secCard);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createField(String label, JComponent comp) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(UIUtils.COLOR_TEXT_MAIN);

        p.add(l);
        p.add(Box.createVerticalStrut(3));
        p.add(comp);
        return p;
    }

    private void handleUpdateProfile(User user) {
        String name = txtFullName.getText().trim();
        String phone = txtPhone.getText().trim();
        String addr = txtAddress.getText().trim();
        String city = txtCity.getText().trim();
        String state = txtState.getText().trim();
        String pin = txtPin.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name cannot be blank.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        user.setFullName(name);
        user.setPhone(phone);
        user.setAddress(addr);
        user.setCity(city);
        user.setState(state);
        user.setPostalCode(pin);

        try {
            boolean ok = customerService.updateProfile(user);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleChangePassword(User user) {
        String oldPw = new String(txtOldPassword.getPassword()).trim();
        String newPw = new String(txtNewPassword.getPassword()).trim();
        String confPw = new String(txtConfirmPassword.getPassword()).trim();

        if (oldPw.isEmpty() || newPw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both current and new password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!newPw.equals(confPw)) {
            JOptionPane.showMessageDialog(this, "New password and confirmation do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean ok = customerService.changePassword(user.getUserId(), oldPw, newPw);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtOldPassword.setText("");
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Current password verification failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


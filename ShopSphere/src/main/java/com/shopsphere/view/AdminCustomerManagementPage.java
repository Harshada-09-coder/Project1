package com.shopsphere.view;

import com.shopsphere.entity.User;
import com.shopsphere.service.CustomerService;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminCustomerManagementPage extends JFrame {

    private final CustomerService customerService = new CustomerService();
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JLabel lblTotalUsers;
    private JLabel lblTableCount;
    private JTextField txtSearch;

    public AdminCustomerManagementPage() {
        setTitle("ShopSphere Admin - Customer Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Customers"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Customers"), BorderLayout.WEST);
        main.add(createScrollableBody(), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);

        loadCustomerData("");
    }

    private JComponent createScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // 1. Header Banner
        body.add(createHeaderActionBar());
        body.add(Box.createVerticalStrut(14));

        // 2. Metrics Row
        body.add(createMetricsRow());
        body.add(Box.createVerticalStrut(14));

        // 3. Search & Filter Bar
        body.add(createFilterToolbar());
        body.add(Box.createVerticalStrut(14));

        // 4. Customers Table Card
        body.add(createCustomersTableCard());

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createHeaderActionBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel h1 = new JLabel("Customer Management (Live Accounts)");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        h1.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("View registered buyers, profile metadata, contact info, and manage customer accounts.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(h1);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JButton btnRefresh = new JButton("Refresh", FontIcon.of(Feather.REFRESH_CW, 12, AdminNavUtils.COLOR_TEXT_MAIN));
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.addActionListener(e -> loadCustomerData(txtSearch.getText().trim()));
        right.add(btnRefresh);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createMetricsRow() {
        JPanel r = new JPanel(new GridLayout(1, 4, 14, 0));
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 85));

        lblTotalUsers = new JLabel("0");
        r.add(createMetricCard("TOTAL CUSTOMERS", lblTotalUsers, "Registered users in DB", Feather.USERS, AdminNavUtils.COLOR_PRIMARY_DARK));
        r.add(createMetricCard("ACTIVE ACCOUNTS", new JLabel("Active"), "Authorized login status", Feather.CHECK_CIRCLE, AdminNavUtils.COLOR_GREEN));
        r.add(createMetricCard("SECURITY AUDIT", new JLabel("SHA-256"), "Hashed passwords", Feather.SHIELD, AdminNavUtils.COLOR_ORANGE));
        r.add(createMetricCard("DATABASE NODE", new JLabel("MySQL"), "Connected: shopsphere", Feather.DATABASE, new Color(0x63, 0x66, 0xF1)));

        return r;
    }

    private JPanel createMetricCard(String title, JLabel valLbl, String sub, Feather icon, Color accent) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valLbl.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(t);
        left.add(valLbl);
        left.add(s);

        p.add(left, BorderLayout.CENTER);
        p.add(new JLabel(FontIcon.of(icon, 18, accent)), BorderLayout.EAST);
        return p;
    }

    private JPanel createFilterToolbar() {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout(10, 0));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 46));

        UIUtils.RoundedPanel sBox = new UIUtils.RoundedPanel(8, AdminNavUtils.COLOR_INPUT_BG, AdminNavUtils.COLOR_BORDER);
        sBox.setPreferredSize(new Dimension(340, 30));
        sBox.setLayout(new BorderLayout());
        sBox.setBorder(new EmptyBorder(0, 8, 0, 8));
        sBox.add(new JLabel(FontIcon.of(Feather.SEARCH, 12, AdminNavUtils.COLOR_TEXT_MUTED)), BorderLayout.WEST);

        txtSearch = new JTextField();
        txtSearch.setToolTipText("Search by name, email, or phone...");
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.addActionListener(e -> loadCustomerData(txtSearch.getText().trim()));
        sBox.add(txtSearch, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JButton btnDelete = new JButton("Delete Selected", FontIcon.of(Feather.TRASH_2, 12, AdminNavUtils.COLOR_RED));
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnDelete.addActionListener(e -> handleDeleteSelected());
        right.add(btnDelete);

        p.add(sBox, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createCustomersTableCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        String[] cols = {"ID", "FULL NAME", "EMAIL", "PHONE", "CITY", "REGISTERED ON"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setRowHeight(34);
        customerTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        customerTable.setShowGrid(false);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(customerTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(800, 360));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(12, 0, 0, 0));

        lblTableCount = new JLabel("Showing 0 customers");
        lblTableCount.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTableCount.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        bottom.add(lblTableCount, BorderLayout.WEST);

        card.add(scroll, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private void loadCustomerData(String searchFilter) {
        tableModel.setRowCount(0);
        try {
            List<User> users = customerService.getAllCustomers();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            int count = 0;
            String term = (searchFilter != null) ? searchFilter.toLowerCase().trim() : "";

            for (User u : users) {
                boolean match = term.isEmpty()
                        || (u.getFullName() != null && u.getFullName().toLowerCase().contains(term))
                        || (u.getEmail() != null && u.getEmail().toLowerCase().contains(term))
                        || (u.getPhone() != null && u.getPhone().toLowerCase().contains(term))
                        || (u.getCity() != null && u.getCity().toLowerCase().contains(term));

                if (match) {
                    String dateStr = (u.getCreatedAt() != null) ? sdf.format(u.getCreatedAt()) : "N/A";
                    tableModel.addRow(new Object[]{
                            u.getUserId(),
                            u.getFullName(),
                            u.getEmail(),
                            (u.getPhone() != null && !u.getPhone().isEmpty()) ? u.getPhone() : "—",
                            (u.getCity() != null && !u.getCity().isEmpty()) ? u.getCity() : "—",
                            dateStr
                    });
                    count++;
                }
            }
            lblTotalUsers.setText(String.valueOf(users.size()));
            lblTableCount.setText("Showing " + count + " of " + users.size() + " customers");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching customers: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteSelected() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete customer account #" + userId + " (" + name + ")?",
                "Confirm Customer Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = customerService.deleteCustomer(userId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCustomerData(txtSearch.getText().trim());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete customer. Ensure no foreign key constraints prevent removal.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


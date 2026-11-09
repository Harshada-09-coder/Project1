package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminOrderManagementPage extends JFrame {

    public AdminOrderManagementPage() {
        setTitle("ShopSphere Admin - Manage Orders & Fulfillment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Orders"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Orders"), BorderLayout.WEST);
        main.add(createOrdersScrollableBody(), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JComponent createOrdersScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // 1. Header Banner with Bulk Action buttons
        body.add(createHeaderActionBar());
        body.add(Box.createVerticalStrut(14));

        // 2. 5 Order KPI Cards (Total, Pending, Processing, Shipped, Delivered)
        body.add(createMetricsRow());
        body.add(Box.createVerticalStrut(14));

        // 3. Search and Filtering Toolbar
        body.add(createFilterToolbar());
        body.add(Box.createVerticalStrut(14));

        // 4. Bulk Selection Workspace Pill
        body.add(createWorkspaceSelectionBar());
        body.add(Box.createVerticalStrut(10));

        // 5. Orders Table with Popups
        body.add(createOrdersTableCard());
        body.add(Box.createVerticalStrut(14));

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

        JLabel h1 = new JLabel("Manage Orders (Live Fulfillment Queue)");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        h1.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("Track, review, and orchestrate customer orders, fulfillment pipelines, and financial reconciliations.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(h1);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JButton btnExport = new JButton("Export CSV", FontIcon.of(Feather.DOWNLOAD, 12, AdminNavUtils.COLOR_TEXT_MAIN));
        btnExport.setBackground(Color.WHITE);

        JButton btnBatchInvoices = new JButton("Batch Invoices", FontIcon.of(Feather.PRINTER, 12, AdminNavUtils.COLOR_TEXT_MAIN));
        btnBatchInvoices.setBackground(Color.WHITE);

        right.add(btnExport);
        right.add(btnBatchInvoices);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createMetricsRow() {
        JPanel r = new JPanel(new GridLayout(1, 5, 10, 0));
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 85));

        r.add(createMetricCard("TOTAL ORDERS", "1,248", "+8.2% vs last month", Feather.SHOPPING_BAG, AdminNavUtils.COLOR_PRIMARY_DARK));
        r.add(createMetricCard("PENDING", "24", "Requires Review", Feather.CLOCK, AdminNavUtils.COLOR_ORANGE));
        r.add(createMetricCard("PROCESSING", "38", "Fulfillment Queue", Feather.PACKAGE, new Color(0x63, 0x66, 0xF1)));
        r.add(createMetricCard("SHIPPED", "52", "In Transit", Feather.TRUCK, new Color(0x02, 0x84, 0xC7)));
        r.add(createMetricCard("DELIVERED", "110", "Completed", Feather.CHECK_CIRCLE, AdminNavUtils.COLOR_GREEN));

        return r;
    }

    private JPanel createMetricCard(String title, String val, String sub, Feather icon, Color accent) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 12, 10, 12));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 9));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JLabel v = new JLabel(val);
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));
        v.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(t);
        left.add(v);
        left.add(s);

        p.add(left, BorderLayout.CENTER);
        p.add(new JLabel(FontIcon.of(icon, 16, accent)), BorderLayout.EAST);
        return p;
    }

    private JPanel createFilterToolbar() {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout(10, 0));
        p.setBorder(new EmptyBorder(8, 12, 8, 12));
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 46));

        UIUtils.RoundedPanel sBox = new UIUtils.RoundedPanel(8, AdminNavUtils.COLOR_INPUT_BG, AdminNavUtils.COLOR_BORDER);
        sBox.setPreferredSize(new Dimension(320, 30));
        sBox.setLayout(new BorderLayout());
        sBox.setBorder(new EmptyBorder(0, 8, 0, 8));
        sBox.add(new JLabel(FontIcon.of(Feather.SEARCH, 12, AdminNavUtils.COLOR_TEXT_MUTED)), BorderLayout.WEST);
        JTextField search = new JTextField("Search by Order ID or Customer Name...");
        search.setBorder(null);
        search.setOpaque(false);
        sBox.add(search, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(new JComboBox<>(new String[]{"All Status", "Processing", "Shipped", "Delivered", "Cancelled"}));
        right.add(new JComboBox<>(new String[]{"All Payments", "UPI", "Credit Card", "Cash on Delivery"}));
        right.add(new JComboBox<>(new String[]{"Sort: Newest First", "Sort: Order Total"}));

        p.add(sBox, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createWorkspaceSelectionBar() {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, new Color(0xDB, 0xEA, 0xFE), AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(6, 12, 6, 12));
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 36));

        JLabel l = new JLabel("2 orders selected in workspace", FontIcon.of(Feather.CHECK_SQUARE, 12, AdminNavUtils.COLOR_PRIMARY_DARK), SwingConstants.LEFT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        rightBtns.setOpaque(false);

        JButton b1 = new JButton("Mark as Shipped");
        b1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        JButton b2 = new JButton("Print Invoices");
        b2.setFont(new Font("Segoe UI", Font.BOLD, 10));

        rightBtns.add(b1);
        rightBtns.add(b2);

        p.add(l, BorderLayout.WEST);
        p.add(rightBtns, BorderLayout.EAST);
        return p;
    }

    private JPanel createOrdersTableCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        String[] cols = {"ORDER ID", "CUSTOMER", "DATE", "ITEMS", "TOTAL", "PAYMENT", "STATUS", "ACTION"};
        Object[][] data = {
                {"ORD-1001", "Rahul Sharma", "28 Oct 2026", "2 Items", "₹4,998", "UPI (Paid)", "● Delivered", "•••"},
                {"ORD-1002", "Priya Patil", "28 Oct 2026", "1 Item", "₹3,999", "Credit Card", "● Shipped", "•••"},
                {"ORD-1003", "Amit Verma", "27 Oct 2026", "3 Items", "₹2,697", "COD (Pending)", "● Processing", "•••"},
                {"ORD-1004", "Sneha Kulkarni", "27 Oct 2026", "1 Item", "₹1,799", "UPI (Paid)", "● Processing", "•••"},
                {"ORD-1005", "Karan Mehta", "26 Oct 2026", "2 Items", "₹5,498", "Debit (Failed)", "● Cancelled", "•••"},
                {"ORD-1006", "Ananya Desai", "25 Oct 2026", "4 Items", "₹6,250", "UPI (Paid)", "● Confirmed", "•••"}
        };

        JTable table = new JTable(data, cols);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setShowGrid(false);

        // Click on Table Row opens Order Details Screen
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String orderId = (String) table.getValueAt(row, 0);
                    String cust = (String) table.getValueAt(row, 1);
                    String total = (String) table.getValueAt(row, 4);
                    new AdminOrderDetailsPage(orderId, cust, total).setVisible(true);
                    dispose();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(800, 220));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }
}
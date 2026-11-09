package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminOrderDetailsPage extends JFrame {

    public AdminOrderDetailsPage() {
        this("ORD-1002", "Priya Patil", "₹3,999.00");
    }

    public AdminOrderDetailsPage(String orderId, String customerName, String total) {
        setTitle("ShopSphere Admin - Order Details (" + orderId + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Order Details"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Orders"), BorderLayout.WEST);
        main.add(createOrderDetailsScrollableBody(orderId, customerName, total), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JComponent createOrderDetailsScrollableBody(String orderId, String customerName, String total) {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // Top Header
        body.add(createTopBanner(orderId));
        body.add(Box.createVerticalStrut(14));

        // 4 KPI Summary Cards
        body.add(createKpiSummaryCards(total));
        body.add(Box.createVerticalStrut(14));

        // 2-Column Split: Left (Items + Fulfillment Timeline) | Right (Customer Info & Addresses)
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Column (60%)
        gbc.gridx = 0;
        gbc.weightx = 0.60;
        gbc.insets = new Insets(0, 0, 0, 16);
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        left.add(createOrderedItemsCard(total));
        left.add(Box.createVerticalStrut(14));
        left.add(createTimelineTrackerCard());
        left.add(Box.createVerticalStrut(14));
        left.add(createUpdateStatusCard());

        grid.add(left, gbc);

        // Right Column (40%)
        gbc.gridx = 1;
        gbc.weightx = 0.40;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);

        right.add(createCustomerInfoCard(customerName));
        right.add(Box.createVerticalStrut(14));
        right.add(createShippingAddressCard(customerName));
        right.add(Box.createVerticalStrut(14));
        right.add(createPaymentDetailsCard());

        grid.add(right, gbc);
        body.add(grid);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createTopBanner(String orderId) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        JLabel title = new JLabel("Order #" + orderId);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        UIUtils.RoundedPanel stPill = new UIUtils.RoundedPanel(10, new Color(0xDC, 0xFC, 0xE7));
        stPill.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
        JLabel stTxt = new JLabel("● Shipped • Paid");
        stTxt.setFont(new Font("Segoe UI", Font.BOLD, 10));
        stTxt.setForeground(AdminNavUtils.COLOR_GREEN);
        stPill.add(stTxt);

        left.add(title);
        left.add(stPill);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JButton btnPrint = new JButton("Print Invoice", FontIcon.of(Feather.PRINTER, 11, AdminNavUtils.COLOR_TEXT_MAIN));
        btnPrint.setBackground(Color.WHITE);
        JButton btnBack = new JButton("← Back to Orders");
        btnBack.setBackground(Color.WHITE);
        btnBack.addActionListener(e -> {
            new AdminOrderManagementPage().setVisible(true);
            dispose();
        });

        right.add(btnPrint);
        right.add(btnBack);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createKpiSummaryCards(String total) {
        JPanel r = new JPanel(new GridLayout(1, 4, 12, 0));
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 75));

        r.add(createKpiMini("ORDER TOTAL", total, "Includes all taxes", Feather.DOLLAR_SIGN));
        r.add(createKpiMini("FULFILLMENT ITEMS", "1 Item (1 SKU)", "1 Parcel Unit", Feather.PACKAGE));
        r.add(createKpiMini("PAYMENT METHOD", "Credit Card", "Mastercard •••• 4022", Feather.CREDIT_CARD));
        r.add(createKpiMini("DELIVERY STATUS", "In Transit", "Est. 31 Oct 2026", Feather.TRUCK));

        return r;
    }

    private JPanel createKpiMini(String tag, String val, String sub, Feather icon) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(8, 12, 8, 12));

        JPanel l = new JPanel();
        l.setLayout(new BoxLayout(l, BoxLayout.Y_AXIS));
        l.setOpaque(false);

        JLabel t = new JLabel(tag);
        t.setFont(new Font("Segoe UI", Font.BOLD, 8));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JLabel v = new JLabel(val);
        v.setFont(new Font("Segoe UI", Font.BOLD, 15));
        v.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        s.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        l.add(t);
        l.add(v);
        l.add(s);

        p.add(l, BorderLayout.CENTER);
        p.add(new JLabel(FontIcon.of(icon, 16, AdminNavUtils.COLOR_PRIMARY_DARK)), BorderLayout.EAST);
        return p;
    }

    private JPanel createOrderedItemsCard(String total) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Ordered Items (1 Product) • Warehouse: BOM-West-02");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel itemRow = new JPanel(new BorderLayout());
        itemRow.setOpaque(false);
        itemRow.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        left.add(new JLabel(FontIcon.of(Feather.HEADPHONES, 24, AdminNavUtils.COLOR_PRIMARY_DARK)));

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);
        txt.add(new JLabel("AeroFlow Max Wireless Headphones"));
        txt.add(new JLabel("<html><span style='color:#64748B; font-size:10px;'>Color: Midnight Matte • SKU: PRD-001</span></html>"));
        left.add(txt);

        JLabel p = new JLabel(total);
        p.setFont(new Font("Segoe UI", Font.BOLD, 14));

        itemRow.add(left, BorderLayout.WEST);
        itemRow.add(p, BorderLayout.EAST);

        card.add(t);
        card.add(itemRow);
        card.add(new JSeparator());
        card.add(new JLabel("Grand Total: " + total));
        return card;
    }

    private JPanel createTimelineTrackerCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Order Timeline & Fulfillment Tracking (Carrier: BlueDart Express)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel steps = new JPanel(new GridLayout(1, 4, 8, 0));
        steps.setOpaque(false);
        steps.setBorder(new EmptyBorder(10, 0, 10, 0));

        steps.add(createStepPill("1. Placed", "28 Oct, 10:30 AM", true));
        steps.add(createStepPill("2. Confirmed", "28 Oct, 11:00 AM", true));
        steps.add(createStepPill("3. Processing", "28 Oct, 02:00 PM", true));
        steps.add(createStepPill("4. Shipped", "29 Oct, 09:15 AM", true));

        card.add(t);
        card.add(steps);
        card.add(new JLabel("Current Checkpoint: Departed Navi Mumbai Hub. On schedule for Baner DC."));
        return card;
    }

    private JPanel createStepPill(String title, String sub, boolean done) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel l1 = new JLabel(title);
        l1.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l1.setForeground(done ? AdminNavUtils.COLOR_GREEN : AdminNavUtils.COLOR_TEXT_MUTED);

        JLabel l2 = new JLabel(sub);
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        l2.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        p.add(l1);
        p.add(l2);
        return p;
    }

    private JPanel createUpdateStatusCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Update Order Status (Audit Log)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        r.setOpaque(false);

        JComboBox<String> cbStatus = new JComboBox<>(new String[]{"Shipped (Active)", "Delivered", "Cancelled"});
        JTextField tfRef = new JTextField("BLUEDART-882910394", 15);
        JButton btnUpdate = new JButton("Update Status");
        btnUpdate.setBackground(new Color(0x33, 0x4E, 0x68));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(e -> JOptionPane.showMessageDialog(this, "Order telemetry status successfully updated!", "Status Modified", JOptionPane.INFORMATION_MESSAGE));

        r.add(cbStatus);
        r.add(tfRef);
        r.add(btnUpdate);

        card.add(t);
        card.add(Box.createVerticalStrut(8));
        card.add(r);
        return card;
    }

    private JPanel createCustomerInfoCard(String name) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Customer Information");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel n = new JLabel(name + " (Verified Buyer)");
        n.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel em = new JLabel("priya.patil@outlook.com");
        JLabel ph = new JLabel("+91 98765 43210");

        card.add(t);
        card.add(Box.createVerticalStrut(6));
        card.add(n);
        card.add(em);
        card.add(ph);
        return card;
    }

    private JPanel createShippingAddressCard(String name) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Shipping Destination");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel a1 = new JLabel(name);
        JLabel a2 = new JLabel("Flat 204, Green Residency, Baner Road");
        JLabel a3 = new JLabel("Pune, Maharashtra - 411045");

        card.add(t);
        card.add(Box.createVerticalStrut(6));
        card.add(a1);
        card.add(a2);
        card.add(a3);
        return card;
    }

    private JPanel createPaymentDetailsCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Payment Information (Verified)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel p1 = new JLabel("Payment ID: PAY-2002");
        JLabel p2 = new JLabel("Method: Credit Card (Mastercard)");
        JLabel p3 = new JLabel("Status: Capture Settled (TLS 256-bit)");

        card.add(t);
        card.add(Box.createVerticalStrut(6));
        card.add(p1);
        card.add(p2);
        card.add(p3);
        return card;
    }
}
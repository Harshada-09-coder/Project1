package com.shopsphere.view;

import com.shopsphere.entity.OrderItem;
import com.shopsphere.entity.Orders;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ValidationException;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class AdminOrderDetailsPage extends JFrame {

    private final OrderService orderService = new OrderService();
    private final int orderId;
    private Orders currentOrder;

    private JComboBox<String> cmbOrderStatus;
    private JComboBox<String> cmbPaymentStatus;
    private JTextField txtTrackingNumber;

    public AdminOrderDetailsPage() {
        this(1);
    }

    public AdminOrderDetailsPage(int orderId) {
        this.orderId = orderId;
        setTitle("ShopSphere Admin - Order Details (#" + orderId + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        this.currentOrder = orderService.getOrderById(orderId);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        root.add(AdminNavUtils.createAdminHeader(this, "Order Details"), BorderLayout.NORTH);

        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(AdminNavUtils.createAdminSidebar(this, "Orders"), BorderLayout.WEST);
        main.add(createOrderDetailsScrollableBody(), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    public AdminOrderDetailsPage(String orderIdStr, String customerName, String total) {
        this(parseOrderId(orderIdStr));
    }

    private static int parseOrderId(String s) {
        if (s == null) return 1;
        String digits = s.replaceAll("[^0-9]", "");
        try {
            return digits.isEmpty() ? 1 : Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private JComponent createOrderDetailsScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        body.add(createTopBanner());
        body.add(Box.createVerticalStrut(14));

        if (currentOrder == null) {
            JLabel notFound = new JLabel("Order #" + orderId + " was not found in the database.");
            notFound.setFont(new Font("Segoe UI", Font.BOLD, 14));
            notFound.setForeground(AdminNavUtils.COLOR_RED);
            body.add(notFound);
            return new JScrollPane(body);
        }

        body.add(createKpiSummaryCards());
        body.add(Box.createVerticalStrut(14));

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.60;
        gbc.insets = new Insets(0, 0, 0, 16);
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        left.add(createOrderedItemsCard());
        left.add(Box.createVerticalStrut(14));
        left.add(createUpdateStatusCard());

        grid.add(left, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.40;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);

        right.add(createCustomerInfoCard());
        right.add(Box.createVerticalStrut(14));
        right.add(createShippingAddressCard());

        grid.add(right, gbc);
        body.add(grid);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createTopBanner() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

        JButton btnBack = new JButton("← Back to Orders");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            new AdminOrderManagementPage().setVisible(true);
            dispose();
        });

        JLabel title = new JLabel("Order Details: #" + orderId);
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        p.add(btnBack, BorderLayout.WEST);
        p.add(title, BorderLayout.EAST);
        return p;
    }

    private JPanel createKpiSummaryCards() {
        JPanel r = new JPanel(new GridLayout(1, 4, 12, 0));
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = (currentOrder.getOrderDate() != null) ? sdf.format(currentOrder.getOrderDate()) : "N/A";

        r.add(createMetric("ORDER TOTAL", String.format("₹%.2f", currentOrder.getTotalAmount()), AdminNavUtils.COLOR_PRIMARY_DARK));
        r.add(createMetric("PAYMENT STATUS", currentOrder.getPaymentStatus(), AdminNavUtils.COLOR_GREEN));
        r.add(createMetric("FULFILLMENT STATUS", currentOrder.getOrderStatus(), AdminNavUtils.COLOR_ORANGE));
        r.add(createMetric("ORDER DATE", dateStr, AdminNavUtils.COLOR_TEXT_MAIN));
        return r;
    }

    private JPanel createMetric(String label, String val, Color c) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(12, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 9));
        l.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JLabel v = new JLabel(val);
        v.setFont(new Font("Segoe UI", Font.BOLD, 15));
        v.setForeground(c);

        p.add(l);
        p.add(Box.createVerticalStrut(4));
        p.add(v);
        return p;
    }

    private JPanel createOrderedItemsCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Purchased Items in this Order");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        String[] cols = {"PRODUCT NAME", "UNIT PRICE", "QTY", "SUBTOTAL"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);

        if (currentOrder.getItems() != null) {
            for (OrderItem oi : currentOrder.getItems()) {
                tm.addRow(new Object[]{
                        oi.getProductName() != null ? oi.getProductName() : "Product #" + oi.getProductId(),
                        String.format("₹%.2f", oi.getUnitPrice()),
                        oi.getQuantity(),
                        String.format("₹%.2f", oi.getSubtotal())
                });
            }
        }

        JTable table = new JTable(tm);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setShowGrid(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(500, 180));

        card.add(t, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        card.add(scroll, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createUpdateStatusCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Update Order & Fulfillment Status");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);
        card.add(t);
        card.add(Box.createVerticalStrut(12));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setOpaque(false);

        cmbOrderStatus = new JComboBox<>(new String[]{"PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"});
        if (currentOrder.getOrderStatus() != null) {
            cmbOrderStatus.setSelectedItem(currentOrder.getOrderStatus().toUpperCase());
        }

        cmbPaymentStatus = new JComboBox<>(new String[]{"PENDING", "PAID", "FAILED", "REFUNDED"});
        if (currentOrder.getPaymentStatus() != null) {
            cmbPaymentStatus.setSelectedItem(currentOrder.getPaymentStatus().toUpperCase());
        }

        JButton btnUpdateStatus = new JButton("Update Status");
        btnUpdateStatus.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnUpdateStatus.addActionListener(e -> {
            String newStatus = (String) cmbOrderStatus.getSelectedItem();
            String newPay = (String) cmbPaymentStatus.getSelectedItem();
            try {
                orderService.updateOrderStatus(orderId, newStatus);
                orderService.updatePaymentStatus(orderId, newPay);
                JOptionPane.showMessageDialog(this, "Order #" + orderId + " updated successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);
                this.currentOrder = orderService.getOrderById(orderId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        row.add(new JLabel("Fulfillment:"));
        row.add(cmbOrderStatus);
        row.add(new JLabel("Payment:"));
        row.add(cmbPaymentStatus);
        row.add(btnUpdateStatus);

        card.add(row);
        card.add(Box.createVerticalStrut(10));

        JPanel trackRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        trackRow.setOpaque(false);
        txtTrackingNumber = new JTextField(currentOrder.getTrackingNumber() != null ? currentOrder.getTrackingNumber() : "", 16);
        JButton btnUpdateTracking = new JButton("Save Tracking #");
        btnUpdateTracking.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnUpdateTracking.addActionListener(e -> {
            String tr = txtTrackingNumber.getText().trim();
            orderService.updateTrackingNumber(orderId, tr);
            JOptionPane.showMessageDialog(this, "Tracking number updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        trackRow.add(new JLabel("Tracking #:"));
        trackRow.add(txtTrackingNumber);
        trackRow.add(btnUpdateTracking);
        card.add(trackRow);

        return card;
    }

    private JPanel createCustomerInfoCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Customer Summary");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);
        card.add(t);
        card.add(Box.createVerticalStrut(10));

        card.add(new JLabel("Customer ID: #" + currentOrder.getUserId()));
        card.add(Box.createVerticalStrut(4));
        card.add(new JLabel("Payment Method: " + currentOrder.getPaymentMethod()));
        card.add(Box.createVerticalStrut(4));
        card.add(new JLabel("Special Notes: " + (currentOrder.getNotes() != null && !currentOrder.getNotes().isEmpty() ? currentOrder.getNotes() : "None")));

        return card;
    }

    private JPanel createShippingAddressCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel t = new JLabel("Shipping Destination");
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);
        card.add(t);
        card.add(Box.createVerticalStrut(10));

        card.add(new JLabel(currentOrder.getShippingAddress()));
        card.add(Box.createVerticalStrut(4));
        card.add(new JLabel(currentOrder.getShippingCity() + ", " + currentOrder.getShippingState() + " " + currentOrder.getShippingPostalCode()));
        card.add(Box.createVerticalStrut(4));
        card.add(new JLabel(currentOrder.getShippingCountry()));

        return card;
    }
}


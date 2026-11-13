package com.shopsphere.view;

import com.shopsphere.entity.Orders;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ValidationException;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminOrderManagementPage extends JFrame {

    private final OrderService orderService = new OrderService();

    private JLabel lblTotalOrders;
    private JLabel lblPendingCount;
    private JLabel lblProcessingCount;
    private JLabel lblShippedCount;
    private JLabel lblDeliveredCount;

    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbStatusFilter;
    private JTextField txtSearch;

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

        loadOrderData();
    }

    private JComponent createOrdersScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // 1. Header Banner
        body.add(createHeaderActionBar());
        body.add(Box.createVerticalStrut(14));

        // 2. 5 Order KPI Cards (Total, Pending, Processing, Shipped, Delivered)
        body.add(createMetricsRow());
        body.add(Box.createVerticalStrut(14));

        // 3. Search and Filtering Toolbar
        body.add(createFilterToolbar());
        body.add(Box.createVerticalStrut(14));

        // 4. Orders Table
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

        JLabel sub = new JLabel("Track, review, and orchestrate customer orders, fulfillment pipelines, and financial status.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(h1);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JButton btnRefresh = new JButton("Refresh", FontIcon.of(Feather.REFRESH_CW, 12, AdminNavUtils.COLOR_TEXT_MAIN));
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.addActionListener(e -> loadOrderData());
        right.add(btnRefresh);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createMetricsRow() {
        JPanel r = new JPanel(new GridLayout(1, 5, 12, 0));
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 85));

        lblTotalOrders = new JLabel("0");
        lblPendingCount = new JLabel("0");
        lblProcessingCount = new JLabel("0");
        lblShippedCount = new JLabel("0");
        lblDeliveredCount = new JLabel("0");

        r.add(createMetricCard("TOTAL ORDERS", lblTotalOrders, Feather.SHOPPING_BAG, AdminNavUtils.COLOR_PRIMARY_DARK));
        r.add(createMetricCard("PENDING", lblPendingCount, Feather.CLOCK, AdminNavUtils.COLOR_ORANGE));
        r.add(createMetricCard("PROCESSING", lblProcessingCount, Feather.LOADER, new Color(0x3B, 0x82, 0xF6)));
        r.add(createMetricCard("SHIPPED", lblShippedCount, Feather.TRUCK, new Color(0x63, 0x66, 0xF1)));
        r.add(createMetricCard("DELIVERED", lblDeliveredCount, Feather.CHECK_CIRCLE, AdminNavUtils.COLOR_GREEN));

        return r;
    }

    private JPanel createMetricCard(String title, JLabel valLbl, Feather icon, Color accent) {
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

        left.add(t);
        left.add(valLbl);

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
        sBox.setPreferredSize(new Dimension(300, 30));
        sBox.setLayout(new BorderLayout());
        sBox.setBorder(new EmptyBorder(0, 8, 0, 8));
        sBox.add(new JLabel(FontIcon.of(Feather.SEARCH, 12, AdminNavUtils.COLOR_TEXT_MUTED)), BorderLayout.WEST);

        txtSearch = new JTextField();
        txtSearch.setToolTipText("Search by Order ID or City...");
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.addActionListener(e -> loadOrderData());
        sBox.add(txtSearch, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        cmbStatusFilter = new JComboBox<>(new String[]{"All Statuses", "PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"});
        cmbStatusFilter.addActionListener(e -> loadOrderData());
        right.add(cmbStatusFilter);

        JButton btnViewDetails = new JButton("View Order Details ➔");
        btnViewDetails.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnViewDetails.addActionListener(e -> openSelectedOrder());
        right.add(btnViewDetails);

        p.add(sBox, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createOrdersTableCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        String[] cols = {"ORDER ID", "USER ID", "DATE", "TOTAL", "PAY METHOD", "PAY STATUS", "STATUS", "CITY"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        ordersTable = new JTable(tableModel);
        ordersTable.setRowHeight(34);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ordersTable.setShowGrid(false);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ordersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openSelectedOrder();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(ordersTable);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(800, 320));

        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private void openSelectedOrder() {
        int row = ordersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to view.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int orderId = (int) tableModel.getValueAt(row, 0);
        new AdminOrderDetailsPage(orderId).setVisible(true);
        dispose();
    }

    private void loadOrderData() {
        tableModel.setRowCount(0);
        try {
            List<Orders> orders = orderService.getAllOrders();

            int total = orders.size();
            int pending = 0;
            int processing = 0;
            int shipped = 0;
            int delivered = 0;

            String statusFilter = (String) cmbStatusFilter.getSelectedItem();
            String term = (txtSearch != null) ? txtSearch.getText().trim().toLowerCase() : "";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            for (Orders o : orders) {
                String st = (o.getOrderStatus() != null) ? o.getOrderStatus().toUpperCase() : "PENDING";
                if (st.contains("PEND")) pending++;
                else if (st.contains("PROC")) processing++;
                else if (st.contains("SHIP")) shipped++;
                else if (st.contains("DELIV")) delivered++;

                boolean statusMatch = (statusFilter == null || statusFilter.equals("All Statuses") || st.equalsIgnoreCase(statusFilter));
                boolean textMatch = term.isEmpty()
                        || String.valueOf(o.getOrderId()).contains(term)
                        || (o.getShippingCity() != null && o.getShippingCity().toLowerCase().contains(term));

                if (statusMatch && textMatch) {
                    String dateStr = (o.getOrderDate() != null) ? sdf.format(o.getOrderDate()) : "N/A";
                    tableModel.addRow(new Object[]{
                            o.getOrderId(),
                            o.getUserId(),
                            dateStr,
                            String.format("₹%.2f", o.getTotalAmount()),
                            o.getPaymentMethod(),
                            o.getPaymentStatus(),
                            o.getOrderStatus(),
                            (o.getShippingCity() != null) ? o.getShippingCity() : "—"
                    });
                }
            }

            lblTotalOrders.setText(String.valueOf(total));
            lblPendingCount.setText(String.valueOf(pending));
            lblProcessingCount.setText(String.valueOf(processing));
            lblShippedCount.setText(String.valueOf(shipped));
            lblDeliveredCount.setText(String.valueOf(delivered));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching orders: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


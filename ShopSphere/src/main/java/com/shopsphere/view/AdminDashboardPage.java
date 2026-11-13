package com.shopsphere.view;

import com.shopsphere.entity.Orders;
import com.shopsphere.entity.Product;
import com.shopsphere.service.AdminService;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.ProductService;
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
import java.util.Map;

public class AdminDashboardPage extends JFrame {

    private final AdminService adminService = new AdminService();
    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();

    private JLabel lblTotalRevenue;
    private JLabel lblTotalOrders;
    private JLabel lblTotalProducts;
    private JLabel lblTotalCustomers;
    private DefaultTableModel ordersTableModel;
    private JPanel lowStockContainer;

    public AdminDashboardPage() {
        setTitle("ShopSphere Admin - Enterprise Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1420, (int) (scr.width * 0.95)), Math.min(940, (int) (scr.height * 0.94)));
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AdminNavUtils.COLOR_BG);

        // Header
        root.add(AdminNavUtils.createAdminHeader(this, "Dashboard"), BorderLayout.NORTH);

        // Split Layout
        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);

        main.add(AdminNavUtils.createAdminSidebar(this, "Dashboard"), BorderLayout.WEST);
        main.add(createDashboardScrollableBody(), BorderLayout.CENTER);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);

        loadDashboardMetrics();
    }

    private JComponent createDashboardScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // 1. Welcome Greeting Banner
        body.add(createGreetingBanner());
        body.add(Box.createVerticalStrut(14));

        // 2. 4 Top KPI Cards
        body.add(createKpiCardsRow());
        body.add(Box.createVerticalStrut(16));

        // 3. Analytics Row: Sales Line Chart (Left) + Donut Order Status (Right)
        JPanel chartsRow = new JPanel(new GridBagLayout());
        chartsRow.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 16);
        chartsRow.add(createSalesOverviewCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        chartsRow.add(createOrderStatusDonutCard(), gbc);

        body.add(chartsRow);
        body.add(Box.createVerticalStrut(16));

        // 4. Bottom Grid: Recent Orders Table (Left) + Low Stock & Top Selling (Right)
        JPanel bottomRow = new JPanel(new GridBagLayout());
        bottomRow.setOpaque(false);

        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 16);
        bottomRow.add(createRecentOrdersTableCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        lowStockContainer = new JPanel();
        lowStockContainer.setLayout(new BoxLayout(lowStockContainer, BoxLayout.Y_AXIS));
        lowStockContainer.setOpaque(false);

        rightPanel.add(createLowStockAlertsCard());
        rightPanel.add(Box.createVerticalStrut(14));
        rightPanel.add(createTopSellingCard());
        bottomRow.add(rightPanel, gbc);

        body.add(bottomRow);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createGreetingBanner() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        row.setOpaque(false);

        JLabel h1 = new JLabel("Welcome back, Admin!");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        h1.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        UIUtils.RoundedPanel hubTag = new UIUtils.RoundedPanel(10, new Color(0xDB, 0xEA, 0xFE));
        hubTag.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
        JLabel hubTxt = new JLabel("Enterprise Hub");
        hubTxt.setFont(new Font("Segoe UI", Font.BOLD, 10));
        hubTxt.setForeground(AdminNavUtils.COLOR_PRIMARY_DARK);
        hubTag.add(hubTxt);

        row.add(h1);
        row.add(hubTag);

        JLabel sub = new JLabel("Real-time live store analytics and management operations.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(row);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        UIUtils.RoundedPanel datePill = new UIUtils.RoundedPanel(10, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        datePill.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        datePill.add(new JLabel(FontIcon.of(Feather.CALENDAR, 12, AdminNavUtils.COLOR_TEXT_MUTED)));
        JLabel dTxt = new JLabel("Live Storefront Node • shopsphere");
        dTxt.setFont(new Font("Segoe UI", Font.BOLD, 11));
        dTxt.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);
        datePill.add(dTxt);

        right.add(datePill);
        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createKpiCardsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 14, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 110));

        lblTotalRevenue = new JLabel("₹0.00");
        lblTotalOrders = new JLabel("0");
        lblTotalProducts = new JLabel("0");
        lblTotalCustomers = new JLabel("0");

        row.add(createKpiCard("TOTAL REVENUE", lblTotalRevenue, "Paid orders volume", Feather.CREDIT_CARD, true));
        row.add(createKpiCard("TOTAL ORDERS", lblTotalOrders, "All fulfillment requests", Feather.SHOPPING_BAG, true));
        row.add(createKpiCard("TOTAL PRODUCTS", lblTotalProducts, "Catalog items in stock", Feather.BOX, false));
        row.add(createKpiCard("TOTAL CUSTOMERS", lblTotalCustomers, "Registered users in DB", Feather.USERS, true));

        return row;
    }

    private JPanel createKpiCard(String title, JLabel valLbl, String sub, Feather icon, boolean isUp) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valLbl.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel((isUp ? "↗ " : "• ") + sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(isUp ? AdminNavUtils.COLOR_GREEN : AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(t);
        left.add(Box.createVerticalStrut(4));
        left.add(valLbl);
        left.add(Box.createVerticalStrut(4));
        left.add(s);

        UIUtils.RoundedPanel icBox = new UIUtils.RoundedPanel(12, new Color(0xF1, 0xF5, 0xF9));
        icBox.setPreferredSize(new Dimension(38, 38));
        icBox.setLayout(new GridBagLayout());
        icBox.add(new JLabel(FontIcon.of(icon, 16, AdminNavUtils.COLOR_PRIMARY_DARK)));

        card.add(left, BorderLayout.CENTER);
        card.add(icBox, BorderLayout.EAST);
        return card;
    }

    private JPanel createSalesOverviewCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel t = new JLabel("Sales & Order Activity");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel legend = new JLabel("● Revenue Performance Trend");
        legend.setFont(new Font("Segoe UI", Font.BOLD, 11));
        legend.setForeground(new Color(0x3B, 0x82, 0xF6));

        top.add(t, BorderLayout.WEST);
        top.add(legend, BorderLayout.EAST);

        JPanel chartCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight() - 30;

                g2.setColor(new Color(0xF1, 0xF5, 0xF9));
                for (int y = 20; y < h; y += 40) {
                    g2.drawLine(40, y, w - 20, y);
                }

                String[] months = {"Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct"};
                int step = (w - 80) / (months.length - 1);
                int[] pointsY = {h - 30, h - 50, h - 70, h - 90, h - 110, h - 130, h - 150};

                Polygon poly = new Polygon();
                poly.addPoint(50, h);
                for (int i = 0; i < months.length; i++) {
                    int x = 50 + (i * step);
                    poly.addPoint(x, pointsY[i]);
                }
                poly.addPoint(50 + (months.length - 1) * step, h);

                GradientPaint gp = new GradientPaint(0, 0, new Color(0x93, 0xC5, 0xFD, 120), 0, h, new Color(0xBF, 0xDB, 0xFE, 20));
                g2.setPaint(gp);
                g2.fill(poly);

                g2.setColor(new Color(0x25, 0x63, 0xEB));
                g2.setStroke(new BasicStroke(2.5f));
                for (int i = 0; i < months.length - 1; i++) {
                    int x1 = 50 + (i * step);
                    int x2 = 50 + ((i + 1) * step);
                    g2.drawLine(x1, pointsY[i], x2, pointsY[i + 1]);
                    g2.fillOval(x1 - 3, pointsY[i] - 3, 6, 6);
                }
                g2.fillOval(50 + (months.length - 1) * step - 3, pointsY[months.length - 1] - 3, 6, 6);

                g2.setColor(AdminNavUtils.COLOR_TEXT_MUTED);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                for (int i = 0; i < months.length; i++) {
                    g2.drawString(months[i], 45 + (i * step), h + 18);
                }
                g2.dispose();
            }
        };
        chartCanvas.setPreferredSize(new Dimension(500, 210));
        chartCanvas.setOpaque(false);

        card.add(top, BorderLayout.NORTH);
        card.add(chartCanvas, BorderLayout.CENTER);
        return card;
    }

    private JPanel createOrderStatusDonutCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Order Status Breakdown");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JPanel donutCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                int cy = 70;
                int r = 55;

                g2.setStroke(new BasicStroke(16f));
                g2.setColor(AdminNavUtils.COLOR_GREEN);
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 0, 180);

                g2.setColor(new Color(0x3B, 0x82, 0xF6));
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 180, 90);

                g2.setColor(new Color(0xF5, 0x9E, 0x0B));
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 270, 90);

                g2.setColor(AdminNavUtils.COLOR_TEXT_MAIN);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2.drawString("Status", cx - 18, cy);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.setColor(AdminNavUtils.COLOR_TEXT_MUTED);
                g2.drawString("Breakdown", cx - 22, cy + 14);

                g2.dispose();
            }
        };
        donutCanvas.setPreferredSize(new Dimension(240, 140));
        donutCanvas.setOpaque(false);

        JPanel legendList = new JPanel(new GridLayout(3, 1, 4, 4));
        legendList.setOpaque(false);
        legendList.add(createLegendRow("● Delivered / Fulfilled", AdminNavUtils.COLOR_GREEN));
        legendList.add(createLegendRow("● Shipped / In Transit", new Color(0x3B, 0x82, 0xF6)));
        legendList.add(createLegendRow("● Processing / Pending", new Color(0xF5, 0x9E, 0x0B)));

        card.add(title, BorderLayout.NORTH);
        card.add(donutCanvas, BorderLayout.CENTER);
        card.add(legendList, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createLegendRow(String label, Color col) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(col);
        p.add(l, BorderLayout.WEST);
        return p;
    }

    private JPanel createRecentOrdersTableCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel t = new JLabel("Recent Orders");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel viewAll = new JLabel("View All Orders ➔");
        viewAll.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewAll.setForeground(AdminNavUtils.COLOR_PRIMARY_DARK);
        viewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new AdminOrderManagementPage().setVisible(true);
                dispose();
            }
        });

        top.add(t, BorderLayout.WEST);
        top.add(viewAll, BorderLayout.EAST);

        String[] cols = {"ORDER ID", "CUSTOMER ID", "AMOUNT", "PAYMENT", "STATUS", "DATE"};
        ordersTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(ordersTableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setShowGrid(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(500, 160));

        card.add(top, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        card.add(scroll, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createLowStockAlertsCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Low Stock Inventory Alerts");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        card.add(t);
        card.add(Box.createVerticalStrut(8));
        card.add(lowStockContainer);
        return card;
    }

    private JPanel createStockAlertRow(String name, String sub, String tag, boolean critical) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel n = new JLabel(name);
        n.setFont(new Font("Segoe UI", Font.BOLD, 11));
        n.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel(sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(n);
        left.add(s);

        JLabel tg = new JLabel(" " + tag + " ");
        tg.setFont(new Font("Segoe UI", Font.BOLD, 9));
        tg.setForeground(critical ? AdminNavUtils.COLOR_RED : AdminNavUtils.COLOR_ORANGE);
        tg.setOpaque(true);
        tg.setBackground(critical ? new Color(0xFE, 0xE2, 0xE2) : new Color(0xFE, 0xF3, 0xC7));

        r.add(left, BorderLayout.WEST);
        r.add(tg, BorderLayout.EAST);
        return r;
    }

    private JPanel createTopSellingCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Featured Store Products");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        card.add(t);
        card.add(Box.createVerticalStrut(8));

        try {
            List<Product> products = productService.getAllActiveProducts();
            int limit = Math.min(3, products.size());
            for (int i = 0; i < limit; i++) {
                Product p = products.get(i);
                JPanel r = new JPanel(new BorderLayout());
                r.setOpaque(false);
                JLabel n = new JLabel(p.getProductName());
                n.setFont(new Font("Segoe UI", Font.BOLD, 11));
                n.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

                JLabel pr = new JLabel(String.format("₹%.2f", p.getPrice()));
                pr.setFont(new Font("Segoe UI", Font.BOLD, 11));
                pr.setForeground(AdminNavUtils.COLOR_PRIMARY_DARK);

                r.add(n, BorderLayout.WEST);
                r.add(pr, BorderLayout.EAST);
                card.add(r);
                card.add(Box.createVerticalStrut(4));
            }
        } catch (Exception ignored) {}

        return card;
    }

    private void loadDashboardMetrics() {
        try {
            Map<String, Object> stats = adminService.getDashboardStats();
            double rev = (stats.get("totalRevenue") instanceof Number) ? ((Number) stats.get("totalRevenue")).doubleValue() : 0.0;
            int orders = (stats.get("totalOrders") instanceof Number) ? ((Number) stats.get("totalOrders")).intValue() : 0;
            int prods = (stats.get("totalProducts") instanceof Number) ? ((Number) stats.get("totalProducts")).intValue() : 0;
            int users = (stats.get("totalUsers") instanceof Number) ? ((Number) stats.get("totalUsers")).intValue() : 0;

            lblTotalRevenue.setText(String.format("₹%,.2f", rev));
            lblTotalOrders.setText(String.format("%,d", orders));
            lblTotalProducts.setText(String.format("%,d", prods));
            lblTotalCustomers.setText(String.format("%,d", users));

            // Load Recent Orders
            ordersTableModel.setRowCount(0);
            List<Orders> orderList = orderService.getAllOrders();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int max = Math.min(5, orderList.size());
            for (int i = 0; i < max; i++) {
                Orders o = orderList.get(i);
                ordersTableModel.addRow(new Object[]{
                        "#" + o.getOrderId(),
                        "User #" + o.getUserId(),
                        String.format("₹%,.2f", o.getTotalAmount()),
                        o.getPaymentMethod(),
                        o.getOrderStatus(),
                        (o.getOrderDate() != null) ? sdf.format(o.getOrderDate()) : "N/A"
                });
            }

            // Load Low Stock Alerts
            lowStockContainer.removeAll();
            List<Product> allProducts = productService.getAllActiveProducts();
            int lowCount = 0;
            for (Product p : allProducts) {
                if (p.getStockQuantity() <= 10) {
                    boolean isCritical = p.getStockQuantity() <= 3;
                    lowStockContainer.add(createStockAlertRow(
                            p.getProductName(),
                            p.getStockQuantity() + " units left",
                            isCritical ? "Critical" : "Low Stock",
                            isCritical
                    ));
                    lowStockContainer.add(Box.createVerticalStrut(4));
                    lowCount++;
                    if (lowCount >= 4) break;
                }
            }
            if (lowCount == 0) {
                JLabel okLbl = new JLabel("All inventory stock levels are healthy.");
                okLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                okLbl.setForeground(AdminNavUtils.COLOR_GREEN);
                lowStockContainer.add(okLbl);
            }
            lowStockContainer.revalidate();
            lowStockContainer.repaint();

        } catch (Exception e) {
            System.err.println("[AdminDashboardPage] Error loading metrics: " + e.getMessage());
        }
    }
}


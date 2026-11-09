package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminDashboardPage extends JFrame {

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

        JLabel sub = new JLabel("Here's what's happening with your store today.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(row);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        UIUtils.RoundedPanel datePill = new UIUtils.RoundedPanel(10, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        datePill.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
        datePill.add(new JLabel(FontIcon.of(Feather.CALENDAR, 12, AdminNavUtils.COLOR_TEXT_MUTED)));
        JLabel dTxt = new JLabel("31 October 2026   •   This Month ▼");
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

        row.add(createKpiCard("TOTAL REVENUE", "₹2,45,680", "+12.5% vs last month", Feather.CREDIT_CARD, true));
        row.add(createKpiCard("TOTAL ORDERS", "1,248", "+8.2% vs last month", Feather.SHOPPING_BAG, true));
        row.add(createKpiCard("TOTAL PRODUCTS", "356", "24 products added recently", Feather.BOX, false));
        row.add(createKpiCard("TOTAL CUSTOMERS", "892", "+15 new this week", Feather.USERS, true));

        return row;
    }

    private JPanel createKpiCard(String title, String val, String sub, Feather icon, boolean isUp) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MUTED);

        JLabel v = new JLabel(val);
        v.setFont(new Font("Segoe UI", Font.BOLD, 20));
        v.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel s = new JLabel((isUp ? "↗ " : "• ") + sub);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        s.setForeground(isUp ? AdminNavUtils.COLOR_GREEN : AdminNavUtils.COLOR_TEXT_MUTED);

        left.add(t);
        left.add(Box.createVerticalStrut(4));
        left.add(v);
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

    // 2D Vector Sales Area Chart Canvas
    private JPanel createSalesOverviewCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel t = new JLabel("Sales Overview");
        t.setFont(new Font("Segoe UI", Font.BOLD, 15));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel legend = new JLabel("● Revenue (₹2.45L)   ● Orders (1,248)");
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

                // Axis Grid Lines
                g2.setColor(new Color(0xF1, 0xF5, 0xF9));
                for (int y = 20; y < h; y += 40) {
                    g2.drawLine(40, y, w - 20, y);
                }

                // Month labels
                String[] months = {"Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct"};
                int step = (w - 80) / (months.length - 1);

                int[] pointsY = {h - 40, h - 70, h - 90, h - 110, h - 100, h - 150, h - 170};
                Polygon poly = new Polygon();
                poly.addPoint(50, h);
                for (int i = 0; i < months.length; i++) {
                    int x = 50 + (i * step);
                    poly.addPoint(x, pointsY[i]);
                }
                poly.addPoint(50 + (months.length - 1) * step, h);

                // Area Fill
                GradientPaint gp = new GradientPaint(0, 0, new Color(0x93, 0xC5, 0xFD, 120), 0, h, new Color(0xBF, 0xDB, 0xFE, 20));
                g2.setPaint(gp);
                g2.fill(poly);

                // Line Plot
                g2.setColor(new Color(0x25, 0x63, 0xEB));
                g2.setStroke(new BasicStroke(2.5f));
                for (int i = 0; i < months.length - 1; i++) {
                    int x1 = 50 + (i * step);
                    int x2 = 50 + ((i + 1) * step);
                    g2.drawLine(x1, pointsY[i], x2, pointsY[i + 1]);
                    g2.fillOval(x1 - 3, pointsY[i] - 3, 6, 6);
                }
                g2.fillOval(50 + (months.length - 1) * step - 3, pointsY[months.length - 1] - 3, 6, 6);

                // Draw X Axis labels
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

    // 2D Vector Donut Chart
    private JPanel createOrderStatusDonutCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("Order Status Overview");
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

                // Donut Segments
                g2.setStroke(new BasicStroke(16f));
                g2.setColor(AdminNavUtils.COLOR_GREEN);
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 0, 170); // Delivered

                g2.setColor(new Color(0x3B, 0x82, 0xF6));
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 170, 80); // Shipped

                g2.setColor(new Color(0xF5, 0x9E, 0x0B));
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 250, 60); // Processing

                g2.setColor(AdminNavUtils.COLOR_RED);
                g2.drawArc(cx - r, cy - r, r * 2, r * 2, 310, 50); // Cancelled

                // Center Text
                g2.setColor(AdminNavUtils.COLOR_TEXT_MAIN);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                g2.drawString("236", cx - 14, cy);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.setColor(AdminNavUtils.COLOR_TEXT_MUTED);
                g2.drawString("Total Orders", cx - 24, cy + 14);

                g2.dispose();
            }
        };
        donutCanvas.setPreferredSize(new Dimension(240, 140));
        donutCanvas.setOpaque(false);

        // Legend Stack below
        JPanel legendList = new JPanel(new GridLayout(4, 1, 4, 4));
        legendList.setOpaque(false);
        legendList.add(createLegendRow("● Delivered", "110", AdminNavUtils.COLOR_GREEN));
        legendList.add(createLegendRow("● Shipped", "52", new Color(0x3B, 0x82, 0xF6)));
        legendList.add(createLegendRow("● Processing", "38", new Color(0xF5, 0x9E, 0x0B)));
        legendList.add(createLegendRow("● Cancelled", "12", AdminNavUtils.COLOR_RED));

        card.add(title, BorderLayout.NORTH);
        card.add(donutCanvas, BorderLayout.CENTER);
        card.add(legendList, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createLegendRow(String label, String count, Color col) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(col);

        JLabel c = new JLabel(count);
        c.setFont(new Font("Segoe UI", Font.BOLD, 11));
        c.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        p.add(l, BorderLayout.WEST);
        p.add(c, BorderLayout.EAST);
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

        top.add(t, BorderLayout.WEST);
        top.add(viewAll, BorderLayout.EAST);

        // Orders Table Mock
        String[] cols = {"ORDER ID", "CUSTOMER", "DATE", "AMOUNT", "PAYMENT", "STATUS"};
        Object[][] data = {
                {"ORD-00125", "Rahul Sharma", "31 Oct 2026", "₹5,296", "UPI", "Processing"},
                {"ORD-00124", "Priya Patel", "30 Oct 2026", "₹2,499", "Card", "Delivered"},
                {"ORD-00123", "Amit Verma", "30 Oct 2026", "₹1,799", "COD", "Shipped"},
                {"ORD-00122", "Sneha Joshi", "29 Oct 2026", "₹3,250", "UPI", "Pending"}
        };
        JTable table = new JTable(data, cols);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(500, 150));

        card.add(top, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        card.add(scroll, BorderLayout.SOUTH);
        return card;
    }

    private JPanel createLowStockAlertsCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, AdminNavUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel("Low Stock Alerts (4 Items)");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        card.add(t);
        card.add(Box.createVerticalStrut(8));
        card.add(createStockAlertRow("Wireless Headphones", "5 units left", "Low Stock", false));
        card.add(Box.createVerticalStrut(6));
        card.add(createStockAlertRow("Smart Watch", "3 units left", "Critical", true));
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

        JLabel t = new JLabel("Top Selling Products");
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        card.add(t);
        card.add(Box.createVerticalStrut(8));
        card.add(createTopSellRow("Wireless Headphones", "125 sold", "₹62,500"));
        card.add(Box.createVerticalStrut(6));
        card.add(createTopSellRow("Smart Watch", "98 sold", "₹49,000"));
        return card;
    }

    private JPanel createTopSellRow(String name, String sold, String rev) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);

        JLabel n = new JLabel(name);
        n.setFont(new Font("Segoe UI", Font.BOLD, 11));
        n.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        JLabel p = new JLabel(rev);
        p.setFont(new Font("Segoe UI", Font.BOLD, 11));
        p.setForeground(AdminNavUtils.COLOR_TEXT_MAIN);

        r.add(n, BorderLayout.WEST);
        r.add(p, BorderLayout.EAST);
        return r;
    }
}
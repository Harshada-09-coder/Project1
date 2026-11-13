package com.shopsphere.view;

import com.shopsphere.entity.OrderItem;
import com.shopsphere.entity.Orders;
import com.shopsphere.entity.Product;
import com.shopsphere.service.OrderService;
import com.shopsphere.util.ImageUtils;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class OrderSuccessPage extends JFrame {

    private final OrderService orderService = new OrderService();
    private final int orderId;
    private Orders order;

    public OrderSuccessPage() {
        this(1);
    }

    public OrderSuccessPage(int orderId) {
        this.orderId = orderId;
        this.order = orderService.getOrderById(orderId);

        setTitle("ShopSphere - Order Confirmation #" + orderId);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int targetW = Math.min(1380, (int) (scr.width * 0.94));
        int targetH = Math.min(900, (int) (scr.height * 0.92));
        setSize(targetW, targetH);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIUtils.COLOR_BG);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel mainLayout = new JPanel(new BorderLayout(16, 0));
        mainLayout.setOpaque(false);
        mainLayout.setBorder(new EmptyBorder(10, 20, 12, 20));

        mainLayout.add(createSidebar(), BorderLayout.WEST);
        mainLayout.add(createScrollableBody(), BorderLayout.CENTER);

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

        JLabel sub = new JLabel("Order Receipt");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        brand.add(title);
        brand.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnCatalog = new JButton("Continue Shopping", FontIcon.of(Feather.SHOPPING_BAG, 12, UIUtils.COLOR_TEXT_MAIN));
        btnCatalog.setContentAreaFilled(false);
        btnCatalog.setBorderPainted(false);
        btnCatalog.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCatalog.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

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

        sidebar.add(createNavButton("Store Catalog", Feather.GRID, () -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        }));
        sidebar.add(Box.createVerticalStrut(6));

        sidebar.add(createNavButton("My Orders", Feather.PACKAGE, () -> {
            new MyOrdersPage().setVisible(true);
            dispose();
        }));
        sidebar.add(Box.createVerticalStrut(6));

        sidebar.add(createNavButton("My Account", Feather.USER, () -> {
            new MyAccountPage().setVisible(true);
            dispose();
        }));

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton createNavButton(String text, Feather icon, Runnable action) {
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

    private JComponent createScrollableBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 20, 20, 20));

        // Success Confirmation Hero Card
        UIUtils.RoundedPanel hero = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel checkIcon = new JLabel(FontIcon.of(Feather.CHECK_CIRCLE, 50, UIUtils.COLOR_GREEN));
        checkIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel h1 = new JLabel("Thank You! Your Order Has Been Placed.");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        h1.setForeground(UIUtils.COLOR_TEXT_MAIN);
        h1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel h2 = new JLabel("Order #" + orderId + " • Payment & Inventory Confirmed");
        h2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        h2.setForeground(UIUtils.COLOR_TEXT_MUTED);
        h2.setAlignmentX(Component.CENTER_ALIGNMENT);

        hero.add(checkIcon);
        hero.add(Box.createVerticalStrut(10));
        hero.add(h1);
        hero.add(Box.createVerticalStrut(4));
        hero.add(h2);
        hero.add(Box.createVerticalStrut(20));

        if (order != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateStr = (order.getOrderDate() != null) ? sdf.format(order.getOrderDate()) : "N/A";

            JPanel infoGrid = new JPanel(new GridLayout(2, 2, 16, 12));
            infoGrid.setOpaque(false);
            infoGrid.setMaximumSize(new Dimension(600, 100));

            infoGrid.add(createInfoBlock("Order Reference", "#" + order.getOrderId()));
            infoGrid.add(createInfoBlock("Order Date & Time", dateStr));
            infoGrid.add(createInfoBlock("Payment Method", order.getPaymentMethod() + " (" + order.getPaymentStatus() + ")"));
            infoGrid.add(createInfoBlock("Total Amount", String.format("₹%,.2f", order.getTotalAmount())));

            hero.add(infoGrid);
            hero.add(Box.createVerticalStrut(20));

            // Items Summary
            if (order.getItems() != null && !order.getItems().isEmpty()) {
                JPanel itemsBox = new JPanel();
                itemsBox.setLayout(new BoxLayout(itemsBox, BoxLayout.Y_AXIS));
                itemsBox.setOpaque(false);
                itemsBox.setMaximumSize(new Dimension(600, 300));

                JLabel itTitle = new JLabel("Items in this Order:");
                itTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
                itemsBox.add(itTitle);
                itemsBox.add(Box.createVerticalStrut(8));

                for (OrderItem oi : order.getItems()) {
                    JPanel row = new JPanel(new BorderLayout(8, 0));
                    row.setOpaque(false);
                    row.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));

                    JPanel leftPart = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
                    leftPart.setOpaque(false);

                    Product op = new Product();
                    op.setProductId(oi.getProductId());
                    op.setProductName(oi.getProductName());
                    ImageIcon tIcon = ImageUtils.getProductIcon(op, 36, 36);
                    if (tIcon != null) {
                        JLabel tLbl = new JLabel(tIcon);
                        tLbl.setPreferredSize(new Dimension(38, 38));
                        leftPart.add(tLbl);
                    }

                    JPanel nameBlock = new JPanel();
                    nameBlock.setLayout(new BoxLayout(nameBlock, BoxLayout.Y_AXIS));
                    nameBlock.setOpaque(false);

                    JLabel name = new JLabel(oi.getProductName() != null ? oi.getProductName() : "Product #" + oi.getProductId());
                    name.setFont(new Font("Segoe UI", Font.BOLD, 12));

                    JLabel qty = new JLabel("Qty: " + oi.getQuantity());
                    qty.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    qty.setForeground(UIUtils.COLOR_TEXT_MUTED);

                    nameBlock.add(name);
                    nameBlock.add(qty);
                    leftPart.add(nameBlock);

                    JLabel price = new JLabel(String.format("₹%,.2f", oi.getSubtotal()));
                    price.setFont(new Font("Segoe UI", Font.BOLD, 12));

                    row.add(leftPart, BorderLayout.WEST);
                    row.add(price, BorderLayout.EAST);
                    itemsBox.add(row);
                    itemsBox.add(Box.createVerticalStrut(8));
                }
                hero.add(itemsBox);
                hero.add(Box.createVerticalStrut(20));
            }
        }

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        btnRow.setOpaque(false);

        JButton btnMyOrders = new JButton("View My Orders", FontIcon.of(Feather.PACKAGE, 13, Color.WHITE)) {
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
        btnMyOrders.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMyOrders.setForeground(Color.WHITE);
        btnMyOrders.setContentAreaFilled(false);
        btnMyOrders.setBorderPainted(false);
        btnMyOrders.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMyOrders.addActionListener(e -> {
            new MyOrdersPage().setVisible(true);
            dispose();
        });

        JButton btnHome = new JButton("Return to Dashboard");
        btnHome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHome.addActionListener(e -> {
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });

        btnRow.add(btnMyOrders);
        btnRow.add(btnHome);
        hero.add(btnRow);

        body.add(hero);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createInfoBlock(String title, String val) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 10));
        t.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel v = new JLabel(val);
        v.setFont(new Font("Segoe UI", Font.BOLD, 13));
        v.setForeground(UIUtils.COLOR_TEXT_MAIN);

        p.add(t);
        p.add(Box.createVerticalStrut(2));
        p.add(v);
        return p;
    }
}


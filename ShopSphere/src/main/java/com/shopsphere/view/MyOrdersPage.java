package com.shopsphere.view;

import com.shopsphere.entity.OrderItem;
import com.shopsphere.entity.Orders;
import com.shopsphere.entity.Product;
import com.shopsphere.entity.User;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.UserSession;
import com.shopsphere.util.ImageUtils;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyOrdersPage extends JFrame {

    private final OrderService orderService = new OrderService();
    private JPanel ordersContainer;
    private JLabel lblHeaderCount;
    private JComboBox<String> cmbStatusFilter;

    public MyOrdersPage() {
        setTitle("ShopSphere - My Orders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int targetW = Math.min(1380, (int) (scr.width * 0.94));
        int targetH = Math.min(920, (int) (scr.height * 0.92));
        setSize(targetW, targetH);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIUtils.COLOR_BG);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel mainLayout = new JPanel(new BorderLayout(16, 0));
        mainLayout.setOpaque(false);
        mainLayout.setBorder(new EmptyBorder(12, 20, 14, 20));

        mainLayout.add(createSidebar(), BorderLayout.WEST);
        mainLayout.add(createOrdersScrollArea(), BorderLayout.CENTER);

        root.add(mainLayout, BorderLayout.CENTER);
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);

        loadOrders();
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

        JLabel sub = new JLabel("Customer Orders & Tracking");
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

        JButton btnCatalog = new JButton("Catalog", FontIcon.of(Feather.GRID, 13, UIUtils.COLOR_TEXT_MAIN));
        btnCatalog.setContentAreaFilled(false);
        btnCatalog.setBorderPainted(false);
        btnCatalog.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCatalog.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

        JButton btnCart = new JButton("My Cart", FontIcon.of(Feather.SHOPPING_BAG, 13, UIUtils.COLOR_TEXT_MAIN));
        btnCart.setContentAreaFilled(false);
        btnCart.setBorderPainted(false);
        btnCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCart.addActionListener(e -> {
            new MyCartPage().setVisible(true);
            dispose();
        });

        right.add(btnHome);
        right.add(btnCatalog);
        right.add(btnCart);

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

        sidebar.add(createNavBtn("My Account", Feather.USER, () -> {
            new MyAccountPage().setVisible(true);
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

    private JPanel createOrdersScrollArea() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(0, 0, 12, 0));

        lblHeaderCount = new JLabel("My Purchase History");
        lblHeaderCount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeaderCount.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightControls.setOpaque(false);

        cmbStatusFilter = new JComboBox<>(new String[]{"All Statuses", "PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"});
        cmbStatusFilter.addActionListener(e -> loadOrders());
        rightControls.add(cmbStatusFilter);

        topBar.add(lblHeaderCount, BorderLayout.WEST);
        topBar.add(rightControls, BorderLayout.EAST);

        ordersContainer = new JPanel();
        ordersContainer.setLayout(new BoxLayout(ordersContainer, BoxLayout.Y_AXIS));
        ordersContainer.setOpaque(false);

        JScrollPane scroll = new JScrollPane(ordersContainer);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        p.add(topBar, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private void loadOrders() {
        ordersContainer.removeAll();
        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in to view your orders.", "Login Required", JOptionPane.INFORMATION_MESSAGE);
            new CustomerLoginPage().setVisible(true);
            dispose();
            return;
        }

        try {
            List<Orders> orders = orderService.getOrderHistory(currentUser.getUserId());
            String filter = (String) cmbStatusFilter.getSelectedItem();

            int count = 0;
            for (Orders o : orders) {
                boolean match = (filter == null || filter.equals("All Statuses") ||
                        (o.getOrderStatus() != null && o.getOrderStatus().equalsIgnoreCase(filter)));
                if (match) {
                    ordersContainer.add(createOrderCard(o));
                    ordersContainer.add(Box.createVerticalStrut(12));
                    count++;
                }
            }

            lblHeaderCount.setText("My Purchase History (" + count + " orders)");

            if (count == 0) {
                UIUtils.RoundedPanel empty = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
                empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
                empty.setBorder(new EmptyBorder(40, 20, 40, 20));

                JLabel icon = new JLabel(FontIcon.of(Feather.PACKAGE, 48, UIUtils.COLOR_TEXT_MUTED));
                icon.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel txt = new JLabel("You have no matching orders.");
                txt.setFont(new Font("Segoe UI", Font.BOLD, 15));
                txt.setForeground(UIUtils.COLOR_TEXT_MAIN);
                txt.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton btnShop = new JButton("Explore Store Catalog");
                btnShop.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnShop.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnShop.addActionListener(e -> {
                    new ProductListingPage("All Categories").setVisible(true);
                    dispose();
                });

                empty.add(icon);
                empty.add(Box.createVerticalStrut(10));
                empty.add(txt);
                empty.add(Box.createVerticalStrut(14));
                empty.add(btnShop);

                ordersContainer.add(empty);
            }

            ordersContainer.revalidate();
            ordersContainer.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching orders: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createOrderCard(Orders o) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 220));

        // Top Header of the Order Card
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        String dateStr = (o.getOrderDate() != null) ? sdf.format(o.getOrderDate()) : "N/A";

        JLabel idLbl = new JLabel("Order #" + o.getOrderId() + "  •  Placed on " + dateStr);
        idLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLbl.setForeground(UIUtils.COLOR_TEXT_MAIN);

        Color stColor = "DELIVERED".equalsIgnoreCase(o.getOrderStatus()) ? UIUtils.COLOR_GREEN :
                "CANCELLED".equalsIgnoreCase(o.getOrderStatus()) ? UIUtils.COLOR_RED : UIUtils.COLOR_ORANGE;

        JLabel stLbl = new JLabel("● " + o.getOrderStatus());
        stLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        stLbl.setForeground(stColor);

        top.add(idLbl, BorderLayout.WEST);
        top.add(stLbl, BorderLayout.EAST);

        // Center Items list
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(8, 0, 8, 0));

        if (o.getItems() != null && !o.getItems().isEmpty()) {
            for (OrderItem oi : o.getItems()) {
                JPanel itemRow = new JPanel(new BorderLayout(10, 0));
                itemRow.setOpaque(false);
                itemRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 46));

                JPanel leftImgBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                leftImgBox.setOpaque(false);
                Product mop = new Product();
                mop.setProductId(oi.getProductId());
                mop.setProductName(oi.getProductName());
                ImageIcon tIcon = ImageUtils.getProductIcon(mop, 42, 42);
                if (tIcon != null) {
                    JLabel tLbl = new JLabel(tIcon);
                    tLbl.setPreferredSize(new Dimension(44, 44));
                    leftImgBox.add(tLbl);
                }

                JPanel itemTexts = new JPanel();
                itemTexts.setLayout(new BoxLayout(itemTexts, BoxLayout.Y_AXIS));
                itemTexts.setOpaque(false);

                JLabel itemL = new JLabel((oi.getProductName() != null ? oi.getProductName() : "Product #" + oi.getProductId()));
                itemL.setFont(new Font("Segoe UI", Font.BOLD, 12));
                itemL.setForeground(UIUtils.COLOR_TEXT_MAIN);

                JLabel detailL = new JLabel(oi.getQuantity() + " unit(s)  •  ₹" + String.format("%.2f", oi.getUnitPrice()) + " each  •  Subtotal: ₹" + String.format("%.2f", oi.getSubtotal()));
                detailL.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                detailL.setForeground(UIUtils.COLOR_TEXT_MUTED);

                itemTexts.add(itemL);
                itemTexts.add(detailL);

                itemRow.add(leftImgBox, BorderLayout.WEST);
                itemRow.add(itemTexts, BorderLayout.CENTER);

                center.add(itemRow);
                center.add(Box.createVerticalStrut(6));
            }
        } else {
            JLabel noItems = new JLabel("Items processed under fulfillment pipeline.");
            noItems.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            noItems.setForeground(UIUtils.COLOR_TEXT_MUTED);
            center.add(noItems);
        }

        // Bottom Footer with Total & Action
        JPanel btm = new JPanel(new BorderLayout());
        btm.setOpaque(false);

        JLabel totalLbl = new JLabel(String.format("Total: ₹%,.2f (%s • %s)", o.getTotalAmount(), o.getPaymentMethod(), o.getPaymentStatus()));
        totalLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        totalLbl.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        rightBtns.setOpaque(false);

        if ("PENDING".equalsIgnoreCase(o.getOrderStatus()) || "PROCESSING".equalsIgnoreCase(o.getOrderStatus())) {
            JButton btnCancel = new JButton("Cancel Order");
            btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnCancel.setForeground(UIUtils.COLOR_RED);
            btnCancel.addActionListener(e -> {
                int conf = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel Order #" + o.getOrderId() + "?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    orderService.updateOrderStatus(o.getOrderId(), "CANCELLED");
                    loadOrders();
                }
            });
            rightBtns.add(btnCancel);
        }

        btm.add(totalLbl, BorderLayout.WEST);
        btm.add(rightBtns, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(btm, BorderLayout.SOUTH);
        return card;
    }
}


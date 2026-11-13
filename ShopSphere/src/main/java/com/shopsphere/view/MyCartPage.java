package com.shopsphere.view;

import com.shopsphere.entity.Cart;
import com.shopsphere.entity.CartItem;
import com.shopsphere.entity.Product;
import com.shopsphere.entity.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.UserSession;
import com.shopsphere.service.ValidationException;
import com.shopsphere.util.ImageUtils;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MyCartPage extends JFrame {

    private final CartService cartService = new CartService();

    private JPanel itemsContainer;
    private JLabel lblCartTitle;
    private JLabel lblSubtotal;
    private JLabel lblTotal;
    private JButton btnCheckout;
    private Cart currentCart;

    public MyCartPage() {
        setTitle("ShopSphere - My Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.min(1440, (int) (scr.width * 0.94));
        int h = Math.min(1024, (int) (scr.height * 0.92));
        setSize(w, h);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIUtils.COLOR_BG);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel mainLayout = new JPanel(new BorderLayout(16, 0));
        mainLayout.setOpaque(false);
        mainLayout.setBorder(new EmptyBorder(12, 20, 14, 20));

        mainLayout.add(createSidebar(), BorderLayout.WEST);
        mainLayout.add(createCartArea(), BorderLayout.CENTER);

        root.add(mainLayout, BorderLayout.CENTER);
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);

        loadCartData();
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

        JLabel sub = new JLabel("Shopping Cart");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        brand.add(title);
        brand.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnCatalog = new JButton("Continue Shopping", FontIcon.of(Feather.ARROW_LEFT, 13, UIUtils.COLOR_TEXT_MAIN));
        btnCatalog.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCatalog.setContentAreaFilled(false);
        btnCatalog.setBorderPainted(false);
        btnCatalog.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCatalog.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

        JButton btnHome = new JButton("Home", FontIcon.of(Feather.HOME, 13, UIUtils.COLOR_TEXT_MAIN));
        btnHome.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHome.setContentAreaFilled(false);
        btnHome.setBorderPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHome.addActionListener(e -> {
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });

        right.add(btnCatalog);
        right.add(btnHome);

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

    private JPanel createCartArea() {
        JPanel panel = new JPanel(new BorderLayout(20, 0));
        panel.setOpaque(false);

        // Left: Scrollable Items List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        lblCartTitle = new JLabel("My Shopping Bag (0 items)");
        lblCartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCartTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        lblCartTitle.setBorder(new EmptyBorder(0, 0, 10, 0));

        itemsContainer = new JPanel();
        itemsContainer.setLayout(new BoxLayout(itemsContainer, BoxLayout.Y_AXIS));
        itemsContainer.setOpaque(false);

        JScrollPane scroll = new JScrollPane(itemsContainer);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        leftPanel.add(lblCartTitle, BorderLayout.NORTH);
        leftPanel.add(scroll, BorderLayout.CENTER);

        // Right: Order Summary Card (340px)
        JPanel summaryCard = createOrderSummaryCard();

        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(summaryCard, BorderLayout.EAST);
        return panel;
    }

    private JPanel createOrderSummaryCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(340, 0));

        JLabel title = new JLabel("Order Summary");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);

        card.add(title);
        card.add(Box.createVerticalStrut(16));

        lblSubtotal = new JLabel("₹0.00");
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSubtotal.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel lblShipping = new JLabel("FREE");
        lblShipping.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblShipping.setForeground(UIUtils.COLOR_GREEN);

        lblTotal = new JLabel("₹0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        card.add(createSummaryRow("Cart Subtotal", lblSubtotal));
        card.add(Box.createVerticalStrut(8));
        card.add(createSummaryRow("Standard Delivery", lblShipping));
        card.add(Box.createVerticalStrut(14));
        card.add(new JSeparator());
        card.add(Box.createVerticalStrut(14));
        card.add(createSummaryRow("Total Amount", lblTotal));
        card.add(Box.createVerticalStrut(24));

        btnCheckout = new JButton("Proceed to Checkout ➔", FontIcon.of(Feather.CHECK, 13, Color.WHITE)) {
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
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setContentAreaFilled(false);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheckout.setMaximumSize(new Dimension(Short.MAX_VALUE, 42));
        btnCheckout.addActionListener(e -> {
            new CheckoutPage().setVisible(true);
            dispose();
        });

        card.add(btnCheckout);
        card.add(Box.createVerticalStrut(12));

        JLabel guarantee = new JLabel("<html><center style='color:#64748B; font-size:10px;'>🔒 100% Safe & Secure Transaction<br>ACID Stock Guaranteed</center></html>");
        guarantee.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(guarantee);

        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel createSummaryRow(String label, JLabel valLbl) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(UIUtils.COLOR_TEXT_MUTED);

        r.add(l, BorderLayout.WEST);
        r.add(valLbl, BorderLayout.EAST);
        return r;
    }

    private void loadCartData() {
        itemsContainer.removeAll();
        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in to view your shopping cart.", "Login Required", JOptionPane.INFORMATION_MESSAGE);
            new CustomerLoginPage().setVisible(true);
            dispose();
            return;
        }

        try {
            currentCart = cartService.getCartForUser(currentUser.getUserId());
            List<CartItem> items = (currentCart != null) ? currentCart.getItems() : null;

            if (items == null || items.isEmpty()) {
                lblCartTitle.setText("My Shopping Bag (0 items)");
                lblSubtotal.setText("₹0.00");
                lblTotal.setText("₹0.00");
                btnCheckout.setEnabled(false);

                UIUtils.RoundedPanel emptyCard = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
                emptyCard.setLayout(new BoxLayout(emptyCard, BoxLayout.Y_AXIS));
                emptyCard.setBorder(new EmptyBorder(40, 20, 40, 20));

                JLabel emptyIcon = new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 50, UIUtils.COLOR_TEXT_MUTED));
                emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel emptyTxt = new JLabel("Your shopping cart is currently empty.");
                emptyTxt.setFont(new Font("Segoe UI", Font.BOLD, 15));
                emptyTxt.setForeground(UIUtils.COLOR_TEXT_MAIN);
                emptyTxt.setAlignmentX(Component.CENTER_ALIGNMENT);

                JButton btnShop = new JButton("Explore Store Catalog");
                btnShop.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btnShop.setAlignmentX(Component.CENTER_ALIGNMENT);
                btnShop.addActionListener(e -> {
                    new ProductListingPage("All Categories").setVisible(true);
                    dispose();
                });

                emptyCard.add(emptyIcon);
                emptyCard.add(Box.createVerticalStrut(12));
                emptyCard.add(emptyTxt);
                emptyCard.add(Box.createVerticalStrut(16));
                emptyCard.add(btnShop);

                itemsContainer.add(emptyCard);
            } else {
                lblCartTitle.setText("My Shopping Bag (" + items.size() + " items)");
                double total = currentCart.getTotalPrice();
                lblSubtotal.setText(String.format("₹%,.2f", total));
                lblTotal.setText(String.format("₹%,.2f", total));
                btnCheckout.setEnabled(true);

                for (CartItem ci : items) {
                    itemsContainer.add(createCartItemCard(ci));
                    itemsContainer.add(Box.createVerticalStrut(10));
                }
            }

            itemsContainer.revalidate();
            itemsContainer.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching cart: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createCartItemCard(CartItem item) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, Color.WHITE, UIUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(12, 16, 12, 16));
        card.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

        JPanel iconBox = new JPanel(new GridBagLayout());
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(64, 64));
        Product cartProd = new Product();
        cartProd.setProductId(item.getProductId());
        cartProd.setProductName(item.getProductName());
        ImageIcon pIcon = ImageUtils.getProductIcon(cartProd, 58, 58);
        if (pIcon != null) {
            iconBox.add(new JLabel(pIcon));
        } else {
            iconBox.add(new JLabel(FontIcon.of(Feather.BOX, 28, UIUtils.COLOR_PRIMARY_DARK)));
        }

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JLabel name = new JLabel(item.getProductName() != null ? item.getProductName() : "Product #" + item.getProductId());
        name.setFont(new Font("Segoe UI", Font.BOLD, 13));
        name.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel unitPrice = new JLabel(String.format("Unit Price: ₹%,.2f", item.getUnitPrice()));
        unitPrice.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        unitPrice.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel subtotal = new JLabel(String.format("Subtotal: ₹%,.2f", item.getSubtotal()));
        subtotal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subtotal.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        center.add(name);
        center.add(Box.createVerticalStrut(2));
        center.add(unitPrice);
        center.add(Box.createVerticalStrut(2));
        center.add(subtotal);

        // Right Qty Controls + Remove
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        right.setOpaque(false);

        JButton btnMinus = new JButton("-");
        btnMinus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnMinus.addActionListener(e -> {
            try {
                cartService.updateCartItemQuantity(item.getCartItemId(), item.getQuantity() - 1, item.getProductId());
                loadCartData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JLabel qtyLbl = new JLabel(String.valueOf(item.getQuantity()));
        qtyLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton btnPlus = new JButton("+");
        btnPlus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPlus.addActionListener(e -> {
            try {
                cartService.updateCartItemQuantity(item.getCartItemId(), item.getQuantity() + 1, item.getProductId());
                loadCartData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnRemove = new JButton(FontIcon.of(Feather.TRASH_2, 13, UIUtils.COLOR_RED));
        btnRemove.setContentAreaFilled(false);
        btnRemove.setBorderPainted(false);
        btnRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemove.setToolTipText("Remove from Cart");
        btnRemove.addActionListener(e -> {
            cartService.removeCartItem(item.getCartItemId());
            loadCartData();
        });

        right.add(btnMinus);
        right.add(qtyLbl);
        right.add(btnPlus);
        right.add(Box.createHorizontalStrut(10));
        right.add(btnRemove);

        card.add(iconBox, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }
}


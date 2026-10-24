package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MyCartPage extends JFrame {

    // Palette Configuration
    public static final Color COLOR_PRIMARY      = new Color(0x8D, 0xB4, 0xD6);
    public static final Color COLOR_PRIMARY_DARK = new Color(0x65, 0x94, 0xBD);
    public static final Color COLOR_LIGHT_BLUE   = new Color(0xD6, 0xE6, 0xF2);
    public static final Color COLOR_BACKGROUND   = new Color(0xF2, 0xF7, 0xFB);
    public static final Color COLOR_CARD         = Color.WHITE;
    public static final Color COLOR_MAIN_TEXT    = new Color(0x24, 0x34, 0x4D);
    public static final Color COLOR_SECONDARY    = new Color(0x6B, 0x72, 0x80);
    public static final Color COLOR_BORDER       = new Color(0xE1, 0xEA, 0xF2);
    public static final Color COLOR_INPUT_BG     = new Color(0xF4, 0xF7, 0xFB);
    public static final Color COLOR_GREEN        = new Color(0x10, 0xB9, 0x81);
    public static final Color COLOR_DANGER       = new Color(0xEF, 0x44, 0x44);

    // In-memory Cart Item Model
    public static class CartItem {
        public final String id;
        public final String name;
        public final String category;
        public final int unitPrice;
        public int quantity;
        public final Feather icon;

        public CartItem(String id, String name, String category, int unitPrice, int quantity, Feather icon) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.icon = icon;
        }

        public int getSubtotal() {
            return unitPrice * quantity;
        }
    }

    private final List<CartItem> cartItems = new ArrayList<>();

    // UI Interactive References
    private JLabel lblCartTitleCount;
    private JLabel lblHeaderCartCount;
    private JPanel itemsListContainer;
    private JPanel centerSplitPanel;
    private JPanel emptyCartView;

    // Summary Labels
    private JLabel lblSummarySubtotal;
    private JLabel lblSummaryShipping;
    private JLabel lblSummaryDiscount;
    private JLabel lblSummaryTotal;

    public MyCartPage() {
        setTitle("ShopSphere - My Shopping Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.min(1440, (int) (scr.width * 0.94));
        int h = Math.min(1024, (int) (scr.height * 0.92));
        setSize(w, h);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        initSampleCartData();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BACKGROUND);

        root.add(createCustomerHeader(), BorderLayout.NORTH);

        JPanel mainLayout = new JPanel(new BorderLayout(16, 0));
        mainLayout.setOpaque(false);
        mainLayout.setBorder(new EmptyBorder(12, 20, 14, 20));

        mainLayout.add(createSidebar(), BorderLayout.WEST);
        mainLayout.add(createScrollableCartArea(), BorderLayout.CENTER);

        root.add(mainLayout, BorderLayout.CENTER);
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);
        recalculateCart();
    }

    private void initSampleCartData() {
        cartItems.clear();
        cartItems.add(new CartItem("C1", "Wireless Bluetooth Headphones", "Electronics", 1499, 1, Feather.HEADPHONES));
        cartItems.add(new CartItem("C2", "Casual Sneakers", "Fashion", 1799, 1, Feather.TAG));
        cartItems.add(new CartItem("C3", "Travel Backpack", "Accessories", 999, 2, Feather.PACKAGE));
    }

    // ==========================================
    // 1. TOP HEADER (With Dynamic Cart Badge)
    // ==========================================
    private JPanel createCustomerHeader() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(COLOR_CARD);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));

        // Brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        UIUtils.RoundedPanel logo = new UIUtils.RoundedPanel(12, COLOR_LIGHT_BLUE);
        logo.setPreferredSize(new Dimension(34, 34));
        logo.setLayout(new GridBagLayout());
        logo.add(new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, COLOR_PRIMARY_DARK)));

        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(COLOR_MAIN_TEXT);

        JLabel sub = new JLabel("Your Everyday Shopping Destination");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_SECONDARY);

        brand.add(logo);
        brand.add(title);
        brand.add(sub);

        // Center Search Box
        UIUtils.RoundedPanel searchBox = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        searchBox.setPreferredSize(new Dimension(360, 36));
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 12, 0, 12));

        JLabel sIcon = new JLabel(FontIcon.of(Feather.SEARCH, 14, COLOR_SECONDARY));
        JTextField topSearch = new JTextField("Search products, categories, or brands...");
        topSearch.setBorder(null);
        topSearch.setOpaque(false);
        topSearch.setForeground(COLOR_SECONDARY);
        topSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(topSearch, BorderLayout.CENTER);

        // Right Quick Actions
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnWishlistNav = createIconButton(Feather.HEART, "Wishlist (0)", () -> showPlaceholder("Wishlist"));

        // Header Cart Button with dynamic label
        lblHeaderCartCount = new JLabel("Cart (" + getTotalDistinctCount() + ")");
        lblHeaderCartCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblHeaderCartCount.setForeground(COLOR_PRIMARY_DARK);

        JButton btnCartNav = new JButton(FontIcon.of(Feather.SHOPPING_BAG, 14, COLOR_PRIMARY_DARK));
        btnCartNav.setText(lblHeaderCartCount.getText());
        btnCartNav.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCartNav.setForeground(COLOR_PRIMARY_DARK);
        btnCartNav.setContentAreaFilled(false);
        btnCartNav.setBorderPainted(false);
        btnCartNav.setFocusPainted(false);

        JButton btnAccount = createIconButton(Feather.USER, "My Account", () -> { new MyAccountPage().setVisible(true);
       dispose();
       });
        right.add(btnWishlistNav);
        right.add(btnCartNav);
        right.add(btnAccount);

        nav.add(brand, BorderLayout.WEST);
        nav.add(searchBox, BorderLayout.CENTER);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JButton createIconButton(Feather icon, String text, Runnable action) {
        JButton btn = new JButton(text, FontIcon.of(icon, 14, COLOR_MAIN_TEXT));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(COLOR_MAIN_TEXT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // ==========================================
    // 2. SIDEBAR (My Cart Active)
    // ==========================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(new EmptyBorder(8, 4, 10, 12));

        JLabel catLbl = new JLabel("SHOP");
        catLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catLbl.setForeground(COLOR_SECONDARY);

        sidebar.add(catLbl);
        sidebar.add(Box.createVerticalStrut(8));

        sidebar.add(createSidebarBtn("All Products", Feather.GRID, false, () -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        }));
        sidebar.add(createSidebarBtn("Electronics", Feather.TV, false, () -> openCategory("Electronics")));
        sidebar.add(createSidebarBtn("Fashion", Feather.TAG, false, () -> openCategory("Fashion")));
        sidebar.add(createSidebarBtn("Home & Living", Feather.HOME, false, () -> openCategory("Home & Living")));
        sidebar.add(createSidebarBtn("Beauty & Care", Feather.SUN, false, () -> openCategory("Beauty & Personal Care")));
        sidebar.add(createSidebarBtn("Books & Stationery", Feather.BOOK, false, () -> openCategory("Books & Stationery")));
        sidebar.add(createSidebarBtn("Sports & Fitness", Feather.ACTIVITY, false, () -> openCategory("Sports & Fitness")));
        sidebar.add(createSidebarBtn("Accessories", Feather.PACKAGE, false, () -> openCategory("Accessories")));

        sidebar.add(Box.createVerticalStrut(20));

        JLabel myShopLbl = new JLabel("MY SHOPPING");
        myShopLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        myShopLbl.setForeground(COLOR_SECONDARY);

        sidebar.add(myShopLbl);
        sidebar.add(Box.createVerticalStrut(8));

        // Highlight "My Cart" as ACTIVE
        sidebar.add(createSidebarBtn("My Cart", Feather.SHOPPING_CART, true, () -> {}));
        sidebar.add(createSidebarBtn("My Orders", Feather.ARCHIVE, false, () -> {
        new MyOrdersPage().setVisible(true);
        dispose();
        }));
        sidebar.add(createSidebarBtn("My Account", Feather.USER, false, () -> { new MyAccountPage().setVisible(true);
        dispose();
        }));
        sidebar.add(createSidebarBtn("Logout", Feather.LOG_OUT, false, () -> {
            new WelcomePage().setVisible(true);
            dispose();
        }));

        return sidebar;
    }

    private void openCategory(String catName) {
        new ProductListingPage(catName).setVisible(true);
        dispose();
    }

    private JButton createSidebarBtn(String title, Feather icon, boolean active, Runnable onClick) {
        JButton btn = new JButton(title, FontIcon.of(icon, 13, active ? Color.WHITE : COLOR_MAIN_TEXT)) {
            @Override
            protected void paintComponent(Graphics g) {
                if (active) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(COLOR_PRIMARY_DARK);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 12));
        btn.setForeground(active ? Color.WHITE : COLOR_MAIN_TEXT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));
        btn.setBorder(new EmptyBorder(6, 10, 6, 10));
        btn.addActionListener(e -> onClick.run());
        return btn;
    }

    // ==========================================
    // 3. MAIN CART AREA
    // ==========================================
    private JComponent createScrollableCartArea() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // Heading & Breadcrumbs
        content.add(createHeadingBanner());
        content.add(Box.createVerticalStrut(14));

        // Two-Column Split or Empty View
        centerSplitPanel = new JPanel(new GridBagLayout());
        centerSplitPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left: Items list (65%)
        gbc.gridx = 0;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 18);
        itemsListContainer = new JPanel();
        itemsListContainer.setLayout(new BoxLayout(itemsListContainer, BoxLayout.Y_AXIS));
        itemsListContainer.setOpaque(false);
        centerSplitPanel.add(itemsListContainer, gbc);

        // Right: Order Summary Card (35%)
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 0);
        centerSplitPanel.add(createOrderSummaryCard(), gbc);

        // Empty Cart View Setup
        emptyCartView = createEmptyCartPanel();
        emptyCartView.setVisible(false);

        content.add(centerSplitPanel);
        content.add(emptyCartView);
        content.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createHeadingBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setOpaque(false);
        banner.setMaximumSize(new Dimension(Short.MAX_VALUE, 58));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        // Breadcrumb
        JPanel breadcrumbs = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        breadcrumbs.setOpaque(false);

        JLabel homeLink = new JLabel("Home");
        homeLink.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        homeLink.setForeground(COLOR_PRIMARY_DARK);
        homeLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CustomerDashboardPage().setVisible(true);
                dispose();
            }
        });

        JLabel slash = new JLabel("/");
        slash.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        slash.setForeground(COLOR_SECONDARY);

        JLabel current = new JLabel("My Cart");
        current.setFont(new Font("Segoe UI", Font.BOLD, 11));
        current.setForeground(COLOR_MAIN_TEXT);

        breadcrumbs.add(homeLink);
        breadcrumbs.add(slash);
        breadcrumbs.add(current);

        JLabel title = new JLabel("My Shopping Cart");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(COLOR_MAIN_TEXT);

        JLabel sub = new JLabel("Review your selected items before proceeding to checkout.");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_SECONDARY);

        left.add(breadcrumbs);
        left.add(Box.createVerticalStrut(2));
        left.add(title);
        left.add(sub);

        lblCartTitleCount = new JLabel(getTotalDistinctCount() + " items in your cart");
        lblCartTitleCount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCartTitleCount.setForeground(COLOR_SECONDARY);

        banner.add(left, BorderLayout.WEST);
        banner.add(lblCartTitleCount, BorderLayout.EAST);
        return banner;
    }

    // ==========================================
    // 4. CART ITEMS LIST (DYNAMIC)
    // ==========================================
    private void renderCartItems() {
        itemsListContainer.removeAll();

        for (CartItem item : cartItems) {
            itemsListContainer.add(createCartItemRow(item));
            itemsListContainer.add(Box.createVerticalStrut(10));
        }

        itemsListContainer.revalidate();
        itemsListContainer.repaint();
    }

    private JPanel createCartItemRow(CartItem item) {
        UIUtils.RoundedPanel rowCard = new UIUtils.RoundedPanel(14, COLOR_CARD, COLOR_BORDER);
        rowCard.setLayout(new BorderLayout(14, 0));
        rowCard.setBorder(new EmptyBorder(12, 14, 12, 14));
        rowCard.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

        // 1. Thumbnail Image Canvas
        JPanel imgBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        imgBox.setPreferredSize(new Dimension(65, 65));
        imgBox.setLayout(new GridBagLayout());
        imgBox.add(new JLabel(FontIcon.of(item.icon, 28, COLOR_PRIMARY_DARK)));

        // 2. Info: Name, Category, Unit Price
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel nameLbl = new JLabel(item.name);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLbl.setForeground(COLOR_MAIN_TEXT);

        JLabel catLbl = new JLabel(item.category.toUpperCase());
        catLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        catLbl.setForeground(COLOR_SECONDARY);

        JLabel unitPriceLbl = new JLabel("Unit Price: ₹" + String.format("%,d", item.unitPrice));
        unitPriceLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        unitPriceLbl.setForeground(COLOR_SECONDARY);

        info.add(nameLbl);
        info.add(Box.createVerticalStrut(2));
        info.add(catLbl);
        info.add(Box.createVerticalStrut(4));
        info.add(unitPriceLbl);

        // 3. Right: Quantity Controls, Subtotal, Remove Button
        JPanel controlsRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 14));
        controlsRight.setOpaque(false);

        // Quantity Box [-] QTY [+]
        UIUtils.RoundedPanel qtyBox = new UIUtils.RoundedPanel(8, COLOR_INPUT_BG, COLOR_BORDER);
        qtyBox.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 2));

        JButton btnMinus = createSmallQtyBtn("−");
        JLabel lblQty = new JLabel(String.valueOf(item.quantity));
        lblQty.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblQty.setForeground(COLOR_MAIN_TEXT);
        lblQty.setPreferredSize(new Dimension(18, 20));
        lblQty.setHorizontalAlignment(SwingConstants.CENTER);
        JButton btnPlus = createSmallQtyBtn("+");

        btnMinus.addActionListener(e -> {
            if (item.quantity > 1) {
                item.quantity--;
                lblQty.setText(String.valueOf(item.quantity));
                recalculateCart();
            } else {
                JOptionPane.showMessageDialog(this, "Quantity cannot be less than 1. Use Remove to delete.", "Minimum Quantity", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnPlus.addActionListener(e -> {
            if (item.quantity < 10) {
                item.quantity++;
                lblQty.setText(String.valueOf(item.quantity));
                recalculateCart();
            } else {
                JOptionPane.showMessageDialog(this, "Maximum 10 units allowed per item.", "Limit", JOptionPane.WARNING_MESSAGE);
            }
        });

        qtyBox.add(btnMinus);
        qtyBox.add(lblQty);
        qtyBox.add(btnPlus);

        // Subtotal Display
        JLabel lblSubtotal = new JLabel("₹" + String.format("%,d", item.getSubtotal()));
        lblSubtotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSubtotal.setForeground(COLOR_MAIN_TEXT);
        lblSubtotal.setPreferredSize(new Dimension(75, 24));
        lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);

        // Remove Button
        JButton btnRemove = new JButton(FontIcon.of(Feather.TRASH_2, 14, COLOR_DANGER));
        btnRemove.setToolTipText("Remove item");
        btnRemove.setContentAreaFilled(false);
        btnRemove.setBorderPainted(false);
        btnRemove.setFocusPainted(false);
        btnRemove.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRemove.addActionListener(e -> {
            cartItems.remove(item);
            recalculateCart();
        });

        controlsRight.add(qtyBox);
        controlsRight.add(lblSubtotal);
        controlsRight.add(btnRemove);

        rowCard.add(imgBox, BorderLayout.WEST);
        rowCard.add(info, BorderLayout.CENTER);
        rowCard.add(controlsRight, BorderLayout.EAST);

        return rowCard;
    }

    private JButton createSmallQtyBtn(String symbol) {
        JButton b = new JButton(symbol);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(COLOR_MAIN_TEXT);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(20, 20));
        return b;
    }

    // ==========================================
    // 5. ORDER SUMMARY CARD
    // ==========================================
    private JPanel createOrderSummaryCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        card.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel title = new JLabel("Order Summary");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(COLOR_MAIN_TEXT);

        lblSummarySubtotal = new JLabel("₹0");
        lblSummaryShipping = new JLabel("Free");
        lblSummaryDiscount = new JLabel("₹0");
        lblSummaryTotal    = new JLabel("₹0");

        lblSummaryTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSummaryTotal.setForeground(COLOR_MAIN_TEXT);

        // Buttons
        JButton btnCheckout = new JButton("Proceed to Checkout ➔") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? COLOR_PRIMARY_DARK.darker() : COLOR_PRIMARY_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setContentAreaFilled(false);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheckout.setMaximumSize(new Dimension(Short.MAX_VALUE, 42));
        btnCheckout.addActionListener(e -> { new CheckoutPage().setVisible(true);
        MyCartPage.this.dispose();
        });


        JButton btnContinue = new JButton("Continue Shopping");
        btnContinue.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnContinue.setForeground(COLOR_MAIN_TEXT);
        btnContinue.setContentAreaFilled(false);
        btnContinue.setBorderPainted(false);
        btnContinue.setFocusPainted(false);
        btnContinue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContinue.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));
        btnContinue.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

        JLabel note = new JLabel("<html><center style='color:#6B7280; font-size:10px;'>"
                + "Shipping and final charges will be confirmed during checkout."
                + "</center></html>");
        note.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(14));
        card.add(createSummaryRow("Subtotal", lblSummarySubtotal));
        card.add(Box.createVerticalStrut(8));
        card.add(createSummaryRow("Shipping", lblSummaryShipping));
        card.add(Box.createVerticalStrut(8));
        card.add(createSummaryRow("Discount", lblSummaryDiscount));
        card.add(Box.createVerticalStrut(12));

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER);
        card.add(sep);
        card.add(Box.createVerticalStrut(12));

        card.add(createSummaryRow("Estimated Total", lblSummaryTotal));
        card.add(Box.createVerticalStrut(18));
        card.add(btnCheckout);
        card.add(Box.createVerticalStrut(8));
        card.add(btnContinue);
        card.add(Box.createVerticalStrut(12));
        card.add(note);

        return card;
    }

    private JPanel createSummaryRow(String label, JLabel valLabel) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(COLOR_SECONDARY);

        valLabel.setFont(valLabel.getFont().deriveFont(13f));
        valLabel.setForeground(COLOR_MAIN_TEXT);

        row.add(lbl, BorderLayout.WEST);
        row.add(valLabel, BorderLayout.EAST);
        return row;
    }

    // ==========================================
    // 6. EMPTY CART STATE
    // ==========================================
    private JPanel createEmptyCartPanel() {
        UIUtils.RoundedPanel panel = new UIUtils.RoundedPanel(18, COLOR_CARD, COLOR_BORDER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(60, 40, 60, 40));

        JLabel icon = new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 54, COLOR_PRIMARY));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel h1 = new JLabel("Your cart is empty");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        h1.setForeground(COLOR_MAIN_TEXT);
        h1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msg = new JLabel("Looks like you haven't added any products to your basket yet.");
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        msg.setForeground(COLOR_SECONDARY);
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnShop = new JButton("Explore Products ➔") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_PRIMARY_DARK);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnShop.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnShop.setForeground(Color.WHITE);
        btnShop.setContentAreaFilled(false);
        btnShop.setBorderPainted(false);
        btnShop.setFocusPainted(false);
        btnShop.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnShop.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnShop.setMaximumSize(new Dimension(180, 40));
        btnShop.addActionListener(e -> {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        });

        panel.add(icon);
        panel.add(Box.createVerticalStrut(14));
        panel.add(h1);
        panel.add(Box.createVerticalStrut(6));
        panel.add(msg);
        panel.add(Box.createVerticalStrut(22));
        panel.add(btnShop);

        return panel;
    }

    // ==========================================
    // 7. DYNAMIC RECALCULATION & STATE UPDATE
    // ==========================================
    private void recalculateCart() {
        if (cartItems.isEmpty()) {
            centerSplitPanel.setVisible(false);
            emptyCartView.setVisible(true);
            lblCartTitleCount.setText("0 items in your cart");
            if (lblHeaderCartCount != null) lblHeaderCartCount.setText("Cart (0)");
            return;
        }

        centerSplitPanel.setVisible(true);
        emptyCartView.setVisible(false);

        int subtotal = 0;
        int totalUnits = 0;

        for (CartItem item : cartItems) {
            subtotal += item.getSubtotal();
            totalUnits += item.quantity;
        }

        // Shipping rule: Free if subtotal >= 2000, else ₹50
        int shipping = (subtotal >= 2000) ? 0 : 50;
        int discount = 0;
        int estimatedTotal = subtotal + shipping - discount;

        // Update labels
        lblSummarySubtotal.setText("₹" + String.format("%,d", subtotal));
        lblSummaryShipping.setText(shipping == 0 ? "Free" : "₹" + shipping);
        lblSummaryShipping.setForeground(shipping == 0 ? COLOR_GREEN : COLOR_MAIN_TEXT);
        lblSummaryDiscount.setText("₹" + discount);
        lblSummaryTotal.setText("₹" + String.format("%,d", estimatedTotal));

        lblCartTitleCount.setText(cartItems.size() + " items in your cart (" + totalUnits + " units)");
        if (lblHeaderCartCount != null) {
            lblHeaderCartCount.setText("Cart (" + cartItems.size() + ")");
        }

        renderCartItems();
    }

    private int getTotalDistinctCount() {
        return cartItems.size();
    }

    private void showPlaceholder(String name) {
        JOptionPane.showMessageDialog(this,
                name + " screen will be connected cleanly in the next step.",
                "ShopSphere Navigation", JOptionPane.INFORMATION_MESSAGE);
    }
}
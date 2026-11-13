package com.shopsphere.view;

import com.shopsphere.entity.Cart;
import com.shopsphere.entity.CartItem;
import com.shopsphere.entity.Product;
import com.shopsphere.entity.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.OrderService;
import com.shopsphere.service.UserSession;
import com.shopsphere.service.ValidationException;
import com.shopsphere.util.ImageUtils;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;

public class CheckoutPage extends JFrame {

    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartService();

    private JTextField txtFullName;
    private JTextField txtPhone;
    private JTextField txtAddress1;
    private JTextField txtCity;
    private JComboBox<String> cmbState;
    private JTextField txtPinCode;
    private JTextField txtNotes;

    private JRadioButton rdoCod;
    private JRadioButton rdoUpi;
    private JRadioButton rdoCard;

    private JLabel lblSummarySubtotal;
    private JLabel lblSummaryTotal;
    private JPanel summaryItemsContainer;
    private Cart cart;

    public CheckoutPage() {
        setTitle("ShopSphere - Secure Checkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int targetW = Math.min(1360, (int) (scr.width * 0.94));
        int targetH = Math.min(880, (int) (scr.height * 0.92));
        setSize(targetW, targetH);
        setMinimumSize(new Dimension(1080, 700));
        setLocationRelativeTo(null);

        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in to proceed to checkout.", "Login Required", JOptionPane.INFORMATION_MESSAGE);
            new CustomerLoginPage().setVisible(true);
            dispose();
            return;
        }

        this.cart = cartService.getCartForUser(currentUser.getUserId());
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty. Please add products before checking out.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
            return;
        }

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIUtils.COLOR_BG);

        root.add(createHeader(), BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(8, 24, 12, 24));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(1160, 680));

        // Two-Column Layout (Left Form + Right Summary)
        JPanel columnsRow = new JPanel(new BorderLayout(24, 0));
        columnsRow.setOpaque(false);

        columnsRow.add(createLeftFormArea(currentUser), BorderLayout.CENTER);
        columnsRow.add(createOrderSummaryCard(), BorderLayout.EAST);

        container.add(columnsRow);
        centerWrapper.add(container);

        root.add(centerWrapper, BorderLayout.CENTER);
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

        JLabel sub = new JLabel("Secure Checkout & Order Placement");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        brand.add(title);
        brand.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnBackCart = new JButton("← Back to Cart", FontIcon.of(Feather.SHOPPING_BAG, 12, UIUtils.COLOR_TEXT_MAIN));
        btnBackCart.setContentAreaFilled(false);
        btnBackCart.setBorderPainted(false);
        btnBackCart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBackCart.addActionListener(e -> {
            new MyCartPage().setVisible(true);
            dispose();
        });

        right.add(btnBackCart);

        nav.add(brand, BorderLayout.WEST);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JComponent createLeftFormArea(User u) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        // 1. Delivery Address Card
        UIUtils.RoundedPanel addrCard = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        addrCard.setLayout(new BoxLayout(addrCard, BoxLayout.Y_AXIS));
        addrCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel aTitle = new JLabel("1. Shipping Address & Contact");
        aTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        aTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        addrCard.add(aTitle);
        addrCard.add(Box.createVerticalStrut(12));

        txtFullName = new JTextField(u.getFullName() != null ? u.getFullName() : "");
        txtPhone = new JTextField(u.getPhone() != null ? u.getPhone() : "");

        JPanel row1 = new JPanel(new GridLayout(1, 2, 12, 0));
        row1.setOpaque(false);
        row1.add(createField("Full Name *", txtFullName));
        row1.add(createField("Mobile Phone (10 digits) *", txtPhone));
        addrCard.add(row1);
        addrCard.add(Box.createVerticalStrut(10));

        txtAddress1 = new JTextField(u.getAddress() != null ? u.getAddress() : "");
        addrCard.add(createField("Street Address Line *", txtAddress1));
        addrCard.add(Box.createVerticalStrut(10));

        txtCity = new JTextField(u.getCity() != null ? u.getCity() : "");
        cmbState = new JComboBox<>(new String[]{
                "Maharashtra", "Karnataka", "Delhi", "Tamil Nadu", "Gujarat",
                "Telangana", "Uttar Pradesh", "West Bengal", "Kerala", "Rajasthan"
        });
        if (u.getState() != null && !u.getState().isEmpty()) {
            cmbState.setSelectedItem(u.getState());
        }

        txtPinCode = new JTextField(u.getPostalCode() != null ? u.getPostalCode() : "");

        JPanel row2 = new JPanel(new GridLayout(1, 3, 12, 0));
        row2.setOpaque(false);
        row2.add(createField("City *", txtCity));

        JPanel stateP = new JPanel();
        stateP.setLayout(new BoxLayout(stateP, BoxLayout.Y_AXIS));
        stateP.setOpaque(false);
        JLabel sLbl = new JLabel("State *");
        sLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        stateP.add(sLbl);
        stateP.add(Box.createVerticalStrut(3));
        stateP.add(cmbState);
        row2.add(stateP);

        row2.add(createField("PIN Code (6 digits) *", txtPinCode));
        addrCard.add(row2);
        addrCard.add(Box.createVerticalStrut(10));

        txtNotes = new JTextField();
        addrCard.add(createField("Delivery Instructions / Notes (Optional)", txtNotes));

        p.add(addrCard);
        p.add(Box.createVerticalStrut(16));

        // 2. Payment Method Card
        UIUtils.RoundedPanel payCard = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        payCard.setLayout(new BoxLayout(payCard, BoxLayout.Y_AXIS));
        payCard.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel payTitle = new JLabel("2. Payment Option");
        payTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        payTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        payCard.add(payTitle);
        payCard.add(Box.createVerticalStrut(12));

        rdoCod = new JRadioButton("Cash on Delivery (COD)", true);
        rdoCod.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rdoCod.setOpaque(false);

        rdoUpi = new JRadioButton("UPI / Instant Transfer");
        rdoUpi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rdoUpi.setOpaque(false);

        rdoCard = new JRadioButton("Credit / Debit Card (Online)");
        rdoCard.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rdoCard.setOpaque(false);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rdoCod);
        bg.add(rdoUpi);
        bg.add(rdoCard);

        payCard.add(rdoCod);
        payCard.add(Box.createVerticalStrut(6));
        payCard.add(rdoUpi);
        payCard.add(Box.createVerticalStrut(6));
        payCard.add(rdoCard);

        p.add(payCard);

        JScrollPane scroll = new JScrollPane(p);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        return scroll;
    }

    private JPanel createOrderSummaryCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, Color.WHITE, UIUtils.COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(380, 0));

        JLabel title = new JLabel("Order Review");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);
        card.add(title);
        card.add(Box.createVerticalStrut(12));

        summaryItemsContainer = new JPanel();
        summaryItemsContainer.setLayout(new BoxLayout(summaryItemsContainer, BoxLayout.Y_AXIS));
        summaryItemsContainer.setOpaque(false);

        double total = (cart != null) ? cart.getTotalPrice() : 0.0;
        if (cart != null && cart.getItems() != null) {
            for (CartItem ci : cart.getItems()) {
                JPanel itemRow = new JPanel(new BorderLayout(8, 0));
                itemRow.setOpaque(false);
                itemRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));

                JPanel leftPart = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
                leftPart.setOpaque(false);

                Product cp = new Product();
                cp.setProductId(ci.getProductId());
                cp.setProductName(ci.getProductName());
                ImageIcon tIcon = ImageUtils.getProductIcon(cp, 36, 36);
                if (tIcon != null) {
                    JLabel tLbl = new JLabel(tIcon);
                    tLbl.setPreferredSize(new Dimension(38, 38));
                    leftPart.add(tLbl);
                }

                JPanel nameBlock = new JPanel();
                nameBlock.setLayout(new BoxLayout(nameBlock, BoxLayout.Y_AXIS));
                nameBlock.setOpaque(false);

                JLabel name = new JLabel(ci.getProductName());
                name.setFont(new Font("Segoe UI", Font.BOLD, 12));
                name.setForeground(UIUtils.COLOR_TEXT_MAIN);

                JLabel qty = new JLabel("Qty: " + ci.getQuantity());
                qty.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                qty.setForeground(UIUtils.COLOR_TEXT_MUTED);

                nameBlock.add(name);
                nameBlock.add(qty);
                leftPart.add(nameBlock);

                JLabel price = new JLabel(String.format("₹%,.2f", ci.getSubtotal()));
                price.setFont(new Font("Segoe UI", Font.BOLD, 12));
                price.setForeground(UIUtils.COLOR_TEXT_MAIN);

                itemRow.add(leftPart, BorderLayout.WEST);
                itemRow.add(price, BorderLayout.EAST);

                summaryItemsContainer.add(itemRow);
                summaryItemsContainer.add(Box.createVerticalStrut(8));
            }
        }

        JScrollPane itemScroll = new JScrollPane(summaryItemsContainer);
        itemScroll.setBorder(null);
        itemScroll.setOpaque(false);
        itemScroll.getViewport().setOpaque(false);
        itemScroll.setPreferredSize(new Dimension(340, 200));

        card.add(itemScroll);
        card.add(Box.createVerticalStrut(12));
        card.add(new JSeparator());
        card.add(Box.createVerticalStrut(12));

        lblSummarySubtotal = new JLabel(String.format("₹%,.2f", total));
        lblSummarySubtotal.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblShipping = new JLabel("FREE");
        lblShipping.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblShipping.setForeground(UIUtils.COLOR_GREEN);

        lblSummaryTotal = new JLabel(String.format("₹%,.2f", total));
        lblSummaryTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSummaryTotal.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        card.add(createSummaryLine("Subtotal", lblSummarySubtotal));
        card.add(Box.createVerticalStrut(6));
        card.add(createSummaryLine("Delivery Charge", lblShipping));
        card.add(Box.createVerticalStrut(10));
        card.add(new JSeparator());
        card.add(Box.createVerticalStrut(10));
        card.add(createSummaryLine("Order Total", lblSummaryTotal));
        card.add(Box.createVerticalStrut(20));

        JButton btnPlaceOrder = new JButton("Confirm & Place Order ➔", FontIcon.of(Feather.CHECK, 13, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x10, 0xB9, 0x81));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnPlaceOrder.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPlaceOrder.setForeground(Color.WHITE);
        btnPlaceOrder.setContentAreaFilled(false);
        btnPlaceOrder.setBorderPainted(false);
        btnPlaceOrder.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPlaceOrder.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));
        btnPlaceOrder.addActionListener(e -> handlePlaceOrderAction());

        card.add(btnPlaceOrder);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel createSummaryLine(String title, JLabel val) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(UIUtils.COLOR_TEXT_MUTED);
        r.add(l, BorderLayout.WEST);
        r.add(val, BorderLayout.EAST);
        return r;
    }

    private JPanel createField(String label, JTextField tf) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        p.add(l);
        p.add(Box.createVerticalStrut(3));
        p.add(tf);
        return p;
    }

    private void handlePlaceOrderAction() {
        String name = txtFullName.getText().trim();
        String phone = txtPhone.getText().trim();
        String address = txtAddress1.getText().trim();
        String city = txtCity.getText().trim();
        String state = (String) cmbState.getSelectedItem();
        String pin = txtPinCode.getText().trim();
        String notes = txtNotes.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name cannot be blank.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtFullName.requestFocus();
            return;
        }
        if (!Pattern.matches("^[6-9]\\d{9}$", phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit mobile number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPhone.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Street address cannot be blank.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtAddress1.requestFocus();
            return;
        }
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "City cannot be blank.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtCity.requestFocus();
            return;
        }
        if (!Pattern.matches("^\\d{6}$", pin)) {
            JOptionPane.showMessageDialog(this, "PIN Code must be exactly 6 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPinCode.requestFocus();
            return;
        }

        String paymentMethod = rdoCod.isSelected() ? "COD" :
                rdoUpi.isSelected() ? "UPI" : "CARD";

        User currentUser = UserSession.getCurrentUser();
        try {
            int orderId = orderService.placeOrderFromCart(
                    currentUser.getUserId(),
                    address,
                    city,
                    state,
                    pin,
                    "India",
                    paymentMethod,
                    notes
            );

            if (orderId > 0) {
                new OrderSuccessPage(orderId).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Order processing failed. Please check stock availability.", "Order Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Order Blocked", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unexpected error processing order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


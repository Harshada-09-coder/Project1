package com.ecommerce.view;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;

public class CheckoutPage extends JFrame {

    // ShopSphere Color Palette
    public static final Color COLOR_PRIMARY      = new Color(0x78, 0xA5, 0xCE);
    public static final Color COLOR_PRIMARY_DARK = new Color(0x5F, 0x8E, 0xB8);
    public static final Color COLOR_LIGHT_BLUE   = new Color(0xD7, 0xE7, 0xF5);
    public static final Color COLOR_BACKGROUND   = new Color(0xF3, 0xF6, 0xFA);
    public static final Color COLOR_CARD         = Color.WHITE;
    public static final Color COLOR_TEXT_MAIN    = new Color(0x1F, 0x2D, 0x42);
    public static final Color COLOR_SECONDARY    = new Color(0x71, 0x7E, 0x93);
    public static final Color COLOR_BORDER       = new Color(0xE3, 0xEC, 0xF4);
    public static final Color COLOR_INPUT_BG     = new Color(0xF7, 0xFA, 0xFD);
    public static final Color COLOR_GREEN        = new Color(0x10, 0xB9, 0x81);

    // Form Fields
    private JTextField txtFullName;
    private JTextField txtPhone;
    private JTextField txtAddress1;
    private JTextField txtAddress2;
    private JTextField txtCity;
    private JComboBox<String> cmbState;
    private JTextField txtPinCode;
    private JComboBox<String> cmbAddressType;
    private JCheckBox chkSaveAddress;

    // Payment Selection
    private JRadioButton rdoCod;
    private JRadioButton rdoUpi;
    private JRadioButton rdoCard;
    private JPanel dynamicPaymentContainer;
    private CardLayout paymentCardLayout;
    private JPanel tabCod;
    private JPanel tabUpi;
    private JPanel tabCard;

    // Payment Inputs
    private JTextField txtUpiId;
    private JTextField txtCardNum;
    private JTextField txtCardHolder;
    private JTextField txtCardExp;
    private JTextField txtCardCvv;

    // Totals
    private final int subtotal = 5296;
    private final int total = 5296;

    public CheckoutPage() {
        setTitle("ShopSphere - Checkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Responsive sizing
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int targetW = Math.min(1360, (int) (scr.width * 0.94));
        int targetH = Math.min(880, (int) (scr.height * 0.92));
        setSize(targetW, targetH);
        setMinimumSize(new Dimension(1080, 700));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BACKGROUND);

        // Header
        root.add(createHeader(), BorderLayout.NORTH);

        // Center Content Box (Centered and Contained)
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(8, 24, 12, 24));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(1160, 680)); // Perfect fixed ratio to prevent stretching

        // 1. Breadcrumb + Step Indicator
        container.add(createTopProgressBar());
        container.add(Box.createVerticalStrut(10));

        // 2. Main Two-Column Layout (Left Form 730px, Right Summary 400px)
        JPanel columnsRow = new JPanel(new BorderLayout(24, 0));
        columnsRow.setOpaque(false);

        columnsRow.add(createLeftContentArea(), BorderLayout.CENTER);
        columnsRow.add(createOrderSummaryCard(), BorderLayout.EAST);

        container.add(columnsRow);

        centerWrapper.add(container);

        JScrollPane scroll = new JScrollPane(centerWrapper);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        root.add(scroll, BorderLayout.CENTER);
        root.add(createFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ==========================================
    // 1. HEADER
    // ==========================================
    private JPanel createHeader() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(COLOR_CARD);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(12, 32, 12, 32)
        ));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        UIUtils.RoundedPanel logo = new UIUtils.RoundedPanel(10, COLOR_LIGHT_BLUE);
        logo.setPreferredSize(new Dimension(32, 32));
        logo.setLayout(new GridBagLayout());
        logo.add(new JLabel(FontIcon.of(Feather.SHOPPING_BAG, 16, COLOR_PRIMARY_DARK)));

        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("|   Your Everyday Shopping Destination");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(COLOR_SECONDARY);

        brand.add(logo);
        brand.add(title);
        brand.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        UIUtils.RoundedPanel trustPill = new UIUtils.RoundedPanel(16, COLOR_INPUT_BG, COLOR_BORDER);
        trustPill.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));
        trustPill.add(new JLabel(FontIcon.of(Feather.SHIELD, 13, COLOR_GREEN)));
        JLabel sec = new JLabel("256-Bit SSL Encrypted Checkout");
        sec.setFont(new Font("Segoe UI", Font.BOLD, 11));
        sec.setForeground(COLOR_TEXT_MAIN);
        trustPill.add(sec);

        right.add(trustPill);

        nav.add(brand, BorderLayout.WEST);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    // ==========================================
    // 2. TOP STEP PROGRESS INDICATOR
    // ==========================================
    private JPanel createTopProgressBar() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 46));

        // Left Titles
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JPanel crumbs = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        crumbs.setOpaque(false);

        JLabel home = createLink("Home", () -> {
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });
        JLabel cart = createLink("My Cart", () -> {
            new MyCartPage().setVisible(true);
            dispose();
        });
        JLabel current = new JLabel("Checkout");
        current.setFont(new Font("Segoe UI", Font.BOLD, 11));
        current.setForeground(COLOR_TEXT_MAIN);

        crumbs.add(home);
        crumbs.add(new JLabel("/"));
        crumbs.add(cart);
        crumbs.add(new JLabel("/"));
        crumbs.add(current);

        JLabel heading = new JLabel("Complete Your Order");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heading.setForeground(COLOR_TEXT_MAIN);

        left.add(crumbs);
        left.add(Box.createVerticalStrut(2));
        left.add(heading);

        // Right Steps
        JPanel steps = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2));
        steps.setOpaque(false);

        steps.add(createStepItem("1", "Cart", false, true));
        steps.add(new JLabel("—"));
        steps.add(createStepItem("2", "Delivery", true, false));
        steps.add(new JLabel("—"));
        steps.add(createStepItem("3", "Payment", true, false));
        steps.add(new JLabel("—"));
        steps.add(createStepItem("4", "Review", false, false));

        row.add(left, BorderLayout.WEST);
        row.add(steps, BorderLayout.EAST);
        return row;
    }

    private JLabel createLink(String text, Runnable action) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(COLOR_PRIMARY_DARK);
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
        return lbl;
    }

    private JPanel createStepItem(String num, String name, boolean active, boolean done) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        p.setOpaque(false);

        UIUtils.RoundedPanel circle = new UIUtils.RoundedPanel(16,
                done ? COLOR_LIGHT_BLUE : (active ? COLOR_PRIMARY_DARK : COLOR_INPUT_BG));
        circle.setPreferredSize(new Dimension(18, 18));
        circle.setLayout(new GridBagLayout());
        JLabel numLbl = new JLabel(done ? "✓" : num);
        numLbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        numLbl.setForeground(active ? Color.WHITE : COLOR_TEXT_MAIN);
        circle.add(numLbl);

        JLabel txt = new JLabel(name);
        txt.setFont(new Font("Segoe UI", (active || done) ? Font.BOLD : Font.PLAIN, 11));
        txt.setForeground((active || done) ? COLOR_TEXT_MAIN : COLOR_SECONDARY);

        p.add(circle);
        p.add(txt);
        return p;
    }

    // ==========================================
    // 3. LEFT COLUMN (ADDRESS + PAYMENT + BUTTONS)
    // ==========================================
    private JPanel createLeftContentArea() {
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        left.add(createDeliveryAddressCard());
        left.add(Box.createVerticalStrut(14));
        left.add(createPaymentMethodCard());
        left.add(Box.createVerticalStrut(14));
        left.add(createBottomActionRow());

        return left;
    }

    // Delivery Address Card
    private JPanel createDeliveryAddressCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 22, 18, 22));

        // Header
        JPanel cardHeader = new JPanel(new BorderLayout());
        cardHeader.setOpaque(false);
        cardHeader.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));

        JLabel title = new JLabel("Delivery Address", FontIcon.of(Feather.MAP_PIN, 15, COLOR_PRIMARY_DARK), SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);

        JButton btnReset = new JButton("Clear / Reset");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnReset.setForeground(COLOR_PRIMARY_DARK);
        btnReset.setContentAreaFilled(false);
        btnReset.setBorderPainted(false);
        btnReset.setFocusPainted(false);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> resetAddressFields());

        cardHeader.add(title, BorderLayout.WEST);
        cardHeader.add(btnReset, BorderLayout.EAST);

        // Fields
        txtFullName = new JTextField("Rahul Sharma");
        txtPhone = new JTextField("9876543210");
        txtAddress1 = new JTextField("24 Green Park Road");
        txtAddress2 = new JTextField("Near City Centre");
        txtCity = new JTextField("Pune");

        String[] states = {"Maharashtra", "Delhi", "Karnataka", "Tamil Nadu", "Gujarat", "Telangana", "Uttar Pradesh"};
        cmbState = new JComboBox<>(states);
        cmbState.setSelectedItem("Maharashtra");
        cmbState.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbState.setBackground(Color.WHITE);

        txtPinCode = new JTextField("411001");

        String[] types = {"Home", "Work", "Other"};
        cmbAddressType = new JComboBox<>(types);
        cmbAddressType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbAddressType.setBackground(Color.WHITE);

        // Form Layout (Left-Aligned Always)
        JPanel r1 = new JPanel(new GridLayout(1, 2, 14, 0));
        r1.setOpaque(false);
        r1.setMaximumSize(new Dimension(Short.MAX_VALUE, 56));
        r1.add(createFormField("Full Name *", txtFullName, Feather.USER));
        r1.add(createFormField("Phone Number (10 Digits) *", txtPhone, Feather.PHONE));

        JPanel r2 = new JPanel(new GridLayout(1, 1));
        r2.setOpaque(false);
        r2.setMaximumSize(new Dimension(Short.MAX_VALUE, 56));
        r2.add(createFormField("Address Line 1 (House/Flat, Building) *", txtAddress1, Feather.HOME));

        JPanel r3 = new JPanel(new GridLayout(1, 1));
        r3.setOpaque(false);
        r3.setMaximumSize(new Dimension(Short.MAX_VALUE, 56));
        r3.add(createFormField("Address Line 2 (Street, Landmark - Optional)", txtAddress2, Feather.MAP));

        JPanel r4 = new JPanel(new GridLayout(1, 3, 12, 0));
        r4.setOpaque(false);
        r4.setMaximumSize(new Dimension(Short.MAX_VALUE, 56));
        r4.add(createFormField("City *", txtCity, Feather.COMPASS));
        r4.add(createComboField("State *", cmbState));
        r4.add(createFormField("PIN Code *", txtPinCode, Feather.HASH));

        // Bottom Row
        JPanel r5 = new JPanel(new BorderLayout());
        r5.setOpaque(false);
        r5.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

        JPanel typePnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        typePnl.setOpaque(false);
        JLabel typeLbl = new JLabel("Deliver to:");
        typeLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        typeLbl.setForeground(COLOR_TEXT_MAIN);
        typePnl.add(typeLbl);
        typePnl.add(cmbAddressType);

        chkSaveAddress = new JCheckBox("Save this address for future checkouts");
        chkSaveAddress.setSelected(true);
        chkSaveAddress.setOpaque(false);
        chkSaveAddress.setFocusPainted(false);
        chkSaveAddress.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chkSaveAddress.setForeground(COLOR_SECONDARY);

        r5.add(typePnl, BorderLayout.WEST);
        r5.add(chkSaveAddress, BorderLayout.EAST);

        card.add(cardHeader);
        card.add(Box.createVerticalStrut(10));
        card.add(r1);
        card.add(Box.createVerticalStrut(6));
        card.add(r2);
        card.add(Box.createVerticalStrut(6));
        card.add(r3);
        card.add(Box.createVerticalStrut(6));
        card.add(r4);
        card.add(Box.createVerticalStrut(8));
        card.add(r5);

        return card;
    }

    private JPanel createFormField(String label, JTextField tf, Feather icon) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        // Strict Left Alignment
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(COLOR_TEXT_MAIN);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        UIUtils.RoundedPanel box = new UIUtils.RoundedPanel(8, COLOR_INPUT_BG, COLOR_BORDER);
        box.setLayout(new BorderLayout());
        box.setBorder(new EmptyBorder(5, 10, 5, 10));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel ic = new JLabel(FontIcon.of(icon, 13, COLOR_SECONDARY));
        tf.setOpaque(false);
        tf.setBorder(new EmptyBorder(0, 8, 0, 0));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setForeground(COLOR_TEXT_MAIN);

        box.add(ic, BorderLayout.WEST);
        box.add(tf, BorderLayout.CENTER);

        p.add(l);
        p.add(Box.createVerticalStrut(3));
        p.add(box);
        return p;
    }

    private JPanel createComboField(String label, JComboBox<?> cb) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(COLOR_TEXT_MAIN);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);

        cb.setPreferredSize(new Dimension(100, 31));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(l);
        p.add(Box.createVerticalStrut(3));
        p.add(cb);
        return p;
    }

    private void resetAddressFields() {
        txtFullName.setText("");
        txtPhone.setText("");
        txtAddress1.setText("");
        txtAddress2.setText("");
        txtCity.setText("");
        txtPinCode.setText("");
        txtFullName.requestFocus();
    }

    // Payment Options Card
    private JPanel createPaymentMethodCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("Payment Options", FontIcon.of(Feather.CREDIT_CARD, 15, COLOR_PRIMARY_DARK), SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel optionsRow = new JPanel(new GridLayout(1, 3, 12, 0));
        optionsRow.setOpaque(false);
        optionsRow.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));
        optionsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        rdoCod  = new JRadioButton("Cash on Delivery");
        rdoUpi  = new JRadioButton("Instant UPI");
        rdoCard = new JRadioButton("Debit / Credit Card");

        rdoCod.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(rdoCod);
        group.add(rdoUpi);
        group.add(rdoCard);

        tabCod  = createPaymentTab(rdoCod, Feather.DOLLAR_SIGN, true);
        tabUpi  = createPaymentTab(rdoUpi, Feather.ZAP, false);
        tabCard = createPaymentTab(rdoCard, Feather.CREDIT_CARD, false);

        optionsRow.add(tabCod);
        optionsRow.add(tabUpi);
        optionsRow.add(tabCard);

        paymentCardLayout = new CardLayout();
        dynamicPaymentContainer = new JPanel(paymentCardLayout);
        dynamicPaymentContainer.setOpaque(false);
        dynamicPaymentContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        dynamicPaymentContainer.setBorder(new EmptyBorder(10, 0, 0, 0));

        dynamicPaymentContainer.add(createCodDetailsView(), "COD");
        dynamicPaymentContainer.add(createUpiDetailsView(), "UPI");
        dynamicPaymentContainer.add(createCardDetailsView(), "CARD");

        rdoCod.addActionListener(e -> {
            paymentCardLayout.show(dynamicPaymentContainer, "COD");
            updateTabs(tabCod, tabUpi, tabCard);
        });
        rdoUpi.addActionListener(e -> {
            paymentCardLayout.show(dynamicPaymentContainer, "UPI");
            updateTabs(tabUpi, tabCod, tabCard);
        });
        rdoCard.addActionListener(e -> {
            paymentCardLayout.show(dynamicPaymentContainer, "CARD");
            updateTabs(tabCard, tabCod, tabUpi);
        });

        card.add(title);
        card.add(Box.createVerticalStrut(10));
        card.add(optionsRow);
        card.add(dynamicPaymentContainer);

        return card;
    }

    private JPanel createPaymentTab(JRadioButton rdo, Feather icon, boolean active) {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, active ? COLOR_LIGHT_BLUE : COLOR_INPUT_BG, COLOR_BORDER);
        p.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 8));
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));

        rdo.setOpaque(false);
        rdo.setFocusPainted(false);
        rdo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        rdo.setForeground(COLOR_TEXT_MAIN);

        p.add(new JLabel(FontIcon.of(icon, 13, COLOR_PRIMARY_DARK)));
        p.add(rdo);

        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rdo.setSelected(true);
                rdo.getActionListeners()[0].actionPerformed(null);
            }
        });

        return p;
    }

    private void updateTabs(JPanel active, JPanel in1, JPanel in2) {
        active.setBackground(COLOR_LIGHT_BLUE);
        in1.setBackground(COLOR_INPUT_BG);
        in2.setBackground(COLOR_INPUT_BG);
        active.repaint();
        in1.repaint();
        in2.repaint();
    }

    private JPanel createCodDetailsView() {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel info = new JLabel("<html><body style='color:#6B7280; font-size:11px; line-height: 1.4;'>"
                + "💵 <b>Pay on Delivery:</b> Keep exact cash ready or scan the delivery executive's UPI QR code upon arrival at your doorstep."
                + "</body></html>");
        p.add(info, BorderLayout.CENTER);
        return p;
    }

    private JPanel createUpiDetailsView() {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel l = new JLabel("Enter your UPI ID (Google Pay, PhonePe, Paytm, BHIM):");
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(COLOR_TEXT_MAIN);

        txtUpiId = new JTextField("rahul.sharma@oksbi");
        txtUpiId.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtUpiId.setPreferredSize(new Dimension(280, 28));

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        row.setOpaque(false);
        row.add(txtUpiId);

        JLabel sub = new JLabel("<html><body style='color:#6B7280; font-size:10px;'>A payment notification will be sent to your mobile UPI app for approval.</body></html>");

        p.add(l);
        p.add(row);
        p.add(sub);
        return p;
    }

    private JPanel createCardDetailsView() {
        UIUtils.RoundedPanel p = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        p.setLayout(new GridLayout(2, 2, 12, 8));
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        txtCardNum = new JTextField("4532 •••• •••• 8892");
        txtCardHolder = new JTextField("Rahul Sharma");
        txtCardExp = new JTextField("12/28");
        txtCardCvv = new JTextField("•••");

        p.add(createFormField("Card Number", txtCardNum, Feather.CREDIT_CARD));
        p.add(createFormField("Cardholder Name", txtCardHolder, Feather.USER));
        p.add(createFormField("Expiry (MM/YY)", txtCardExp, Feather.CALENDAR));
        p.add(createFormField("CVV", txtCardCvv, Feather.LOCK));

        return p;
    }

    // Bottom Action Row
    private JPanel createBottomActionRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Short.MAX_VALUE, 44));

        JButton btnBack = new JButton("← Back to Cart");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setForeground(COLOR_TEXT_MAIN);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            new MyCartPage().setVisible(true);
            dispose();
        });

        JButton btnPlace = new JButton("Place Order ➔") {
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
        btnPlace.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPlace.setForeground(Color.WHITE);
        btnPlace.setContentAreaFilled(false);
        btnPlace.setBorderPainted(false);
        btnPlace.setFocusPainted(false);
        btnPlace.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPlace.setPreferredSize(new Dimension(160, 38));
        btnPlace.addActionListener(e -> handlePlaceOrderAction());

        row.add(btnBack);
        row.add(btnPlace);
        return row;
    }

    // ==========================================
    // 4. RIGHT COLUMN (ORDER SUMMARY)
    // ==========================================
    private JPanel createOrderSummaryCard() {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(16, COLOR_CARD, COLOR_BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 18, 18, 18));
        card.setPreferredSize(new Dimension(380, 520)); // Fixed width so it never cuts off!

        JLabel title = new JLabel("Order Summary (3 items)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(COLOR_TEXT_MAIN);

        JPanel items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        items.setOpaque(false);
        items.setBorder(new EmptyBorder(8, 0, 8, 0));

        items.add(createSummaryRowItem("Wireless Headphones", 1, 1499, Feather.HEADPHONES));
        items.add(Box.createVerticalStrut(8));
        items.add(createSummaryRowItem("Casual Sneakers", 1, 1799, Feather.TAG));
        items.add(Box.createVerticalStrut(8));
        items.add(createSummaryRowItem("Travel Backpack", 2, 999, Feather.PACKAGE));

        JLabel lblSub = new JLabel("₹" + String.format("%,d", subtotal));
        JLabel lblShip = new JLabel("Free");
        lblShip.setForeground(COLOR_GREEN);
        JLabel lblDis = new JLabel("₹0");
        JLabel lblTot = new JLabel("₹" + String.format("%,d", total));
        lblTot.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTot.setForeground(COLOR_TEXT_MAIN);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(items);

        JSeparator s1 = new JSeparator();
        s1.setForeground(COLOR_BORDER);
        card.add(s1);
        card.add(Box.createVerticalStrut(10));

        card.add(createSummaryPriceRow("Cart Subtotal", lblSub));
        card.add(Box.createVerticalStrut(6));
        card.add(createSummaryPriceRow("Standard Delivery", lblShip));
        card.add(Box.createVerticalStrut(6));
        card.add(createSummaryPriceRow("Coupon / Discount", lblDis));
        card.add(Box.createVerticalStrut(10));

        JSeparator s2 = new JSeparator();
        s2.setForeground(COLOR_BORDER);
        card.add(s2);
        card.add(Box.createVerticalStrut(10));

        card.add(createSummaryPriceRow("Estimated Total", lblTot));
        card.add(Box.createVerticalStrut(14));

        UIUtils.RoundedPanel note = new UIUtils.RoundedPanel(10, COLOR_INPUT_BG, COLOR_BORDER);
        note.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 6));
        note.add(new JLabel(FontIcon.of(Feather.CHECK_CIRCLE, 13, COLOR_GREEN)));
        JLabel txt = new JLabel("Guaranteed 7-Day Easy Replacements");
        txt.setFont(new Font("Segoe UI", Font.BOLD, 10));
        txt.setForeground(COLOR_SECONDARY);
        note.add(txt);

        card.add(note);
        return card;
    }

    private JPanel createSummaryRowItem(String name, int qty, int unitPrice, Feather icon) {
        JPanel r = new JPanel(new BorderLayout(8, 0));
        r.setOpaque(false);

        UIUtils.RoundedPanel icBox = new UIUtils.RoundedPanel(8, COLOR_INPUT_BG);
        icBox.setPreferredSize(new Dimension(28, 28));
        icBox.setLayout(new GridBagLayout());
        icBox.add(new JLabel(FontIcon.of(icon, 13, COLOR_PRIMARY_DARK)));

        JPanel txt = new JPanel();
        txt.setLayout(new BoxLayout(txt, BoxLayout.Y_AXIS));
        txt.setOpaque(false);

        JLabel n = new JLabel(name);
        n.setFont(new Font("Segoe UI", Font.BOLD, 11));
        n.setForeground(COLOR_TEXT_MAIN);

        JLabel q = new JLabel("Qty: " + qty + " × ₹" + String.format("%,d", unitPrice));
        q.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        q.setForeground(COLOR_SECONDARY);

        txt.add(n);
        txt.add(q);

        JLabel price = new JLabel("₹" + String.format("%,d", qty * unitPrice));
        price.setFont(new Font("Segoe UI", Font.BOLD, 12));
        price.setForeground(COLOR_TEXT_MAIN);

        r.add(icBox, BorderLayout.WEST);
        r.add(txt, BorderLayout.CENTER);
        r.add(price, BorderLayout.EAST);
        return r;
    }

    private JPanel createSummaryPriceRow(String label, JLabel val) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Short.MAX_VALUE, 22));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(COLOR_SECONDARY);

        val.setFont(val.getFont().deriveFont(12f));
        r.add(l, BorderLayout.WEST);
        r.add(val, BorderLayout.EAST);
        return r;
    }

    // ==========================================
    // 5. VALIDATION & SUBMIT
    // ==========================================
    private void handlePlaceOrderAction() {
        String name  = txtFullName.getText().trim();
        String phone = txtPhone.getText().trim();
        String a1    = txtAddress1.getText().trim();
        String city  = txtCity.getText().trim();
        String pin   = txtPinCode.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name cannot be blank.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtFullName.requestFocus();
            return;
        }

        if (!Pattern.matches("^[6-9]\\d{9}$", phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit Indian mobile number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtPhone.requestFocus();
            return;
        }

        if (a1.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address Line 1 cannot be blank.", "Validation Error", JOptionPane.WARNING_MESSAGE);
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

        String method = rdoCod.isSelected() ? "Cash on Delivery" :
                        rdoUpi.isSelected() ? "UPI (Instant)" : "Credit/Debit Card";

        String msg = "<html><body style='width:340px; font-size:11px; line-height:1.45;'>"
                + "<h3 style='color:#1F2D42; margin-bottom:6px;'>🎉 Order Placed Successfully!</h3>"
                + "<b>Customer:</b> " + name + " (" + phone + ")<br>"
                + "<b>Delivery:</b> " + a1 + ", " + city + ", " + cmbState.getSelectedItem() + " - " + pin + "<br>"
                + "<b>Payment Mode:</b> " + method + "<br>"
                + "<b>Total Amount:</b> ₹" + String.format("%,d", total) + "<br><br>"
                + "<i style='color:#717E93;'>This is a UI demonstration. No actual payment has been processed.</i>"
                + "</body></html>";

        int opt = JOptionPane.showOptionDialog(
                this, msg, "Order Confirmation",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, new String[]{"Return to Catalog", "Close"}, "Return to Catalog"
        );

        if (opt == 0) {
            new ProductListingPage("All Categories").setVisible(true);
            dispose();
        }
        new OrderSuccessPage().setVisible(true);
        CheckoutPage.this.dispose();

    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 36, 12, 36));

        JLabel left = new JLabel("●  ShopSphere Enterprise Gateway — Connected to Master Node (Cluster US-East-1)");
        left.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        left.setForeground(COLOR_SECONDARY);

        JLabel right = new JLabel("© 2026 ShopSphere. All rights reserved.   •   Privacy Policy   •   Terms of Service");
        right.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        right.setForeground(COLOR_SECONDARY);

        footer.add(left, BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }
}
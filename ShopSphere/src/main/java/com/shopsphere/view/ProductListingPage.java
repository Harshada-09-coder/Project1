package com.shopsphere.view;

import com.shopsphere.entity.Category;
import com.shopsphere.entity.Product;
import com.shopsphere.entity.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import com.shopsphere.service.UserSession;
import com.shopsphere.service.ValidationException;
import com.shopsphere.util.ImageUtils;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductListingPage extends JFrame {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final CartService cartService = new CartService();

    private final Map<Integer, String> categoryMap = new HashMap<>();
    private final Map<String, Integer> categoryNameToId = new HashMap<>();

    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();

    private JTextField txtSearch;
    private JComboBox<String> cmbCategory;
    private JTextField txtMinPrice;
    private JTextField txtMaxPrice;
    private JComboBox<String> cmbSort;
    private JLabel lblProductCount;
    private JPanel gridCardsPanel;

    public ProductListingPage() {
        this("All Categories");
    }

    public ProductListingPage(String initialCategoryOrSearch) {
        setTitle("ShopSphere - All Products Catalog");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        int w = Math.min(1440, (int) (scr.width * 0.94));
        int h = Math.min(1024, (int) (scr.height * 0.92));
        setSize(w, h);
        setMinimumSize(new Dimension(1120, 720));
        setLocationRelativeTo(null);

        loadCategories();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIUtils.COLOR_BG);

        // Header
        root.add(createCustomerHeader(), BorderLayout.NORTH);

        // Center Content Split: Sidebar (West) + Product Catalog Area (Center)
        JPanel mainArea = new JPanel(new BorderLayout(16, 0));
        mainArea.setOpaque(false);
        mainArea.setBorder(new EmptyBorder(12, 20, 14, 20));

        mainArea.add(createSidebar(), BorderLayout.WEST);
        mainArea.add(createMainContentArea(), BorderLayout.CENTER);

        root.add(mainArea, BorderLayout.CENTER);
        root.add(UIUtils.createCommonFooter(), BorderLayout.SOUTH);

        setContentPane(root);

        loadProducts(initialCategoryOrSearch);
    }

    private void loadCategories() {
        categoryMap.clear();
        categoryNameToId.clear();
        try {
            List<Category> list = categoryService.getAllActiveCategories();
            for (Category c : list) {
                categoryMap.put(c.getCategoryId(), c.getCategoryName());
                categoryNameToId.put(c.getCategoryName(), c.getCategoryId());
            }
        } catch (Exception ignored) {}
    }

    private JPanel createCustomerHeader() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(UIUtils.COLOR_CARD);
        nav.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtils.COLOR_BORDER),
                new EmptyBorder(10, 24, 10, 24)
        ));

        // Brand
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);

        JLabel title = new JLabel("ShopSphere");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel sub = new JLabel("All Products Catalog");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        brand.add(title);
        brand.add(sub);

        // Center Search Box
        UIUtils.RoundedPanel searchBox = new UIUtils.RoundedPanel(10, UIUtils.COLOR_INPUT_BG, UIUtils.COLOR_BORDER);
        searchBox.setPreferredSize(new Dimension(380, 34));
        searchBox.setLayout(new BorderLayout());
        searchBox.setBorder(new EmptyBorder(0, 12, 0, 12));

        JLabel sIcon = new JLabel(FontIcon.of(Feather.SEARCH, 13, UIUtils.COLOR_TEXT_MUTED));
        txtSearch = new JTextField();
        txtSearch.setToolTipText("Search products, brands, or categories...");
        txtSearch.setBorder(null);
        txtSearch.setOpaque(false);
        txtSearch.addActionListener(e -> applyFilters());

        searchBox.add(sIcon, BorderLayout.WEST);
        searchBox.add(txtSearch, BorderLayout.CENTER);

        // Right Navigation Actions
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        JButton btnHome = createHeaderNavBtn("Home", Feather.HOME, () -> {
            new CustomerDashboardPage().setVisible(true);
            dispose();
        });

        JButton btnCart = createHeaderNavBtn("Cart", Feather.SHOPPING_BAG, () -> {
            new MyCartPage().setVisible(true);
            dispose();
        });

        JButton btnOrders = createHeaderNavBtn("Orders", Feather.PACKAGE, () -> {
            new MyOrdersPage().setVisible(true);
            dispose();
        });

        JButton btnAccount = createHeaderNavBtn("Account", Feather.USER, () -> {
            new MyAccountPage().setVisible(true);
            dispose();
        });

        JButton btnLogout = createHeaderNavBtn("Logout", Feather.LOG_OUT, () -> {
            UserSession.logout();
            new WelcomePage().setVisible(true);
            dispose();
        });

        right.add(btnHome);
        right.add(btnCart);
        right.add(btnOrders);
        right.add(btnAccount);
        right.add(btnLogout);

        nav.add(brand, BorderLayout.WEST);
        nav.add(searchBox, BorderLayout.CENTER);
        nav.add(right, BorderLayout.EAST);
        return nav;
    }

    private JButton createHeaderNavBtn(String title, Feather icon, Runnable action) {
        JButton btn = new JButton(title, FontIcon.of(icon, 13, UIUtils.COLOR_TEXT_MAIN));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtils.COLOR_CARD);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel fTitle = new JLabel("FILTER PRODUCTS");
        fTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        fTitle.setForeground(UIUtils.COLOR_TEXT_MAIN);
        fTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        sidebar.add(fTitle);
        sidebar.add(Box.createVerticalStrut(14));

        JLabel cTitle = new JLabel("Category");
        cTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        cTitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        cTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        sidebar.add(cTitle);
        sidebar.add(Box.createVerticalStrut(4));

        cmbCategory = new JComboBox<>();
        cmbCategory.addItem("All Categories");
        for (String cName : categoryNameToId.keySet()) {
            cmbCategory.addItem(cName);
        }
        cmbCategory.setPreferredSize(new Dimension(200, 32));
        cmbCategory.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        cmbCategory.addActionListener(e -> applyFilters());
        sidebar.add(cmbCategory);
        sidebar.add(Box.createVerticalStrut(16));

        JLabel pTitle = new JLabel("Price Range (₹)");
        pTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        pTitle.setForeground(UIUtils.COLOR_TEXT_MUTED);
        pTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
        sidebar.add(pTitle);
        sidebar.add(Box.createVerticalStrut(4));

        JPanel pRange = new JPanel(new GridLayout(1, 2, 8, 0));
        pRange.setOpaque(false);
        pRange.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        pRange.setPreferredSize(new Dimension(200, 32));
        txtMinPrice = new JTextField("0");
        txtMinPrice.setPreferredSize(new Dimension(90, 30));
        txtMinPrice.setMaximumSize(new Dimension(100, 30));
        txtMaxPrice = new JTextField("50000");
        txtMaxPrice.setPreferredSize(new Dimension(90, 30));
        txtMaxPrice.setMaximumSize(new Dimension(100, 30));
        pRange.add(txtMinPrice);
        pRange.add(txtMaxPrice);
        sidebar.add(pRange);
        sidebar.add(Box.createVerticalStrut(12));

        JButton btnApply = new JButton("Apply Filters");
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnApply.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btnApply.setPreferredSize(new Dimension(200, 34));
        btnApply.addActionListener(e -> applyFilters());
        sidebar.add(btnApply);

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JPanel createMainContentArea() {
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);

        // Top Filter Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(0, 0, 10, 0));

        lblProductCount = new JLabel("Showing all products");
        lblProductCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblProductCount.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightControls.setOpaque(false);

        cmbSort = new JComboBox<>(new String[]{"Sort: Default", "Price: Low to High", "Price: High to Low", "Name: A-Z"});
        cmbSort.addActionListener(e -> applySort());
        rightControls.add(cmbSort);

        topBar.add(lblProductCount, BorderLayout.WEST);
        topBar.add(rightControls, BorderLayout.EAST);

        // Card Grid
        gridCardsPanel = new JPanel(new GridLayout(0, 3, 16, 16));
        gridCardsPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(gridCardsPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        center.add(topBar, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        return center;
    }

    private void loadProducts(String initial) {
        try {
            allProducts = productService.getAllActiveProducts();
            if (initial != null && !initial.isEmpty() && !initial.equals("All Categories")) {
                if (categoryNameToId.containsKey(initial)) {
                    cmbCategory.setSelectedItem(initial);
                } else {
                    txtSearch.setText(initial);
                }
            }
            applyFilters();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching catalog: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        filteredProducts.clear();
        String catSelected = (String) cmbCategory.getSelectedItem();
        Integer catId = (catSelected != null && !catSelected.equals("All Categories")) ? categoryNameToId.get(catSelected) : null;
        String term = txtSearch.getText().trim().toLowerCase();

        double min = 0;
        double max = Double.MAX_VALUE;
        try {
            if (!txtMinPrice.getText().trim().isEmpty()) min = Double.parseDouble(txtMinPrice.getText().trim());
            if (!txtMaxPrice.getText().trim().isEmpty()) max = Double.parseDouble(txtMaxPrice.getText().trim());
        } catch (NumberFormatException ignored) {}

        for (Product p : allProducts) {
            boolean catOk = (catId == null || p.getCategoryId() == catId);
            boolean priceOk = (p.getPrice() >= min && p.getPrice() <= max);
            boolean textOk = term.isEmpty()
                    || (p.getProductName() != null && p.getProductName().toLowerCase().contains(term))
                    || (p.getBrand() != null && p.getBrand().toLowerCase().contains(term));

            if (catOk && priceOk && textOk) {
                filteredProducts.add(p);
            }
        }

        applySort();
    }

    private void applySort() {
        int idx = cmbSort.getSelectedIndex();
        if (idx == 1) {
            filteredProducts.sort(Comparator.comparingDouble(Product::getPrice));
        } else if (idx == 2) {
            filteredProducts.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        } else if (idx == 3) {
            filteredProducts.sort(Comparator.comparing(Product::getProductName));
        }

        renderCards();
    }

    private void renderCards() {
        gridCardsPanel.removeAll();
        lblProductCount.setText("Showing " + filteredProducts.size() + " of " + allProducts.size() + " products");

        for (Product p : filteredProducts) {
            gridCardsPanel.add(createProductCard(p));
        }

        gridCardsPanel.revalidate();
        gridCardsPanel.repaint();
    }

    private JPanel createProductCard(Product p) {
        UIUtils.RoundedPanel card = new UIUtils.RoundedPanel(14, Color.WHITE, UIUtils.COLOR_BORDER);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(14, 14, 14, 14));

        JPanel iconBox = new JPanel(new GridBagLayout());
        iconBox.setOpaque(false);
        iconBox.setPreferredSize(new Dimension(140, 120));
        ImageIcon pIcon = ImageUtils.getProductIcon(p, 130, 110);
        if (pIcon != null) {
            JLabel imgLabel = new JLabel(pIcon);
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconBox.add(imgLabel);
        } else {
            iconBox.add(new JLabel(FontIcon.of(Feather.BOX, 40, UIUtils.COLOR_PRIMARY_DARK)));
        }

        JPanel centerInfo = new JPanel();
        centerInfo.setLayout(new BoxLayout(centerInfo, BoxLayout.Y_AXIS));
        centerInfo.setOpaque(false);

        String catName = categoryMap.getOrDefault(p.getCategoryId(), "General");
        JLabel lblCat = new JLabel(catName.toUpperCase());
        lblCat.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblCat.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblName = new JLabel(p.getProductName());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(UIUtils.COLOR_TEXT_MAIN);

        JLabel lblBrand = new JLabel((p.getBrand() != null && !p.getBrand().isEmpty()) ? "by " + p.getBrand() : "Quality assured");
        lblBrand.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblBrand.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JLabel lblPrice = new JLabel(String.format("₹%,.2f", p.getPrice()));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setForeground(UIUtils.COLOR_PRIMARY_DARK);

        JLabel lblStock = new JLabel(p.getStockQuantity() > 0 ? "In Stock (" + p.getStockQuantity() + ")" : "Out of Stock");
        lblStock.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblStock.setForeground(p.getStockQuantity() > 0 ? UIUtils.COLOR_GREEN : UIUtils.COLOR_RED);

        centerInfo.add(lblCat);
        centerInfo.add(Box.createVerticalStrut(4));
        centerInfo.add(lblName);
        centerInfo.add(lblBrand);
        centerInfo.add(Box.createVerticalStrut(6));
        centerInfo.add(lblPrice);
        centerInfo.add(lblStock);

        JButton btnAdd = new JButton("Add to Cart", FontIcon.of(Feather.SHOPPING_BAG, 12, Color.WHITE)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(p.getStockQuantity() > 0 ? new Color(0x60, 0x8E, 0xB5) : Color.GRAY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setContentAreaFilled(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setEnabled(p.getStockQuantity() > 0);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> handleAddToCart(p));

        card.add(iconBox, BorderLayout.NORTH);
        card.add(centerInfo, BorderLayout.CENTER);
        card.add(btnAdd, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() != btnAdd) {
                    new ProductDetailsPage(p.getProductId()).setVisible(true);
                    dispose();
                }
            }
        });

        return card;
    }

    private void handleAddToCart(Product p) {
        User currentUser = UserSession.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in to add items to your shopping cart.", "Login Required", JOptionPane.INFORMATION_MESSAGE);
            new CustomerLoginPage().setVisible(true);
            dispose();
            return;
        }

        try {
            boolean ok = cartService.addToCart(currentUser.getUserId(), p.getProductId(), 1);
            if (ok) {
                JOptionPane.showMessageDialog(this, "'" + p.getProductName() + "' added to your cart!", "Cart Updated", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(this, ve.getMessage(), "Cart Warning", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to add product to cart: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


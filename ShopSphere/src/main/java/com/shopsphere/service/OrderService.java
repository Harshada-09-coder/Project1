package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.entity.Cart;
import com.shopsphere.entity.CartItem;
import com.shopsphere.entity.OrderItem;
import com.shopsphere.entity.Orders;
import com.shopsphere.entity.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Service orchestrating customer checkout, order placement with atomic transactions,
 * stock reservation/deduction, and fulfillment pipeline management.
 */
public class OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    /**
     * Places an order from the user's active shopping cart using ACID transaction.
     * Validates stock availability, deducts stock, inserts order + items, and clears cart.
     */
    public int placeOrderFromCart(int userId, String shippingAddress, String city, String state,
                                  String postalCode, String country, String paymentMethod, String notes) {
        // Validate inputs (Rule 10)
        if (userId <= 0) {
            throw new ValidationException("A valid customer account is required to place an order.");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new ValidationException("Shipping address is required.");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new ValidationException("City is required.");
        }
        if (state == null || state.trim().isEmpty()) {
            throw new ValidationException("State is required.");
        }
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new ValidationException("Postal/PIN code is required.");
        }

        Cart cart = cartDAO.getOrCreateCart(userId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new ValidationException("Cannot place order: Your shopping cart is empty.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double calculatedTotal = 0.0;

        // Verify stock for all items BEFORE beginning the transaction (Rule 4)
        for (CartItem ci : cart.getItems()) {
            Product p = productDAO.findById(ci.getProductId());
            if (p == null || !p.isActive()) {
                throw new ValidationException("Product '" + ci.getProductName() + "' is no longer available.");
            }
            if (p.getStockQuantity() < ci.getQuantity()) {
                throw new ValidationException("Insufficient stock for '" + p.getProductName() +
                        "'. Requested: " + ci.getQuantity() + ", Available: " + p.getStockQuantity());
            }

            double unitPrice = p.getPrice();
            OrderItem oi = new OrderItem(p.getProductId(), ci.getQuantity(), unitPrice);
            oi.setProductName(p.getProductName());
            oi.setBrand(p.getBrand());
            oi.setImageUrl(p.getImageUrl());
            orderItems.add(oi);
            calculatedTotal += oi.getSubtotal();
        }

        String validPaymentMethod = (paymentMethod != null && !paymentMethod.trim().isEmpty()) ? paymentMethod.trim().toUpperCase() : "COD";
        Orders order = new Orders(
                userId,
                calculatedTotal,
                validPaymentMethod,
                shippingAddress.trim(),
                city.trim(),
                state.trim(),
                postalCode.trim(),
                notes != null ? notes.trim() : ""
        );
        order.setShippingCountry(country != null && !country.trim().isEmpty() ? country.trim() : "India");

        // Execute atomic transaction (Rules 6, 7, 8, 9)
        int orderId = orderDAO.placeOrder(order, orderItems, cart.getCartId());
        if (orderId <= 0) {
            throw new ValidationException("Order transaction failed or was rolled back due to stock conflict.");
        }

        return orderId;
    }

    public List<Orders> getOrderHistory(int userId) {
        if (userId <= 0) return new ArrayList<>();
        return orderDAO.findByUserId(userId);
    }

    public Orders getOrderById(int orderId) {
        if (orderId <= 0) return null;
        return orderDAO.findById(orderId);
    }

    public List<Orders> getAllOrders() {
        return orderDAO.findAllOrders();
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        if (orderId <= 0 || newStatus == null || newStatus.trim().isEmpty()) {
            throw new ValidationException("Valid order ID and status are required.");
        }
        boolean ok = orderDAO.updateOrderStatus(orderId, newStatus.trim().toUpperCase());
        if (!ok) {
            throw new ValidationException("Failed to update status for order #" + orderId);
        }
        return true;
    }

    public boolean updatePaymentStatus(int orderId, String paymentStatus) {
        if (orderId <= 0 || paymentStatus == null || paymentStatus.trim().isEmpty()) {
            throw new ValidationException("Valid order ID and payment status required.");
        }
        return orderDAO.updatePaymentStatus(orderId, paymentStatus.trim().toUpperCase());
    }

    public boolean updateTrackingNumber(int orderId, String trackingNumber) {
        return orderDAO.updateTrackingNumber(orderId, trackingNumber);
    }
}

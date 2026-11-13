package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {

    private OrderService orderService;
    private StubOrderDAO stubOrderDAO;
    private StubCartDAO stubCartDAO;
    private StubProductDAO stubProductDAO;

    private static class StubProductDAO extends ProductDAO {
        private final Map<Integer, Product> products = new HashMap<>();

        public void put(Product p) {
            products.put(p.getProductId(), p);
        }

        @Override
        public Product findById(int id) {
            return products.get(id);
        }
    }

    private static class StubCartDAO extends CartDAO {
        private final Map<Integer, Cart> carts = new HashMap<>();

        public void put(int userId, Cart c) {
            carts.put(userId, c);
        }

        @Override
        public Cart getOrCreateCart(int userId) {
            return carts.get(userId);
        }
    }

    private static class StubOrderDAO extends OrderDAO {
        private final List<Orders> ordersList = new ArrayList<>();
        private int idCounter = 1001;

        @Override
        public int placeOrder(Orders order, List<OrderItem> items, Integer cartId) {
            order.setOrderId(idCounter++);
            order.setItems(items);
            ordersList.add(order);
            return order.getOrderId();
        }

        @Override
        public Orders findById(int id) {
            for (Orders o : ordersList) {
                if (o.getOrderId() == id) return o;
            }
            return null;
        }

        @Override
        public boolean updateOrderStatus(int orderId, String newStatus) {
            Orders o = findById(orderId);
            if (o != null) {
                o.setOrderStatus(newStatus);
                return true;
            }
            return false;
        }
    }

    @BeforeEach
    void setUp() {
        stubOrderDAO = new StubOrderDAO();
        stubCartDAO = new StubCartDAO();
        stubProductDAO = new StubProductDAO();
        orderService = new OrderService(stubOrderDAO, stubCartDAO, stubProductDAO);

        Product p = new Product();
        p.setProductId(1);
        p.setProductName("Wireless Headphones");
        p.setPrice(2499.0);
        p.setStockQuantity(20);
        p.setActive(true);
        stubProductDAO.put(p);
    }

    @Test
    @DisplayName("Place Order: Rejects invalid user ID (<= 0)")
    void testPlaceOrder_InvalidUserId_ThrowsValidationException() {
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            orderService.placeOrderFromCart(0, "123 Main St", "Mumbai", "Maharashtra", "400001", "India", "COD", "");
        });
        assertTrue(ex.getMessage().contains("customer account is required"));
    }

    @Test
    @DisplayName("Place Order: Rejects empty shipping address")
    void testPlaceOrder_MissingAddress_ThrowsValidationException() {
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            orderService.placeOrderFromCart(1, "   ", "Mumbai", "Maharashtra", "400001", "India", "COD", "");
        });
        assertTrue(ex.getMessage().contains("Shipping address is required"));
    }

    @Test
    @DisplayName("Place Order: Rejects empty shopping cart")
    void testPlaceOrder_EmptyCart_ThrowsValidationException() {
        Cart emptyCart = new Cart(1);
        stubCartDAO.put(1, emptyCart);

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            orderService.placeOrderFromCart(1, "123 Main St", "Mumbai", "Maharashtra", "400001", "India", "COD", "");
        });
        assertTrue(ex.getMessage().contains("shopping cart is empty"));
    }

    @Test
    @DisplayName("Place Order: Rejects checkout when requested quantity exceeds stock (Rule 4)")
    void testPlaceOrder_InsufficientStock_ThrowsValidationException() {
        Cart cart = new Cart(1);
        cart.setCartId(10);
        cart.addItem(new CartItem(1, 10, 1, 25, 2499.0)); // 25 requested, but stock is 20
        stubCartDAO.put(1, cart);

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            orderService.placeOrderFromCart(1, "123 Main St", "Mumbai", "Maharashtra", "400001", "India", "COD", "");
        });
        assertTrue(ex.getMessage().contains("Insufficient stock"));
    }

    @Test
    @DisplayName("Place Order: Successfully executes transaction for valid cart")
    void testPlaceOrder_Success() {
        Cart cart = new Cart(1);
        cart.setCartId(10);
        cart.addItem(new CartItem(1, 10, 1, 2, 2499.0)); // 2 units requested, stock is 20
        stubCartDAO.put(1, cart);

        int orderId = orderService.placeOrderFromCart(
                1,
                "456 Park Avenue",
                "Pune",
                "Maharashtra",
                "411001",
                "India",
                "UPI",
                "Please ring bell"
        );

        assertTrue(orderId >= 1000);
        Orders placed = stubOrderDAO.findById(orderId);
        assertNotNull(placed);
        assertEquals(4998.0, placed.getTotalAmount());
        assertEquals("Pune", placed.getShippingCity());
        assertEquals("UPI", placed.getPaymentMethod());
    }

    @Test
    @DisplayName("Update Order Status: Enforces non-empty status and valid order ID")
    void testUpdateOrderStatus_Validation() {
        assertThrows(ValidationException.class, () -> orderService.updateOrderStatus(0, "SHIPPED"));
        assertThrows(ValidationException.class, () -> orderService.updateOrderStatus(1, "   "));
    }
}

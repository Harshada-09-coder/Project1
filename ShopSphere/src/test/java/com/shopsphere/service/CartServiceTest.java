package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.entity.Cart;
import com.shopsphere.entity.CartItem;
import com.shopsphere.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    private CartService cartService;
    private StubCartDAO stubCartDAO;
    private StubProductDAO stubProductDAO;

    private static class StubProductDAO extends ProductDAO {
        private final Map<Integer, Product> map = new HashMap<>();

        public void put(Product p) {
            map.put(p.getProductId(), p);
        }

        @Override
        public Product findById(int id) {
            return map.get(id);
        }
    }

    private static class StubCartDAO extends CartDAO {
        private final Map<Integer, Cart> userCarts = new HashMap<>();
        private int itemCounter = 1;

        @Override
        public Cart getOrCreateCart(int userId) {
            return userCarts.computeIfAbsent(userId, uid -> {
                Cart c = new Cart(uid);
                c.setCartId(uid * 10);
                return c;
            });
        }

        @Override
        public boolean addItemToCart(int userId, int productId, int quantity) {
            Cart cart = getOrCreateCart(userId);
            for (CartItem ci : cart.getItems()) {
                if (ci.getProductId() == productId) {
                    ci.setQuantity(ci.getQuantity() + quantity);
                    return true;
                }
            }
            CartItem item = new CartItem(itemCounter++, cart.getCartId(), productId, quantity, 100.0);
            cart.addItem(item);
            return true;
        }

        @Override
        public boolean updateItemQuantity(int cartItemId, int newQuantity) {
            for (Cart c : userCarts.values()) {
                for (CartItem ci : c.getItems()) {
                    if (ci.getCartItemId() == cartItemId) {
                        ci.setQuantity(newQuantity);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean removeCartItem(int cartItemId) {
            for (Cart c : userCarts.values()) {
                c.getItems().removeIf(ci -> ci.getCartItemId() == cartItemId);
            }
            return true;
        }
    }

    @BeforeEach
    void setUp() {
        stubProductDAO = new StubProductDAO();
        stubCartDAO = new StubCartDAO();
        cartService = new CartService(stubCartDAO, stubProductDAO);

        // Seed sample product with 10 units in stock
        Product p = new Product();
        p.setProductId(1);
        p.setProductName("Bluetooth Speaker");
        p.setPrice(1299.0);
        p.setStockQuantity(10);
        p.setActive(true);
        stubProductDAO.put(p);
    }

    @Test
    @DisplayName("Rule 5: Cart quantity must be greater than zero")
    void testAddToCart_ZeroOrNegativeQuantity_ThrowsValidationException() {
        ValidationException ex1 = assertThrows(ValidationException.class, () -> {
            cartService.addToCart(1, 1, 0);
        });
        assertTrue(ex1.getMessage().contains("greater than zero"));

        ValidationException ex2 = assertThrows(ValidationException.class, () -> {
            cartService.addToCart(1, 1, -2);
        });
        assertTrue(ex2.getMessage().contains("greater than zero"));
    }

    @Test
    @DisplayName("Rule 4: Cannot add more than available stock")
    void testAddToCart_ExceedsAvailableStock_ThrowsValidationException() {
        // Product has 10 units in stock; attempting to add 15 units must fail
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            cartService.addToCart(1, 1, 15);
        });
        assertTrue(ex.getMessage().contains("exceeds available stock") || ex.getMessage().contains("Only 10 units available"));
    }

    @Test
    @DisplayName("Add to Cart: Successfully adds within available stock")
    void testAddToCart_ValidStock_Success() {
        boolean ok = cartService.addToCart(1, 1, 4);
        assertTrue(ok);

        Cart cart = cartService.getCartForUser(1);
        assertEquals(1, cart.getItems().size());
        assertEquals(4, cart.getItems().get(0).getQuantity());
    }

    @Test
    @DisplayName("Rule 4 & 5: Cumulative quantity in cart cannot exceed available stock")
    void testAddToCart_CumulativeQuantity_ExceedsStock_ThrowsValidationException() {
        // Add 7 units (allowed, 7 <= 10)
        cartService.addToCart(1, 1, 7);

        // Attempting to add 5 more units (7 + 5 = 12 > 10) must fail
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            cartService.addToCart(1, 1, 5);
        });
        assertTrue(ex.getMessage().contains("exceeds available stock"));
    }

    @Test
    @DisplayName("Update Cart Item: Non-positive quantity removes item")
    void testUpdateCartItemQuantity_ZeroQuantity_RemovesItem() {
        cartService.addToCart(1, 1, 2);
        Cart cart = cartService.getCartForUser(1);
        int cartItemId = cart.getItems().get(0).getCartItemId();

        cartService.updateCartItemQuantity(cartItemId, 0, 1);
        assertEquals(0, cart.getItems().size());
    }
}

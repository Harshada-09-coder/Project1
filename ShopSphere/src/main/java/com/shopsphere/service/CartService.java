package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.entity.Cart;
import com.shopsphere.entity.CartItem;
import com.shopsphere.entity.Product;

import java.util.List;

/**
 * Service encapsulating Shopping Cart operations with inventory availability validation.
 */
public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.productDAO = new ProductDAO();
    }

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    /**
     * Gets or creates an active cart for the customer.
     */
    public Cart getCartForUser(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user ID to load shopping cart.");
        }
        return cartDAO.getOrCreateCart(userId);
    }

    /**
     * Adds a product to the cart ensuring quantity is positive and doesn't exceed stock.
     */
    public boolean addToCart(int userId, int productId, int quantity) {
        // Rule 5: Cart quantities must be positive
        if (quantity <= 0) {
            throw new ValidationException("Cart quantity must be greater than zero (Rule 5).");
        }

        Product product = productDAO.findById(productId);
        if (product == null || !product.isActive()) {
            throw new ValidationException("Product is not available for purchase.");
        }

        // Rule 4: Customers cannot order more than available stock
        if (quantity > product.getStockQuantity()) {
            throw new ValidationException("Cannot add " + quantity + " units to cart. Only " +
                    product.getStockQuantity() + " units available in stock.");
        }

        // Check if item is already in cart
        Cart cart = getCartForUser(userId);
        if (cart != null && cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getProductId() == productId) {
                    int totalRequested = item.getQuantity() + quantity;
                    if (totalRequested > product.getStockQuantity()) {
                        throw new ValidationException("Total quantity in cart (" + totalRequested +
                                ") exceeds available stock (" + product.getStockQuantity() + ").");
                    }
                    break;
                }
            }
        }

        boolean added = cartDAO.addItemToCart(userId, productId, quantity);
        if (!added) {
            throw new ValidationException("Could not add product to cart.");
        }
        return true;
    }

    /**
     * Updates cart item quantity, checking against stock availability.
     */
    public boolean updateCartItemQuantity(int cartItemId, int newQuantity, int productId) {
        // If non-positive, remove item
        if (newQuantity <= 0) {
            return removeCartItem(cartItemId);
        }

        // Validate stock availability
        Product product = productDAO.findById(productId);
        if (product != null && newQuantity > product.getStockQuantity()) {
            throw new ValidationException("Requested quantity (" + newQuantity +
                    ") exceeds available stock (" + product.getStockQuantity() + ").");
        }

        boolean updated = cartDAO.updateItemQuantity(cartItemId, newQuantity);
        if (!updated) {
            throw new ValidationException("Could not update item quantity.");
        }
        return true;
    }

    public boolean removeCartItem(int cartItemId) {
        if (cartItemId <= 0) return false;
        return cartDAO.removeCartItem(cartItemId);
    }

    public boolean clearCart(int cartId) {
        if (cartId <= 0) return false;
        return cartDAO.clearCart(cartId);
    }
}

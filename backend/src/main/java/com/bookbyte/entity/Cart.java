package com.bookbyte.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart entity representing a user's shopping cart.
 * 
 * <p>This entity manages the shopping cart functionality for users,
 * allowing them to add books and manage quantities before checkout.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Shopping cart management</li>
 *   <li>Automatic total calculation</li>
 *   <li>Cart item management</li>
 *   <li>User association</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    /**
     * Unique identifier for the cart.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User who owns this cart.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * List of items in the cart.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, 
                fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    /**
     * Total price of all items in the cart.
     */
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    /**
     * Total number of items in the cart.
     */
    @Column(name = "total_items")
    private Integer totalItems = 0;

    /**
     * Timestamp when the cart was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the cart was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Adds an item to the cart.
     * 
     * @param item the cart item to add
     */
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
        updateTotals();
    }

    /**
     * Removes an item from the cart.
     * 
     * @param item the cart item to remove
     */
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
        updateTotals();
    }

    /**
     * Updates the cart totals based on current items.
     */
    public void updateTotals() {
        this.totalPrice = items.stream()
                .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.totalItems = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Clears all items from the cart.
     */
    public void clearCart() {
        items.clear();
        updateTotals();
    }
}
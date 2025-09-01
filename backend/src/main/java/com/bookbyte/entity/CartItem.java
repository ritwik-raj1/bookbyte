package com.bookbyte.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CartItem entity representing an individual item in a user's shopping cart.
 * 
 * <p>This entity manages the relationship between books and carts,
 * storing quantity and price information for each item.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Book-cart association</li>
 *   <li>Quantity management</li>
 *   <li>Price calculation</li>
 *   <li>Cart relationship management</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    /**
     * Unique identifier for the cart item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cart that contains this item.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * Book in this cart item.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /**
     * Quantity of the book in the cart.
     */
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    /**
     * Price of the book at the time it was added to cart.
     */
    @Column(name = "price_at_time", precision = 10, scale = 2, nullable = false)
    private BigDecimal priceAtTime;

    /**
     * Timestamp when the item was added to the cart.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the item was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Calculates the total price for this cart item.
     * 
     * @return the total price (price * quantity)
     */
    public BigDecimal getTotalPrice() {
        return priceAtTime.multiply(new BigDecimal(quantity));
    }

    /**
     * Updates the quantity and recalculates totals.
     * 
     * @param newQuantity the new quantity
     */
    public void updateQuantity(Integer newQuantity) {
        this.quantity = newQuantity;
        if (cart != null) {
            cart.updateTotals();
        }
    }
}
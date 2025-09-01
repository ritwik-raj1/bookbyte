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
 * OrderItem entity representing an individual item in a customer's order.
 * 
 * <p>This entity manages the relationship between books and orders,
 * storing quantity and price information for each ordered item.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Book-order association</li>
 *   <li>Quantity management</li>
 *   <li>Price snapshot at order time</li>
 *   <li>Total price calculation</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    /**
     * Unique identifier for the order item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Order that contains this item.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Book in this order item.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /**
     * Quantity of the book ordered.
     */
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    /**
     * Price of the book at the time of order.
     */
    @Column(name = "price_at_time", precision = 10, scale = 2, nullable = false)
    private BigDecimal priceAtTime;

    /**
     * Timestamp when the order item was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the order item was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Calculates the total price for this order item.
     * 
     * @return the total price (price * quantity)
     */
    public BigDecimal getTotalPrice() {
        return priceAtTime.multiply(new BigDecimal(quantity));
    }
}
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
 * Order entity representing a customer's order.
 * 
 * <p>This entity manages order information including status tracking,
 * customer details, and order items.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Order status management</li>
 *   <li>Customer association</li>
 *   <li>Order item management</li>
 *   <li>Total calculation</li>
 *   <li>Status tracking</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    /**
     * Unique identifier for the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Order number for customer reference.
     */
    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    /**
     * User who placed this order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Current status of the order.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    /**
     * Total amount of the order.
     */
    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * Shipping address for the order.
     */
    @Column(name = "shipping_address", columnDefinition = "TEXT", nullable = false)
    private String shippingAddress;

    /**
     * Reason for cancellation if order is cancelled.
     */
    @Column(name = "cancellation_reason")
    private String cancellationReason;

    /**
     * List of items in the order.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, 
                fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Timestamp when the order was placed.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the order was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Timestamp when the order was shipped.
     */
    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    /**
     * Timestamp when the order was delivered.
     */
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    /**
     * Timestamp when the order was cancelled.
     */
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    /**
     * Enumeration of order statuses.
     */
    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }

    /**
     * Pre-persist method to generate order number if not set.
     */
    @PrePersist
    protected void onCreate() {
        if (orderNumber == null) {
            this.orderNumber = "ORD" + System.currentTimeMillis();
        }
    }

    /**
     * Updates the order total based on current items.
     */
    public void updateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Adds an item to the order.
     * 
     * @param item the order item to add
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        updateTotal();
    }

    /**
     * Updates the order status and sets appropriate timestamps.
     * 
     * @param newStatus the new status to set
     */
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        LocalDateTime now = LocalDateTime.now();
        
        switch (newStatus) {
            case SHIPPED:
                this.shippedAt = now;
                break;
            case DELIVERED:
                this.deliveredAt = now;
                break;
            case CANCELLED:
                this.cancelledAt = now;
                break;
        }
    }
}
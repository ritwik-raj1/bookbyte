package com.bookbyte.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
 * Book entity representing a book in the BookByte catalog.
 * 
 * <p>This entity stores all information about a book including
 * title, author, price, category, and stock information.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Comprehensive book details</li>
 *   <li>Stock management</li>
 *   <li>Category classification</li>
 *   <li>Soft delete functionality</li>
 *   <li>Audit trail with timestamps</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    /**
     * Unique identifier for the book.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the book.
     */
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Author of the book.
     */
    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    @Column(name = "author", nullable = false)
    private String author;

    /**
     * ISBN number of the book.
     */
    @Column(name = "isbn", unique = true)
    private String isbn;

    /**
     * Description of the book.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Price of the book.
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Category of the book.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private BookCategory category;

    /**
     * Current stock quantity available.
     */
    @Min(value = 0, message = "Stock cannot be negative")
    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    /**
     * Publication year of the book.
     */
    @Min(value = 1800, message = "Publication year must be after 1800")
    @Max(value = 2024, message = "Publication year cannot be in the future")
    @Column(name = "publication_year")
    private Integer publicationYear;

    /**
     * Publisher of the book.
     */
    @Column(name = "publisher")
    private String publisher;

    /**
     * Number of pages in the book.
     */
    @Min(value = 1, message = "Page count must be at least 1")
    @Column(name = "pages")
    private Integer pages;

    /**
     * Language of the book.
     */
    @Column(name = "language")
    private String language = "English";

    /**
     * Flag indicating if the book is available for purchase.
     */
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    /**
     * Flag indicating if the book has been soft deleted.
     */
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    /**
     * Timestamp when the book was added to the catalog.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the book was last updated.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * List of cart items containing this book.
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    /**
     * List of order items containing this book.
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Enumeration of book categories.
     */
    public enum BookCategory {
        FICTION, NON_FICTION, SCIENCE_FICTION, MYSTERY, ROMANCE, 
        THRILLER, BIOGRAPHY, HISTORY, SCIENCE, TECHNOLOGY, 
        PHILOSOPHY, RELIGION, CHILDREN, YOUNG_ADULT, ACADEMIC
    }
}
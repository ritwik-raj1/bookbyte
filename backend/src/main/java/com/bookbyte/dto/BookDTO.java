package com.bookbyte.dto;

import com.bookbyte.entity.Book;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Book information.
 * 
 * <p>This DTO is used for transferring book data between the frontend
 * and backend, including validation rules.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Book creation and update</li>
 *   <li>Input validation</li>
 *   <li>Category management</li>
 *   <li>Stock management</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    /**
     * Book's unique identifier.
     */
    private Long id;

    /**
     * Title of the book.
     */
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    /**
     * Author of the book.
     */
    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;

    /**
     * ISBN number of the book.
     */
    private String isbn;

    /**
     * Description of the book.
     */
    private String description;

    /**
     * Price of the book.
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    /**
     * Category of the book.
     */
    @NotNull(message = "Category is required")
    private Book.BookCategory category;

    /**
     * Current stock quantity available.
     */
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock = 0;

    /**
     * Publication year of the book.
     */
    @Min(value = 1800, message = "Publication year must be after 1800")
    @Max(value = 2024, message = "Publication year cannot be in the future")
    private Integer publicationYear;

    /**
     * Publisher of the book.
     */
    private String publisher;

    /**
     * Number of pages in the book.
     */
    @Min(value = 1, message = "Page count must be at least 1")
    private Integer pages;

    /**
     * Language of the book.
     */
    private String language = "English";

    /**
     * Flag indicating if the book is available for purchase.
     */
    private Boolean isAvailable = true;

    /**
     * Timestamp when the book was added to the catalog.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the book was last updated.
     */
    private LocalDateTime updatedAt;
}
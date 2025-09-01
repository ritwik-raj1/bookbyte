package com.bookbyte.repository;

import com.bookbyte.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Book entity operations.
 * 
 * <p>This repository provides methods for database operations related to books,
 * including search, filtering, and catalog management.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Book search and filtering</li>
 *   <li>Category-based queries</li>
 *   <li>Price range filtering</li>
 *   <li>Stock management</li>
 *   <li>Soft delete support</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds all available books that are not deleted.
     * 
     * @return List of available books
     */
    List<Book> findByIsDeletedFalseAndIsAvailableTrue();

    /**
     * Finds books by category that are available and not deleted.
     * 
     * @param category the book category to filter by
     * @return List of books in the specified category
     */
    List<Book> findByCategoryAndIsDeletedFalseAndIsAvailableTrue(Book.BookCategory category);

    /**
     * Finds books by title containing the given string (case-insensitive).
     * 
     * @param title the title to search for
     * @return List of books whose titles contain the search string
     */
    List<Book> findByTitleContainingIgnoreCaseAndIsDeletedFalseAndIsAvailableTrue(String title);

    /**
     * Finds books by author containing the given string (case-insensitive).
     * 
     * @param author the author to search for
     * @return List of books whose authors contain the search string
     */
    List<Book> findByAuthorContainingIgnoreCaseAndIsDeletedFalseAndIsAvailableTrue(String author);

    /**
     * Finds books by title or author containing the given string (case-insensitive).
     * 
     * @param searchTerm the term to search for in title or author
     * @return List of books matching the search criteria
     */
    @Query("SELECT b FROM Book b WHERE (LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND b.isDeleted = false AND b.isAvailable = true")
    List<Book> findByTitleOrAuthorContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Finds books within a price range that are available and not deleted.
     * 
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return List of books within the price range
     */
    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :minPrice AND :maxPrice " +
           "AND b.isDeleted = false AND b.isAvailable = true")
    List<Book> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, 
                                  @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Finds books with stock greater than zero that are available and not deleted.
     * 
     * @return List of books with available stock
     */
    List<Book> findByStockGreaterThanAndIsDeletedFalseAndIsAvailableTrue(Integer stock);

    /**
     * Finds books by publisher that are available and not deleted.
     * 
     * @param publisher the publisher to filter by
     * @return List of books from the specified publisher
     */
    List<Book> findByPublisherAndIsDeletedFalseAndIsAvailableTrue(String publisher);

    /**
     * Finds books by publication year that are available and not deleted.
     * 
     * @param year the publication year to filter by
     * @return List of books from the specified year
     */
    List<Book> findByPublicationYearAndIsDeletedFalseAndIsAvailableTrue(Integer year);

    /**
     * Finds books by language that are available and not deleted.
     * 
     * @param language the language to filter by
     * @return List of books in the specified language
     */
    List<Book> findByLanguageAndIsDeletedFalseAndIsAvailableTrue(String language);

    /**
     * Finds all books with pagination support.
     * 
     * @param pageable pagination information
     * @return Page of books
     */
    Page<Book> findByIsDeletedFalseAndIsAvailableTrue(Pageable pageable);

    /**
     * Counts books by category that are available and not deleted.
     * 
     * @param category the book category to count
     * @return the number of books in the specified category
     */
    Long countByCategoryAndIsDeletedFalseAndIsAvailableTrue(Book.BookCategory category);

    /**
     * Finds books with low stock (less than or equal to specified threshold).
     * 
     * @param threshold the stock threshold
     * @return List of books with low stock
     */
    @Query("SELECT b FROM Book b WHERE b.stock <= :threshold AND b.isDeleted = false")
    List<Book> findBooksWithLowStock(@Param("threshold") Integer threshold);
}
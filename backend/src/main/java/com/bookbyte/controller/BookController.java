package com.bookbyte.controller;

import com.bookbyte.dto.BookDTO;
import com.bookbyte.entity.Book;
import com.bookbyte.service.BookService;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for handling book-related operations.
 * 
 * <p>This controller provides endpoints for book management, search,
 * filtering, and bulk import functionality.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Book CRUD operations</li>
 *   <li>Book search and filtering</li>
 *   <li>CSV bulk import</li>
 *   <li>Public and admin endpoints</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BookController {

    private final BookService bookService;

    /**
     * Retrieves all available books (public endpoint).
     * 
     * @return list of all available books
     */
    @GetMapping("/public/all")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        List<BookDTO> bookDTOs = bookService.convertToDTOList(books);
        return ResponseEntity.ok(bookDTOs);
    }

    /**
     * Searches for books by title or author (public endpoint).
     * 
     * @param searchTerm the search term
     * @return list of matching books
     */
    @GetMapping("/public/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String searchTerm) {
        List<Book> books = bookService.searchBooks(searchTerm);
        List<BookDTO> bookDTOs = bookService.convertToDTOList(books);
        return ResponseEntity.ok(bookDTOs);
    }

    /**
     * Filters books by category (public endpoint).
     * 
     * @param category the book category
     * @return list of books in the specified category
     */
    @GetMapping("/public/category/{category}")
    public ResponseEntity<List<BookDTO>> getBooksByCategory(@PathVariable Book.BookCategory category) {
        List<Book> books = bookService.getBooksByCategory(category);
        List<BookDTO> bookDTOs = bookService.convertToDTOList(books);
        return ResponseEntity.ok(bookDTOs);
    }

    /**
     * Filters books by price range (public endpoint).
     * 
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of books within the price range
     */
    @GetMapping("/public/price-range")
    public ResponseEntity<List<BookDTO>> getBooksByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Book> books = bookService.getBooksByPriceRange(minPrice, maxPrice);
        List<BookDTO> bookDTOs = bookService.convertToDTOList(books);
        return ResponseEntity.ok(bookDTOs);
    }

    /**
     * Retrieves a book by ID (public endpoint).
     * 
     * @param id the book ID
     * @return the book if found
     */
    @GetMapping("/public/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        try {
            Book book = bookService.findById(id);
            BookDTO bookDTO = bookService.convertToDTO(book);
            return ResponseEntity.ok(bookDTO);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Creates a new book (admin only).
     * 
     * @param bookDTO the book data
     * @return the created book
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookDTO bookDTO) {
        try {
            Book book = bookService.createBook(bookDTO);
            BookDTO createdBookDTO = bookService.convertToDTO(book);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book created successfully");
            response.put("book", createdBookDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Updates an existing book (admin only).
     * 
     * @param id the book ID
     * @param bookDTO the updated book data
     * @return the updated book
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        try {
            Book book = bookService.updateBook(id, bookDTO);
            BookDTO updatedBookDTO = bookService.convertToDTO(book);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Book updated successfully");
            response.put("book", updatedBookDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Soft deletes a book (admin only).
     * 
     * @param id the book ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Updates book stock (admin only).
     * 
     * @param id the book ID
     * @param quantity the quantity to add/subtract
     * @return the updated book
     */
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            Book book = bookService.updateStock(id, quantity);
            BookDTO bookDTO = bookService.convertToDTO(book);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stock updated successfully");
            response.put("book", bookDTO);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Gets books with low stock (admin only).
     * 
     * @param threshold the stock threshold
     * @return list of books with low stock
     */
    @GetMapping("/admin/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookDTO>> getBooksWithLowStock(@RequestParam(defaultValue = "10") Integer threshold) {
        List<Book> books = bookService.getBooksWithLowStock(threshold);
        List<BookDTO> bookDTOs = bookService.convertToDTOList(books);
        return ResponseEntity.ok(bookDTOs);
    }

    /**
     * Imports books from CSV file (admin only).
     * 
     * @param file the CSV file to import
     * @return import result
     */
    @PostMapping("/admin/import-csv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importBooksFromCSV(@RequestParam("file") MultipartFile file) {
        try {
            int importedCount = bookService.importBooksFromCSV(file);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Books imported successfully");
            response.put("importedCount", importedCount);
            
            return ResponseEntity.ok(response);
        } catch (IOException | CsvValidationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error importing CSV file: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
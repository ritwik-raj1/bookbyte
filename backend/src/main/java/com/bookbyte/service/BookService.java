package com.bookbyte.service;

import com.bookbyte.dto.BookDTO;
import com.bookbyte.entity.Book;
import com.bookbyte.repository.BookRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing book operations.
 * 
 * <p>This service handles book CRUD operations, search, filtering,
 * and bulk import functionality.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Book creation and management</li>
 *   <li>Search and filtering</li>
 *   <li>Stock management</li>
 *   <li>CSV bulk import</li>
 *   <li>Soft delete functionality</li>
 * </ul></p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Creates a new book in the catalog.
     * 
     * @param bookDTO the book data transfer object
     * @return the created book
     */
    public Book createBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setDescription(bookDTO.getDescription());
        book.setPrice(bookDTO.getPrice());
        book.setCategory(bookDTO.getCategory());
        book.setStock(bookDTO.getStock());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setPublisher(bookDTO.getPublisher());
        book.setPages(bookDTO.getPages());
        book.setLanguage(bookDTO.getLanguage());
        book.setIsAvailable(bookDTO.getIsAvailable());

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully: {}", savedBook.getTitle());
        return savedBook;
    }

    /**
     * Retrieves all available books.
     * 
     * @return list of all available books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findByIsDeletedFalseAndIsAvailableTrue();
    }

    /**
     * Finds a book by its ID.
     * 
     * @param id the book ID
     * @return the book if found
     * @throws RuntimeException if book not found
     */
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .filter(book -> !book.getIsDeleted())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    /**
     * Updates an existing book.
     * 
     * @param id the book ID
     * @param bookDTO the updated book data
     * @return the updated book
     */
    public Book updateBook(Long id, BookDTO bookDTO) {
        Book book = findById(id);
        
        if (bookDTO.getTitle() != null) {
            book.setTitle(bookDTO.getTitle());
        }
        if (bookDTO.getAuthor() != null) {
            book.setAuthor(bookDTO.getAuthor());
        }
        if (bookDTO.getIsbn() != null) {
            book.setIsbn(bookDTO.getIsbn());
        }
        if (bookDTO.getDescription() != null) {
            book.setDescription(bookDTO.getDescription());
        }
        if (bookDTO.getPrice() != null) {
            book.setPrice(bookDTO.getPrice());
        }
        if (bookDTO.getCategory() != null) {
            book.setCategory(bookDTO.getCategory());
        }
        if (bookDTO.getStock() != null) {
            book.setStock(bookDTO.getStock());
        }
        if (bookDTO.getPublicationYear() != null) {
            book.setPublicationYear(bookDTO.getPublicationYear());
        }
        if (bookDTO.getPublisher() != null) {
            book.setPublisher(bookDTO.getPublisher());
        }
        if (bookDTO.getPages() != null) {
            book.setPages(bookDTO.getPages());
        }
        if (bookDTO.getLanguage() != null) {
            book.setLanguage(bookDTO.getLanguage());
        }
        if (bookDTO.getIsAvailable() != null) {
            book.setIsAvailable(bookDTO.getIsAvailable());
        }

        Book updatedBook = bookRepository.save(book);
        log.info("Book updated successfully: {}", updatedBook.getTitle());
        return updatedBook;
    }

    /**
     * Soft deletes a book (marks as deleted but keeps in database).
     * 
     * @param id the book ID
     * @return the deleted book
     */
    public Book deleteBook(Long id) {
        Book book = findById(id);
        book.setIsDeleted(true);
        book.setIsAvailable(false);
        
        Book deletedBook = bookRepository.save(book);
        log.info("Book soft deleted: {}", deletedBook.getTitle());
        return deletedBook;
    }

    /**
     * Searches for books by title or author.
     * 
     * @param searchTerm the search term
     * @return list of matching books
     */
    public List<Book> searchBooks(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByTitleOrAuthorContainingIgnoreCase(searchTerm.trim());
    }

    /**
     * Filters books by category.
     * 
     * @param category the book category
     * @return list of books in the specified category
     */
    public List<Book> getBooksByCategory(Book.BookCategory category) {
        return bookRepository.findByCategoryAndIsDeletedFalseAndIsAvailableTrue(category);
    }

    /**
     * Filters books by price range.
     * 
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return list of books within the price range
     */
    public List<Book> getBooksByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Gets books with low stock.
     * 
     * @param threshold the stock threshold
     * @return list of books with low stock
     */
    public List<Book> getBooksWithLowStock(Integer threshold) {
        return bookRepository.findBooksWithLowStock(threshold);
    }

    /**
     * Updates book stock.
     * 
     * @param id the book ID
     * @param quantity the quantity to add/subtract
     * @return the updated book
     */
    public Book updateStock(Long id, Integer quantity) {
        Book book = findById(id);
        int newStock = book.getStock() + quantity;
        if (newStock < 0) {
            throw new RuntimeException("Stock cannot be negative");
        }
        book.setStock(newStock);
        
        Book updatedBook = bookRepository.save(book);
        log.info("Book stock updated: {} - New stock: {}", updatedBook.getTitle(), newStock);
        return updatedBook;
    }

    /**
     * Imports books from a CSV file.
     * 
     * @param file the CSV file to import
     * @return number of books imported
     * @throws IOException if file reading fails
     * @throws CsvValidationException if CSV validation fails
     */
    public int importBooksFromCSV(MultipartFile file) throws IOException, CsvValidationException {
        int importedCount = 0;
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            // Skip header row
            reader.readNext();
            
            while ((line = reader.readNext()) != null) {
                if (line.length >= 6) {
                    try {
                        Book book = new Book();
                        book.setTitle(line[0].trim());
                        book.setAuthor(line[1].trim());
                        book.setIsbn(line[2].trim());
                        book.setDescription(line[3].trim());
                        book.setPrice(new BigDecimal(line[4].trim()));
                        book.setCategory(Book.BookCategory.valueOf(line[5].trim().toUpperCase()));
                        book.setStock(Integer.parseInt(line[6].trim()));
                        book.setPublicationYear(Integer.parseInt(line[7].trim()));
                        book.setPublisher(line[8].trim());
                        book.setPages(Integer.parseInt(line[9].trim()));
                        book.setLanguage(line[10].trim());
                        book.setIsAvailable(true);
                        book.setIsDeleted(false);
                        
                        bookRepository.save(book);
                        importedCount++;
                    } catch (Exception e) {
                        log.error("Error importing book from CSV line: {}", String.join(",", line), e);
                    }
                }
            }
        }
        
        log.info("Imported {} books from CSV file", importedCount);
        return importedCount;
    }

    /**
     * Converts Book entity to BookDTO.
     * 
     * @param book the book entity
     * @return the book DTO
     */
    public BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        dto.setCategory(book.getCategory());
        dto.setStock(book.getStock());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setPublisher(book.getPublisher());
        dto.setPages(book.getPages());
        dto.setLanguage(book.getLanguage());
        dto.setIsAvailable(book.getIsAvailable());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        return dto;
    }

    /**
     * Converts list of Book entities to BookDTOs.
     * 
     * @param books the list of book entities
     * @return the list of book DTOs
     */
    public List<BookDTO> convertToDTOList(List<Book> books) {
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
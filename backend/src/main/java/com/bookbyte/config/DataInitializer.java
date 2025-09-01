package com.bookbyte.config;

import com.bookbyte.entity.Book;
import com.bookbyte.entity.User;
import com.bookbyte.repository.BookRepository;
import com.bookbyte.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Data initializer for populating the database with sample data.
 * 
 * <p>This component runs on application startup to create initial
 * data including admin user and sample books.</p>
 * 
 * @author BookByte Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
        initializeSampleBooks();
        log.info("Data initialization completed successfully");
    }

    /**
     * Creates the default admin user if it doesn't exist.
     */
    private void initializeAdminUser() {
        if (!userRepository.existsByEmail("admin@bookbyte.com")) {
            User adminUser = new User();
            adminUser.setName("Admin User");
            adminUser.setEmail("admin@bookbyte.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setAge(25);
            adminUser.setAddress("123 Admin Street, Admin City, AC 12345");
            adminUser.setRole(User.UserRole.ADMIN);
            adminUser.setCustomerId("ADMIN001");
            adminUser.setIsActive(true);

            userRepository.save(adminUser);
            log.info("Admin user created successfully");
        }
    }

    /**
     * Creates sample books if none exist.
     */
    private void initializeSampleBooks() {
        if (bookRepository.count() == 0) {
            Book book1 = createSampleBook(
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "978-0743273565",
                "A story of the fabulously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.",
                new BigDecimal("12.99"),
                Book.BookCategory.FICTION,
                50,
                1925,
                "Scribner",
                180,
                "English"
            );

            Book book2 = createSampleBook(
                "To Kill a Mockingbird",
                "Harper Lee",
                "978-0446310789",
                "The story of young Scout Finch and her father Atticus in a racially divided Alabama town.",
                new BigDecimal("14.99"),
                Book.BookCategory.FICTION,
                45,
                1960,
                "Grand Central Publishing",
                281,
                "English"
            );

            Book book3 = createSampleBook(
                "1984",
                "George Orwell",
                "978-0451524935",
                "A dystopian novel about totalitarianism and surveillance society.",
                new BigDecimal("11.99"),
                Book.BookCategory.SCIENCE_FICTION,
                40,
                1949,
                "Signet",
                328,
                "English"
            );

            Book book4 = createSampleBook(
                "The Art of Computer Programming",
                "Donald Knuth",
                "978-0201896831",
                "A comprehensive monograph written by Donald Knuth about computer programming.",
                new BigDecimal("89.99"),
                Book.BookCategory.TECHNOLOGY,
                25,
                1997,
                "Addison-Wesley",
                672,
                "English"
            );

            Book book5 = createSampleBook(
                "Clean Code",
                "Robert C. Martin",
                "978-0132350884",
                "A handbook of agile software craftsmanship.",
                new BigDecimal("49.99"),
                Book.BookCategory.TECHNOLOGY,
                35,
                2008,
                "Prentice Hall",
                464,
                "English"
            );

            bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4, book5));
            log.info("Sample books created successfully");
        }
    }

    /**
     * Creates a sample book with the given parameters.
     * 
     * @param title the book title
     * @param author the book author
     * @param isbn the ISBN number
     * @param description the book description
     * @param price the book price
     * @param category the book category
     * @param stock the available stock
     * @param publicationYear the publication year
     * @param publisher the publisher
     * @param pages the number of pages
     * @param language the book language
     * @return the created book
     */
    private Book createSampleBook(String title, String author, String isbn, String description,
                                 BigDecimal price, Book.BookCategory category, Integer stock,
                                 Integer publicationYear, String publisher, Integer pages, String language) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setDescription(description);
        book.setPrice(price);
        book.setCategory(category);
        book.setStock(stock);
        book.setPublicationYear(publicationYear);
        book.setPublisher(publisher);
        book.setPages(pages);
        book.setLanguage(language);
        book.setIsAvailable(true);
        book.setIsDeleted(false);
        return book;
    }
}
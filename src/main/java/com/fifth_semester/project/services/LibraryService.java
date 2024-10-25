package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowingRecordRepository borrowingRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FeeRepository feeRepository;

    private static final double DAILY_PENALTY = 200.0; // Penalty per day

    // Search for books by title, author, or ISBN
    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingOrAuthorContaining(query, query);
    }

    // Check availability of a book
    public boolean isBookAvailable(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return book.getAvailabilityStatus() == BookStatus.AVAILABLE;
    }

    // Reserve a book
    @Transactional
    public String reserveBook(Long bookId, Long studentId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailabilityStatus() != BookStatus.AVAILABLE) {
            return "Book is not available for reservation.";
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Mark book as reserved
        book.setAvailabilityStatus(BookStatus.RESERVED);
        bookRepository.save(book);
        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setStudent(student);
        borrowingRecord.setBorrowDate(LocalDate.now());
        borrowingRecordRepository.save(borrowingRecord);

        return "Book reserved successfully.";
    }

    // Borrow a book
    @Transactional
    public String borrowBook(Long bookId, Long studentId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getAvailabilityStatus() != BookStatus.AVAILABLE) {
            return "Book is not available for borrowing.";
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Create a new borrowing record
        BorrowingRecord borrowingRecord = new BorrowingRecord(student, book, LocalDate.now());
        borrowingRecordRepository.save(borrowingRecord);

        // Mark book as checked out
        book.setAvailabilityStatus(BookStatus.CHECKED_OUT);
        bookRepository.save(book);

        return "Book borrowed successfully.";
    }

    // Return a book
    @Transactional
    public String returnBook(Long bookId, Long studentId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        BorrowingRecord borrowingRecord = borrowingRecordRepository.findByStudentAndBookAndReturnDateIsNull(student, book)
                .orElseThrow(() -> new RuntimeException("Borrowing record not found"));

        // Update return date
        borrowingRecord.setReturnDate(LocalDate.now());
        borrowingRecordRepository.save(borrowingRecord);

        // Mark book as available
        book.setAvailabilityStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);

        return "Book returned successfully.";
    }

    @Transactional
    public String applyOverduePenalties(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);

        // Fetch borrowing records where books have not been returned (returnDate is null) and are overdue
        List<BorrowingRecord> overdueRecords = borrowingRecordRepository
                .findByStudentAndBorrowDateBeforeAndReturnDateIsNull(student, oneWeekAgo);

        if (overdueRecords.isEmpty()) {
            return "No overdue books found for the student.";
        }

        // Loop through all overdue records and apply penalties
        for (BorrowingRecord record : overdueRecords) {
            LocalDate borrowedDate = record.
                    getBorrowDate();
            LocalDate today = LocalDate.now();

            // Calculate the number of overdue days (from 7th day onwards)
            long overdueDays = ChronoUnit.DAYS.between(borrowedDate.plusWeeks(1), today);

            if (overdueDays > 0) {
                double penalty = overdueDays * DAILY_PENALTY; // Calculate penalty

                // Update student's fee structure or add a new penalty record
                Fee fee = createOrUpdatePenaltyFee(student, penalty);
                feeRepository.save(fee);
            }
        }



        return "Overdue penalties applied.";
    }

    // Create or update penalty in student's fee structure
    @Transactional
    public Fee createOrUpdatePenaltyFee(Student student, double penaltyAmount) {
        // Look for an existing fee or create a new one
        Fee fee = feeRepository.findLatestUnpaidFeeForStudent(student.getId())
                .orElseGet(() -> new Fee(student, penaltyAmount, 0.0, penaltyAmount, "Unpaid", "Library_Penalty"));

        // Update the fee with the new penalty
        double newDueAmount = fee.getDueAmount() + penaltyAmount;
        fee.setDueAmount(newDueAmount);
        fee.setTotalAmount(fee.getTotalAmount() + penaltyAmount);

        return fee;
    }
    // Notify and create notifications for students with books due for return
    public void notifyStudentsForDueBookReturns() {
        LocalDate currentDate = LocalDate.now();
        LocalDate dayBeforeCurrentDate = currentDate.minusDays(1);

        // Get all borrowing records where the return date is today or tomorrow (or overdue)
        List<BorrowingRecord> dueRecords = borrowingRecordRepository.findByReturnDateBetween(dayBeforeCurrentDate, currentDate);

        for (BorrowingRecord record : dueRecords) {
            Student student = record.getStudent();
            String email = student.getEmail();
            String message = "Reminder: The book titled '" + record.getBook().getTitle() +
                    "' is due for return on " + record.getReturnDate() + ". Please return it to avoid penalties.";

            // Send email notification
            emailService.sendBookReturnReminder(email, student.getUsername(), record.getBook().getTitle(), record.getReturnDate());

            // Create and save notification in the database
            Notification notification = new Notification(
                    message,
                    NotificationType.BOOK_RETURN,
                    NotificationStatus.UNREAD,
                    student,
                    LocalDateTime.now()
            );
            notificationRepository.save(notification);
        }
    }

    // Schedule the task to run daily at 00:01
    @Scheduled(cron = "0 0 0 * * *")
    public void sendDailyBookReturnReminders() {
        notifyStudentsForDueBookReturns();
        List<Student> allStudents = studentRepository.findAll();

        for (Student student : allStudents) {
            applyOverduePenalties(student.getId());
        }
    }
}

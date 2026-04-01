package com.deepika.postpartum.expensetracker.exceptions;

/**
 * Base Custom Exception for the Expense Tracker application.
 *
 * This exception acts as the parent class
 * for all expense-related exceptions.
 *
 * Example child exceptions:
 * - InvalidExpenseException
 * - ExpenseNotFoundException
 *
 * Why create a custom exception?
 * - To represent application-specific errors
 * - To improve error handling clarity
 * - To separate business errors from system errors
 *
 * Design:
 * This class extends RuntimeException,
 * so it is an unchecked exception.
 *
 * Benefits of RuntimeException:
 * - No mandatory try-catch
 * - Cleaner business logic
 * - Used for validation and business rule failures
 */
public class ExpenseException extends RuntimeException {

    /**
     * Constructor with error message.
     * Used when:
     * Only message needs to be passed.

     * Example:
     * throw new ExpenseException("Invalid expense");
     */
    public ExpenseException(String message) {

        super(message);

        // Calls RuntimeException constructor
        // and passes the message to it.
    }

    /**
     * Constructor with message and cause.

     * Used when:
     * Wrapping another exception.
     * Example:
     * throw new ExpenseException("Database error",e);
     */
    public ExpenseException(String message, Throwable cause) {
        super(message, cause);
    }

}
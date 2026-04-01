package com.deepika.postpartum.expensetracker.exceptions;

/**
 * Exception thrown when invalid expense data
 * is provided to the system.

 * This exception is typically used in the
 * Service Layer for validation failures.
 * Common Scenarios:
 * - Amount is less than or equal to zero
 * - Required fields are null
 * - Invalid ID value
 * - Invalid date
 * - Invalid month or year

 * Example:

 * throw new InvalidExpenseException("Amount must be greater than zero");

 * This class extends ExpenseException,
 * which is the base custom exception
 * for the application.
 */
public class InvalidExpenseException extends ExpenseException {

    /**
     * Constructor with error message.
     */
    public InvalidExpenseException(String message) {
        super(message);
    }

}
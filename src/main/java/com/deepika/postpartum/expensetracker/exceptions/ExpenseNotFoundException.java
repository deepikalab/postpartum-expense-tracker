package com.deepika.postpartum.expensetracker.exceptions;

/**
 * Exception thrown when an expense record
 * cannot be found in the system.

 * Typical Scenarios:
 * - Expense ID does not exist
 * - Record already deleted
 * - No data found for search criteria

 * Example:

 * throw new ExpenseNotFoundException(
 *     "Expense not found with ID: " + id
 * );

 * This class extends ExpenseException,
 * which is the base custom exception
 * for the application.
 */
public class ExpenseNotFoundException extends ExpenseException {

    /**
     * Constructor with error message.
     */
    public ExpenseNotFoundException(String message) {
        super(message);
    }

}
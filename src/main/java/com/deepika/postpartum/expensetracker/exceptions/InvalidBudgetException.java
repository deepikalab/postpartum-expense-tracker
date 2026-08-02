package com.deepika.postpartum.expensetracker.exceptions;

/**
 * Exception thrown when invalid budget data
 * is provided to the system.
 *
 * This exception is typically used in the
 * Service Layer for budget validation failures.
 *
 * Common Scenarios:
 * - Budget amount is null
 * - Budget amount is less than or equal to zero
 * - Invalid month value
 * - Invalid year value
 * - Budget does not exist for the given month and year
 *
 * Example:
 *
 * throw new InvalidBudgetException(
 *        "Budget amount must be greater than zero");
 *
 * This class extends ExpenseException,
 * which is the base custom exception
 * for the application.
 */
public class InvalidBudgetException extends ExpenseException {

    /**
     * Creates an InvalidBudgetException
     * with the specified error message.
     *
     * @param message error description
     */
    public InvalidBudgetException(String message) {
        super(message);
    }
}
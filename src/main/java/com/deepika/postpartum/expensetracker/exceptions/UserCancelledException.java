package com.deepika.postpartum.expensetracker.exceptions;
/**
 * Exception thrown when the user cancels the current operation.
 *
 * This exception is used to handle user navigation actions
 * from the console application.
 *
 * Common Scenarios:
 * - User enters "B" to go back to the previous menu
 * - User cancels an ongoing operation
 *
 * Example:
 *
 * throw new UserCancelledException(
 *        "Operation cancelled. Returning to menu...");
 *
 * This class extends RuntimeException because cancellation
 * is a control flow action and does not require handling
 * as a checked exception.
 */
public class UserCancelledException extends RuntimeException {

    /**
     * Creates a UserCancelledException with the specified error message.
     *
     * @param message message describing the cancellation reason
     */
    public UserCancelledException(String message) {
        super(message);
    }
}
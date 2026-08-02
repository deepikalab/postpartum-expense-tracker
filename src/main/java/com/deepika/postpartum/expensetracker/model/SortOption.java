package com.deepika.postpartum.expensetracker.model;

/**
 * Defines the available sorting options for expenses.
 *
 * Sorting can be performed based on:
 * - Expense date
 * - Expense amount
 *
 * Used by the service layer to apply sorting logic.
 */
public enum SortOption {

    /**
     * Sort expenses by date in ascending order (oldest first).
     */
    DATE_ASC,

    /**
     * Sort expenses by date in descending order (newest first).
     */
    DATE_DESC,

    /**
     * Sort expenses by amount in ascending order (lowest first).
     */
    AMOUNT_ASC,

    /**
     * Sort expenses by amount in descending order (highest first).
     */
    AMOUNT_DESC

}
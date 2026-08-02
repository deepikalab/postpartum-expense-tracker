package com.deepika.postpartum.expensetracker.model;

import java.math.BigDecimal;

/**
 * Represents budget usage details for a specific month.
 *
 * Responsibilities:
 * - Stores configured monthly budget amount
 * - Stores total expense amount spent during the month
 * - Stores remaining or exceeded budget amount
 * - Stores percentage of budget utilized
 *
 * This class acts as a data transfer object (DTO) used to transfer
 * budget status information from the Service Layer to the UI Layer.
 */
public class BudgetStatus {


    /**
     * Total budget amount configured for the month.
     */
    private final BigDecimal budgetAmount;


    /**
     * Total expense amount spent during the month.
     */
    private final BigDecimal spent;


    /**
     * Remaining budget amount after deducting expenses.
     *
     * <p>
     * A positive value indicates available budget.
     * A negative value indicates the budget has been exceeded.
     * </p>
     */
    private final BigDecimal remaining;


    /**
     * Percentage of the budget amount already utilized.
     *
     * <p>
     * Example:
     * Budget = 1000
     * Spent = 400
     * Percentage Used = 40%
     * </p>
     */
    private final BigDecimal percentageUsed;


    /**
     * Creates a BudgetStatus object containing budget usage details.
     *
     * @param budgetAmount configured monthly budget amount
     * @param spent total expense amount spent during the month
     * @param remaining remaining budget amount or exceeded amount
     * @param percentageUsed percentage of budget utilized
     */
    public BudgetStatus(
            BigDecimal budgetAmount,
            BigDecimal spent,
            BigDecimal remaining,
            BigDecimal percentageUsed) {

        this.budgetAmount = budgetAmount;
        this.spent = spent;
        this.remaining = remaining;
        this.percentageUsed = percentageUsed;
    }


    /**
     * Returns the configured monthly budget amount.
     *
     * @return budget amount
     */
    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }


    /**
     * Returns the total amount spent during the month.
     *
     * @return spent amount
     */
    public BigDecimal getSpent() {
        return spent;
    }


    /**
     * Returns the remaining budget amount.
     *
     * @return remaining amount or exceeded amount when negative
     */
    public BigDecimal getRemaining() {
        return remaining;
    }


    /**
     * Returns the percentage of budget utilized.
     *
     * @return percentage used
     */
    public BigDecimal getPercentageUsed() {
        return percentageUsed;
    }

}
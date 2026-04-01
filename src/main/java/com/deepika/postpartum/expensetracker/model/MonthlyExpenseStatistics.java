package com.deepika.postpartum.expensetracker.model;

import java.math.BigDecimal;

/**
 * MonthlyExpenseStatistics Model Class.

 * Represents calculated summary statistics
 * for expenses within a specific month.

 * This is a read-only object used for reporting.

 * Example statistics:
 * - Total Expense
 * - Average Expense
 * - Maximum Expense
 * - Minimum Expense
 * - Number of Transactions

 * Design Decision:
 * This class is immutable:
 * - No setters
 * - Fields are final
 * - Values are set only through constructor

 */
public class MonthlyExpenseStatistics {

    /**
     * Total expense amount for the month.
     */
    private final BigDecimal total;

    /**
     * Average expense amount.
     */
    private final BigDecimal average;

    /**
     * Highest expense recorded.
     */
    private final BigDecimal maximum;

    /**
     * Lowest expense recorded.
     */
    private final BigDecimal minimum;

    /**
     * Number of expense records.
     */
    private final int count;

    /**
     * Parameterized Constructor.

     * Used when:
     * DAO calculates statistics
     * and returns this object.
     */
    public MonthlyExpenseStatistics(
            BigDecimal total,
            BigDecimal average,
            BigDecimal maximum,
            BigDecimal minimum,
            int count) {

        this.total = total;
        this.average = average;
        this.maximum = maximum;
        this.minimum = minimum;
        this.count = count;
    }

    // ================= GETTERS =================

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public int getCount() {
        return count;
    }

    // ================= toString =================

    /**
     * Provides formatted statistics output.
     */
    @Override
    public String toString() {

        return "Monthly Expense Statistics:\n" +
                "Total   : " + total + "\n" +
                "Average : " + average + "\n" +
                "Maximum : " + maximum + "\n" +
                "Minimum : " + minimum + "\n" +
                "Count   : " + count;
    }

}
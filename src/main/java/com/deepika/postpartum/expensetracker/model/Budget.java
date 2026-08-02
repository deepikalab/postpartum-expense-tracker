package com.deepika.postpartum.expensetracker.model;

import java.math.BigDecimal;


/**
 * Represents a monthly budget configuration.
 *
 * Stores:
 * - Month for which the budget is created
 * - Year for which the budget is created
 * - Allocated budget amount
 *
 * This class acts as a domain model and does not contain
 * business validation logic.
 */
public class Budget {

    private Long id;

    private int month;

    private int year;

    private BigDecimal budgetAmount;


    /**
     * Default constructor.
     */
    public Budget() {
    }


    /**
     * Creates a monthly budget object.
     *
     * @param month        month for the budget
     * @param year         year for the budget
     * @param budgetAmount allocated budget amount
     */
    public Budget(
            int month,
            int year,
            BigDecimal budgetAmount) {

        this.month = month;
        this.year = year;
        this.budgetAmount = budgetAmount;
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public int getMonth() {
        return month;
    }


    public void setMonth(int month) {
        this.month = month;
    }


    public int getYear() {
        return year;
    }


    public void setYear(int year) {
        this.year = year;
    }


    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }


    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

}
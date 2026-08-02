package com.deepika.postpartum.expensetracker.service;

import com.deepika.postpartum.expensetracker.model.Budget;
import com.deepika.postpartum.expensetracker.model.BudgetStatus;

import java.math.BigDecimal;

/**
 * Service Layer Interface for Budget Management.
 *
 * Responsibilities:
 * - Manages monthly budget operations
 * - Handles budget alerts based on expenses
 * - Provides budget status information
 */
public interface BudgetService {


    /**
     * Creates or updates a monthly budget
     * for a specific month and year.
     *
     * @param month Month number (1-12)
     * @param year Year
     * @param amount Budget amount
     * @return saved budget details
     */
    Budget setMonthlyBudget(
            int month,
            int year,
            BigDecimal amount);


    /**
     * Retrieves budget details including spent amount
     * and remaining balance.
     *
     * @param month Month number (1-12)
     * @param year Year
     * @return budget status details
     */
    BudgetStatus viewBudgetStatus(
            int month,
            int year);


    /**
     * Checks spending against the configured budget
     * and returns an alert message when limits are reached.
     *
     * @param month Month number (1-12)
     * @param year Year
     * @return budget alert message
     */
    String showBudgetAlert(
            int month,
            int year);


    /**
     * Deletes the budget configured for
     * a specific month and year.
     *
     * @param month Month number (1-12)
     * @param year Year
     */
    void deleteBudget(
            int month,
            int year);

}
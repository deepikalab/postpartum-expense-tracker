package com.deepika.postpartum.expensetracker.service;

import com.deepika.postpartum.expensetracker.model.Expense;
import com.deepika.postpartum.expensetracker.model.ExpenseType;
import com.deepika.postpartum.expensetracker.model.MonthlyExpenseStatistics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service Layer Interface for Expense Management.

 * Responsibilities:
 * - Contains business logic
 * - Validates data before sending to DAO
 * - Coordinates between UI Layer and DAO Layer
 * - Handles transactions (if required)

 * Design Principle:
 * Programming to an interface allows loose coupling
 * and supports multiple implementations.

 * Example:
 * - ExpenseServiceImpl (Database)
 * - FileExpenseServiceImpl (File system)
 */
public interface ExpenseService {

    // ================= CRUD OPERATIONS =================

    /**
     * Adds a new expense record.
     *
     * @param expense Expense object to be saved
     * @return Saved Expense with generated ID
     */
    Expense addExpense(Expense expense);

    /**
     * Retrieves all expense records.
     *
     * @return List of all expenses
     */
    List<Expense> getAllExpenses();

    /**
     * Retrieves a single expense by ID.
     *
     * @param id Expense ID
     * @return Matching Expense
     */
    Expense getExpenseById(Long id);

    /**
     * Updates an existing expense record.
     *
     * @param expense Updated expense data
     * @return Updated Expense
     */
    Expense updateExpense(Expense expense);

    /**
     * Deletes an expense by ID.
     *
     * @param id Expense ID
     */
    void deleteExpense(Long id);

    // ================= SEARCH OPERATIONS =================

    /**
     * Retrieves expenses filtered by category.
     *
     * @param type Expense category
     * @return List of matching expenses
     */
    List<Expense> getExpenseByType(ExpenseType type);

    /**
     * Retrieves expenses recorded on a specific date.
     *
     * @param expenseDate Date to search
     * @return List of expenses
     */
    List<Expense> getExpenseByDate(LocalDate expenseDate);

    /**
     * Retrieves expenses between two dates.
     *
     * @param startDate Start date
     * @param endDate   End date
     * @return List of expenses within range
     */
    List<Expense> getExpenseByDateRange(LocalDate startDate, LocalDate endDate);

    // ================= ANALYTICS / REPORTING =================

    /**
     * Calculates total expense for a specific month.
     *
     * @param month Month number (1-12)
     * @param year  Year
     * @return Total expense amount
     */
    BigDecimal getTotalMonthlyExpense(int month, int year);

    /**
     * Calculates total expense between two dates.
     *
     * @param startDate Start date
     * @param endDate   End date
     * @return Total expense amount
     */
    BigDecimal getTotalExpenseBetweenDates(LocalDate startDate,LocalDate endDate);

    /**
     * Calculates monthly statistics including:
     * - Total
     * - Average
     * - Maximum
     * - Minimum
     * - Count
     *
     * @param month Month number (1-12)
     * @param year  Year
     * @return MonthlyExpenseStatistics object
     */
    MonthlyExpenseStatistics getMonthlyExpenseStatistics(int month,int year);

}
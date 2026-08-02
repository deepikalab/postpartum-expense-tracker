package com.deepika.postpartum.expensetracker.service;

import com.deepika.postpartum.expensetracker.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service Layer Interface for Expense Management.
 *
 * Responsibilities:
 * - Handles expense-related business operations
 * - Validates data before persistence
 * - Coordinates between UI layer and DAO layer
 * - Provides expense search, analytics, and reporting operations
 *
 * Design Principle:
 * Programming to an interface promotes loose coupling
 * and allows multiple implementations.
 */
public interface ExpenseService {


    // ================= CRUD OPERATIONS =================

    /**
     * Adds a new expense record.
     *
     * @param expense Expense object to be saved
     * @return Saved expense with generated ID
     */
    Expense addExpense(Expense expense);

    /**
     * Retrieves all expense records.
     *
     * @return List of all expenses
     */
    List<Expense> getAllExpenses();

    /**
     * Retrieves an expense using its identifier.
     *
     * @param id Expense ID
     * @return Matching expense
     */
    Expense getExpenseById(Long id);

    /**
     * Updates an existing expense record.
     *
     * @param expense Updated expense details
     * @return Updated expense
     */
    Expense updateExpense(Expense expense);

    /**
     * Updates a specific field of an expense.
     *
     * @param id Expense ID
     * @param fieldChoice Field selection to update
     * @param value New field value
     * @return Updated expense
     */
    Expense patchExpense(
            Long id,
            int fieldChoice,
            Object value);

    /**
     * Deletes an expense using its identifier.
     *
     * @param id Expense ID
     */
    void deleteExpense(Long id);


    // ================= SEARCH OPERATIONS =================

    /**
     * Retrieves expenses by expense category.
     *
     * @param type Expense category
     * @return List of matching expenses
     */
    List<Expense> getExpensesByType(ExpenseType type);

    /**
     * Retrieves expenses by person type.
     *
     * @param personType Person type (Mother/Baby)
     * @return List of matching expenses
     */
    List<Expense> getExpensesByPerson(PersonType personType);

    /**
     * Retrieves expenses recorded on a specific date.
     *
     * @param expenseDate Date to search
     * @return List of expenses
     */
    List<Expense> getExpensesByDate(LocalDate expenseDate);

    /**
     * Retrieves expenses within a given date range.
     *
     * @param startDate Starting date
     * @param endDate Ending date
     * @return List of expenses within the range
     */
    List<Expense> getExpensesByDateRange(
            LocalDate startDate,
            LocalDate endDate);


    /**
     * Retrieves expenses for a specific month and year.
     *
     * @param month Month number (1-12)
     * @param year Year
     * @return List of monthly expenses
     */
    List<Expense> getExpensesByMonth(
                                int month,
                                int year);

    // ================= ANALYTICS / REPORTING =================


    /**
     * Calculates total expense for a specific month.
     *
     * @param month Month number (1-12)
     * @param year Year
     * @return Total expense amount
     */
    BigDecimal getTotalMonthlyExpense(
                        int month,
                        int year);


    /**
     * Calculates total expense between two dates.
     *
     * @param startDate Starting date
     * @param endDate Ending date
     * @return Total expense amount
     */
    BigDecimal getTotalExpenseBetweenDates(
                        LocalDate startDate,
                        LocalDate endDate);


    /**
     * Generates monthly expense statistics.
     *
     * <p>
     * Includes total, average, maximum, minimum,
     * and expense count.
     * </p>
     *
     * @param month Month number (1-12)
     * @param year Year
     * @return Monthly expense statistics
     */
    MonthlyExpenseStatistics getMonthlyExpenseStatistics(
                                    int month,
                                    int year);

    /**
     * Generates a monthly expense report.
     *
     * @param month Month number (1-12)
     * @param year Year
     * @return Generated monthly report
     */
    String generateMonthlyReport(
                            int month,
                            int year);
    /**
     * Generates an expense report based on category.
     *
     * @param expenseType Expense category
     * @return Generated category report
     */
    String generateCategoryReport(
            ExpenseType expenseType);

    /**
     * Generates an expense report for a date range.
     *
     * @param startDate Starting date
     * @param endDate Ending date
     * @return Generated date range report
     */
    String generateDateRangeReport(
                    LocalDate startDate,
                    LocalDate endDate);


    /**
     * Saves a generated report to a file.
     *
     * @param fileName Name of the output file
     * @param report Report content
     */
    void saveReport(
                    String fileName,
                    String report);


    /**
     * Sorts expenses based on the selected option.
     *
     * @param sortOption Sorting criteria
     * @return Sorted list of expenses
     */
    List<Expense> sortExpenses(
              SortOption sortOption);

}
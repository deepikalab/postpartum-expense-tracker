package com.deepika.postpartum.expensetracker.service;

import com.deepika.postpartum.expensetracker.DAO.ExpenseDAO;
import com.deepika.postpartum.expensetracker.exceptions.InvalidExpenseException;
import com.deepika.postpartum.expensetracker.model.Expense;
import com.deepika.postpartum.expensetracker.model.ExpenseType;
import com.deepika.postpartum.expensetracker.model.MonthlyExpenseStatistics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service Layer Implementation.

 * Responsibilities:
 * - Implements business logic
 * - Validates inputs before calling DAO
 * - Ensures data integrity
 * - Handles business rules

 * Layer:
 * UI -> Service -> DAO -> Database

 * Important:
 * DAO should never be accessed directly from UI.
 */
public class ExpenseServiceImpl implements ExpenseService {

    // DAO dependency
    private final ExpenseDAO expenseDAO;

    /**
     * Constructor Injection.
     * Allows flexibility for testing and future dependency injection.
     */
    public ExpenseServiceImpl() {
        this.expenseDAO = new ExpenseDAO();
    }

    // ================= ADD EXPENSE =================

    @Override
    public Expense addExpense(Expense expense) {

        validateExpense(expense);

        return expenseDAO.addExpense(expense);
    }

    // ================= UPDATE EXPENSE =================

    @Override
    public Expense updateExpense(Expense expense) {

        validateExpense(expense);

        if (expense.getId() == null || expense.getId() <= 0) {
            throw new InvalidExpenseException("Invalid expense ID");
        }

        return expenseDAO.updateExpense(expense);
    }

    // ================= DELETE EXPENSE =================

    @Override
    public void deleteExpense(Long id) {

        if (id == null || id <= 0) {
            throw new InvalidExpenseException("Invalid expense ID");
        }

        expenseDAO.deleteExpense(id);
    }

    // ================= READ OPERATIONS =================

    @Override
    public List<Expense> getAllExpenses() {
        return expenseDAO.getAllExpenses();
    }

    @Override
    public Expense getExpenseById(Long id) {

        if (id == null || id <= 0) {
            throw new InvalidExpenseException("Invalid expense ID");
        }

        return expenseDAO.getExpenseById(id);
    }

    @Override
    public List<Expense> getExpenseByType(ExpenseType type) {

        if (type == null) {
            throw new InvalidExpenseException("Expense type cannot be null");
        }

        return expenseDAO.getExpenseByType(type);
    }

    @Override
    public List<Expense> getExpenseByDate(LocalDate expenseDate) {

        if (expenseDate == null || expenseDate.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException("Invalid expense date");
        }

        return expenseDAO.getExpenseByDate(expenseDate);
    }

    @Override
    public List<Expense> getExpenseByDateRange(LocalDate startDate, LocalDate endDate) {

        validateDate(startDate, endDate);

        return expenseDAO.getExpenseByDateRange(startDate, endDate
        );
    }

    // ================= REPORTING =================

    @Override
    public BigDecimal getTotalMonthlyExpense(int month, int year) {

        validateMonthAndYear(month, year);

        return expenseDAO.getTotalMonthlyExpense(month, year
        );
    }

    @Override
    public BigDecimal getTotalExpenseBetweenDates(LocalDate startDate, LocalDate endDate) {

        validateDate(startDate, endDate);

        return expenseDAO.getTotalExpenseBetweenDates(startDate, endDate);
    }

    @Override
    public MonthlyExpenseStatistics getMonthlyExpenseStatistics(int month, int year) {

        validateMonthAndYear(month, year);

        return expenseDAO.getMonthlyExpenseStatistics(month,year);
    }

    // ================= VALIDATION METHODS =================

    /**
     * Validates expense object before database operation.
     */
    private void validateExpense(Expense expense) {

        if (expense == null) {
            throw new InvalidExpenseException("Expense cannot be null");
        }

        if (expense.getExpenseType() == null || expense.getPersonType() == null) {

            throw new InvalidExpenseException("Person type or expense type is missing");
        }

        if (expense.getExpenseAmount() == null || expense.getExpenseAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidExpenseException("Amount must be greater than zero");
        }
    }

    /**
     * Validates date range.
     */
    private void validateDate(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {

            throw new IllegalArgumentException("Start date and end date must not be null");
        }

        if (startDate.isAfter(endDate) || startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException("Invalid date range");
        }
    }

    /**
     * Validates month and year.
     */
    private void validateMonthAndYear(int month, int year) {

        if (month < 1 || month > 12) {
            throw new InvalidExpenseException("Invalid month");
        }

        if (year < 2000 || year > LocalDate.now().getYear()) {
            throw new InvalidExpenseException("Invalid year");
        }
    }

}
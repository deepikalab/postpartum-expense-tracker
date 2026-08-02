package com.deepika.postpartum.expensetracker.service;


import com.deepika.postpartum.expensetracker.dao.BudgetDAO;
import com.deepika.postpartum.expensetracker.dao.ExpenseDAO;
import com.deepika.postpartum.expensetracker.exceptions.InvalidBudgetException;
import com.deepika.postpartum.expensetracker.exceptions.InvalidExpenseException;
import com.deepika.postpartum.expensetracker.model.Budget;
import com.deepika.postpartum.expensetracker.model.BudgetStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Service implementation responsible for budget business logic.
 *
 * Responsibilities:
 * - Creates and updates monthly budgets
 * - Calculates budget utilization
 * - Provides budget status information
 *
 * Does NOT contain:
 * - Database operations
 * - Console input/output handling
 */
public class BudgetServiceImpl implements BudgetService {

    /**
     * Data Access Object responsible for budget database operations.
     */
    private final BudgetDAO budgetDAO;


    /**
     * Data Access Object responsible for expense database operations.
     */
    private final ExpenseDAO expenseDAO;

    /**
     * Common validation constants.
     */
    private static final BigDecimal WARNING_PERCENTAGE =
            BigDecimal.valueOf(80);

    private static final BigDecimal FULL_USAGE_PERCENTAGE =
            BigDecimal.valueOf(100);

    private static final BigDecimal PERCENTAGE_MULTIPLIER =
            BigDecimal.valueOf(100);

    private static final int MIN_YEAR = 2000;


    /**
     * Creates BudgetServiceImpl with required DAO dependencies.
     *
     * @param expenseDAO DAO responsible for expense operations
     * @param budgetDAO DAO responsible for budget operations
     */
    public BudgetServiceImpl(
            ExpenseDAO expenseDAO,
            BudgetDAO budgetDAO) {

        this.expenseDAO = expenseDAO;
        this.budgetDAO = budgetDAO;
    }


    /**
     * Creates or updates a monthly spending budget
     * for a specific month and year.
     *
     * @param month month for which the budget is set
     * @param year year for which the budget is set
     * @param amount monthly budget amount
     * @return saved budget details
     * @throws InvalidExpenseException if month, year,
     *         or amount is invalid
     */
    @Override
    public Budget setMonthlyBudget(
            int month,
            int year,
            BigDecimal amount) {


        validateBudget(month, year, amount);

        Budget budget = new Budget(month, year, amount);

        // Saves new budget or updates existing budget
        budgetDAO.save(budget);

        return budget;
    }


    /**
     * Deletes the budget configured for a specific month and year.
     *
     * @param month month of the budget
     * @param year year of the budget
     * @throws InvalidExpenseException if no budget exists
     */
    @Override
    public void deleteBudget(
            int month,
            int year) {


        // Check whether budget exists before deleting
        getBudget(month, year);

        budgetDAO.deleteByMonthAndYear(month, year);
    }


    /**
     * Checks monthly spending against the configured budget
     * and returns an alert message when spending reaches limits.
     *
     * @param month month to check
     * @param year year to check
     * @return budget alert message
     * @throws InvalidExpenseException if no budget exists
     */
    @Override
    public String showBudgetAlert(
            int month,
            int year) {

        Budget budget = budgetDAO
                .findByMonthAndYear(month, year)
                .orElse(null);

        // No budget configured for this month
        if (budget == null) {
            return "";
        }

        BigDecimal spent = expenseDAO
                .getTotalMonthlyExpense(month, year);

        BigDecimal budgetAmount =
                budget.getBudgetAmount();

        BigDecimal remaining =
                budgetAmount.subtract(spent);

        BigDecimal percentage =
                spent.divide(budgetAmount, 2, RoundingMode.HALF_UP)
                        .multiply(PERCENTAGE_MULTIPLIER);

        if (percentage.compareTo(FULL_USAGE_PERCENTAGE) > 0) {

            return "🚨 Budget Exceeded!\n"
                    + "Budget     : ₹" + budgetAmount
                    + "\nSpent      : ₹" + spent
                    + "\nExceeded By: ₹" + remaining.abs()
                    + "\nUsed       : " + percentage + "%";

        } else if (percentage.compareTo(FULL_USAGE_PERCENTAGE) == 0) {

            return "⚠️ Budget Fully Utilized!\n"
                    + "Budget     : ₹" + budgetAmount
                    + "\nSpent      : ₹" + spent
                    + "\nRemaining  : ₹0.00"
                    + "\nUsed       : 100.00%";

        } else if (percentage.compareTo(WARNING_PERCENTAGE) >= 0) {

            return "⚠️ Budget Warning!\n"
                    + "Budget     : ₹" + budgetAmount
                    + "\nSpent      : ₹" + spent
                    + "\nRemaining  : ₹" + remaining
                    + "\nUsed       : " + percentage + "%";
        }

        return "";
    }


    /**
     * Displays budget details including total spending
     * and remaining balance for a month and year.
     *
     * @param month month for budget status
     * @param year year for budget status
     * @return budget status details
     * @throws InvalidExpenseException if no budget exists
     */
    @Override
    public BudgetStatus viewBudgetStatus(
            int month,
            int year) {


        Budget budget = getBudget(month, year);

        BigDecimal spent = expenseDAO
                .getTotalMonthlyExpense(month, year);

        BigDecimal budgetAmount =
                budget.getBudgetAmount();


        // Calculate remaining budget after expenses
        BigDecimal remaining =
                budgetAmount.subtract(spent);

        BigDecimal percentageUsed =
                spent.divide(budgetAmount, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

        return new BudgetStatus(
                budgetAmount,
                spent,
                remaining,
                percentageUsed
        );
    }


    /**
     * Retrieves budget details for a specific month and year.
     *
     * @param month month of budget
     * @param year year of budget
     * @return budget details
     * @throws InvalidBudgetException if budget is not found
     */
    private Budget getBudget(
            int month,
            int year) {


        return budgetDAO.findByMonthAndYear(month, year)
                .orElseThrow(() ->
                        new InvalidBudgetException(
                                "No budget set for "
                                        + month
                                        + "/"
                                        + year));
    }


    /**
     * Validates budget input details.
     *
     * @param month budget month
     * @param year budget year
     * @param amount budget amount
     */
    private void validateBudget(
            int month,
            int year,
            BigDecimal amount) {


        if (month < 1 || month > 12) {

            throw new InvalidBudgetException(
                    "Invalid month. Please enter a value between 1 and 12.");
        }


        if (year < MIN_YEAR ||
                year > LocalDate.now().getYear()) {

            throw new InvalidBudgetException(
                    "Invalid year.");
        }


        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidBudgetException(
                    "Budget amount must be greater than zero.");
        }
    }
}
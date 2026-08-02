package com.deepika.postpartum.expensetracker.service;

import com.deepika.postpartum.expensetracker.dao.ExpenseDAO;
import com.deepika.postpartum.expensetracker.exceptions.ExpenseNotFoundException;
import com.deepika.postpartum.expensetracker.exceptions.InvalidBudgetException;
import com.deepika.postpartum.expensetracker.exceptions.InvalidExpenseException;
import com.deepika.postpartum.expensetracker.model.*;
import com.deepika.postpartum.expensetracker.report.ExpenseReportGenerator;
import com.deepika.postpartum.expensetracker.report.ReportCsvWriter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Service layer implementation for the Expense Tracker application.

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

    /**
     * Data Access Object responsible for database operations.
     */
    private final ExpenseDAO expenseDAO;

    /**
     * Generates formatted expense reports.
     */
    private final ExpenseReportGenerator reportGenerator;

    /**
     * Writes generated reports into CSV files.
     */
    private final ReportCsvWriter csvWriter;

    private final BudgetService budgetService;

    /**
     * Common validation constants.
     */
    private static final BigDecimal ZERO =
            BigDecimal.ZERO;

    private static final int MIN_MONTH = 1;

    private static final int MAX_MONTH = 12;

    private static final int MIN_YEAR = 2000;

    /**
     * Creates a new ExpenseService implementation.
     *
     * @param expenseDAO DAO used for expense persistence
     * @param reportGenerator generates formatted reports
     * @param csvWriter exports reports to CSV
     */
    public ExpenseServiceImpl(
            ExpenseDAO expenseDAO,
            ExpenseReportGenerator reportGenerator,
            ReportCsvWriter csvWriter,
            BudgetService budgetService) {

        this.expenseDAO = expenseDAO;
        this.reportGenerator = reportGenerator;
        this.csvWriter = csvWriter;
        this.budgetService = budgetService;
    }

    // =====================================================
    //                     ADD EXPENSE
    // =====================================================

    /**
     * Adds a new expense after validating all business rules.
     *
     * @param expense expense to be saved
     * @return saved expense with generated ID
     * @throws InvalidExpenseException if the expense data is invalid
     */
    @Override
    public Expense addExpense(Expense expense) {

        // Validate expense before saving.
        validateExpense(expense);

        Expense savedExpense =
                expenseDAO.addExpense(expense);

        // Check budget after adding expense
        checkBudgetAfterExpenseChange(savedExpense);

        return savedExpense;
    }

    // =====================================================
    //                   UPDATE EXPENSE
    // =====================================================

    /**
     * Updates an existing expense.
     *
     * @param expense updated expense details
     * @return updated expense
     * @throws InvalidExpenseException if the expense or ID is invalid
     */
    @Override
    public Expense updateExpense(Expense expense) {

        // Validate expense details.
        validateExpense(expense);

        // Expense ID must exist for update.
        if (expense.getId() == null || expense.getId() <= 0) {
            throw new InvalidExpenseException("Invalid expense ID");
        }


        // Fetch existing expense from database
        Expense existingExpense = getExpenseById(expense.getId());

        // Check whether user actually changed any field
        if (isSameExpense(existingExpense, expense)) {

            throw new InvalidExpenseException(
                    "No changes detected. Expense was not updated."
            );
        }
        // Persist the updated expense only if changes exist
        Expense updatedExpense = expenseDAO.updateExpense(expense);

        // Check budget after update
        checkBudgetAfterExpenseChange(updatedExpense);

        return updatedExpense;
    }
    // =====================================================
    //                   PATCH EXPENSE
    // =====================================================
    /**
     * Updates a single field of an existing expense.
     *
     * <p>Field Mapping:</p>
     * <ul>
     *     <li>1 - Person Type</li>
     *     <li>2 - Expense Type</li>
     *     <li>3 - Expense Amount</li>
     *     <li>4 - Description</li>
     *     <li>5 - Expense Date</li>
     * </ul>
     *
     * @param id expense ID
     * @param fieldChoice field to update
     * @param value new value for the selected field
     * @return updated expense
     * @throws InvalidExpenseException if the ID or selected field is invalid
     */
    @Override
    public Expense patchExpense(
            Long id,
            int fieldChoice,
            Object value) {

        // Validate expense ID.
        if (id == null || id <= 0) {
            throw new InvalidExpenseException("Invalid expense ID");
        }

        // Retrieve existing expense.
        Expense expense = getExpenseById(id);

        // Update only the requested field while preserving
        // the remaining expense information.
        switch (fieldChoice) {

            case 1 -> {
                PersonType personType = (PersonType) value;

                if (expense.getPersonType() == personType) {
                    throw new InvalidExpenseException(
                            "Person type is already " + personType);
                }

                expense.setPersonType(personType);
            }

            case 2 -> {
                ExpenseType expenseType = (ExpenseType) value;

                if (expense.getExpenseType() == expenseType) {
                    throw new InvalidExpenseException(
                            "Expense category is already " + expenseType);
                }

                expense.setExpenseType(expenseType);
            }

            case 3 -> {
                BigDecimal amount = (BigDecimal) value;

                if (expense.getExpenseAmount().compareTo(amount) == 0) {
                    throw new InvalidExpenseException(
                            "Expense amount is already ₹" + amount);
                }

                expense.setExpenseAmount(amount);
            }

            case 4 -> {
                String description = (String) value;

                if (Objects.equals(
                        expense.getDescription(), description)) {

                    throw new InvalidExpenseException(
                            "Description is already the same");
                }

                expense.setDescription(description);
            }

            case 5 -> {
                LocalDate date = (LocalDate) value;

                if (expense.getExpenseDate().equals(date)) {
                    throw new InvalidExpenseException(
                            "Expense date is already " + date);
                }

                expense.setExpenseDate(date);
            }

            default ->
                    throw new InvalidExpenseException(
                            "Invalid patch option");
        }

        // Ensure the updated expense still satisfies all business rules.
        validateExpense(expense);

        Expense updatedExpense = expenseDAO.updateExpense(expense);

        // Check budget after patch update
        checkBudgetAfterExpenseChange(updatedExpense);

        return updatedExpense;
    }

    // =====================================================
    //                    DELETE EXPENSE
    // =====================================================

    /**
     * Deletes an expense using its ID and updates budget status.
     *
     * @param id expense ID
     * @throws InvalidExpenseException if ID is invalid
     * @throws ExpenseNotFoundException if expense does not exist
     */
    @Override
    public void deleteExpense(Long id) {

        // Ensure the supplied ID is valid.
        if (id == null || id <= 0) {
            throw new InvalidExpenseException("Invalid expense ID");
        }
        // Fetch expense before deletion for budget update.
        Expense expense = getExpenseById(id);

        // Delete expense from database.
        expenseDAO.deleteExpense(id);

        // Recalculates budget after deletion.
        checkBudgetAfterExpenseChange(expense);
    }

    // =====================================================
    //                  READ OPERATIONS
    // =====================================================

    /**
     * Retrieves all expenses.
     *
     * @return list of all expenses
     */
    @Override
    public List<Expense> getAllExpenses() {

        return expenseDAO.getAllExpenses();
    }


    /**
     * Retrieves an expense using its ID.
     *
     * @param id expense ID
     * @return matching expense
     * @throws InvalidExpenseException if the ID is invalid
     * @throws ExpenseNotFoundException if no expense exists for the given ID
     */
    @Override
    public Expense getExpenseById(Long id) {

        // Validate expense ID.
        if (id == null || id <= 0) {
            throw new InvalidExpenseException("Invalid expense ID");
        }

        return expenseDAO.getExpenseById(id);
    }

    /**
     * Retrieves all expenses for a specific expense category.
     *
     * @param type expense category
     * @return list of matching expenses
     * @throws InvalidExpenseException if the expense type is null
     */
    @Override
    public List<Expense> getExpensesByType(ExpenseType type) {

        // Expense category must be provided.
        if (type == null) {
            throw new InvalidExpenseException(
                    "Expense type cannot be null");
        }

        return expenseDAO.getExpensesByType(type);
    }

    /**
     * Retrieves all expenses for a specific person.
     *
     * @param personType person whose expenses are required
     * @return list of matching expenses
     * @throws InvalidExpenseException if the person type is null
     */
    @Override
    public List<Expense> getExpensesByPerson(
            PersonType personType) {

        // Person type must be provided.
        if (personType == null) {
            throw new InvalidExpenseException(
                    "Person type cannot be null");
        }

        return expenseDAO.getExpensesByPerson(personType);
    }

    /**
     * Retrieves all expenses recorded on a specific date.
     *
     * @param expenseDate expense date
     * @return list of expenses recorded on the given date
     * @throws InvalidExpenseException if the date is invalid
     */
    @Override
    public List<Expense> getExpensesByDate(LocalDate expenseDate) {

        // Expense date must not be null or in the future.
        if (expenseDate == null ||
                expenseDate.isAfter(LocalDate.now())) {

            throw new InvalidExpenseException(
                    "Invalid expense date");
        }

        return expenseDAO.getExpensesByDate(expenseDate);
    }

    /**
     * Retrieves all expenses between two dates.
     *
     * @param startDate start date
     * @param endDate end date
     * @return list of expenses within the given date range
     * @throws InvalidExpenseException if the date range is invalid
     */
    @Override
    public List<Expense> getExpensesByDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        // Validate the supplied date range.
        validateDate(startDate, endDate);

        return expenseDAO.getExpensesByDateRange(
                        startDate,
                        endDate);
    }

    /**
     * Retrieves all expenses for a specific month and year.
     *
     * @param month month (1-12)
     * @param year year
     * @return list of monthly expenses
     * @throws InvalidExpenseException if month or year is invalid
     */
    public List<Expense> getExpensesByMonth(
            int month,
            int year) {

        // Validate month and year before querying.
        validateMonthAndYear(month, year);

        return expenseDAO.getExpensesByMonth(
                month,
                year);
    }

    // =====================================================
    //                     ANALYTICS
    // =====================================================

    /**
     * Calculates the total expense for a given month.
     *
     * @param month month (1-12)
     * @param year year
     * @return total monthly expense
     * @throws InvalidExpenseException if month or year is invalid
     */
    @Override
    public BigDecimal getTotalMonthlyExpense(
            int month,
            int year) {

        // Validate month and year.
        validateMonthAndYear(month, year);

        return expenseDAO.getTotalMonthlyExpense(
                month,
                year);
    }

    /**
     * Calculates the total expense between two dates.
     *
     * @param startDate start date
     * @param endDate end date
     * @return total expense for the specified date range
     * @throws InvalidExpenseException if the date range is invalid
     */
    @Override
    public BigDecimal getTotalExpenseBetweenDates(
            LocalDate startDate,
            LocalDate endDate) {

        // Validate the date range.
        validateDate(startDate, endDate);

        return expenseDAO.getTotalExpenseBetweenDates(
                startDate,
                endDate);
    }

    /**
     * Generates monthly expense statistics.
     *
     * Statistics include:
     * - Total expense
     * - Average expense
     * - Highest expense
     * - Lowest expense
     * - Number of expenses
     *
     * @param month month (1-12)
     * @param year year
     * @return monthly expense statistics
     * @throws InvalidExpenseException if month or year is invalid
     */
    @Override
    public MonthlyExpenseStatistics getMonthlyExpenseStatistics(
            int month,
            int year) {

        // Validate month and year.
        validateMonthAndYear(month, year);

        return expenseDAO.getMonthlyExpenseStatistics(
                month,
                year);
    }


// =====================================================
//                       REPORTS
// =====================================================
    /**
     * Generates a report for a specific expense category.
     *
     * @param expenseType Expense category to include in the report
     * @return Formatted category report
     */
    @Override
    public String generateCategoryReport(
            ExpenseType expenseType) {

        if (expenseType == null) {
            throw new InvalidExpenseException(
                    "Expense category cannot be null");
        }

        List<Expense> expenses =
                expenseDAO.getExpensesByType(expenseType);

        if (expenses.isEmpty()) {
            throw new ExpenseNotFoundException(
                    "No expenses found for category: " + expenseType);
        }

        return reportGenerator.generateReport(
                "CATEGORY REPORT",
                "Category : " + expenseType,
                expenses);
    }


    /**
     * Generates a report for expenses within the specified date range.
     *
     * @param startDate Start date of the report period
     * @param endDate   End date of the report period
     * @return Formatted date range report
     */
    @Override
    public String generateDateRangeReport(
                        LocalDate startDate,
                        LocalDate endDate) {

        // Validate input dates before querying the database
        validateDate(startDate, endDate);

        List<Expense> expenses =
                expenseDAO.getExpensesByDateRange(
                        startDate,
                        endDate
                );

        if (expenses.isEmpty()) {
            throw new ExpenseNotFoundException(
                    "No expenses found between "
                            + startDate + " and " + endDate);
        }

        return reportGenerator.generateReport(
                "DATE RANGE REPORT",
                "From : " + startDate
                        + "\nTo   : " + endDate,
                expenses
        );
    }

    /**
     * Generates an expense report for the given month and year.
     *
     * @param month month for the report
     * @param year year for the report
     * @return generated monthly expense report
     */
    @Override
    public String generateMonthlyReport(int month, int year) {

        // Validate input before querying database
        validateMonthAndYear(month, year);

        List<Expense> expenses = getExpensesByMonth(month, year);

        if (expenses.isEmpty()) {
            throw new ExpenseNotFoundException(
                    "No expenses found for " + month + "/" + year);
        }

        return reportGenerator.generateReport(
                "MONTHLY REPORT",
                "Month : " + month + "\nYear  : " + year,
                expenses);
    }

    /**
     * Saves the generated report as a CSV file.
     *
     * @param fileName Name of the CSV file
     * @param report   Report content
     */
    @Override
    public void saveReport(
            String fileName,
            String report) {

        if (fileName == null || fileName.isBlank()) {
            throw new InvalidExpenseException(
                    "Report file name cannot be empty");
        }

        if (report == null || report.isBlank()) {
            throw new InvalidExpenseException(
                    "Report content cannot be empty");
        }

        csvWriter.saveReport(fileName, report);
    }

// =====================================================
//                       SORTING
// =====================================================

    /**
     * Returns expenses sorted based on the selected option.
     *
     * @param sortOption Sorting criteria
     * @return Sorted expense list
     */
    @Override
    public List<Expense> sortExpenses(
            SortOption sortOption) {

        if (sortOption == null) {
            throw new InvalidExpenseException(
                    "Sort option cannot be null.");
        }

        return expenseDAO.sortExpenses(sortOption);
    }

    // ==========================================================
    //                PRIVATE VALIDATION METHODS
    // ==========================================================

    /**
     * Validates an Expense object before performing
     * add, update, or patch operations.
     *
     * @param expense Expense object to validate
     */
    private void validateExpense(Expense expense) {

        if (expense == null) {
            throw new InvalidExpenseException(
                    "Expense cannot be null");
        }

        if (expense.getPersonType() == null) {
            throw new InvalidExpenseException(
                    "Person type is required");
        }

        if (expense.getExpenseType() == null) {
            throw new InvalidExpenseException(
                    "Expense category is required");
        }

        if (expense.getExpenseAmount() == null) {
            throw new InvalidExpenseException(
                    "Expense amount is required");
        }

        if (expense.getExpenseAmount()
                .compareTo(ZERO) <= 0) {

            throw new InvalidExpenseException(
                    "Amount must be greater than zero");
        }

        if (expense.getExpenseDate() == null) {
            throw new InvalidExpenseException(
                    "Expense date is required");
        }

        if (expense.getExpenseDate().isAfter(LocalDate.now())) {
            throw new InvalidExpenseException(
                    "Future expense dates are not allowed");
        }
    }

    /**
     * Validates a date range.
     *
     * Rules:
     * - Start date cannot be null
     * - End date cannot be null
     * - Year must be 2000 or above
     * - Start date must not be after end date
     * - Future dates are not allowed
     *
     * @param startDate Start date
     * @param endDate   End date
     */
    private void validateDate(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new InvalidExpenseException(
                    "Start date and end date must not be null");
        }

        if (startDate.getYear() < MIN_YEAR ||
                endDate.getYear() < MIN_YEAR) {

            throw new InvalidExpenseException(
                    "Year should be " + MIN_YEAR + " or above");
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidExpenseException(
                    "Start date cannot be after end date");
        }

        if (startDate.isAfter(LocalDate.now())
                || endDate.isAfter(LocalDate.now())) {

            throw new InvalidExpenseException(
                    "Future dates are not allowed");
        }
    }
    /**
     * Validates month and year.
     *
     * Rules:
     * - Month must be between 1 and 12
     * - Year must be between 2000 and current year
     *
     * @param month Month value
     * @param year  Year value
     */
    private void validateMonthAndYear(
            int month,
            int year) {

        if (month < MIN_MONTH || month > MAX_MONTH) {
            throw new InvalidExpenseException(
                    "Invalid month");
        }

        if (year < MIN_YEAR ||
                year > LocalDate.now().getYear()) {

            throw new InvalidExpenseException(
                    "Invalid year");
        }
    }

    /**
     * Compares existing expense data with updated expense data.
     *
     * @param existingExpense expense data currently stored in database
     * @param updatedExpense new expense data received for update
     * @return true if all fields are unchanged, otherwise false
     */
    private boolean isSameExpense(
            Expense existingExpense,
            Expense updatedExpense) {

        return existingExpense.getPersonType()
                .equals(updatedExpense.getPersonType())

                && existingExpense.getExpenseType()
                .equals(updatedExpense.getExpenseType())

                && existingExpense.getExpenseAmount()
                .compareTo(updatedExpense.getExpenseAmount()) == 0

                && Objects.equals(
                existingExpense.getDescription(),
                updatedExpense.getDescription()
        )

                && existingExpense.getExpenseDate()
                .equals(updatedExpense.getExpenseDate());
    }

    /**
     * Checks budget status after expense changes.
     *
     * @param expense updated expense
     */
    private void checkBudgetAfterExpenseChange(Expense expense) {

        try {

            String alert =
                    budgetService.showBudgetAlert(
                            expense.getExpenseDate().getMonthValue(),
                            expense.getExpenseDate().getYear()
                    );

            if (!alert.isBlank()) {
                System.out.println(alert);
            }

        } catch (InvalidBudgetException e) {

            // No budget created for this month
            // Expense can still be added.
        }
    }

}
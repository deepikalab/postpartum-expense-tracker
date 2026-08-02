package com.deepika.postpartum.expensetracker.dao;

/**
 * Centralized repository of SQL queries used by ExpenseDAO.
 *
 * Responsibilities:
 * - Stores database queries related to expense operations
 * - Keeps SQL statements separate from DAO logic
 * - Improves maintainability by avoiding duplicate queries
 *
 * This class is not intended to be instantiated.
 */
public final class ExpenseQueries {

    // Prevents object creation
    private ExpenseQueries() {

    }

    // ================= CRUD QUERIES =================

    /**
     * SQL query to insert a new expense record.
     *
     * Uses parameter placeholders (?) for PreparedStatement
     * to safely bind values and prevent SQL injection.
     */
    static final String INSERT_SQL =
            "INSERT INTO expense " +
                    "(person_type, expense_type, expense_amount, description, expense_date) " +
                    "VALUES (?, ?, ?, ?, ?) ";

    /**
     * SQL query to update an existing expense by ID.
     */
    static final String UPDATE_SQL =
            "UPDATE expense SET person_type=?, expense_type=?, " +
                    "expense_amount=?, description=?, expense_date=? WHERE id=?";

    /**
     * SQL query to delete an expense by ID.
     */
    static final String DELETE_SQL =
            "DELETE FROM expense WHERE id=?";

    /**
     * SQL query to retrieve all expenses sorted by latest date.
     */
    static final String SELECT_ALL_SQL =
            "SELECT * FROM expense ORDER BY expense_date DESC";

    /**
     * SQL query to retrieve an expense using its ID.
     */
    static final String SELECT_BY_ID_SQL =
            "SELECT * FROM expense WHERE id = ?";


    // ================= SEARCH QUERIES =================


    /**
     * SQL query to find expenses recorded on a specific date.
     */
    static final String SELECT_BY_SINGLE_DATE_SQL =
            "SELECT * FROM expense WHERE expense_date = ?";


    /**
     * SQL query to retrieve expenses within a date range.
     */
    static final String SELECT_BY_DATE_RANGE_SQL =
            "SELECT * FROM expense WHERE expense_date BETWEEN ? AND ? ORDER BY expense_date";

    /**
     * SQL query to retrieve expenses for a specific month and year.
     *
     * MONTH() and YEAR() functions allow filtering
     * directly using date components.
     */
    static final String SELECT_BY_MONTH_SQL =
            "SELECT * FROM expense WHERE MONTH(expense_date)= ? AND YEAR(expense_date)=?";

    /**
     * SQL query to filter expenses by category.
     */
    static final String SELECT_BY_TYPE_SQL =
            "SELECT * FROM expense WHERE expense_type = ? ORDER BY expense_date DESC";

    /**
     * SQL query to filter expenses by person type.
     */
    static final String SELECT_BY_PERSON_SQL =
            "SELECT * FROM expense WHERE person_type = ?";


    // ================= ANALYTICS QUERIES =================

    /**
     * Calculates total expense for a specific month.
     *
     * Uses SQL SUM() aggregate function.
     */
    static final String MONTHLY_TOTAL_SQL =
            "SELECT SUM(expense_amount) AS TotalMonthlyExpense " +
                    "FROM expense WHERE MONTH(expense_date)=? AND YEAR(expense_date)=?";

    /**
     * Calculates total expense between two dates.
     */
    static final String DATE_RANGE_TOTAL_SQL =
            "SELECT SUM(expense_amount) AS TotalExpenseDateRange " +
                    "FROM expense WHERE expense_date BETWEEN ? AND ?";

    /**
     * Calculates monthly expense statistics.
     *
     * Uses aggregate functions:
     * SUM - Total expense
     * AVG - Average expense
     * MAX - Highest expense
     * MIN - Lowest expense
     * COUNT - Number of expenses
     */
    static final String MONTHLY_STATS_SQL =
            "SELECT SUM(expense_amount) AS total, " +
                    "AVG(expense_amount) AS average, " +
                    "MAX(expense_amount) AS maximum, " +
                    "MIN(expense_amount) AS minimum, " +
                    "COUNT(*) AS count " +
                    "FROM expense " +
                    "WHERE MONTH(expense_date) = ? AND YEAR(expense_date) = ?";


    // ================= SORT QUERIES =================

    /**
     * SQL query to sort expenses by date in ascending order.
     */
    static final String SORT_DATE_ASC_SQL =
            "SELECT * FROM expense ORDER BY expense_date ASC";

    /**
     * SQL query to sort expenses by date in descending order.
     */
    static final String SORT_DATE_DESC_SQL =
            "SELECT * FROM expense ORDER BY expense_date DESC";

    /**
     * SQL query to sort expenses by amount from lowest to highest.
     */
    static final String SORT_AMOUNT_ASC_SQL =
            "SELECT * FROM expense ORDER BY expense_amount ASC";

    /**
     * SQL query to sort expenses by amount from highest to lowest.
     */
    static final String SORT_AMOUNT_DESC_SQL =
            "SELECT * FROM expense ORDER BY expense_amount DESC";

}
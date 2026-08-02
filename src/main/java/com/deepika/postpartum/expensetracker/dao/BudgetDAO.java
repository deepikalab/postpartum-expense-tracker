package com.deepika.postpartum.expensetracker.dao;

import com.deepika.postpartum.expensetracker.model.Budget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


/**
 * Handles database operations related to monthly budgets.
 *
 * Responsibilities:
 * - Saves monthly budget details
 * - Retrieves budget details by month and year
 * - Updates existing budget amounts
 *
 * Does NOT contain:
 * - Input validation
 * - Business rules
 * - User interaction
 */
public class BudgetDAO {

    private final Connection connection;

    /**
     * Creates BudgetDAO with database connection.
     *
     * @param connection Database connection
     */
    public BudgetDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Saves a monthly budget.
     *
     * If a budget already exists for the same month and year,
     * the existing budget amount is updated.
     *
     * @param budget Budget details to save
     */
    public void save(Budget budget) {

        String sql = " INSERT INTO budget (month, year, budget_amount) " +
                " VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE budget_amount = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1, budget.getMonth());

            statement.setInt(
                    2, budget.getYear());

            statement.setBigDecimal(
                    3, budget.getBudgetAmount());

            statement.setBigDecimal(
                    4, budget.getBudgetAmount());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save budget", e);
        }
    }

    /**
     * Retrieves a budget for a specific month and year.
     *
     * @param month Budget month
     * @param year Budget year
     * @return Optional containing budget if found,
     *         otherwise empty
     */
    public Optional<Budget> findByMonthAndYear(
                            int month,
                            int year) {

        String sql = "SELECT id, month, year, budget_amount " +
                "FROM budget WHERE month = ? AND year = ?" ;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, month);
            statement.setInt(2, year);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                Budget budget = new Budget();

                budget.setId(
                        resultSet.getLong("id")
                );

                budget.setMonth(
                        resultSet.getInt("month")
                );

                budget.setYear(
                        resultSet.getInt("year")
                );

                budget.setBudgetAmount(
                                resultSet.
                                        getBigDecimal("budget_amount")
                );

                return Optional.of(budget);
            }

        } catch (SQLException e) {

            throw new RuntimeException("Failed to retrieve budget", e);
        }

        return Optional.empty();
    }


    /**
     * Deletes a budget for the specified month and year.
     *
     * @param month Budget month
     * @param year Budget year
     */
    public void deleteByMonthAndYear(
                    int month,
                    int year) {


        String sql = "DELETE FROM budget " +
                "WHERE month = ? AND year = ?" ;


        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, month);

            statement.setInt(2, year);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to delete budget", e);
        }
    }
}
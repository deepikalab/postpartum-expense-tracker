package com.deepika.postpartum.expensetracker.config;

import com.deepika.postpartum.expensetracker.dao.BudgetDAO;
import com.deepika.postpartum.expensetracker.dao.ExpenseDAO;
import com.deepika.postpartum.expensetracker.report.ExpenseReportGenerator;
import com.deepika.postpartum.expensetracker.report.ReportCsvWriter;
import com.deepika.postpartum.expensetracker.service.*;
import com.deepika.postpartum.expensetracker.util.DBConnection;

import java.sql.Connection;


/**
 * Application configuration class responsible for creating and
 * wiring application components.
 *
 * Responsibilities:
 * - Creates service objects
 * - Provides required dependencies
 * - Maintains loose coupling between layers
 *
 * This class works as a simple manual dependency injection
 * configuration since the application is a console-based project.
 */
public class ApplicationConfig {

    /**
     * Private constructor prevents object creation
     * because this class only provides static factory methods.
     */
    private ApplicationConfig() {}


    /**
     * Creates and configures ExpenseService with required dependencies.
     *
     * Dependencies provided:
     * - ExpenseDAO for database operations
     * - ExpenseReportGenerator for generating reports
     * - ReportCsvWriter for saving reports
     *
     * @return configured ExpenseService implementation
     */
    public static ExpenseService createExpenseService() {

        Connection connection =
                DBConnection.getConnection();

        ExpenseDAO expenseDAO =
                new ExpenseDAO(connection);

        ExpenseReportGenerator reportGenerator =
                new ExpenseReportGenerator();

        ReportCsvWriter csvWriter =
                new ReportCsvWriter();


        BudgetService budgetService =
                createBudgetService();

        return new ExpenseServiceImpl(
                expenseDAO,
                reportGenerator,
                csvWriter,
                budgetService
        );
    }

    /**
     * Creates and configures the BudgetService instance.
     *
     * Initializes required dependencies:
     * - Database connection
     * - ExpenseDAO for calculating expense totals
     * - BudgetDAO for budget database operations
     *
     * @return fully configured BudgetService instance
     */
    public static BudgetService createBudgetService() {

        Connection connection =
                DBConnection.getConnection();

        ExpenseDAO expenseDAO =
                new ExpenseDAO(connection);

        BudgetDAO budgetDAO =
                new BudgetDAO(connection);

        return new BudgetServiceImpl(
                expenseDAO,
                budgetDAO);
    }
}
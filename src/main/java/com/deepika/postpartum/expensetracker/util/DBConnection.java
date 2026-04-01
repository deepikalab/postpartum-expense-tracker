package com.deepika.postpartum.expensetracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class responsible for creating database connections.

 * Responsibilities:
 * - Centralizes database configuration
 * - Provides reusable method to obtain JDBC connection
 * - Encapsulates low-level connection logic

 * Design Notes:
 * - This is a utility class (only static methods)
 * - Constructor is private to prevent object creation
 * - If database credentials change, update only here
 */

public final class DBConnection {

    // Database connection URL
    private static final String URL = "jdbc:mysql://localhost:3306/postpartum_expense_tracker";

    // Database username
    private static final String USERNAME = "root";

    // Database password
    private static final String PASSWORD = "9090";

    /**
     * Private constructor prevents instantiation of utility class.
     */
    private DBConnection() {// Prevent object creation
    }

    /**
     * Creates and returns a new database connection.
     * @return Connection object to MySQL database
     * Throws:RuntimeException if connection fails.
     */
    public static Connection getConnection() {

        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            // Wrap checked exception into runtime exception
            throw new RuntimeException("Failed to connect to database", e);
        }
    }
}
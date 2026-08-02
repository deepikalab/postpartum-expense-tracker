package com.deepika.postpartum.expensetracker.ui;

import com.deepika.postpartum.expensetracker.model.Expense;

import java.util.List;

/**
 * Provides reusable console formatting methods
 * for displaying application messages.
 *
 * Responsibility:
 * - Maintains consistent console output format
 * - Improves readability of Command-Line Interface application
 *
 * Does NOT contain business logic.
 */
public final class ConsoleFormatter {

    private ConsoleFormatter() {
    }

    /**
     * Displays a list of expenses in a formatted table.
     *
     * If the list is empty or null, the specified
     * message is displayed instead.
     *
     * @param expenses list of expenses to display
     * @param emptyMessage message displayed when no expenses are available
     */
    public static void displayExpenses(
            List<Expense> expenses, String emptyMessage) {

        if (expenses == null || expenses.isEmpty()) {

            System.out.println(emptyMessage);
            return;
        }

//        System.out.println("\nID | PERSON | CATEGORY | AMOUNT | DESCRIPTION | DATE");
//        System.out.println("------------------------------------------------------");

        expenses.forEach(System.out::println);
    }

    /**
     * Displays a formatted success message.
     *
     * @param message success message to display
     */
    public static void showSuccess(String message) {

        System.out.println("\n==================================================");
        System.out.println("       ✓ " + message.toUpperCase());
        System.out.println("==================================================");
    }

    /**
     * Displays expense details below a formatted heading.
     *
     * @param title heading text
     * @param object object details to display
     */
    public static void showDetails(
            String title,
            Object object) {

        System.out.println("\n" + title + ":");
        System.out.println(object);
        System.out.println("=========================================");
    }

    /**
     * Displays a complete success message
     * with related object details.
     *
     * @param message success message
     * @param title details heading
     * @param object object to display
     */
    public static void showSuccessWithDetails(
            String message,
            String title,
            Object object) {

        showSuccess(message);
        showDetails(title, object);
    }

    /**
     * Displays a formatted error message.
     *
     * @param message error details
     */
    public static void showError(String message) {

        System.out.println("\n====================================================");
        System.out.println(" ✗ ERROR: " + message);
        System.out.println("====================================================");
    }

    public static void showInfo(String message){

        System.out.println("\n-------------------------------------------------");
        System.out.println("          "+message);
        System.out.println("-------------------------------------------------");
    }
}
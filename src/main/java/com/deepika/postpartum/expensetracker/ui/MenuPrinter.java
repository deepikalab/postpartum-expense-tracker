package com.deepika.postpartum.expensetracker.ui;


/**
 * Responsible only for displaying console menus.
 *
 * Responsibilities:
 * - Displays application navigation options
 * - Organizes menu items into logical sections
 *
 * Does NOT contain:
 * - Business logic
 * - Database operations
 * - User input handling
 *
 * Design:
 * Utility class with static methods.
 */
public final class MenuPrinter {

    /**
     * Private constructor to prevent object creation
     * because this class only provides static utility methods.
     */
    private MenuPrinter() {}

    /**
     * Displays the main application menu
     * with available expense, report, budget,
     * and utility operations.
     */
    public static void showMenu() {

        System.out.println("\n========== Expense Tracker ==========");

        System.out.println("\n----- Expense Management -----");

        System.out.println("1.  Add Expense");
        System.out.println("2.  Update Expense");
        System.out.println("3.  Patch Expense");
        System.out.println("4.  Delete Expense");
        System.out.println("5.  View All Expenses");

        System.out.println("\n----- Search Expenses -----");

        System.out.println("6.  Search Expense by ID");
        System.out.println("7.  Search Expense by Category");
        System.out.println("8.  Search Expense by Person");
        System.out.println("9.  Search Expense by Date");
        System.out.println("10. Search Expense by Date Range");


        System.out.println("\n----- Summary -----");

        System.out.println("11. Monthly Total");
        System.out.println("12. Date Based Total");
        System.out.println("13. Monthly Statistics");


        System.out.println("\n----- Reports -----");

        System.out.println("14. Generate Category Report");
        System.out.println("15. Generate Monthly Report");
        System.out.println("16. Generate Date Range Report");

        System.out.println("\n----- Budget -----");

        System.out.println("17. Set Monthly Budget");
        System.out.println("18. View Budget Status");
        System.out.println("19. Delete Budget");


        System.out.println("\n----- Utilities -----");

        System.out.println("20. Sort Expenses");
        System.out.println("\n21. Exit");

        System.out.println("\n====================================");

    }

    /**
     * Displays a simple section title.
     *
     * @param title Title to display
     */
    public static void showTitle(String title) {

        System.out.println("\n======= " + title + " =======");
    }

    /**
     * Displays navigation instruction for returning to the menu.
     */

    public static void showBackInstruction() {

        System.out.println("Press B any time to return to the menu.");
    }

}
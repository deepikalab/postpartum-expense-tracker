package com.deepika.postpartum.expensetracker.app;

import com.deepika.postpartum.expensetracker.exceptions.ExpenseException;
import com.deepika.postpartum.expensetracker.model.Expense;
import com.deepika.postpartum.expensetracker.model.MonthlyExpenseStatistics;
import com.deepika.postpartum.expensetracker.model.PersonType;
import com.deepika.postpartum.expensetracker.model.ExpenseType;
import com.deepika.postpartum.expensetracker.service.ExpenseService;
import com.deepika.postpartum.expensetracker.service.ExpenseServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * UI Layer (Application Entry Point)
 *
 * Responsibilities:
 * - Displays menu and interacts with user
 * - Validates user input
 * - Converts input into domain objects
 * - Delegates business logic to Service Layer
 *
 * Note:
 * This class should NOT contain database logic.
 */
public class ExpenseTrackerApp {

    // Shared Scanner instance for reading console input
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Programming to an Interface (Loose Coupling):
     * Allows switching implementation without changing UI layer.
     */
    private static final ExpenseService SERVICE = new ExpenseServiceImpl();

    // Standard date format used across the application
    private static final DateTimeFormatter INPUT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Controls application loop
    private static boolean running = true;

    public static void main(String[] args) {

        while (running) {

            showMenu();

            int choice = readInt("Enter your choice: ");

            try {

                switch (choice) {

                    case 1 -> addExpense();
                    case 2 -> updateExpense();
                    case 3 -> deleteExpense();
                    case 4 -> viewAllExpenses();
                    case 5 -> searchByID();
                    case 6 -> searchByType();
                    case 7 -> searchByDate();
                    case 8 -> searchByDateRange();
                    case 9 -> monthlyTotal();
                    case 10 -> dateBasedTotal();
                    case 11 -> monthlyStatistics();
                    case 12 -> exitApp();

                    default -> System.out.println("Invalid choice! Try again.");
                }

            } catch (ExpenseException e) {

                // Business-level errors
                System.err.println("Application Error: " + e.getMessage());

            } catch (Exception e) {

                // Unexpected system errors
                System.err.println("Unexpected System Error: " + e.getMessage());
            }
        }
    }

    /**
     * Displays main navigation menu.
     */
    private static void showMenu() {

        System.out.println("\n------ Postpartum Expense Tracker ------");

        System.out.println(
                "1. Add expense     | 2. Update expense | 3. Delete expense");

        System.out.println(
                "4. View all        | 5. Search by ID   | 6. Search by type");

        System.out.println(
                "7. Search date     | 8. Date range     | 9. Monthly summary");

        System.out.println(
                "10. Custom summary | 11. Statistics    | 12. Exit");
    }

    // ================= CRUD OPERATIONS =================

    private static void addExpense() {

        System.out.println("\n====== EXPENSE INSERTION =====\n");

        Expense expense = readExpenseData(null);

        Expense saved = SERVICE.addExpense(expense);

        System.out.println("Expense saved with ID: " + saved.getId());
    }

    private static void updateExpense() {

        System.out.println("\n====== EXPENSE UPDATE =====\n");

        Long id = readLong("Enter ID to update: ");

        Expense expense = readExpenseData(id);

        Expense updated = SERVICE.updateExpense(expense);

        System.out.println("Expense updated successfully");

        System.out.println(updated);
    }

    private static void deleteExpense() {

        System.out.println("\n====== EXPENSE DELETION =====\n");

        Long id = readLong("Enter Expense ID to delete: ");

        SERVICE.deleteExpense(id);

        System.out.println("Expense deleted successfully");
    }

    private static void viewAllExpenses() {

        System.out.println("\n===== ALL EXPENSES =====");

        var expenses = SERVICE.getAllExpenses();

        displayExpenses(expenses,"No records available");
    }

    // ================= SEARCH OPERATIONS =================

    private static void searchByID() {

        System.out.println("\n===== ID BASED SEARCH =====\n");

        Long id = readLong("Enter Expense ID to search: ");

        Expense expense = SERVICE.getExpenseById(id);

        System.out.println(expense);
    }

    private static void searchByType() {

        System.out.println("\n===== TYPE BASED SEARCH =====\n");

        ExpenseType expenseType = readExpenseType();

        var expenses = SERVICE.getExpenseByType(expenseType);

        displayExpenses(expenses,"No records available for type "+expenseType);
    }

    private static void searchByDate() {

        System.out.println("\n===== DATE BASED SEARCH =====\n");

        LocalDate date = readDate("Enter date (dd-MM-yyyy): ");

        var expenses = SERVICE.getExpenseByDate(date);//Returns List

        displayExpenses(expenses,"No records available for " +date.format(INPUT_DATE_FORMATTER));

    }

    private static void searchByDateRange() {

        System.out.println("\n===== DATE RANGE BASED SEARCH =====\n");

        LocalDate startDate = readDate("Enter start date (dd-MM-yyyy): ");

        LocalDate endDate = readDate("Enter end date (dd-MM-yyyy): ");

        var expenses = SERVICE.getExpenseByDateRange(startDate,endDate);

        displayExpenses(expenses,"No records available for "+startDate.format(INPUT_DATE_FORMATTER)+" and "+endDate.format(INPUT_DATE_FORMATTER));
    }

    // ================= ANALYTICS =================

    private static void monthlyTotal() {

        System.out.println("\n===== TOTAL MONTHLY EXPENSE =====\n");

        int month = readInt("Enter month (1-12): ");

        int year = readInt("Enter year: ");

        BigDecimal total = SERVICE.getTotalMonthlyExpense(month,year);

        String monthName = Month.of(month).getDisplayName(TextStyle.FULL,Locale.ENGLISH);

        System.out.println(
                "Total Expense of " + monthName + " " + year + " : " + total);
    }

    private static void dateBasedTotal() {

        System.out.println("\n===== DATE BASED TOTAL EXPENSE =====\n");

        LocalDate startDate = readDate("Enter start date (dd-MM-yyyy): ");

        LocalDate endDate = readDate("Enter end date (dd-MM-yyyy): ");

        BigDecimal total =
                SERVICE.getTotalExpenseBetweenDates(startDate,endDate);

        System.out.println(
                "Total Expense between " + startDate + " & " + endDate + ": " + total);
    }

    private static void monthlyStatistics() {

        System.out.println("\n===== MONTHLY EXPENSE STATISTICS =====\n");

        int month = readInt("Enter month (1-12): ");

        int year = readInt("Enter year: ");

        MonthlyExpenseStatistics stats = SERVICE.getMonthlyExpenseStatistics(month, year);

        System.out.println(stats);
    }

    // ================= DATA INPUT HELPERS =================

    /**
     * Builds Expense object from user input.
     */
    private static Expense readExpenseData(Long id) {

        PersonType personType = readPersonType();

        ExpenseType expenseType = readExpenseType();

        BigDecimal amount = readAmount("Enter amount: ");

        String description = readString("Enter description: ");

        LocalDate date = readDate("Enter date (dd-MM-yyyy): ");

        return new Expense(id, personType, expenseType, amount, description, date);
    }

    private static PersonType readPersonType() {

        while (true) {

            System.out.println("Select Person Type:");

            System.out.println("1. MOTHER");

            System.out.println("2. BABY");

            int choice = readInt("Enter choice: ");

            switch (choice) {

                case 1: return PersonType.MOTHER;

                case 2: return PersonType.BABY;

                default: System.out.println("Error! Please choose 1 or 2.");
            }
        }
    }

    private static ExpenseType readExpenseType() {

        while (true) {

            System.out.println("Select expense type:");

            ExpenseType[] types = ExpenseType.values();

            for (int i = 0; i < types.length; i++) {

                System.out.println((i + 1) + ". " + types[i]);
            }

            int choice = readInt("Enter choice: ");

            if (choice > 0 && choice <= types.length) {

                return types[choice - 1];
            }

            System.out.println("Invalid choice! Enter number between 1 and " + types.length);
        }
    }
    // ================= DISPLAY HELPERS =================

    private static void displayExpenses(List<Expense> expenses, String emptyMessage) {

        if (expenses.isEmpty()) {

            System.out.println(emptyMessage);
        }
        else {
            expenses.forEach(System.out::println);
        }
    }
    // ================= LOW-LEVEL INPUT HELPERS =================

    private static Long readLong(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Long.parseLong(SCANNER.nextLine().trim());

            } catch (NumberFormatException e) {

                System.out.println("Invalid input. Enter digits only.");
            }
        }
    }

    private static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(SCANNER.nextLine().trim());

            } catch (NumberFormatException e) {

                System.out.println("Invalid input. Enter whole number.");
            }
        }
    }

    private static String readString(String message) {

        System.out.print(message);

        return SCANNER.nextLine();
    }

    private static BigDecimal readAmount(String message) {

        while (true) {

            try {

                System.out.print(message);

                return new BigDecimal(SCANNER.nextLine().trim());

            } catch (NumberFormatException e) {

                System.out.println("Invalid amount! Enter valid number.");
            }
        }
    }

    private static LocalDate readDate(String message) {

        while (true) {

            try {

                System.out.print(message);

                return LocalDate.parse(SCANNER.nextLine(),INPUT_DATE_FORMATTER);

            } catch (DateTimeParseException e) {

                System.out.println("Invalid format! Use DD-MM-YYYY.");
            }
        }
    }

    private static void exitApp() {

        System.out.println("Exiting the application...");

        running = false;

        SCANNER.close();// Closes All resources on exit.
    }
}
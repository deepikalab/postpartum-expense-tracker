package com.deepika.postpartum.expensetracker.app;

import com.deepika.postpartum.expensetracker.config.ApplicationConfig;
import com.deepika.postpartum.expensetracker.exceptions.ExpenseException;
import com.deepika.postpartum.expensetracker.exceptions.UserCancelledException;
import com.deepika.postpartum.expensetracker.model.*;
import com.deepika.postpartum.expensetracker.service.BudgetService;
import com.deepika.postpartum.expensetracker.service.ExpenseService;
import com.deepika.postpartum.expensetracker.ui.ConsoleFormatter;
import com.deepika.postpartum.expensetracker.ui.ConsoleInputHandler;
import com.deepika.postpartum.expensetracker.ui.MenuPrinter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
/**
 * Main application class for Postpartum Expense Tracker.
 *
 * Responsibilities:
 * - Controls application flow
 * - Handles user menu selection
 * - Coordinates between UI and service layers
 * - Displays operation results
 *
 * Does NOT contain:
 * - Database operations
 * - Business logic
 * - Input validation logic
 *
 * Uses:
 * - ExpenseService for expense operations
 * - BudgetService for budget management
 * - ConsoleInputHandler for user input
 * - MenuPrinter for displaying menus
 */
public class ExpenseTrackerApp {

    // Handles all console input operations
    private static final ConsoleInputHandler INPUT =
            new ConsoleInputHandler(new Scanner(System.in));


    // Expense business operations service
    private static final ExpenseService EXPENSE_SERVICE =
            ApplicationConfig.createExpenseService();


    // Budget management service
    private static final BudgetService BUDGET_SERVICE =
            ApplicationConfig.createBudgetService();


    // Controls application execution loop
    private static boolean running = true;

    // =====================================================
    //                APPLICATION ENTRY POINT
    // =====================================================
    /**
     * Starts the Expense Tracker application.
     *
     * Displays the main menu, accepts user selections,
     * delegates operations to the service layer,
     * and keeps the application running until the
     * user chooses to exit.
     *
     * @param args command-line arguments (not used)
     */

    public static void main(String[] args) {

        while (running) {
            try {
                // ===MAIN MENU ===
                MenuPrinter.showMenu();
                int choice = INPUT.readInt("Enter your choice: ");

                // Routes user selection to corresponding operations based on menu choice
                switch (choice) {

                    // === CRUD OPERATIONS ===
                    case 1 -> addExpense();
                    case 2 -> updateExpense();
                    case 3 -> patchExpense();
                    case 4 -> deleteExpense();
                    case 5 -> viewAllExpenses();

                    // === SEARCH OPERATIONS ===
                    case 6 -> searchById();
                    case 7 -> searchByType();
                    case 8 -> searchByPerson();
                    case 9 -> searchByDate();
                    case 10 -> searchByDateRange();

                    // === ANALYTICS ===
                    case 11 -> monthlyTotal();
                    case 12 -> dateBasedTotal();
                    case 13 -> monthlyStatistics();

                    // === REPORTS ===
                    case 14 -> generateCategoryReport();
                    case 15 -> generateMonthlyReport();
                    case 16 -> generateDateRangeReport();

                    // === BUDGET OPERATIONS ===
                    case 17 -> setMonthlyBudget();
                    case 18 -> viewBudgetStatus();
                    case 19 -> deleteBudget();

                    // === SORTING ===
                    case 20 -> sortExpenses();

                    // === EXIT APPLICATION ===
                    case 21 -> exitApp();

                    default -> ConsoleFormatter.showError(
                            "Invalid choice. Try again."
                    );

                }
                //Pauses the program until the user presses Enter.

                if (running) {
                    pressEnterToContinue();
                }
            }
            catch(UserCancelledException e){
                ConsoleFormatter.showInfo(e.getMessage());
            }

            catch (ExpenseException e) {
                ConsoleFormatter.showError(
                        "Application Error : " + e.getMessage());

                if (running) {
                    pressEnterToContinue();
                }

            } catch (Exception e) {
                ConsoleFormatter.showError(
                        "Unexpected Error : " + e.getMessage());

                if (running) {
                    pressEnterToContinue();
                }
            }
        }
    }

    // ================= CRUD OPERATIONS =================

    /**
     * Collects expense details from user and creates a new expense record.
     */
    private static void addExpense() {

        MenuPrinter.showTitle("ADD EXPENSE");

        Expense expense = readExpenseData(null);

        Expense saved = EXPENSE_SERVICE.addExpense(expense);

        ConsoleFormatter.showSuccessWithDetails(
                "Expense saved successfully with ID: "+saved.getId(),
                "Saved Expense",
                saved
        );

    }

    /**
     * Updates an existing expense using the provided expense ID.
     */
    private static void updateExpense() {

        MenuPrinter.showTitle("UPDATE EXPENSE");
        MenuPrinter.showBackInstruction();

        Long id = INPUT.readLong("Enter expense ID : ");

        // Check whether expense exists before asking for update details
        Expense existingExpense = EXPENSE_SERVICE.getExpenseById(id);

        System.out.println("\nCurrent Expense:");
        System.out.println(existingExpense);

        System.out.println("\nEnter new expense details:");

        Expense expense = readExpenseData(id);

        Expense updated = EXPENSE_SERVICE.updateExpense(expense);

        ConsoleFormatter.showSuccessWithDetails(
                "Expense updated successfully",
                "Updated Expense",
                updated
        );
    }

    /**
     * Updates an existing expense using the provided expense ID.
     */
    private static void patchExpense() {

        MenuPrinter.showTitle("PATCH EXPENSE");
        MenuPrinter.showBackInstruction();

        Long id = INPUT.readLong("Enter expense ID : ");

        Expense existing = EXPENSE_SERVICE.getExpenseById(id);
        System.out.println("\nCurrent Expense:");
        System.out.println(existing);

        System.out.println("\nSelect field to update");
        System.out.println("1. Person Type");
        System.out.println("2. Expense Type");
        System.out.println("3. Amount");
        System.out.println("4. Description");
        System.out.println("5. Date");

        int choice = INPUT.readInt("Enter choice : ");

        /* Stores the new field value.
           Object is used because different fields require different data types
           such as PersonType, ExpenseType, BigDecimal, String, and LocalDate.
         */
        Object value;

        switch (choice) {
            case 1 -> value = INPUT.readPersonType();
            case 2 -> value = INPUT.readExpenseType();
            case 3 -> value = INPUT.readAmount("Enter amount : ");
            case 4 -> value = INPUT.readString("Enter description : ").trim();
            case 5 -> value = INPUT.readDate("Enter date(dd-MM-yyyy): ");
            default -> throw new ExpenseException("Invalid patch option.");
        }

        Expense updated = EXPENSE_SERVICE.patchExpense(id, choice, value);

        ConsoleFormatter.showSuccessWithDetails(
                "Expense updated successfully",
                "Updated Expense",
                updated
        );
    }

    /**
     * Deletes an expense after user confirmation.
     */
    private static void deleteExpense() {

        MenuPrinter.showTitle("DELETE EXPENSE");
        MenuPrinter.showBackInstruction();

        Long id = INPUT.readLong("Enter expense ID : ");

        Expense expense = EXPENSE_SERVICE.getExpenseById(id);
        System.out.println("\nExpense Details:");
        System.out.println(expense);

        String choice = INPUT.readYesNo("\nAre you sure you want to delete? (Y/N): ");

        if (choice.equalsIgnoreCase("Y") ||
                choice.equalsIgnoreCase("YES")) {

            EXPENSE_SERVICE.deleteExpense(id);

            ConsoleFormatter.showSuccess(
                    "Expense deleted successfully"
            );

        }
        else
          {
            ConsoleFormatter.showInfo(
                    "Deletion cancelled"
            );
        }
    }

    /**
     * Retrieves and displays all available expenses.
     */
    private static void viewAllExpenses() {

        MenuPrinter.showTitle("ALL EXPENSES");
        MenuPrinter.showBackInstruction();

        List<Expense> expenses = EXPENSE_SERVICE.getAllExpenses();

        showExpenseRetrievedMessage(expenses);

        ConsoleFormatter.displayExpenses(
                expenses, "No expenses available.");
    }

    // ================= SEARCH =================

    /**
     * Searches expenses based on expense ID.
     */
    private static void searchById() {

        MenuPrinter.showTitle("SEARCH EXPENSE BY ID");
        MenuPrinter.showBackInstruction();

        Long id = INPUT.readLong("Enter expense ID : ");
        Expense expense = EXPENSE_SERVICE.getExpenseById(id);
        ConsoleFormatter.showSuccess(
                "Expense retrieved successfully");
        System.out.println(expense);
    }

    /**
     * Searches expenses by category.
     */
    private static void searchByType(){

        MenuPrinter.showTitle("SEARCH EXPENSE BY CATEGORY");
        MenuPrinter.showBackInstruction();

        ExpenseType type = INPUT.readExpenseType();

        List<Expense> expenses =
                EXPENSE_SERVICE.getExpensesByType(type);

        showExpenseRetrievedMessage(expenses);

        ConsoleFormatter.displayExpenses(
                expenses, "No expenses found for " + type);
    }

    /**
     * Searches expenses by person type.
     */
    private static void searchByPerson() {

        MenuPrinter.showTitle("SEARCH EXPENSE BY PERSON");
        MenuPrinter.showBackInstruction();

        PersonType person = INPUT.readPersonType();

        List<Expense> expenses =
                EXPENSE_SERVICE.getExpensesByPerson(person);

        showExpenseRetrievedMessage(expenses);

        ConsoleFormatter.displayExpenses(
                expenses, "No expenses found for " + person);
    }

    /**
     * Searches expenses recorded on a specific date.
     */
    private static void searchByDate() {

        MenuPrinter.showTitle("SEARCH EXPENSES BY DATE");
        MenuPrinter.showBackInstruction();

        LocalDate date =
                INPUT.readDate("Enter date(dd-MM-yyyy): ");

        List<Expense> expenses =
                EXPENSE_SERVICE.getExpensesByDate(date);

        showExpenseRetrievedMessage(expenses);

        ConsoleFormatter.displayExpenses(
                expenses, "No expenses found for " + date);
    }

    /**
     * Searches expenses within a given date range.
     */
    private static void searchByDateRange() {

        MenuPrinter.showTitle("SEARCH EXPENSES BY DATE RANGE");
        MenuPrinter.showBackInstruction();

        LocalDate start =
                INPUT.readDate("Enter start date(dd-MM-yyyy): ");

        LocalDate end =
                INPUT.readDate("Enter end date(dd-MM-yyyy): ");

        List<Expense> expenses =
                EXPENSE_SERVICE.getExpensesByDateRange(start, end);

        showExpenseRetrievedMessage(expenses);

        ConsoleFormatter.displayExpenses(
                expenses, "No expenses found between "
                        + start + " and " + end);
    }


    // ================= ANALYTICS =================

    /**
     * Displays total expenses for a selected month and year.
     */
    private static void monthlyTotal() {

        MenuPrinter.showTitle("MONTHLY EXPENSE TOTAL");
        MenuPrinter.showBackInstruction();

        int month = INPUT.readMonth("Enter month(1-12): ");
        int year = INPUT.readYear("Enter year: ");

        BigDecimal total = EXPENSE_SERVICE.getTotalMonthlyExpense(month, year);

        ConsoleFormatter.showSuccess(
                "Total expense calculated successfully");

        System.out.println(
                "Total expense : ₹" + total);
    }

    /**
     * Calculates total expenses between two dates.
     */
    private static void dateBasedTotal() {

        MenuPrinter.showTitle("DATE RANGE EXPENSE TOTAL");
        MenuPrinter.showBackInstruction();

        LocalDate start = INPUT.readDate("Enter start date(dd-MM-yyyy): ");
        LocalDate end = INPUT.readDate("Enter end date(dd-MM-yyyy): ");

        BigDecimal total = EXPENSE_SERVICE.getTotalExpenseBetweenDates(start, end);

        if (total.compareTo(BigDecimal.ZERO) > 0) {
            ConsoleFormatter.showSuccess(
                    "Total expense calculated successfully");
        }

        System.out.println(
                "Total expense between " + start + " and " + end + " : ₹"
                        + total);
    }

    /**
     * Displays monthly expense statistics.
     */
    private static void monthlyStatistics() {

        MenuPrinter.showTitle("MONTHLY EXPENSE STATISTICS");
        MenuPrinter.showBackInstruction();

        int month = INPUT.readMonth("Enter month: ");
        int year = INPUT.readYear("Enter year: ");

        MonthlyExpenseStatistics statistics =
                EXPENSE_SERVICE.getMonthlyExpenseStatistics(month, year);

        System.out.println(statistics);
    }

    // ================= REPORTS =================

    /**
     * Generates a category-wise expense report.
     */
    private static void generateCategoryReport() {

        MenuPrinter.showTitle("EXPENSE CATEGORY REPORT");

        ExpenseType type = INPUT.readExpenseType();
        String report = EXPENSE_SERVICE.generateCategoryReport(type);
        saveReportChoice("category-report-" + type + ".csv", report);
    }

    /**
     * Generates a monthly expense report.
     */
    private static void generateMonthlyReport() {

        MenuPrinter.showTitle("MONTHLY EXPENSE REPORT");
        MenuPrinter.showBackInstruction();

        int month = INPUT.readMonth("Enter month: ");
        int year = INPUT.readYear("Enter year: ");
        String report = EXPENSE_SERVICE.generateMonthlyReport(month, year);
        saveReportChoice("monthly-report-" + month + "-" + year + ".csv", report);
    }

    /**
     * Generates an expense report for a date range.
     */
    private static void generateDateRangeReport() {

        MenuPrinter.showTitle("DATE RANGE EXPENSE REPORT");
        MenuPrinter.showBackInstruction();

        LocalDate start = INPUT.readDate("Enter start date(dd-MM-yyyy): ");
        LocalDate end = INPUT.readDate("Enter end date(dd-MM-yyyy): ");

        String report = EXPENSE_SERVICE.generateDateRangeReport(start, end);

        saveReportChoice(
                "date-range-report-" + start + "_to_" + end + ".csv",
                report
        );
    }

    /**
     * Asks user whether the generated report should be saved as CSV.
     */
    private static void saveReportChoice(String fileName, String report) {

        String choice = INPUT.readYesNo(
                "\nDo you want to save this report as CSV?(Y/N): ");

        if (!INPUT.isYes(choice)) {

            ConsoleFormatter.showInfo(
                    "Report not saved");
            return;
        }


        if (!INPUT.confirmOverwrite(fileName)) {

            ConsoleFormatter.showInfo(
                    "Report not saved");
            return;
        }


        EXPENSE_SERVICE.saveReport(fileName, report);

        ConsoleFormatter.showSuccess(
                "Report saved successfully"
        );
    }
    // ================= BUDGET =================

    /**
     * Sets a monthly spending budget.
     */
    private static void setMonthlyBudget() {

        MenuPrinter.showTitle("SET EXPENSE MONTHLY BUDGET");
        MenuPrinter.showBackInstruction();

        int month = INPUT.readMonth("Enter month: ");
        int year = INPUT.readYear("Enter year: ");
        BigDecimal amount = INPUT.readAmount("Enter budget amount: ");

        Budget budget =
                BUDGET_SERVICE.setMonthlyBudget(
                                    month,
                                    year,
                                    amount);

        ConsoleFormatter.showSuccess(
                "Monthly budget set successfully: ₹"
                        + budget.getBudgetAmount()
        );
    }

    /**
     * Displays budget usage and remaining balance.
     */
    private static void viewBudgetStatus() {

        MenuPrinter.showTitle("EXPENSE BUDGET STATUS");
        MenuPrinter.showBackInstruction();

        int month = INPUT.readMonth("Enter month: ");
        int year = INPUT.readYear("Enter year: ");

        BudgetStatus status =
                BUDGET_SERVICE.viewBudgetStatus(month, year);

        System.out.println("\n===== BUDGET STATUS =====");

        System.out.println(
                "Budget     : ₹" + status.getBudgetAmount());

        System.out.println(
                "Spent      : ₹" + status.getSpent());

        System.out.println(
                "Used       : " + status.getPercentageUsed() + "%");


        if (status.getRemaining()
                .compareTo(BigDecimal.ZERO) >= 0) {

            System.out.println(
                    "Remaining  : ₹" + status.getRemaining());

        } else {

            System.out.println(
                    "Exceeded By: ₹"
                            + status.getRemaining().abs());
        }

    }

    /**
     * Deletes a monthly budget after user confirmation.
     */
    private static void deleteBudget() {

        MenuPrinter.showTitle("DELETE MONTHLY BUDGET");
        MenuPrinter.showBackInstruction();

        int month = INPUT.readMonth("Enter month: ");

        int year = INPUT.readYear("Enter year: ");

        String choice = INPUT.readYesNo(
                "Are you sure you want to delete this budget? (Y/N): ");

        if (INPUT.isYes(choice)) {

            BUDGET_SERVICE.deleteBudget(month, year);
            ConsoleFormatter.showSuccess(
                    "Budget deleted successfully"
            );

        } else {

            ConsoleFormatter.showInfo(
                    "Budget deletion cancelled"
            );
        }
    }

    // ================= SORT =================

    /**
     * Sorts expenses based on selected sorting criteria.
     */
    private static void sortExpenses() {

        System.out.println("\n===== SORT EXPENSES =====");

        System.out.println("1. Date Ascending");
        System.out.println("2. Date Descending");
        System.out.println("3. Amount Low to High");
        System.out.println("4. Amount High to Low");

        int choice = INPUT.readInt("Enter choice: ");
        SortOption option;

        switch (choice) {
            case 1 -> option = SortOption.DATE_ASC;
            case 2 -> option = SortOption.DATE_DESC;
            case 3 -> option = SortOption.AMOUNT_ASC;
            case 4 -> option = SortOption.AMOUNT_DESC;
            default -> throw new ExpenseException("Invalid sorting option.");
        }
        List<Expense> expenses = EXPENSE_SERVICE.sortExpenses(option);

        ConsoleFormatter.displayExpenses(
                    expenses, "No expenses found.");
    }

    // ================= INPUT HELPERS =================

    /**
     * Collects expense details from user input and creates Expense object.
     *
     * @param id existing expense ID during update, null for new expense
     * @return populated Expense object
     */
    private static Expense readExpenseData(Long id) {

        PersonType personType = INPUT.readPersonType();
        ExpenseType expenseType = INPUT.readExpenseType();
        BigDecimal amount = INPUT.readAmount("Enter amount: ");
        String description = INPUT.readString("Enter description(optional): ").trim();
        LocalDate date = INPUT.readDate("Enter date(dd-MM-yyyy): ");

        return new Expense(id, personType, expenseType, amount, description, date);
    }

    // =============== EXPENSE DISPLAY HELPER =============
    /**
     * Displays success message when expenses are found.
     *
     * @param expenses retrieved expense list
     */
    private static void showExpenseRetrievedMessage(
            List<Expense> expenses) {

        if (expenses != null && !expenses.isEmpty()) {
            ConsoleFormatter.showSuccess(
                    "Expenses retrieved successfully");
        }
    }

    // =============== RETURN TO MENU =============

    /**
     * Pauses the application until the user presses Enter.
     */
    private static void pressEnterToContinue() {

        INPUT.waitForEnter("\nPress Enter to return to the menu...");
    }

    // ================= EXIT =================

    /**
     * Stops application execution and closes input resources.
     */
    private static void exitApp() {

            System.out.println("\nExiting the tracker...\n");

            System.out.println("====================================");
            System.out.println(" Thank you for using Expense Tracker!");
            System.out.println(" We hope it helped you manage your expenses.");
            System.out.println("====================================");

            running = false;
            INPUT.close();
        }
    }

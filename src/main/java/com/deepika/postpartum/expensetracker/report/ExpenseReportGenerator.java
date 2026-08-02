package com.deepika.postpartum.expensetracker.report;

import com.deepika.postpartum.expensetracker.model.Expense;

import java.math.BigDecimal;
import java.util.List;


/**
 * Responsible for generating expense reports.
 *
 * Responsibilities:
 * - Formats expense data for console display
 * - Generates CSV formatted report content
 * - Calculates total expenses from report data
 *
 * Does NOT contain:
 * - Database operations
 * - Business validation logic
 */
public class ExpenseReportGenerator {

    /**
     * Generates a formatted expense report based on the provided data.
     *
     * @param title Report title
     * @param filterDetails Applied filter details for the report
     * @param expenses List of expenses to include in the report
     * @return Generated report content in CSV format
     */
    public String generateReport(
                    String title,
                    String filterDetails,
                    List<Expense> expenses) {

        if (expenses == null || expenses.isEmpty()) {

            return title + "\n"
                    + filterDetails
                    + "\n\nNo expenses available.";
        }


        BigDecimal totalExpense = BigDecimal.ZERO;

        StringBuilder csv = new StringBuilder();

        // Build CSV report header information
        csv.append(title).append("\n");
        csv.append(filterDetails).append("\n\n");
        csv.append("ID,Date,Category,Amount\n");

        // Display report details in console
        System.out.println(
                "\n========== " + title + " ==========");

        System.out.println(filterDetails);
        System.out.println("--------------------------------------------");

        // Print expense details with fixed column widths for readability
        System.out.printf(
                "%-5s %-12s %-15s %-10s%n",
                "ID",
                "Date",
                "Category",
                "Amount"
        );

        System.out.println("--------------------------------------------");

        for (Expense expense : expenses) {

            System.out.printf(
                    "%-5d %-12s %-15s %-10.2f%n",
                    expense.getId(),
                    expense.getExpenseDate(),
                    expense.getExpenseType(),
                    expense.getExpenseAmount()
            );

            // Append expense details to CSV content
            csv.append(expense.getId()).append(",");
            csv.append(expense.getExpenseDate()).append(",");
            csv.append(expense.getExpenseType()).append(",");
            csv.append(expense.getExpenseAmount()).append("\n");

            // Accumulate total expense amount
            totalExpense = totalExpense.add(
                            expense.getExpenseAmount());
        }


        System.out.println("--------------------------------------------");

        System.out.println(
                "Total Expenses : " + expenses.size());

        System.out.println(
                "Total Amount   : " + totalExpense);

        // Add summary details to CSV
        csv.append("\n");

        csv.append("Total Expenses,").append(expenses.size())
                            .append("\n");

        csv.append("Total Amount,").append(totalExpense);

        return csv.toString();

    }
}
package com.deepika.postpartum.expensetracker.ui;


import com.deepika.postpartum.expensetracker.exceptions.UserCancelledException;
import com.deepika.postpartum.expensetracker.model.ExpenseType;
import com.deepika.postpartum.expensetracker.model.PersonType;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Handles all console-based user input operations.
 *
 * Responsibilities:
 * - Reads and converts user input into required data types
 * - Performs basic input validation
 * - Provides reusable input methods for the UI layer
 *
 * Does not contain:
 * - Business logic
 * - Database operations
 * - Application workflow control
 */
public class ConsoleInputHandler {


    private final Scanner scanner;


    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");


    /**
     * Creates a ConsoleInputHandler with a scanner instance.
     *
     * @param scanner Scanner object used for reading input
     */
    public ConsoleInputHandler(Scanner scanner) {

        this.scanner = scanner;
    }


    /**
     * Reads an integer value from the console.
     *
     * @param message Message displayed before input
     * @return Integer value entered by user
     * @throws UserCancelledException if user enters B to go back
     */
    public int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("B")) {
                throw new UserCancelledException(
                        "Operation cancelled. Returning to menu...");
            }

            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a number or B.");
            }
        }
    }


    /**
     * Reads a long value from the console.
     *
     * @param message Message displayed before input
     * @return Long value entered by user
     * @throws UserCancelledException if user enters B to go back
     */
    public Long readLong(String message) {

        while (true) {

            try {

                System.out.print(message);
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("B")) {
                    throw new UserCancelledException(
                            "Operation cancelled. Returning to menu..."
                    );
                }

                return Long.parseLong(input);

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input. Enter digits only or B to go back."
                );
            }
        }
    }
    /**
     * Reads a text value from the console.
     *
     * @param message Message displayed before input
     * @return User entered text
     * @throws UserCancelledException if user enters B to go back
     */
    public String readString(String message) {

        System.out.print(message);

        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("B")) {

            throw new UserCancelledException(
                    "Operation cancelled. Returning to menu...");
        }

        return input;
    }

    /**
     * Reads and validates a monetary amount.
     *
     * @param message Message displayed before input
     * @return Valid BigDecimal amount
     * @throws UserCancelledException if user enters B to go back
     */
    public BigDecimal readAmount(String message){

        while (true) {

            try {

                System.out.print(message);

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("B")) {

                    throw new UserCancelledException(
                            "Operation cancelled. Returning to menu..."
                    );
                }

                BigDecimal amount = new BigDecimal(input);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {

                    System.out.println(
                            "Amount must be greater than zero."
                    );
                    continue;
                }

                return amount;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid amount. Please enter a valid amount."
                );
            }
        }
    }

    /**
     * Reads and validates a date value.
     *
     * @param message Message displayed before input
     * @return Valid date entered by user
     * @throws UserCancelledException if user enters B to go back
     */
    public LocalDate readDate(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            // Allows user to go back
            if (input.equalsIgnoreCase("B")) {
                throw new UserCancelledException(
                        "Operation cancelled. Returning to menu..."
                );
            }

            try {

                LocalDate date = LocalDate.parse(
                        input,
                        DATE_FORMATTER
                );

                if (date.getYear() < 2000) {
                    System.out.println(
                            "Year should be 2000 or above."
                    );
                    continue;
                }

                if (date.isAfter(LocalDate.now())) {
                    System.out.println(
                            "Future dates are not allowed. Please enter today's date or an earlier date."
                    );
                    continue;
                }

                return date;

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid format! Use DD-MM-YYYY."
                );
            }
        }
    }

    /**
     * Reads and validates a month value.
     *
     * @param message Message displayed before input
     * @return Month number between 1 and 12
     */
    public int readMonth(String message){

        while(true){

            int month = readInt(message);

            if(month >=1 && month <=12){
                return month;
            }
            System.out.println(
                    "Month should be between 1 and 12"
            );
        }
    }


    /**
     * Reads and validates a year value.
     *
     * @param message Message displayed before input
     * @return Valid year
     */
    public int readYear(String message){

        while(true){

            int year = readInt(message);

            if(year >= 2000 && year <= LocalDate.now().getYear()){
                return year;
            }
            System.out.println(
                    "Year should be between 2000 and "
                            + LocalDate.now().getYear()
            );
        }
    }

    /**
     * Reads and validates person type selection.
     *
     * @return Selected person type
     * @throws UserCancelledException if user enters B to go back
     */
    public PersonType readPersonType() {

        while (true) {

            System.out.println("\nSelect Person Type:");

            System.out.println("1. MOTHER");
            System.out.println("2. BABY");

            String input = readString(
                    "Enter choice: "
            );

            try {

                int choice = Integer.parseInt(input);

                switch (choice) {

                    case 1 -> {
                        return PersonType.MOTHER;
                    }

                    case 2 -> {
                        return PersonType.BABY;
                    }

                    default -> System.out.println(
                            "Invalid choice. Please enter 1, 2 or B."
                    );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter 1, 2 or B."
                );
            }
        }
    }


    /**
     * Reads and validates expense category selection.
     *
     * @return Selected expense category
     * @throws UserCancelledException if user enters B to go back
     */
    public ExpenseType readExpenseType() {

        while (true) {

            System.out.println("\nSelect Expense Category:");

            ExpenseType[] types = ExpenseType.values();

            for (int i = 0; i < types.length; i++) {

                System.out.println(
                        (i + 1) + ". " + types[i]
                );
            }

            String input = readString(
                    "Enter choice: "
            );

            try {

                int choice = Integer.parseInt(input);

                if (choice > 0 && choice <= types.length) {
                    return types[choice - 1];
                }
                System.out.println(
                        "Invalid category. Please select available category number or B."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter category number or B."
                );
            }
        }
    }
    /**
     * Reads a yes/no choice from the user.
     *
     * @param message Message displayed before input
     * @return "Y" for yes and "N" for no
     */
    public String readYesNo(String message) {

        while (true) {

            System.out.print(message);

            String choice = scanner.nextLine()
                            .trim()
                            .toUpperCase();

            if (choice.equals("Y") || choice.equals("YES")) {
                return "Y";
            }
            if (choice.equals("N") || choice.equals("NO")) {
                return "N";
            }
            System.out.println("Invalid input. Please enter Y/N.");
        }
    }

    /**
     * Checks whether the user's input represents a positive confirmation.
     *
     * @param input user input
     * @return {@code true} if the input is "Y" or "YES"
     *         (case-insensitive), otherwise {@code false}
     */
    public boolean isYes(String input) {

        return input.equalsIgnoreCase("Y")
                || input.equalsIgnoreCase("YES");
    }

    /**
     * Checks whether a report file already exists and,
     * if necessary, asks the user whether it should be
     * overwritten.
     *
     * @param fileName name of the report file
     * @return {@code true} if the file does not exist or
     *         the user confirms overwriting; otherwise
     *         {@code false}
     */
    public boolean confirmOverwrite(String fileName) {

        File file = new File("reports", fileName);

        if (!file.exists()) {
            return true;
        }

        String choice = readYesNo(
                "Report already exists. Do you want to overwrite? (Y/N): ");

        return isYes(choice);
    }

    /**
     * Waits until the user presses Enter.
     */
    public void waitForEnter(String message) {

        System.out.print(message);

        scanner.nextLine();

    }



    /**
     * Closes scanner resources.
     */
    public void close(){

        scanner.close();

    }
}
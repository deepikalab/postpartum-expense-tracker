package com.deepika.postpartum.expensetracker.model;

/**
 * ExpenseType Enum.

 * Represents different categories of expenses
 * in the Postpartum Expense Tracker system.

 * Why use Enum?
 * - Provides a fixed set of constants
 * - Prevents invalid values
 * - Improves type safety
 * - Makes code more readable

 * Example Usage:

 * ExpenseType type = ExpenseType.MILK;

 * System.out.println(type);
 * System.out.println(type.getDisplayName());

 * Output:
 * MILK
 * Milk / Formula

 */
public enum ExpenseType {

    /**
     * Expense categories.

     * Each enum constant has:
     * - Internal name (MILK)
     * - Display name ("Milk / Formula")
     */

    MILK("Milk / Formula"),

    DIAPER("Diapers"),

    VACCINE("Vaccination"),

    DOCTOR("Doctor Consultation"),

    CLOTHING("Clothing"),

    MEDICINE("Medicine"),

    FOOD("Food"),

    MISCELLANEOUS("Miscellaneous");

    /**
     * User-friendly display name.
     */
    private final String displayName;

    /**
     * Parameterized enum constructor.

     * Called automatically for each enum constant.
     */
    ExpenseType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns user-friendly display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Overrides default toString().

     * Useful for:
     * - Printing
     * - UI display
     * - Logs
     */
    @Override
    public String toString() {
        return displayName;
    }

}
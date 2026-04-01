package com.deepika.postpartum.expensetracker.model;

/**
 * PersonType Enum.

 * Represents the person category associated
 * with an expense in the Postpartum Expense Tracker.

 * Example:
 * - MOTHER
 * - BABY

 * Why use Enum?
 * - Restricts values to predefined constants
 * - Prevents invalid input
 * - Improves code readability
 * - Provides type safety

 * Example Usage:

 * PersonType person = PersonType.MOTHER;

 * System.out.println(person);
 * System.out.println(person.getDisplayName());

 * Output:
 * Mother

 */
public enum PersonType {

    /**
     * Expense belongs to mother.
     */
    MOTHER("Mother"),

    /**
     * Expense belongs to baby.
     */
    BABY("Baby");

    /**
     * User-friendly display name.
     */
    private final String displayName;

    /**
     * Parameterized enum constructor.

     * Automatically called for each enum constant.
     */
    PersonType(String displayName) {
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
     * - Console display
     * - Reports
     * - UI menus
     */
    @Override
    public String toString() {
        return displayName;
    }

}
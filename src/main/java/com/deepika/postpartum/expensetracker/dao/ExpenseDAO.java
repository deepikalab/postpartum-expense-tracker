package com.deepika.postpartum.expensetracker.dao;
import static com.deepika.postpartum.expensetracker.dao.ExpenseQueries.*;
import com.deepika.postpartum.expensetracker.exceptions.ExpenseException;
import com.deepika.postpartum.expensetracker.exceptions.ExpenseNotFoundException;
import com.deepika.postpartum.expensetracker.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
/**
 * Data Access Object responsible for managing Expense database operations.
 *
 * Responsibilities:
 * - Executes SQL queries related to expenses
 * - Performs CRUD operations
 * - Converts database records into Expense objects
 * - Handles database exceptions and converts them into application exceptions
 *
 * This layer does not contain business rules or validation logic.
 * Business decisions are handled in the Service layer.
 */
public class ExpenseDAO {

    /**
     * Database connection used for executing SQL operations.
     */
    private final Connection connection;

    /**
     * Creates an ExpenseDAO using the provided database connection.
     *
     * @param connection active database connection
     */
    public ExpenseDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Saves a new expense record into the database.
     *
     * Uses generated keys to retrieve the auto-generated database ID
     * and updates the Expense object with the generated value.
     *
     * @param expense expense object connection containing data to persist
     * @return expense object with generated ID
     * @throws ExpenseException if database operation fails
     */
    public Expense addExpense(Expense expense) {
        /*
         * RETURN_GENERATED_KEYS allows retrieval of the ID generated
         * by the database AUTO_INCREMENT column.
         */
        try(PreparedStatement ps = connection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1,expense.getPersonType().name());
            ps.setString(2,expense.getExpenseType().name());
            ps.setBigDecimal(3,expense.getExpenseAmount());
            ps.setString(4,expense.getDescription());
            ps.setDate(5,Date.valueOf(expense.getExpenseDate()));

            ps.executeUpdate();

            /*
             * Retrieves the generated primary key after insertion
             * and updates the object with the database ID.
             */
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    expense.setId(rs.getLong(1));
                }
            }


        } catch (SQLException e) {

           throw new ExpenseException("Failed to insert expense",e);
        }
    return expense;
    }

    /**
     * Updates an existing expense record.
     *
     * The update is performed using the expense ID.
     * If no record exists with the given ID, an ExpenseNotFoundException
     * is thrown.
     *
     * @param expense updated expense details
     * @return updated expense object
     */
    public Expense updateExpense(Expense expense){
        try (
             PreparedStatement ps = connection.prepareStatement(UPDATE_SQL)){

            ps.setString(1,expense.getPersonType().name());
            ps.setString(2,expense.getExpenseType().name());
            ps.setBigDecimal(3,expense.getExpenseAmount());
            ps.setString(4,expense.getDescription());
            ps.setDate(5,Date.valueOf(expense.getExpenseDate()));
            ps.setLong(6,expense.getId());

            int rows = ps.executeUpdate();
            if(rows==0){
                throw new ExpenseNotFoundException("Expense not found with ID: " + expense.getId());

            }
            return expense;

        } catch (SQLException e) {
            throw new ExpenseException("Failed to update the expense",e);
        }

    }

    /**
     * Deletes an expense record using its ID.
     *
     * Validates the affected row count to ensure that
     * the requested expense existed before deletion.
     *
     * @param id expense identifier
     * @throws ExpenseNotFoundException if expense does not exist
     */
    public void deleteExpense(Long id){
        try (
             PreparedStatement ps = connection.prepareStatement(DELETE_SQL)){

            ps.setLong(1,id);

            int rows = ps.executeUpdate();

            if(rows==0){
                throw new ExpenseNotFoundException("Expense not found with ID: "+id);
            }

        } catch (SQLException e) {
            throw new ExpenseException("Failed to delete the expenses", e);
        }

    }

    /**
     * Retrieves all expense records from the database.
     *
     * Each database row is converted into an Expense object
     * using centralized mapping logic.
     *
     * @return list of all expenses
     */
    public List<Expense> getAllExpenses() {


        List <Expense> expenses = new ArrayList<>();


        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                // Mapping DB rows to Plain Old Java Objects (POJOs)
                expenses.add(mapRowToExpense(rs));
            }


        } catch (SQLException e) {
            throw new ExpenseException("Failed to fetch the expenses", e);
        }

        return expenses;
    }

    /**
     * Retrieves an expense using its unique identifier.
     *
     * @param id expense identifier
     * @return matching expense
     * @throws ExpenseNotFoundException if no expense is found
     */
    public Expense getExpenseById(Long id){

        try (
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID_SQL))
        {   ps.setLong(1,id);

            try(ResultSet rs = ps.executeQuery()){

                if(rs.next()){

                    return mapRowToExpense(rs);

                }else{
                    throw new ExpenseNotFoundException("Expense not found with ID: " + id);
                }
            }

        } catch (SQLException e) {
            throw new ExpenseException("Failed to fetch the expenses", e);
        }

    }

    /**
     * Retrieves expenses filtered by expense category.
     *
     * Converts the ExpenseType enum into its database string value
     * before executing the query.
     *
     * @param type expense category
     * @return list of matching expenses
     */
    public List<Expense> getExpensesByType(ExpenseType type){

        List<Expense> expenses = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_TYPE_SQL)) {

            // Converts the Java Enum to its String name for the SQL WHERE clause
            ps.setString(1, type.name());

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    expenses.add(mapRowToExpense(rs));
                }
            }
        }
        catch (SQLException e) {
            throw new ExpenseException("Failed to fetch the "+type+" expenses",e);
        }
        return expenses;
    }

    /**
     * Retrieves expenses filtered by person type.
     *
     * @param personType person category associated with expense
     * @return list of matching expenses
     */
    public List<Expense> getExpensesByPerson(PersonType personType){

        List<Expense> expenses = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_PERSON_SQL)) {

            // Converts Enum to String value stored in database
            ps.setString(
                    1,
                    personType.name());

            try(ResultSet rs = ps.executeQuery()){

                while(rs.next()){

                    expenses.add(
                            mapRowToExpense(rs)
                    );
                }
            }

        }
        catch(SQLException e){

            throw new ExpenseException(
                    "Failed to fetch the "
                            + personType
                            + " expenses",
                    e
            );
        }


        return expenses;
    }

    /**
     * Retrieves expenses recorded on a specific date.
     *
     * @param expenseDate date used for filtering
     * @return list of expenses for the given date
     */
    public List<Expense> getExpensesByDate(LocalDate expenseDate){
        List<Expense> expenses = new ArrayList<>();
    try(PreparedStatement ps = connection.prepareStatement(SELECT_BY_SINGLE_DATE_SQL)){
        //Converts LocalDate to SQL Date
        ps.setDate(1,Date.valueOf(expenseDate));

        try(ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               expenses.add(mapRowToExpense(rs));
            }

        }
        } catch (SQLException e) {
            throw new ExpenseException("Failed to fetch expenses for date: " + expenseDate,e);
        }

    return expenses;
    }

    /**
     * Retrieves expenses recorded for a specific month and year.
     *
     * @param month month number
     * @param year year value
     * @return list of expenses matching the month and year
     */
    public List<Expense> getExpensesByMonth(int month,int year){

        List<Expense> expenses = new ArrayList<>();

        try(PreparedStatement ps = connection.prepareStatement(SELECT_BY_MONTH_SQL)){

            ps.setInt(1,month);
            ps.setInt(2,year);

            try(ResultSet rs= ps.executeQuery()){
                while(rs.next()){
                    expenses.add(mapRowToExpense(rs));
                }
            }

        } catch (SQLException e) {
            throw new ExpenseException("Failed to expenses for "+ Month.of(month)+" "+year,e);
        }
        return expenses;
    }

    /**
     * Retrieves expenses between two dates.
     *
     * DAO returns an empty list when no records match.
     * Handling of empty results is decided by the Service layer.
     *
     * @param startDate starting date
     * @param endDate ending date
     * @return expenses within the given date range
     */
    public List<Expense> getExpensesByDateRange(LocalDate startDate,LocalDate endDate){
        List<Expense> expenses = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(SELECT_BY_DATE_RANGE_SQL)){

            ps.setDate(1,Date.valueOf(startDate));
            ps.setDate(2,Date.valueOf(endDate));

            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    //Mapping to POJO
                    expenses.add(mapRowToExpense(rs));
                }

            }

        } catch (SQLException e) {
            throw new ExpenseException("Failed to fetch expense between "+startDate+" and "+endDate,e);
        }
        return expenses;//DAO should return an empty list, NOT throw an exception, when no records are found.
    }

    /**
     * Calculates total expense amount for a specific month.
     *
     * Uses SQL aggregation instead of loading all records into memory.
     *
     * @param month month number
     * @param year year value
     * @return total expense amount
     */
   public BigDecimal getTotalMonthlyExpense(int month,int year){

       try (
            PreparedStatement ps = connection.prepareStatement(MONTHLY_TOTAL_SQL)){

           ps.setInt(1,month);
           ps.setInt(2,year);

           try(ResultSet rs= ps.executeQuery()){
               if(rs.next()){
                  BigDecimal totalMonthlyExpense =  rs.getBigDecimal("TotalMonthlyExpense");
                   // SQL SUM() returns NULL if no rows match; we convert that to 0 for Java
                   return (totalMonthlyExpense == null) ? BigDecimal.ZERO : totalMonthlyExpense;
               }
           }
           }catch (SQLException e) {
            throw new ExpenseException("Failed to fetch monthly total",e);
        }
        return BigDecimal.ZERO;
   }

    /**
     * Calculates total expense amount within a date range.
     *
     * @param startDate starting date
     * @param endDate ending date
     * @return total expense amount
     */
    public BigDecimal getTotalExpenseBetweenDates(LocalDate startDate,LocalDate endDate){
        try(
            PreparedStatement ps = connection.prepareStatement(DATE_RANGE_TOTAL_SQL)){

            ps.setDate(1,Date.valueOf(startDate));
            ps.setDate(2,Date.valueOf(endDate));

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    BigDecimal totalExpenseBetweenDates = rs.getBigDecimal("TotalExpenseDateRange");

                    // Safety check for nulls in aggregate functions
                    return (totalExpenseBetweenDates == null) ? BigDecimal.ZERO : totalExpenseBetweenDates;
                }
            }

        } catch (SQLException e) {
            throw new ExpenseException("Failed to fetch the total expense between "+startDate+" "+endDate,e);
        }
        return BigDecimal.ZERO;

    }

    /**
     * Retrieves monthly expense statistics including:
     * - Total amount
     * - Average expense
     * - Maximum expense
     * - Minimum expense
     * - Number of expenses
     *
     * Uses SQL aggregate functions for efficient calculation.
     *
     * @param month month number
     * @param year year value
     * @return monthly statistics object
     */
    public MonthlyExpenseStatistics getMonthlyExpenseStatistics(int month,int year) {
        try (
             PreparedStatement ps = connection.prepareStatement(MONTHLY_STATS_SQL)) {

            ps.setInt(1,month);
            ps.setInt(2,year);

           try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    // Handle NULL results from SQL (e.g., if a month has no data)
                    BigDecimal total = rs.getBigDecimal("total");
                    BigDecimal average = rs.getBigDecimal("average");
                    BigDecimal maximum = rs.getBigDecimal("maximum");
                    BigDecimal minimum = rs.getBigDecimal("minimum");
                    int count = rs.getInt("count");

                    if(total == null) total = BigDecimal.ZERO;
                    if(average == null) average =  BigDecimal.ZERO;
                    if(maximum == null) maximum = BigDecimal.ZERO;
                    if(minimum == null) minimum = BigDecimal.ZERO;

                    return new MonthlyExpenseStatistics(total,average,maximum,minimum,count);// Return new Monthly statistics Object

                }
           }
        } catch (SQLException e) {
            throw new ExpenseException("Failed to fetch Monthly Expense Statistics", e);
        }
        return new MonthlyExpenseStatistics(
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,0);
    }

    /**
     * Retrieves expenses sorted based on the selected sorting option.
     *
     * @param sortOption sorting criteria such as date or amount order
     * @return list of expenses sorted according to the option
     * @throws ExpenseException if sorting option is invalid or database operation fails
     */
    public List<Expense> sortExpenses(
            SortOption sortOption) {


        if (sortOption == null) {

            throw new ExpenseException(
                    "Sort option cannot be null.");
        }

        String sql;

        switch (sortOption) {

            case DATE_ASC ->
                    sql = SORT_DATE_ASC_SQL;

            case DATE_DESC ->
                    sql = SORT_DATE_DESC_SQL;

            case AMOUNT_ASC ->
                    sql = SORT_AMOUNT_ASC_SQL;

            case AMOUNT_DESC ->
                    sql = SORT_AMOUNT_DESC_SQL;

            default ->
                    throw new ExpenseException(
                            "Invalid sorting option.");
        }

        List<Expense> expenses = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                expenses.add(mapRowToExpense(rs));
            }

        } catch (SQLException e) {
            throw new ExpenseException(
                    "Failed to sort expenses.",
                    e);
        }

        return expenses;
    }
    /**
     * Converts a database ResultSet row into an Expense object.
     *
     * Centralizing mapping avoids duplicate conversion logic
     * across multiple DAO methods.
     *
     * @param rs database result row
     * @return mapped Expense object
     * @throws SQLException if database column access fails
     */

   private Expense mapRowToExpense(ResultSet rs)throws SQLException {

       // Creating NEW object for each row
       return new Expense(
               rs.getLong("id"),
               PersonType.valueOf(rs.getString("person_type")),
               ExpenseType.valueOf(rs.getString("expense_type")),
               rs.getBigDecimal("expense_amount"),
               rs.getString("description") == null
                       ? ""
                       : rs.getString("description"),
               rs.getDate("expense_date").toLocalDate()
       );
   }
}

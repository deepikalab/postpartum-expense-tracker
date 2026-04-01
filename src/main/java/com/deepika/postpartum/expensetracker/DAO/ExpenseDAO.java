package com.deepika.postpartum.expensetracker.DAO;

import com.deepika.postpartum.expensetracker.exceptions.ExpenseException;
import com.deepika.postpartum.expensetracker.exceptions.ExpenseNotFoundException;
import com.deepika.postpartum.expensetracker.exceptions.InvalidExpenseException;
import com.deepika.postpartum.expensetracker.model.Expense;
import com.deepika.postpartum.expensetracker.model.ExpenseType;
import com.deepika.postpartum.expensetracker.model.PersonType;
import com.deepika.postpartum.expensetracker.model.MonthlyExpenseStatistics;
import com.deepika.postpartum.expensetracker.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * Data Access Object (DAO) for the Expense table.
 * Encapsulates all Database interactions (CRUD + Statistics).

 * DAO Layer Responsibility:
 * - Handles all database operations
 * - Contains SQL queries
 * - Maps database rows to Java objects
 * - Does NOT contain business logic

 Note:
 * Transaction management should be handled at Service layer,
 * not inside DAO.
 */
public class ExpenseDAO {
    // --- SQL QUERIES (Centralized for easy maintenance) ---

    //Uses '?' as placeholders for PreparedStatements to prevent SQL Injection
    private static final String INSERT_SQL = "INSERT INTO expense " +
            "(person_type, expense_type, expense_amount, description, expense_date) " +
            "VALUES (?, ?, ?, ?, ?) ";

    private static final String UPDATE_SQL= "UPDATE expense SET person_type=?, expense_type=?, expense_amount=?, description=?, expense_date=? WHERE id=?";

    private static final String DELETE_SQL= "DELETE FROM expense WHERE id=?";

    private static final String SELECT_ALL_SQL= "SELECT * FROM expense";

    private static final String SELECT_BY_ID_SQL= "SELECT * FROM expense WHERE id = ?";

    private static final String SELECT_BY_SINGLE_DATE_SQL= "SELECT * FROM expense WHERE expense_date = ?";

    private static final String SELECT_BY_DATE_RANGE_SQL= "SELECT * FROM expense WHERE expense_date BETWEEN ? AND ? ORDER BY expense_date";

    private static final String FILTER_BY_TYPE_SQL= "SELECT * FROM expense WHERE expense_type = ? ORDER BY expense_date DESC";

    // Database functions like MONTH() and YEAR() allow us to filter by date parts directly in SQL
    private static final String MONTHLY_TOTAL_SQL= "SELECT SUM(expense_amount) AS TotalMonthlyExpense FROM expense WHERE MONTH(expense_date)=? AND YEAR(expense_date)=?";

    // Calculates total expense within a date range
    private static final String DATE_RANGE_TOTAL_SQL= "SELECT SUM(expense_amount) AS TotalExpenseDateRange FROM expense WHERE expense_date BETWEEN ? AND ?";

    // Aggregate functions (SUM, AVG, MAX, MIN, COUNT) perform calculations on the DB.
    private static final String MONTHLY_STATS_SQL = "SELECT SUM(expense_amount) AS total," +
            "AVG(expense_amount) AS average,"+
            "MAX(expense_amount) AS maximum," +
            "MIN(expense_amount) AS minimum," +
            "COUNT(*) AS count" +
            "FROM expense" +
            "WHERE MONTH(expense_date) = ? AND YEAR(expense_date) = ?";

    /**
     * Inserts a new Expense into the database and retrieves the auto-generated ID.
     */
    public Expense addExpense(Expense expense){
        // Statement.RETURN_GENERATED_KEYS allows us to get the ID created by the Database (AUTO_INCREMENT)
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)){

            // Mapping POJO fields to SQL parameters
            ps.setString(1,expense.getPersonType().name());// Converts Enum to String
            ps.setString(2,expense.getExpenseType().name());
            ps.setBigDecimal(3,expense.getExpenseAmount());
            ps.setString(4,expense.getDescription());
            ps.setDate(5,Date.valueOf(expense.getExpenseDate()));// Converts LocalDate to java.sql.Date

            ps.executeUpdate();

            // Retrieve the ID that the Database just assigned to this row
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    expense.setId(rs.getLong(1));
                }
            }


        } catch (SQLException e) {
           // Wrap low-level SQLExceptions into our application specific Exception
           throw new ExpenseException("Failed to insert expense",e);
        }
    return expense;
    }

    /**
     * Updates an existing record.
     * Throws ExpenseNotFoundException if the ID doesn't exist.
     */
    public Expense updateExpense(Expense expense){
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)){

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
     * Deletes an expense by ID.
     * Throws ExpenseNotFoundException if record does not exist.
     */
    public void deleteExpense(Long id){
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)){

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
     * Fetches all records and converts them into a List of Expense objects.
     */
    public List<Expense> getAllExpenses() {


        List <Expense> expenses = new ArrayList<>();


        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery();) {

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
     * Finds a single expense.
     * If not found, throws a custom ExpenseNotFound exception.
     */
    public Expense getExpenseById(Long id){

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL);
        )
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
     * Filters expenses based on their category (Enum).
     */
    public List<Expense> getExpenseByType(ExpenseType type){
        List<Expense> expenses = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(FILTER_BY_TYPE_SQL)) {

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
     * Retrieves expenses for a specific date.
     */
    public List<Expense> getExpenseByDate(LocalDate expenseDate){
        List<Expense> expenses = new ArrayList<>();
    try(Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(SELECT_BY_SINGLE_DATE_SQL);
        ){
        // Converts LocalDate to SQL Date
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
     * Retrieves expenses between two dates.
     * Returns empty list if no records are found.
     */
    public List<Expense> getExpenseByDateRange(LocalDate startDate,LocalDate endDate){
        List<Expense> expenses = new ArrayList<>();
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_BY_DATE_RANGE_SQL);
        ){
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
     * Calculates total expense for a specific month.
     */
   //For reading, no need to Pass Expense object only filtering parameters like id,dates etc
   public BigDecimal getTotalMonthlyExpense(int month,int year){

       try (Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(MONTHLY_TOTAL_SQL)){

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
     * Calculates total expense between two dates.
     */
    public BigDecimal getTotalExpenseBetweenDates(LocalDate startDate,LocalDate endDate){
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(DATE_RANGE_TOTAL_SQL)){

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
     * Calculates monthly statistics:
     * total, average, maximum, minimum, count.
     */
    public MonthlyExpenseStatistics getMonthlyExpenseStatistics(int month,int year) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(MONTHLY_STATS_SQL)) {

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
        return new MonthlyExpenseStatistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,0);
    }
    /**
     * Centralized mapping logic to convert ResultSet row into Expense object.
     */
   private Expense mapRowToExpense(ResultSet rs)throws SQLException{
       // Creating NEW object for each row
       Expense expense = new Expense();
       expense.setId(rs.getLong("id"));
       expense.setPersonType(PersonType.valueOf(rs.getString("person_type")));
       expense.setExpenseType(ExpenseType.valueOf(rs.getString("expense_type")));
       expense.setExpenseAmount(rs.getBigDecimal("expense_amount"));
       expense.setDescription(rs.getString("description"));
       expense.setExpenseDate(rs.getDate("expense_date").toLocalDate());
       return expense;
   }
}

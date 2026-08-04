# Postpartum Expense Tracker
                                                                                                                                                                                                                                  
A Java-based console application designed to manage, track, and analyze postpartum-related expenses efficiently.
                                                                                                                                                                                                                                  
The application helps parents manage expenses incurred for the mother and baby during the postpartum period, including medical care, vaccinations, diapers, milk/formula, clothing, medicines, food, and other essential expenses.
                                                                                                                                                                                                                                  
Users can record daily expenses, categorize spending, search and sort expenses, monitor monthly budgets, receive budget alerts while adding, updating, or modifying expenses, generate expense reports, export reports to CSV, and analyze spending patterns through monthly totals and statistics.
                                                                                                                                                              
                                                                                                                                                               The application follows a clean layered architecture with clear separation of responsibilities between the UI, Service, DAO, Report, Model, Configuration, Utility, and Database layers, demonstrating Core Java, JDBC, object-oriented programming, exception handling, input validation, and clean coding practices.                                                                                                                                                                                                                         
---

# 📌 Project Overview

Postpartum Expense Tracker was developed to demonstrate backend development concepts using Core Java and JDBC by building a real-world console application for managing postpartum expenses for the mother and baby.

The application focuses on:

- Object-Oriented Programming (OOP) principles
- Layered architecture design
- Database connectivity using JDBC
- Expense and budget management
- Budget status monitoring and budget alerts
- Custom exception handling and user cancellation workflows
- Input validation and formatted console output
- Expense analytics and reporting
- File handling and CSV report generation
- Expense sorting using enums
- Clean code practices

---

# 🏗️ Application Architecture

The project follows a layered architecture:

```text
                  User
                    │
                    ▼
         Console UI Layer (app, ui)
                    │
                    ▼
             Service Layer
    (Business Logic & Validation)
            │              │
            │              ▼
            │        Report Layer
            │     (Report & CSV Export)
            ▼
              DAO Layer
      (Database Communication)
                  │
                  ▼
             MySQL Database

             ExpenseQueries
                   │
                   ▼
       Centralized SQL Management
```

---

# ✨ Features

## 1. Expense Management

Users can perform complete expense management:

✅ Add expense
✅ View all expenses
✅ Update expense
✅ Patch update specific fields
✅ Delete expense with confirmation
✅ Automatic budget alert after expense modifications (when a monthly budget exists)

---

## 2. Expense Search

Expenses can be searched using:

- Expense ID
- Expense Category
- Person Type
- Specific Date
- Date Range


---

## 3. Expense Analytics

The application provides spending analysis through:

### Monthly Total

Calculates total expenses for a selected month and year.

### Date Range Total

Calculates total spending between two dates.

### Monthly Statistics

Provides:

- Total expense
- Average expense
- Maximum expense
- Minimum expense
- Number of expenses


---

## 4. Expense Reports

Reports display
- Expense details
- Total number of expenses
- Total amount spent

Generated reports can optionally be exported as CSV files.

### Category Report

Example:

```
MEDICAL expenses
```

### Monthly Report

Example:

```
July 2026 Expense Report
```

### Date Range Report

Example:

```
01-07-2026 to 31-07-2026
```


Reports display:

- Expense details
- Total number of expenses
- Total amount spent


---

## 5. CSV Export

Generated reports can optionally be saved as CSV files.

Reports are stored inside:

```
reports/
```

Example:

```
reports/
 |
 ├── monthly-report-7-2026.csv
 ├── category-report-MEDICAL.csv
 └── date-range-report.csv
```


---

## 6. Budget Management

    The application supports monthly budget tracking and helps 
    users monitor their spending against the configured budget limit.

Users can:

    ✅ Set monthly budget
    ✅ Update existing monthly budget
    ✅ View budget status
    ✅ Delete monthly budget
    ✅ Track percentage of budget utilized
    ✅ Receive budget alerts automatically after expense changes

### Budget Status
    
    Budget status includes:
    
    - Budget amount
    - Total spent
    - Remaining or exceeded amount
    - Percentage of budget utilized

Example:

```
===== BUDGET STATUS =====
Budget : ₹10000.00
Spent : ₹7500.00
Remaining : ₹2500.00
Used : 75.00%
```
---  
### Budget Alerts  
    The application automatically checks budget usage whenever an expense is:
    
    - Added
    - Updated
    - Patched
    - Deleted
    
 Budget alerts are displayed based on the percentage of the monthly budget utilized:
  
  - Warning when 80% or more of the budget is used
  - Notification when 100% of the budget is used
  - Alert when spending exceeds the configured budget

Example: 

  #### Budget Warning:
```
===== BUDGET ALERT =====
Warning: You have used 80% of your budget.
```
 #### Budget Fully Used: 
``` 
===== BUDGET ALERT =====
 Budget Fully Used!
```
 #### Budget exceeded:
```
===== BUDGET ALERT =====
 Budget exceeded!
``` 
If no budget is configured for the selected month:h:

 - Expense operations (Add, Update, Patch, and Delete) continue normally.  
 - No budget alert is displayed.     
 - Budget-related operations (View Budget Status and Delete Budget) require an existing monthly budget.

## 7. Expense Sorting
           
 The application allows expenses to be sorted using predefined sorting options.    
                                                                                   
 Supported sorting options:                                                        
                                                                                   
 - Date (Newest to Oldest)                                                         
 - Date (Oldest to Newest)                                                         
 - Amount (Highest to Lowest)                                                      
 - Amount (Lowest to Highest)                                                      
                                                                                   
 Sorting is implemented using the **SortOption** enum.                             
                                                                                                                                                              
 ---
 
 ## 8. Console Navigation
 
 The application provides convenient console navigation to improve the user experience.
 
 Features include:
 
 - Press **B** at any input prompt to return to the previous menu
 - Confirmation prompts before deleting expenses
 - Success and error messages for all operations
 - Consistent menu-driven interface
 
 ---

# 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| Java 17 | Application development |
| JDBC | Database connectivity |
| MySQL | Data storage |
| Maven | Dependency management |
| Git | Version control |

---

# 📂 Project Structure

```text
src
└── main
    └── java
        └── com.deepika.postpartum.expensetracker
            │
            ├── app
            │   └── ExpenseTrackerApp.java
            │
            ├── config
            │   └── ApplicationConfig.java
            │
            ├── dao
            │   ├── BudgetDAO.java
            │   ├── ExpenseDAO.java
            │   └── ExpenseQueries.java
            │
            ├── exceptions
            │   ├── ExpenseException.java
            │   ├── ExpenseNotFoundException.java
            │   ├── InvalidBudgetException.java
            │   ├── InvalidExpenseException.java
            │   └── UserCancelledException.java
            │
            ├── model
            │   ├── Budget.java
            │   ├── BudgetStatus.java
            │   ├── Expense.java
            │   ├── ExpenseType.java
            │   ├── MonthlyExpenseStatistics.java
            │   ├── PersonType.java
            │   └── SortOption.java
            │
            ├── report
            │   ├── ExpenseReportGenerator.java
            │   └── ReportCsvWriter.java
            │
            ├── service
            │   ├── BudgetService.java
            │   ├── BudgetServiceImpl.java
            │   ├── ExpenseService.java
            │   └── ExpenseServiceImpl.java
            │
            ├── ui
            │   ├── ConsoleFormatter.java
            │   ├── ConsoleInputHandler.java
            │   └── MenuPrinter.java
            │
            └── util
                └── DBConnection.java
```
---

# 🧩 Design Principles Followed

The application follows a layered architecture where each layer has a well-defined responsibility. This separation improves maintainability, readability, scalability, and testability.

---

## Layer Overview

| Layer | Packages | Purpose |
|--------|----------|---------|
| UI Layer | `app`, `ui` | Handles user interaction, navigation, input, and output |
| Service Layer | `service` | Implements business logic, validations, analytics, and budget management |
| DAO Layer | `dao` | Performs database operations using JDBC |
| Model Layer | `model` | Represents domain objects and application enums |
| Report Layer | `report` | Generates expense reports and exports them to CSV files |
| Configuration Layer | `config` | Configures and initializes application components |
| Utility Layer | `util` | Provides reusable utilities such as database connection management |
| Exception Layer | `exceptions` | Defines custom exceptions for consistent error handling |

---

## Separation of Concerns

Each layer has a specific responsibility and communicates only with the appropriate layer.

```
User
   │
   ▼
UI Layer
   │
   ▼
Service Layer
   │
   ▼
DAO Layer
   │
   ▼
MySQL Database
```

---

## UI Layer (`app`, `ui`)

Handles user interaction through menus, input collection, navigation, confirmation prompts, and displaying results without containing business or database logic.

### Responsible for

- Displaying menus
- Reading and validating user input
- Supporting navigation using **B** to return to the previous menu
- Displaying success, warning, and error messages
- Showing formatted output and reports

### Does not contain

- Business logic
- Database operations

---

## Service Layer (`service`)

Contains application business rules, validations, exception handling, and coordinates communication between the UI and DAO layers.

### Responsible for

- Business rules
- Input validation
- Expense management
- Budget management
- Budget alert generation
- Expense analytics and statistics
- Coordinating DAO operations

---

## DAO Layer (`dao`)

Manages database operations by executing SQL queries, communicating with the database, and converting database records into Java objects.

### Responsible for

- SQL execution
- Database communication
- ResultSet mapping
- Expense CRUD operations
- Budget CRUD operations

---

## Model Layer (`model`)

Represents the application's domain objects and enums used throughout the application.

### Responsible for

- Expense information
- Monthly budget information
- Budget status
- Monthly expense statistics
- Person types
- Expense categories
- Sorting options

---

## Report Layer (`report`)

Responsible for generating expense reports and exporting them into CSV files.

### Responsible for

- Category reports
- Monthly reports
- Date range reports
- CSV export

---

## Configuration Layer (`config`)

Creates and configures application components.

### Responsible for

- Initializing services
- Managing object creation
- Wiring application dependencies

---

## Utility Layer (`util`)

Provides reusable helper classes used across the application.

### Responsible for

- Database connection management
- Common utility functionality

---

## Exception Handling (`exceptions`)

Contains custom exceptions to provide
meaningful error handling throughout the application.

Custom exceptions are implemented:


```
Exception
│
├── ExpenseException
│   ├── ExpenseNotFoundExceptionundException
│   │      • Thrown when the requested expense cannot be found.
│   │
│   ├── InvalidExpenseException
│   │      • Thrown when expense data is invalid, such as:            
│   │        - Invalid expense IDseException 
│   │        - Invalid amount
│   │        - Invalid month or year
│   │        - Future dates
│   │        - Invalid date range
│   │        - Missing required fields
│   │        - Invalid person type or expense category
│   │
│   └── InvalidBudgetException
│          • Thrown when budget data is invalid, such as:
│            - Invalid budget amount
│            - Invalid budget month or year
│            - Budget not found for the selected month
│
└── UserCancelledException
       • Thrown when the user chooses to cancel an operation
         (by entering 'B') and return to the main menu.
```
---

# ✅ Validations Implemented

The application validates:

✔ Expense cannot be null  
✔ Person type is mandatory   
✔ Expense category is mandatory  
✔ Expense amount cannot be null   
✔ Expense amount must be greater than zero   
✔ Expense date cannot be null   
✔ Future expense dates are not allowed   
✔ Start date cannot be after end date   
✔ Date range cannot contain null dates   
✔ Month must be between 1 and 12   
✔ Year must be between 2000 and the current year    
✔ Budget amount cannot be null            
✔ Budget amount must be greater than zero  
✔ Invalid menu options are handled      
✔ Invalid expense IDs are rejected      
✔ Invalid budget operations are handled

---

# 🗄️ Database Design

Database:

```text
postpartum_tracker
```

The application uses two tables:

- `expense` – Stores individual expense records.
- `budget` – Stores the monthly budget for expense tracking.

---

## Expense Table

| Column | Type |
|--------|------|
| id | BIGINT PRIMARY KEY AUTO_INCREMENT |
| person_type | VARCHAR(50) |
| expense_type | VARCHAR(50) |
| expense_amount | DECIMAL(10,2) |
| description | VARCHAR(255) |
| expense_date | DATE |

---

## Budget Table

| Column | Type |
|--------|------|
| id | BIGINT PRIMARY KEY AUTO_INCREMENT |
| month | INT |
| year | INT |
| budget_amount | DECIMAL(10,2) |

**Constraint**

A unique constraint is applied on `(month, year)` to ensure only one budget can exist for a particular month and year.

---

## Relationship Between Tables

The `expense` and `budget` tables are **logically related** through the **month** and **year** values.

The application extracts the month and year from the `expense_date` column in the `expense` table and compares the total monthly expenses with the corresponding budget stored in the `budget` table.

```text
                Expense Table
        ┌──────────────────────────┐
        │ expense_date             │
        │ expense_amount           │
        └─────────────┬────────────┘
                      │
      MONTH(expense_date)
      YEAR(expense_date)
                      │
                      ▼
               Budget Table
        ┌──────────────────────────┐
        │ month                    │
        │ year                     │
        │ budget_amount            │
        └─────────────┬────────────┘
                      │
                      ▼
             BudgetService Layer
                      │
                      ▼
         • Total Monthly Expense
         • Remaining Budget
         • Budget Utilization
         • Budget Alerts

```

No foreign key relationship is required because a single monthly budget is shared by all expenses recorded within that month.

---

## Enum Storage

Enums are stored as **String** values in the database.

### PersonType

```text
MOTHER
BABY
```

### ExpenseType

```text
MILK
DIAPER
VACCINE
DOCTOR
CLOTHING
MEDICINE
FOOD
MISCELLANEOUS

```
### SortOption

````text

DATE_ASC
DATE_DESC
AMOUNT_ASC
AMOUNT_DESC

````
---

# ▶️ How to Run the Application

## 1. Prerequisites

Install:

- Java 17+
- MySQL
- Maven


Verify Java:

```bash
java -version
```


Verify Maven:

```bash
mvn -version
```


---

# 2. Clone Repository

```bash
git clone <repository-url>
```

Navigate:

```bash
cd postpartum-expense-tracker
```


---

# 3. Setup Database

Open MySQL.

Create database:

```sql
CREATE DATABASE postpartum_tracker;
```


Select database:

```sql
USE postpartum_tracker;
```


Create Expense table:

```sql
CREATE TABLE expense (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    person_type VARCHAR(50) NOT NULL,

    expense_type VARCHAR(50) NOT NULL,

    expense_amount DECIMAL(10,2) NOT NULL,

    description VARCHAR(255),

    expense_date DATE NOT NULL

);
```
 Create Budget table:     
                           
 ```sql                                                                                               
 CREATE TABLE budget (                                           
                                                                 
     id BIGINT AUTO_INCREMENT PRIMARY KEY,                       
                                                                 
     month INT NOT NULL,                                         
                                                                 
     year INT NOT NULL,                                          
                                                                 
     budget_amount DECIMAL(10,2) NOT NULL,                       
                                                                 
     CONSTRAINT unique_month_year UNIQUE (month, year)           
                                                                 
 );                                                              
        
   ```
---

# 4. Configure Database Connection

Open:

```
DBConnection.java
```


Update:

```java
private static final String URL =
        "jdbc:mysql://localhost:3306/postpartum_tracker";

private static final String USER =
        "your_username";

private static final String PASSWORD =
        "your_password";
```


Replace with your MySQL credentials.


---

# 5. Build Project

Run:

```bash
mvn clean install
```


---

# 6. Run Application


## Using IntelliJ IDEA

1. Open project
2. Load Maven dependencies
3. Open:

```
ExpenseTrackerApp.java
```

4. Right click
5. Select:

```
Run ExpenseTrackerApp.main()
```


---

## Using Command Line

Compile:

```bash
mvn compile
```


Run:

```bash
java -cp target/classes com.deepika.postpartum.expensetracker.app.ExpenseTrackerApp
```


---

# 🖥️ Application Menu

```
========== Expense Tracker ==========

----- Expense Management -----

1. Add Expense
2. Update Expense
3. Patch Expense
4. Delete Expense
5. View All Expenses


----- Search Expenses -----

6. Search Expense by ID
7. Search Expense by Category
8. Search Expense by Person
9. Search Expense by Date
10. Search Expense by Date Range


----- Summary -----

11. Monthly Total
12. Date Based Total
13. Monthly Statistics


----- Reports -----

14. Generate Category Report
15. Generate Monthly Report
16. Generate Date Range Report


----- Budget -----

17. Set Monthly Budget
18. View Budget Status
19. Delete Budget

----- Utilities -----

20. Sort Expenses


21. Exit
```

---

# 📝 Example Usage

## Add Expense

Select:

```
1. Add Expense
```

Enter:

```
Person Type:
MOTHER

Category:
MEDICAL

Amount:
1500

Description:
Doctor consultation

Date:
29-07-2026
```


Expense will be stored in MySQL.


---

## Generate Monthly Report

Select:

```
15. Generate Monthly Report
```

Enter:

```
Month:
7

Year:
2026
```


Application displays the report.

It will ask:

```
Do you want to save this report as CSV?(Y/N)
```


Select:

```
Y
```


CSV file will be created:

```
reports/
```
---

## Set Monthly Budget

Select:

```
17. Set Monthly Budget
```

Enter:

```
Month:
7

Year:
2026

Budget Amount:
10000
```

The application stores the monthly budget.

Whenever expenses are added, updated, patched, or deleted for that month, the application automatically checks the budget and displays an alert when applicable.

---

## View Budget Status

Select:

```
18. View Budget Status
```

Example Output:

```
===== BUDGET STATUS =====

Budget     : ₹10000.00
Spent      : ₹7500.00
Remaining  : ₹2500.00
Used       : 75.00%
```

---

# 🚨 Troubleshooting

## Database Connection Error

Check:

- MySQL server is running
- Database name is correct
- Username/password are correct
- JDBC URL is correct


---

## Maven Build Failure

Run:

```bash
mvn clean install -U
```


---

## Empty Report

Reports require existing expenses.

Add expenses before generating reports.


## No Budget Alert Displayed

Budget alerts are shown only when a monthly budget has been configured for the selected month and year.

If no budget exists:

- Expense operations continue normally.
- No budget alert is displayed.

---

## CSV File Not Generated

Ensure that:

- The `reports` directory is writable.
- You selected **Y** when prompted to save the report.

---

# 🔮 Future Enhancements

Possible improvements:

- Spring Boot REST API version
- Spring Data JPA integration
- User authentication and authorization
- Web-based dashboard
- Charts and expense visualization
- Email notifications for budget alerts
- Cloud database deployment
- Docker deployment
- Comprehensive unit and integration testing
- REST API documentation using Swagger/OpenAPI

---

# 👩‍💻 Author

**Deepika V S**

Java Backend Developer

**Technologies:** Java • JDBC • MySQL • Spring Boot

This project was developed as a backend console application to demonstrate object-oriented programming, layered architecture, JDBC-based database interaction, budget management, report generation, and clean coding practices.

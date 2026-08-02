-- =====================================================
-- Database: Postpartum Expense Tracker
-- Author: Deepika
-- Description: Database schema for the Postpartum Expense Tracker application.
-- =====================================================

-- Create Database
CREATE DATABASE IF NOT EXISTS postpartum_expense_tracker;

-- Select Database
USE postpartum_expense_tracker;

-- =====================================================
-- Table: expense
-- Stores postpartum expenses for mother and baby care.
-- =====================================================

CREATE TABLE expense (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    person_type VARCHAR(50) NOT NULL,
    expense_type VARCHAR(50) NOT NULL,
    expense_amount DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    expense_date DATE NOT NULL
);

-- =====================================================
-- Table: budget
-- Stores monthly budget information.
-- Only one budget is allowed per month and year.
-- =====================================================

CREATE TABLE budget (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    month INT NOT NULL,
    year INT NOT NULL,
    budget_amount DECIMAL(10,2) NOT NULL,
    CONSTRAINT unique_month_year UNIQUE (month, year)
);

-- =====================================================
-- Sample Data (Optional)
-- Uncomment if you want initial records.
-- =====================================================

/*
INSERT INTO expense
(person_type, expense_type, expense_amount, description, expense_date)
VALUES
('Mother', 'Medicine', 1200.00, 'Iron tablets', '2026-07-01'),
('Baby', 'Diapers', 850.00, 'Monthly diaper pack', '2026-07-02');

INSERT INTO budget
(month, year, budget_amount)
VALUES
(7, 2026, 15000.00);
*/
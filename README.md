# expense-tracker
A console-based [expense-tracker](https://github.com/Lerghis/expense_tracker)Java application for recording, managing, and analyzing personal expenses. 
The project demonstrates practical data persistence, robust input handling, simple reporting features, and CSV-based storage without external 
libraries.

---

## **Features**

- Add, view, update, and delete expenses
- Automatic unique ID assignment for each expense
- CSV persistence for all expenses
- Monthly budget system stored separately in budget.csv
- Warnings when monthly spending exceeds the set budget
- Expense summaries:
- Total summary
- Summary for a specific month
- Summary by category
- Expense filtering by category
- Full exception handling for safe user input
- Clean file structure using a dedicated data directory

---

## **Program Structure**

The application follows a modular method design. Each method handles a single responsibility for clarity and maintainability.

Typical call flow:

main()
→ Menu()
→ addExpense()
→ viewExpenses()
→ updateExpense()
→ deleteExpense()
→ showExpenseSummary()
→ showExpenseSummaryForSpecificMonth()
→ viewExpensesByCategory()
→ setMonthlyBudget()
→ viewBudgetStatus()

Storage responsibilities
Storage()
→ createDataDirectory()
→ createExpenseFile()
→ createBudgetFile()
→ loadExpenses()
→ loadBudget()

addExpense()
updateExpense()
saveExpenses()
saveBudget()

This ensures the program always initializes its data correctly and persists changes reliably.

## **Input Handling & Exception Logic**

- All user input is read with keyboard.nextLine()
- Manual parsing via Integer.parseInt() / Double.parseDouble()
- Defensive try/catch blocks to prevent crashes
- Invalid input (letters, empty strings, negative values) is safely rejected
- No mixed nextInt() or nextDouble(), so no leftover newlines

This guarantees stable behavior across all menus and actions.

## **Data Persistence**

Expenses
- Stored in a CSV file inside the data directory
- Each line contains:
id,name,amount,date,category

Monthly Budget
- Stored in budget.csv
- The first line contains a single numeric budget value
- Loaded at startup and saved whenever changed

Expense Reporting
1. Total Summary
- Counts all expenses and sums the total amount.

2. Summary for Specific Month
- User selects a month (1–12)
- Only expenses from the current year and that month is included

3. Category Summary
- Lists all expenses that match the selected category, then prints totals.

4. Budget Status

Shows:
- Monthly budget
- Total spent in the current month
- Remaining or exceeded amount

## **How to Run**

1. Clone the repository:

```bash
git clone https://github.com/Lerghis/number_guessing_game.git
cd number_guessing_game
```

2. Compile the program:

```bash
javac Main.java
```

3. Run the program:

```bash
java Main
```

4. Follow the on-screen menu to add expenses, set budgets, filter reports, and manage data.
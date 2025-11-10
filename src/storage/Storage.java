package storage;

import model.Expense;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Storage
{
    final String dirPath = "csv_data";
    final String expenseList = "expenses.csv";
    private final List<Expense> expenses;
    Scanner keyboard;

    /**
     * The constructor of this class, it is used to initialize the expenses list
     */
    public Storage()
    {
        expenses = new ArrayList<>();
        keyboard = new Scanner(System.in);

        createDataDirectory();
        createExpenseFile();
        loadExpenses();
    }

    /**
     * Creates the application data directory if it does not already exist.
     * The directory path is defined by {@code dirPath}.
     * This method ensures that the application has a proper storage location
     * for task files. If the directory is successfully created, the path is printed.
     * If it already exists, a message indicating that is displayed.
     */
    public void createDataDirectory()
    {
        File directory = new File(dirPath);
        if (!directory.exists())
        {
            if (directory.mkdirs())
                System.out.println("Directory created: " + directory.getAbsolutePath());
        }
        else
        {
            System.out.println("Directory already exists: " + directory.getAbsolutePath());
        }
    }

    /**
     * Creates the CSV file used to store tasks inside the application data directory.
     * If the file already exists, the method prints a message instead of overwriting it.
     * In case of an I/O error, the exception stack trace is displayed.
     */
    public void createExpenseFile()
    {
        try
        {
            File obj = new File(dirPath, expenseList);

            // creating the file
            if (obj.createNewFile())
            {
                System.out.println("File created: " + obj.getAbsolutePath());
            }
            else
            {
                System.out.println("File already exists: " + obj.getAbsolutePath());
            }
        }
        catch (IOException ex)
        {
            System.out.println("An error has occured while creating the File.");
            ex.printStackTrace();
        }
    }

    /**
     * Adds a new expense to the list.
     * This method interacts with the user via the console to:
     *
     *  Enter a non-empty expense description
     *  Enter a valid expense amount
     *  Optionally provide a date for when the expense occur
     *
     * After collecting input, an Expense object is created, added to the
     * internal list, expenses id number is generated automatically.
     */
    public void addExpense()
    {
        // === Expense Description ===
        System.out.println("\n--------------------");
        System.out.print("Enter expense name: ");
        String name = keyboard.nextLine().trim();
        while(name.isEmpty())
        {
            System.out.println("Description cannot be empty.");
            System.out.print("Enter again: ");
            name = keyboard.nextLine().trim();
        }

        // === Expense Amount ===
        double amount = 0;
        do
        {
            try
            {
                System.out.println("--------------");
                System.out.print("Enter amount: ");
                String input = keyboard.nextLine().trim();
                amount = Double.parseDouble(input);
            }
            catch (NumberFormatException ex1)
            {
                System.out.println("Amount must be a number. Please try again.");
            }
            catch (InputMismatchException ex2)
            {
                System.out.println("Please enter a valid number.");
                keyboard.nextLine();
            }
        }
        while (amount <= 0);

        // === Expense Date ===
        System.out.println("-------------------------------------------------------------------------------");
        System.out.print("Enter the date of the expense (yyyy-mm-dd) or leave empty to default to today: ");
        String dateOfExpenseInput = keyboard.nextLine().trim();

        LocalDate dateOfExpense;
        if (dateOfExpenseInput.isEmpty())
        {
            dateOfExpense = LocalDate.now(); // default to today
        }
        else
        {
            try
            {
                dateOfExpense = LocalDate.parse(dateOfExpenseInput);
            }
            catch (DateTimeParseException ex)
            {
                System.out.println("Invalid date format. Using today's date instead.");
                dateOfExpense = LocalDate.now();
            }
        }

        // === Expense Category ===
        System.out.println("-----------------------");
        System.out.print("Enter expense category: ");
        String category = keyboard.nextLine().trim();
        while(category.isEmpty())
        {
            System.out.println("Category cannot be empty.");
            System.out.print("Enter again: ");
            category = keyboard.nextLine().trim();
        }

        // === Create Expense ===
        Expense tmp = new Expense(name, amount, dateOfExpense, category);
        expenses.add(tmp);
        System.out.println("\n--------------------------");
        System.out.println("Expense added successfully (" + tmp.getId() + ").");
        saveExpenses();

        Pause();
    }

    /**
     * It is used to update expenses by calling the updateExpenseData method.
     * Prints a confirmation message to the user and upon a positive response proceeds to edit the expense.
     */
    public void updateExpense()
    {
        System.out.println("\n\n    Update Expenses.");
        System.out.println("==============================");

        int i = 1;
        for (Expense expense: expenses)
        {
            System.out.println();
            System.out.println((i++) + ". " + expense.toString());
        }

        if (expenses.isEmpty())
        {
            System.out.println("\nThere are no expenses.");
            return; // return to the main menu
        }

        boolean validInput = false;
        int expenseIndex = -1; // we initialize with -1 to show that the choice has yet to be made and because the index numbers in Arrays start from 0

        do
        {
            System.out.print("\nEnter the number of the expense you want to update (or enter 0 to cancel): ");

            try
            {
                String userInputIndex = keyboard.nextLine().trim();
                expenseIndex = Integer.parseInt(userInputIndex);

                if (expenseIndex == 0)
                    return;
                expenseIndex--;
                if (expenseIndex >= 0 && expenseIndex < expenses.size())
                    validInput = true;
                else
                    System.out.println("\nInvalid choice. Please enter a number of an existing expense.");
            }
            catch (NumberFormatException ex)
            {
                System.out.println("\nInvalid choice. Please enter a number.");
            }
        }
        while (!validInput);

        // === Updating the expense ===
        Expense tmp = expenses.get(expenseIndex);

        // View details of the selected expense
        viewExpenseDetails(tmp);

        System.out.print("\nAre you sure you want to edit this expense? [Y/N]: ");
        String editChoice = keyboard.nextLine().trim();
        if (editChoice.equals("Y") || editChoice.equals("y"))
        {
            updateExpenseData(tmp);
            saveExpenses();
            System.out.println("\nThe expense was updated successfully.");
        }
        else
            System.out.println("\nUpdating was canceled.");

        Pause();
    }

    /**
     * Allows the editing of an expense's information.
     * @param editing the expense object to be edited
     * @return always returns true indicating that the editing was completed successfully
     */
    public boolean updateExpenseData(Expense editing)
    {
        System.out.println("\nTo keep the current value simply press Enter.\n");

        // === Update Description ===
        System.out.print("New description (" + editing.getName() + "): ");
        String newDescription = keyboard.nextLine().trim();
        if (!newDescription.isBlank())
            editing.setName(newDescription);

        // === Update Amount ===
        boolean validInput = false;
        do
        {
            try
            {
                System.out.print("New amount (" + editing.getAmount() + "): ");
                String newAmount = keyboard.nextLine().trim();
                if (newAmount.isEmpty())
                {
                    validInput = true;
                }
                else
                {
                    editing.setAmount(Double.parseDouble(newAmount));
                    validInput = true;
                }

            }
            catch (NumberFormatException ex)
            {
                System.out.println("\nOnly numbers are allowed. Please try again.");
            }
        }
        while (!validInput);

        // === Update Date ===
        System.out.print("New date (" + editing.getDateOfExpense() + "): ");
        String newDateInput = keyboard.nextLine().trim();

        if (!newDateInput.isEmpty())
        {
            try
            {
                LocalDate newDate = LocalDate.parse(newDateInput);
                editing.setDateOfExpense(newDate);
            }
            catch (DateTimeParseException ex)
            {
                System.out.println("\nInvalid date format. Keeping previous date.");
            }
        }

        return true;
    }

    /**
     * Deletes an expense from the list by user selection.
     *
     * Prompts the user to choose an expense number, validates the input, and then
     * deletes the corresponding expense. After deletion, the updated list is saved
     * back to the file system. If the input is invalid, the user is asked again.
     */
    public void deleteExpense()
    {
        if (expenses.isEmpty())
        {
            System.out.println("No expenses to delete.");
            return;
        }

        int choice = -1;
        do
        {
            System.out.println("\nSelect the number of the expense you want to delete");
            System.out.println("=========================================================================");
            for (int i = 0; i < expenses.size(); i++)
            {
                System.out.println((i + 1) + ". " + expenses.get(i));
            }

            System.out.println("==========================================================================");
            System.out.print("\nEnter the number of the expense you want to delete: ");
            String input = keyboard.nextLine().trim();

            try
            {
                choice = Integer.parseInt(input);
                if (choice > 0 && choice <= expenses.size())
                {
                    expenses.remove(choice -1);
                    System.out.println("\nExpense deleted successfully!");
                    saveExpenses();
                }
                else
                {
                    System.out.println("\nInvalid expense number! Please try again.");
                    choice = -1;
                }
            }
            catch (NumberFormatException ex)
            {
                System.out.println("\nInvalid input. Please enter a valid number.");
            }
        }
        while (choice == -1);

        Pause();
    }

    /**
     * Displays all expenses currently stored in memory.
     *
     * The method prints a formatted list of expenses along with their index.
     * If there are no expenses, it informs the user. Execution pauses after displaying.
     */
    public void viewExpenses()
    {
        int i = 1;
        System.out.println("\nYour expenses: ");
        System.out.println("=====================================================================");

        if (expenses.isEmpty())
            System.out.println("No expenses yet.");
        else
        {
            for (Expense expense: expenses)
            {
                System.out.println();
                System.out.println((i++) + ". " + expense.toString());
            }
        }

        Pause();
    }

    public void viewExpenseDetails(Expense expense)
    {
        System.out.println("\n  EXPENSE INFO");
        System.out.println("====================");
        System.out.println("Expense ID.........: " + expense.getId());
        System.out.println("Description........: " + expense.getName());
        System.out.println("Amount.............: " + expense.getAmount());
        System.out.println("Date...............: " + expense.getDateOfExpense());
    }

    /**
     * Displays a general summary of all recorded expenses.
     * <p>
     * The method calculates the total number of expenses and their combined amount.
     * If no expenses are recorded, it notifies the user and exits.
     */
    public void viewExpenseSummary()
    {
        int totalCounter = 0;
        double totalAmount = 0;

        if (expenses.isEmpty())
        {
            System.out.println("\nNo expenses recorded.");
            return;
        }

        for (Expense expense: expenses)
        {
            totalAmount += expense.getAmount();
            totalCounter++;
        }

        System.out.println("\nSummary of Expenses");
        System.out.println("=====================");
        System.out.println("Total expenses: " + totalCounter);
        System.out.println("Total amount: $" + totalAmount);

        Pause();
    }

    /**
     * Displays a summary of expenses for a specific month of the current year.
     * <p>
     * The user is prompted to select a month number (1–12). The method validates the input,
     * filters expenses matching that month and year, and calculates the total number
     * and combined amount of those expenses. It then prints the results in a formatted summary.
     * <p>
     * If the user enters invalid input or an out-of-range month, an appropriate message is shown.
     */
    public void viewExpenseSummaryForSpecificMonth()
    {
        int currentYear = LocalDate.now().getYear();
        int counter = 0;
        double totalAmount = 0.0;

        System.out.println("\nExpenses by Month");
        System.out.println("=================");

        int monthChosen = 0;
        do
        {
            System.out.print("\nSelect the month for which you want to see the total expenses (1 - 12): ");
            String input = keyboard.nextLine().trim();

            try
            {
                monthChosen = Integer.parseInt(input);
                if (monthChosen > 0 && monthChosen <= 12)
                {
                    for (Expense expense: expenses)
                    {
                        LocalDate date = expense.getDateOfExpense();
                        if (date.getYear() == currentYear && date.getMonthValue() == monthChosen)
                        {
                            counter++;
                            totalAmount += expense.getAmount();
                        }
                    }
                }
                else
                {
                    System.out.println("\nInvalid month number. Please enter a number between 1 and 12.");
                }
            }
            catch (NumberFormatException ex)
            {
                System.out.println("\nInvalid input. Please enter a number between 1 and 12.");
            }
        }
        while (monthChosen < 1 || monthChosen > 12);

        System.out.println("\nExpense summary for " + YearMonth.of(currentYear, monthChosen));
        System.out.println("==================================");
        System.out.println("Total expenses: " + counter);
        System.out.println("Total amount: $" + totalAmount);

        Pause();
    }

    public void viewExpensesByCategory()
    {
        int totalCounter = 0;
        double totalAmount = 0;

        System.out.println("\n------------------------------");
        System.out.print("Enter category to filter by: ");
        String category = keyboard.nextLine().trim();

        System.out.println("\nExpenses in category: " + category);
        System.out.println("==================================");

        for (Expense expense: expenses)
        {
            if (expense.getCategory().equalsIgnoreCase(category))
            {
                totalAmount += expense.getAmount();
                totalCounter++;
                System.out.println(expense);
            }
        }

        if (totalCounter == 0)
        {
            System.out.println("No expenses found for category " + category);
            return;
        }

        System.out.println("\nExpense summary for category " + category);
        System.out.println("==================================");
        System.out.println("Total expenses: " + totalCounter);
        System.out.println("Total amount: $" + totalAmount);

        Pause();
    }

    /**
     * Saves all expenses from memory to the task CSV file.
     *
     * Each expense is serialized into a single CSV line using
     * {Expense.AsCsvLine()}. Existing file contents are overwritten.
     * If the file cannot be found or written, an error message is displayed.
     */
    public void saveExpenses()
    {
        File file = new File(dirPath, expenseList);

        try(PrintWriter writer = new PrintWriter(new FileWriter(file, false)))
        {
            for (Expense expense: expenses)
            {
                writer.println(expense.AsCsvLine());
            }
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Cannot open expenses file. Not found.");
        }
        catch (IOException ex)
        {
            System.out.println("IO Error in saving tasks data. Should never happen.");
            ex.printStackTrace();
        }
    }

    /**
     * Loads expenses from the expense CSV file into memory.
     * Reads each line of the file and attempts to parse it into a {@link Expense}.
     * Parsing includes safe handling of:
     *
     *   ID
     *   Description
     *   Amount
     *   Date
     *
     * Invalid lines are skipped with a warning. After loading, the total number
     * of expenses read is displayed.
     */
    public void loadExpenses()
    {
        File file = new File (dirPath, expenseList);
        String line; // Variable for reading each line from the file
        String[] parts;
        int id;
        String description;
        double amount;
        LocalDate date;
        String category;
        Expense newExpense; // Expense object to be created from each line

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            while ((line = reader.readLine()) != null)
            {
                parts = line.split(","); // separation of line data based on ","
                if (parts.length < 5)
                {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                id = Integer.parseInt(parts[0].trim());
                description = parts[1].trim();
                amount = Double.parseDouble(parts[2].trim());
                date = LocalDate.parse(parts[3].trim());
                category = parts[4].trim();

                newExpense = new Expense(id, description, amount, date, category); // New expense creation
                expenses.add(newExpense);
            }
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Cannot Open File: " + file.getAbsolutePath());
            return;
        }
        catch (IOException ex)
        {
            System.out.println("Error reading file: " + file.getAbsolutePath());
            ex.printStackTrace();
            //return;
        }

        System.out.println("Data read successfully. Total expenses: " + expenses.size());
    }

    /**
     * Pauses program execution until the user presses Enter.
     *
     * This method is intended for console-based interaction only and is skipped
     * in GUI mode. It ensures the user has time to read console output before
     * proceeding.
     */
    public void Pause()
    {
        boolean interactiveMode = true; // set false in GUI mode
        if (!interactiveMode) return; // skip pause in GUI mode

        System.out.print("\n\nPress <Enter> to continue....");

        // flush any leftover input
        while (keyboard.hasNextLine())
        {
            String leftover = keyboard.nextLine();
            if (leftover.isEmpty()) break;
        }

        System.out.println();
    }

}

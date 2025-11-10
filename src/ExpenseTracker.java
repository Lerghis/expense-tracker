import storage.Storage;

import java.io.IOException;
import java.util.Scanner;

public class ExpenseTracker
{
    Storage storage;
    Scanner keyboard;

    public ExpenseTracker()
    {
        storage = new Storage();
        keyboard = new Scanner(System.in);
    }

    public void Menu()
    {
        int choice;
        do
        {
            //Cls();
            System.out.println("\n\n    M E N U");
            System.out.println("===================\n");
            System.out.println("[1].......Add Expense");
            System.out.println("[2].......Update Expense");
            System.out.println("[3].......Delete Expense");
            System.out.println("[4].......View Expenses");
            System.out.println("[5].......View Summary of Expenses");
            System.out.println("[6].......View Summary of Expenses for a Specific Month");
            System.out.println("[7].......Filter Expenses by Category");
            System.out.println("[9].......Exit App");
            System.out.print("\nEnter your choice: ");
            choice = keyboard.nextInt();
            keyboard.nextLine(); // Clear Buffer

            switch (choice)
            {
                case 1 -> storage.addExpense();
                case 2 -> storage.updateExpense();
                case 3 -> storage.deleteExpense();
                case 4 -> storage.viewExpenses();
                case 5 -> storage.viewExpenseSummary();
                case 6 -> storage.viewExpenseSummaryForSpecificMonth();
                case 7 -> storage.viewExpensesByCategory();
                case 9 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }
        }
        while(choice != 9);
    }

    // Calling Save methods to save the data
    public void FinalJobs()
    {
        storage.saveExpenses();
    }
    public static void main(String[] args)
    {
        ExpenseTracker Program = new ExpenseTracker();
        Program.Menu();
        Program.FinalJobs();
        Program.keyboard.close();
    }

    public void Cls()
    {

        try
        {
            new ProcessBuilder ("cmd", "/c", "cls").inheritIO().
                    start(). waitFor();
        }
        catch (IOException ex)
        {

        }
        catch (InterruptedException ex)
        {

        }
    }
    public void Pause()
    {
        System.out.print ("\n\nPress <Enter> to continue....");
        keyboard.nextLine();
        System.out.println ("\n");
    }
}

package model;

import java.time.LocalDate;

public class Expense
{
    private static int nextId = 1;
    private final int id;
    private String name;
    private double amount;
    private LocalDate dateOfExpense;
    private String category;

    // Constructor that auto-generates ID
    public Expense(String name, double amount, LocalDate dateOfExpense, String category)
    {
        this.id = nextId++;
        this.name = name;
        this.amount = amount;
        this.dateOfExpense = dateOfExpense;
        this.category = category;
    }

    // Constructor for loading existing expenses (ensures existing expenses keep their original IDs)
    public Expense(int id, String name, double amount, LocalDate dateOfExpense, String category)
    {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.dateOfExpense = dateOfExpense;
        this.category = category;

        // This part keeps the automatic ID counter in sync with the highest ID already used to avoid duplicates.
        if (id >= nextId)
        {
            nextId = id + 1;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDateOfExpense() {
        return dateOfExpense;
    }

    public void setDateOfExpense(LocalDate dateOfExpense) {
        this.dateOfExpense = dateOfExpense;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Converts an expense into a CSV-formatted line.
     * <p>
     * The returned string contains the expense fields separated by commas,
     * ordered as: id, name, amount, dateOfExpense, category.
     *
     * @return a CSV representation of the expense
     */
    public String AsCsvLine()
    {
        return id + "," + name + "," + amount + "," + dateOfExpense + "," + category;
    }

    /**
     * Returns a human-readable string representation of the expense.
     * <p>
     * Includes id, description, amount, date, and category in a formatted line.
     *
     * @return a formatted string describing the expense
     */
    @Override
    public String toString()
    {
        return "Expense ID: " + id + ", Description: " + name + ", Amount: " + "$" + amount + ", Time: " + dateOfExpense + ", Category: " + category;
    }
}

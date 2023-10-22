package coe528finalproject;

/**
 *
 * @author Samuel & Sanjay
 */
import java.util.ArrayList;

public class Owner {

    /* Instance variables */
    private static Owner instance;
    private final String username = "admin";
    private final String password = "admin";

    // Private constructor so cannot be directly instantiated
    private Owner() {

    }

    // SINGLETON DESIGN
    public static Owner getInstance() {
        if (instance == null) {
            instance = new Owner();
        }
        return instance;
    }

    /* overloading add and delete methods*/
    // Adding customer
    public void add(Customer c) {
        BookStore.customers.add(c);
    }

    // Adding book 
    public void add(Book b) {
        BookStore.books.add(b);
    }

    // Deleting customer
    public void deleteBook(int q) {
        BookStore.books.remove(q);
    }

    // Deleting book
    public void deleteCustomer(int q) {
        BookStore.customers.remove(q);

    }
}

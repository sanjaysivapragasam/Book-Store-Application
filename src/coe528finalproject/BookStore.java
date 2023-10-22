package coe528finalproject;

/**
 *
 * @author Samuel & Sanjay
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.lang.*;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.WindowEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.TableView.TableViewSelectionModel;
import java.text.DecimalFormat;
import java.util.HashSet;

public class BookStore extends Application {

    /* Variables and Lists */
    private Owner owner;
    private Customer c;
    protected static List<Book> books = new ArrayList<Book>();
    protected static List<Customer> customers = new ArrayList<Customer>();
    static List<Book> selectedBook = new ArrayList<Book>();
    TableColumn<Book, Boolean> checkbox = new TableColumn<>("Select");
    TableView<Book> booktable = new TableView<Book>();
    TableView<Customer> customertable = new TableView<Customer>();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /* Buttons */
    //createFirstGridPane
    Button loginButton = new Button("Login");
    Button logoutButton = new Button("Logout");
    //createOwnerGridPane
    Button booksButton = new Button("Books");
    Button customersButton = new Button("Customers");
    //ownerBooksScreen
    Button backButton = new Button("Back");
    Button addBookButton = new Button("Add");
    Button deleteBookButton = new Button("Delete");
    //ownerCustomersScreen
    Button addCustomerButton = new Button("Add");
    Button deleteCustomerButton = new Button("Delete");
    //createCustomerGridPane
    Button buyButton = new Button("Buy");
    Button buyWithPointsButton = new Button("Reedeem Points and Buy");

    /* Labels */
    Label welcome = new Label("Welcome to BookStore App!");
    Label usernameLabel = new Label("Username:");
    Label passwordLabel = new Label("Password:");
    Label nameLabel = new Label("Name:");
    Label priceLabel = new Label("Price:");
    Label buyLabel = new Label("Buy:");
    Label pointsBuyLabel = new Label("Buy With Points:");
    Label message = new Label();

    /* Input textfields */
    //createFirstGridPane
    TextField usernameText = new TextField();
    PasswordField passwordText = new PasswordField();
    //ownerBooksScreen
    TextField addBookName = new TextField();
    TextField addBookPrice = new TextField();
    //ownerCustomersScreen
    TextField addUsername = new TextField();
    TextField addPassword = new TextField();

    @Override //Override the start method in the Application class
    public void start(Stage primaryStage) {

        /* Prompts for all of the textfields*/
        usernameText.setPromptText("Username");
        addBookName.setPromptText("Name");
        passwordText.setPromptText("Password");
        addBookPrice.setPromptText("Price");
        addUsername.setPromptText("Username");
        addPassword.setPromptText("Password");

        primaryStage.setTitle("BookStoreApp");
        primaryStage.setScene(new Scene(createFirstGridPane(), 500, 500));
        primaryStage.show();

        //X to close the window 
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                System.out.println("window (i.e stage) is closing");
                // Writing new changes to txt files after the program is no longer active
                write("books.txt");
                write("customers.txt");
            }
        }
        );
        // Login Button 
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    String username = usernameText.getText();
                    String password = passwordText.getText();
                    System.out.println("Username: " + username);
                    System.out.println("Password: " + password);
                    if (username.equals("admin") && password.equals("admin")) { // For owner
                        System.out.println("Successful owner login.");
                        owner = Owner.getInstance();
                        primaryStage.setScene(new Scene(createOwnerGridPane(), 800, 800));
                        usernameText.clear();
                        passwordText.clear();
                    } else { // For customers
                        int k = 0;
                        for (int i = 0; i < customers.size(); i++) {
                            if (username.equals(customers.get(i).getUsername()) && password.equals(customers.get(i).getPassword())) {
                                c = customers.get(i);
                                System.out.println("Successful customer login.");
                                k = k + 1; // Increment k by 1 when valid credentials are found
                                primaryStage.setScene(new Scene(createCustomerGridPane(), 800, 800));
                                usernameText.clear();
                                passwordText.clear();
                            }
                        }
                        if (k == 0) { // Invalid credentials
                            System.out.println("User does not exist yet! Please register with the Owner before using the app.");
                        }
                    }
                } catch (NullPointerException p) {
                    System.out.println("Please enter the username or password");
                    // p.printStackTrace();
                }
            }
        }
        );

        // Logout Button
        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setScene(new Scene(createFirstGridPane(), 500, 500));
            }
        }
        );
        // Books button from owner-start-screen
        booksButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setScene(new Scene(ownerBooksScreen(), 800, 800));
            }
        }
        );
        // Customers button from owner-start-screen
        customersButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setScene(new Scene(ownerCustomersScreen(), 800, 800));
            }
        }
        );
        // Going back to owner-start-screen  
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Back button of anonymous clicked");
                primaryStage.setScene(new Scene(createOwnerGridPane(), 500, 500));
            }
        }
        );
        // Adding book from owner-books-screen
        addBookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String title = addBookName.getText();
                String price = addBookPrice.getText();

                if (title.equals("") || price.equals("")) {
                    System.out.println("Please type both parameters");
                } else if (price.matches("^[a-zA-Z]*$")) {
                    System.out.println("Please type numbers for the price");
                    addBookPrice.clear();
                } else {
                    double p = Double.parseDouble(price);
                    price = df.format(p);
                    System.out.println(p);
                    boolean a = false;
                    for (Book b : books) {
                        if (b.getName().equals(title)) {
                            a = true;
                        }
                    }
                    if (a == false) {
                        Book tempBook = new Book(title, price);
                        owner.add(tempBook);
                        addBookName.clear();
                        addBookPrice.clear();
                        primaryStage.setScene(new Scene(ownerBooksScreen(), 800, 800));
                    } else {
                        System.out.println("Book already exist");
                        addBookName.clear();
                        addBookPrice.clear();
                    }
                }
            }
        }
        );
        // Deleting book from owner-books-screen
        deleteBookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                TableView.TableViewSelectionModel<Book> selectionModel = booktable.getSelectionModel();
                owner.deleteBook(selectionModel.getSelectedIndex());
                primaryStage.setScene(new Scene(ownerBooksScreen(), 800, 800));
            }
        }
        );
        // Adding customer from owner-customers-screen
        addCustomerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String user = addUsername.getText();
                String pass = addPassword.getText();
                if (user.equals("") || pass.equals("")) {
                    System.out.println("Please type all the parameters");
                } else {
                    boolean a = false;
                    for (Customer b : customers) {
                        if (b.getUsername().equals(user)) {
                            a = true;
                        }
                    }
                    if (a == true) {
                        System.out.println("User already exist");
                        addUsername.clear();
                        addPassword.clear();
                    } else {
                        Customer tempcus = new Customer(user, pass, "0");
                        owner.add(tempcus);
                        addUsername.clear();
                        addPassword.clear();
                        primaryStage.setScene(new Scene(ownerCustomersScreen(), 800, 800));
                    }
                }
            }
        }
        );
        // Deleting customer from owner-customers-screen
        deleteCustomerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                TableView.TableViewSelectionModel<Customer> selectionModel = customertable.getSelectionModel();
                owner.deleteCustomer(selectionModel.getSelectedIndex());
                primaryStage.setScene(new Scene(ownerCustomersScreen(), 800, 800));
            }
        }
        );
        // Buy button from customer-start-screen
        buyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Double sum = 0.00;
                if (selectedBook.isEmpty()) {
                    System.out.println("Nothing selected");
                } else {
                    List<Book> d = new ArrayList<>(new HashSet<>(selectedBook));
                    for (Book b : d) {
                     //   System.out.println("hi : " + b);
                        sum = sum + Double.parseDouble(b.getPrice());
                        //System.out.println(sum);
                        books.remove(b);
                    }
                    c.setTransactionCost(sum);
                    c.addPoints();
                    System.out.println("Buy button of anonymous clicked");
                    selectedBook = new ArrayList<Book>();
                    primaryStage.setScene(new Scene(createPaymentGidPane(), 500, 500));
                }
                selectedBook = new ArrayList<Book>();
            }
        }
        );
        // Buy with points button from customer-start-screen
        buyWithPointsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Double sum = 0.00;
                if (selectedBook.isEmpty()) {
                    System.out.println("Nothing selected");
                } else if (c.getPoints().equals("0") || Integer.parseInt(c.getPoints()) < 100) {
                    System.out.println("You do not have enough points to redeem (points : " + c.getPoints() + ")");
                } else {
                    List<Book> d = new ArrayList<>(new HashSet<>(selectedBook));
                    for (Book b : d) {
                      //  System.out.println("hi : " + b);
                        sum = sum + Double.parseDouble(b.getPrice());
                       // System.out.println(sum);
                        books.remove(b);
                    }
                    c.setTransactionCost(sum);
                    c.buy();
                    System.out.println("Buy button of anonymous clicked");
                    selectedBook = new ArrayList<Book>();
                    primaryStage.setScene(new Scene(createPaymentGidPane(), 500, 500));
                }
                selectedBook = new ArrayList<Book>();
            }

        }
        );
    }

    // Read txt file information
    public static void read(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));
            if (filename.equals("books.txt")) {
                while (sc.hasNext()) {
                    // Getting tempBook Information --> Title & Price
                    String[] bookInfo = sc.nextLine().split(",");
                    String title = bookInfo[0];
                    String price = bookInfo[1];
                    // Creating & adding tempBook to tempBookList 
                    Book tempBook = new Book(title, price);
                    books.add(tempBook);
                }
            } else if (filename.equals("customers.txt")) {
                while (sc.hasNext()) {
                    // Getting tempCustomer Information --> username, password, points
                    String[] customerInformation = sc.nextLine().split(",");
                    String user = customerInformation[0];
                    String pass = customerInformation[1];
                    String points = customerInformation[2];
                    // Creating & adding tempCustomer to tempCustomerList 
                    Customer tempCustomer = new Customer(user, pass, points);
                    customers.add(tempCustomer);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Write updated information to txt file
    public static void write(String filename) {
        File file = new File(filename);
        try {
            FileWriter fw = new FileWriter(file, false);
            file.createNewFile();
            if (filename.equals("books.txt")) {
                for (int i = 0; i < books.size(); i++) {
                    fw.write(books.get(i).getName() + "," + books.get(i).getPrice() + "\n");
                }
            } else if (filename.equals("customers.txt")) {
                for (int i = 0; i < customers.size(); i++) {
                    fw.write(customers.get(i).getUsername() + "," + customers.get(i).getPassword()
                            + "," + customers.get(i).getPoints() + "\n");
                }
            }
            fw.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /* Display screen */
    //Login page
    public GridPane createFirstGridPane() {
        GridPane gp1 = new GridPane();
        gp1.setAlignment(Pos.CENTER);
        // ADD EVERYTHING
        gp1.add(welcome, 10, 0);
        gp1.add(usernameText, 100, 1);
        gp1.add(usernameLabel, 10, 1);
        gp1.add(passwordText, 100, 3);
        gp1.add(passwordLabel, 10, 3);
        gp1.add(loginButton, 200, 4);
        return gp1;
    }

    // Owner-start-screen
    public GridPane createOwnerGridPane() {
        GridPane gp1 = new GridPane();
        gp1.setAlignment(Pos.CENTER);
        // ADD EVERYTHING
        gp1.add(booksButton, 0, 0);
        gp1.add(customersButton, 0, 5);
        gp1.add(logoutButton, 0, 10);
        return gp1;
    }

    // Owner-books-screen
    public GridPane ownerBooksScreen() {
        GridPane gp1 = new GridPane();
        gp1.setAlignment(Pos.CENTER);
        booktable = new TableView<Book>();
        booktable.setEditable(false);
        ObservableList<Book> bookList = FXCollections.observableList(books);
        //NAME
        TableColumn booknameTable = new TableColumn("Book Name");
        booknameTable.setMinWidth(150);
        booknameTable.setCellValueFactory(
                new PropertyValueFactory<Book, String>("name"));
        booknameTable.setCellFactory(TextFieldTableCell.forTableColumn());
        booknameTable.setOnEditCommit(
                new EventHandler<CellEditEvent<Book, String>>() {
            @Override
            public void handle(CellEditEvent<Book, String> t) {
                ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue());
            }
        }
        );
        //PRICE
        TableColumn priceTable = new TableColumn("Book Price");
        priceTable.setMinWidth(150);
        priceTable.setCellValueFactory(
                new PropertyValueFactory<Book, String>("price"));
        priceTable.setCellFactory(TextFieldTableCell.forTableColumn());

        priceTable.setOnEditCommit(new EventHandler<CellEditEvent<Book, String>>() {
            @Override
            public void handle(CellEditEvent<Book, String> t) {
                ((Book) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPrice(t.getNewValue());
            }
        }
        );
        //CREATE TABLE
        booktable.setItems(bookList);
        booktable.getColumns().addAll(booknameTable, priceTable);
        // SPACING
        //gp1.setVgap(20);
        //gp1.setHgap(15);
        // ADD EVERYTHING
        gp1.add(addBookName, 0, 2);
        gp1.add(addBookPrice, 1, 2);
        gp1.add(addBookButton, 2, 2);
        gp1.add(deleteBookButton, 2, 3);
        gp1.add(backButton, 0, 3);
        gp1.add(nameLabel, 0, 1);
        gp1.add(priceLabel, 1, 1);
        gp1.add(booktable, 0, 0);
        return gp1;
    }

    // Owner-customer-screen
    public GridPane ownerCustomersScreen() {
        GridPane gp1 = new GridPane();
        gp1.setAlignment(Pos.CENTER);
        customertable = new TableView<Customer>();
        customertable.setEditable(false);
        ObservableList<Customer> customerList = FXCollections.observableList(customers);
        //USERNAME
        TableColumn usernameTable = new TableColumn("Username");
        usernameTable.setMinWidth(100);
        usernameTable.setCellValueFactory(
                new PropertyValueFactory<Customer, String>("username"));
        usernameTable.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameTable.setOnEditCommit(
                new EventHandler<CellEditEvent<Customer, String>>() {
            @Override
            public void handle(CellEditEvent<Customer, String> t) {
                ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setUsername(t.getNewValue());
            }
        }
        );

        //PASSWORD
        TableColumn passwordTable = new TableColumn("Password");
        passwordTable.setMinWidth(100);
        passwordTable.setCellValueFactory(
                new PropertyValueFactory<Customer, String>("password"));
        passwordTable.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordTable.setOnEditCommit(
                new EventHandler<CellEditEvent<Customer, String>>() {
            @Override
            public void handle(CellEditEvent<Customer, String> t) {
                ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPassword(t.getNewValue());
            }
        }
        );
        //POINTS
        TableColumn pointsTable = new TableColumn("Points");
        pointsTable.setMinWidth(100);
        pointsTable.setCellValueFactory(
                new PropertyValueFactory<Customer, String>("points"));
        pointsTable.setCellFactory(TextFieldTableCell.forTableColumn());
        pointsTable.setOnEditCommit(
                new EventHandler<CellEditEvent<Customer, String>>() {
            @Override
            public void handle(CellEditEvent<Customer, String> t) {
                ((Customer) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPoint(t.getNewValue());
            }
        }
        );

        //CREATE TABLE
        customertable.setItems(customerList);
        customertable.getColumns().addAll(usernameTable, passwordTable, pointsTable);
        // SPACING
        gp1.setVgap(20);
        gp1.setHgap(15);
        // ADD EVERYTHING
        gp1.add(addUsername, 0, 2);
        gp1.add(addPassword, 1, 2);
        gp1.add(addCustomerButton, 2, 2);
        gp1.add(deleteCustomerButton, 2, 3);
        gp1.add(backButton, 0, 3);
        gp1.add(usernameLabel, 0, 1);
        gp1.add(passwordLabel, 1, 1);
        gp1.add(customertable, 0, 0);
        return gp1;
    }

    // Customer-start-screen
    public GridPane createCustomerGridPane() {
        GridPane gp1 = new GridPane();
        gp1.setAlignment(Pos.CENTER);
        final TableView<Book> table = new TableView<Book>();
        table.setEditable(true);
        ObservableList<Book> bookList = FXCollections.observableList(books);
        //NAME
        TableColumn booknameTable = new TableColumn("Book Name");
        booknameTable.setMinWidth(150);
        booknameTable.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        booknameTable.setCellFactory(TextFieldTableCell.forTableColumn());
        booknameTable.setEditable(false);
        //PRICE
        TableColumn priceTable = new TableColumn("Book Price");
        priceTable.setMinWidth(150);
        priceTable.setCellValueFactory(new PropertyValueFactory<Book, String>("price"));
        priceTable.setCellFactory(TextFieldTableCell.forTableColumn());
        priceTable.setEditable(false);
        //CheckBox
        checkbox.setCellValueFactory(new PropertyValueFactory<>("selected"));
        checkbox.setCellFactory(CheckBoxTableCell.forTableColumn(checkbox));
        checkbox.setMinWidth(100);
        checkbox.setEditable(true);
        //CREATE TABLE
        table.setItems(bookList);
        table.getColumns().addAll(booknameTable, priceTable, checkbox);

        // Updating books that customer selects
        for (Book b : books) {
            /* For testing purposes */
            //  System.out.println("In here."); 
            b.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    //  System.out.println(b.getName() + " is selected.");
                    selectedBook.add(b);
                    System.out.println(b);
                    //  printList();
                } else {
                    //   System.out.println(b.getName() + " is deselected.");
                    selectedBook.remove(b);
                    // printList();
                }
            });
        }

        // SPACING
        gp1.setVgap(20);
        gp1.setHgap(15);

        // ADD EVERYTHING
        message = new Label("Welcome " + c.getUsername() + ". You have " + c.getPoints() + " points. Your status is " + c.changeStatus() + ".");
        gp1.add(message, 0, 0);
        gp1.add(logoutButton, 2, 3);
        gp1.add(buyButton, 0, 3);
        gp1.add(buyWithPointsButton, 1, 3);
        gp1.add(buyLabel, 0, 2);
        gp1.add(pointsBuyLabel, 1, 2);
        gp1.add(table, 0, 1);
        return gp1;
    }

    // Customer-cost-screen 
    public GridPane createPaymentGidPane() {
        GridPane gp1 = new GridPane();
        gp1.setAlignment(Pos.CENTER);
        // SPACING
        gp1.setVgap(20);
        gp1.setHgap(15);

        // ADD EVERYTHING
        gp1.add(new Label("Total Cost: " + c.getTransactionCost()), 0, 0);
        gp1.add(new Label("Points: " + c.getPoints()), 0, 1);
        gp1.add(new Label("Status: " + c.changeStatus()), 0, 2);
        gp1.add(logoutButton, 0, 3);
        return gp1;
    }

    // Printout selected book (used for testing) 
    public void printList() {
        for (Book b : selectedBook) {
            System.out.println(b);
        }
    }

    // main method to read information and launch program
    public static void main(String[] args) {
        read("books.txt");
        read("customers.txt");
        launch(args);
    }
}

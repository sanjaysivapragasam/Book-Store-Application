package coe528finalproject;

import java.text.DecimalFormat;

/**
 *
 * @author Samuel & Sanjay
 */
public class Customer {

    // to ensure transaction cost is rounded to two decimal places
    //private static final DecimalFormat df = new DecimalFormat("0.00");
    
    /* Instance variables */
    private String username;
    private String password;
    private String points;
    private Status myStatus;
    private double transactionCost;
    

    /* Constructor */
    public Customer(String username, String password, String points) {
        this.username = username;
        this.password = password;
        this.points = points;
    }

    /* Get and Set methods */
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String s) {
        this.username = s;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String s) {
        this.password = s;
    }

    public String getPoints() {
        return this.points;
    }

    public void setPoint(String s) {
        this.points = s;
    }

    public void setTransactionCost(double transactionCost) {
        this.transactionCost = transactionCost;
    }

    public double getTransactionCost() {
        return (transactionCost);
    }

    public void setPoints(String points) {
        int pointsInt = Integer.parseInt(points);
        if (pointsInt < 0) {
            pointsInt = 0;
        }
        this.points = Integer.toString(pointsInt);
    }

    /* Additonal methods for changes regarding status, points and transactions*/
    public void updateTransactionCost(double amount) {
        if (amount < 0) {
            amount = 0;
        }
        this.transactionCost = amount;
    }

    public void addPoints() {
        int pointsInt = Integer.parseInt(this.points);
        int cost = (int) this.transactionCost;
        points = Integer.toString(pointsInt + cost * 10);
    }

    public Status changeStatus() {
        int pointsInt = Integer.parseInt(points);
        if (pointsInt >= 1000) {
            myStatus = new Gold();
        } else {
            myStatus = new Silver();
        }
        return myStatus;
    }

    public void buy() {
        changeStatus();
        myStatus.buy(this, this.transactionCost);
        System.out.println("hi");
    }

    @Override
    public String toString() {
        return this.username + " " + this.password + " " + this.points + " " + this.transactionCost;
    }
}

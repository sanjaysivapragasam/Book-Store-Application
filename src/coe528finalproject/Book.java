package coe528finalproject;

/**
 *
 * @author Samuel & Sanjay
 */
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Book {

    /* Instance variables */
    private String name;
    private String price;
    private BooleanProperty selected;

    /* Constructor */
    public Book(String name, String price) {
        this.name = name;
        this.price = price;
        this.selected = new SimpleBooleanProperty(false);
    }

    /* Get and set methods*/
    public String getName() {
        return name;
    }
    public void setName(String n){
        this.name = n;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String n){
        this.price = n;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public boolean getSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    @Override
    public String toString() {
        return this.name + " " + this.price;
    }
}

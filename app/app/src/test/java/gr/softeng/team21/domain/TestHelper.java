package gr.softeng.team21.domain;

import java.util.HashMap;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Helper class for testing purposes.
 * Provides static instances of domain objects (products, customer, address)
 * and utility methods to populate or clear the in-memory database.
 * @author PAVLOS GRATSANIS
 */
public class TestHelper {
    private static Money priceMonitor = new Money(150, "€");
    private static ProductType monitor = new ProductType("Samsung 24\"", "Full HD Monitor", priceMonitor, "m101");
    private static Money priceLaptop = new Money ( 500, "€" );
    private static ProductType laptop = new ProductType ( "Laptop Dell", "High End", priceLaptop, "l101" );
    private static Money priceMouse = new Money ( 50, "€" );
    private static ProductType mouse = new ProductType ( "Mouse Logitech", "Wireless", priceMouse, "m102" );
    private static  Money priceKeyboard = new Money(80, "€");
    private static ProductType keyboard = new ProductType("Razer Blackwidow", "Mechanical Keyboard", priceKeyboard, "l102");
    private static Address address = new Address("Ermou", "15", "Athens","Greece" ,"10563");
    private static EmailAddress email = new EmailAddress("giannis@mail.com");
    private static Customer customer = new Customer(
            "giannispap", "Giannis", "pass1234", "Papadopoulos",
            "697123456", email, "CUST-001", new Date());


    /**
     * Populates the ProductTypeDAOMemory with a set of predefined products
     * (Laptop, Mouse, Keyboard, Monitor).
     * Adds them only if they do not already exist.
     */
    public static void addProductsManually() {
        ProductTypeDAOMemory repo = ProductTypeDAOMemory.getInstance();

        if (repo.getProduct(laptop.getProductCode ()) == null) {
            repo.addProductType(laptop);
        }
        if (repo.getProduct(mouse.getProductCode ()) == null) {
            repo.addProductType(mouse);
        }
        if (repo.getProduct(keyboard.getProductCode ()) == null) {
            repo.addProductType(keyboard);
        }
        if (repo.getProduct(monitor.getProductCode ()) == null) {
            repo.addProductType(monitor);
        }
    }

    /**
     * Clears all data from the ProductTypeDAOMemory.
     */
    public static void clear(){
        ProductTypeDAOMemory.getInstance ().clear ();
    }

    /**
     * Returns the map of products currently stored in the DAO.
     * @return a HashMap containing the products
     */
    public static HashMap<String,ProductType> getProducts ( ) {
        return ProductTypeDAOMemory.getInstance ( ).getProducts ( );
    }

    /**
     * Returns the price object defined for the laptop.
     * @return the laptop price
     */
    public static Money getPriceLaptop ( ) {
        return priceLaptop;
    }

    /**
     * Returns the test laptop object.
     * @return the laptop
     */
    public static ProductType getLaptop ( ) {
        return laptop;
    }

    /**
     * Returns the test keyboard object.
     * @return the keyboard
     */
    public static ProductType getKeyboard ( ) {
        return keyboard;
    }

    /**
     * Returns the test mouse object.
     * @return the mouse
     */
    public static ProductType getMouse ( ) {
        return mouse;
    }

    /**
     * Returns the test address object.
     * @return the address
     */
    public static Address getAddress ( ) {
        return address;
    }

    /**
     * Returns the test email address object.
     * @return the email address
     */
    public static EmailAddress getEmail ( ) {
        return email;
    }

    /**
     * Returns the test customer object.
     * @return the customer
     */
    public static Customer getCustomer ( ) {
        return customer;
    }

    /**
     * Returns the test monitor object.
     * @return the monitor
     */
    public static ProductType getMonitor ( ) {
        return monitor;
    }
}
package gr.softeng.team21.domain;

import java.util.HashMap;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.memorydao.MemoryInitializer;
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

    public static void addProductsManually() {
        ProductTypeDAO repo = MemoryInitializer.getProductTypeDAO();

        if (repo.getProduct(laptop.getProductCode()).join() == null) {
            repo.addProductType(laptop).join();
        }
        if (repo.getProduct(mouse.getProductCode()).join() == null) {
            repo.addProductType(mouse).join();
        }
        if (repo.getProduct(keyboard.getProductCode()).join() == null) {
            repo.addProductType(keyboard).join();
        }
        if (repo.getProduct(monitor.getProductCode()).join() == null) {
            repo.addProductType(monitor).join();
        }
    }

    public static void clear(){
        MemoryInitializer.getProductTypeDAO().clear().join();
    }

    public static HashMap<String,ProductType> getProducts ( ) {
        return MemoryInitializer.getProductTypeDAO().getProducts().join();
    }

    public static Money getPriceLaptop ( ) { return priceLaptop; }
    public static ProductType getLaptop ( ) { return laptop; }
    public static ProductType getKeyboard ( ) { return keyboard; }
    public static ProductType getMouse ( ) { return mouse; }
    public static Address getAddress ( ) { return address; }
    public static EmailAddress getEmail ( ) { return email; }
    public static Customer getCustomer ( ) { return customer; }
    public static ProductType getMonitor ( ) { return monitor; }
}
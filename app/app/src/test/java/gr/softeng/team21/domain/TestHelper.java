package gr.softeng.team21.domain;

import java.util.HashMap;

import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

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
    public static void clear(){
        ProductTypeDAOMemory.getInstance ().clear ();
    }

    public static HashMap<String,ProductType> getProducts ( ) {
        return ProductTypeDAOMemory.getInstance ( ).getProducts ( );
    }
    public static Money getPriceLaptop ( ) {
        return priceLaptop;
    }

    public static ProductType getLaptop ( ) {
        return laptop;
    }

    public static ProductType getKeyboard ( ) {
        return keyboard;
    }

    public static ProductType getMouse ( ) {
        return mouse;
    }

    public static Address getAddress ( ) {
        return address;
    }

    public static EmailAddress getEmail ( ) {
        return email;
    }

    public static Customer getCustomer ( ) {
        return customer;
    }

    public static ProductType getMonitor ( ) {
        return monitor;
    }
}
package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WholesaleProductTest {

    private WholesaleProduct product;
    private Money price;

    @Before
    public void setUp() {
        price = new Money(70, "euro");
        product = new WholesaleProduct(2526, "Fifa 26", "EASports", "PS5 game", price);
    }

    // -----------------------------------------------------
    // TEST CONSTRUCTOR + GETTERS
    // -----------------------------------------------------
    @Test
    public void testConstructorAndGetters() {
        assertEquals(2526, product.getId());
        assertEquals("Fifa 26", product.getName());
        assertEquals("EASports", product.getSupName());
        assertEquals("PS5 game", product.getDescription());
        assertEquals(price, product.getPrice());
    }

    // -----------------------------------------------------
    // TEST SETTERS
    // -----------------------------------------------------
    @Test
    public void testSetId() {
        product.setId(9999);
        assertEquals(9999, product.getId());
    }

    @Test
    public void testSetName() {
        product.setName("Fifa 27");
        assertEquals("Fifa 27", product.getName());
    }

    @Test
    public void testSetSupName() {
        product.setSupName("EA Sports Europe");
        assertEquals("EA Sports Europe", product.getSupName());
    }

    @Test
    public void testSetDescription() {
        product.setDescription("Football game");
        assertEquals("Football game", product.getDescription());
    }

    @Test
    public void testSetPrice() {
        Money newPrice = new Money(80, "euro");
        product.setPrice(newPrice);
        assertEquals(newPrice, product.getPrice());
    }
}

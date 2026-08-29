package gr.softeng.team21.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Unit tests for the {@link SupOrder} domain class.
 * Verifies the correct calculation of full amounts, validation of foreign keys (admin ID),
 * and checks appropriate data encapsulation.
 * @author Γιάννης Μονοχολιάς
 */
public class SupOrderTest {

    private SupOrder sup;
    private ArrayList<OrderLine> orderProducts;

    /**
     * Initializes the testing environment before each test.
     * Prepares multiple order lines and creates a supply order associated with a test Admin ID.
     */
    @Before
    public void setUp() throws Exception {
        orderProducts = new ArrayList<>();

        OrderLine orderLine = new OrderLine(
                new WholesaleProduct(2526, "Fifa 26", "EASports", "PS5 game", new Money(70, "euro")),
                10
        );

        OrderLine orderLine2 = new OrderLine(
                new WholesaleProduct(22526, "Fifa 25", "EASports", "PS5 game", new Money(70, "euro")),
                10
        );

        orderProducts.add(orderLine);
        orderProducts.add(orderLine2);

        // Constructor adjusted to use Admin ID (Foreign Key) instead of Admin object
        sup = new SupOrder(new Date(), 123, "ADM-001", orderProducts);
    }

    /**
     * Verifies that the total amount of the entire supply order is calculated accurately
     * by summarizing the individual bills of each order line.
     */
    @Test
    public void testFullAmount() {
        Money result = sup.fullAmount();

        BigDecimal expected = new BigDecimal(0)
                .add(orderProducts.get(0).totalBill())
                .add(orderProducts.get(1).totalBill());

        assertEquals(expected, result.getAmount());
        assertEquals("euro", result.getCurrency());
    }

    /**
     * Verifies the getter and setter for the Admin ID (Foreign Key).
     */
    @Test
    public void testSetAndGetAdminId() {
        String expectedId = "ADM-999";
        sup.setAdminId(expectedId);
        assertEquals(expectedId, sup.getAdminId());
    }

    /**
     * Verifies the getter and setter for the supply order ID.
     */
    @Test
    public void testSetAndGetId() {
        int expectedId = 555;
        sup.setId(expectedId);
        assertEquals(expectedId, sup.getId());
    }

    /**
     * Verifies the getter and setter for the supply order date.
     */
    @Test
    public void testSetAndGetDate() {
        Date now = new Date();
        sup.setDate(now);
        assertEquals(now, sup.getDate());
    }

    /**
     * Verifies that the order products provided in the constructor are safely
     * stored and accessible via properly encapsulated getters.
     */
    @Test
    public void testConstructorStoresOrderProducts() {
        assertEquals(orderProducts, sup.getOrderProducts());
        assertEquals(2, sup.getOrderProducts().size());
    }
}
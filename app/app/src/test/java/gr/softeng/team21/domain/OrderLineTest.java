package gr.softeng.team21.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Unit tests for the {@link OrderLine} domain class.
 * Verifies the correct behavior of order line attributes, including
 * calculations of total bills, and ensures correct encapsulation.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderLineTest {

    private OrderLine orderLine;
    private ArrayList<OrderLine> orderProducts;

    /**
     * Initializes the testing environment before each test.
     * Sets up a sample product and an OrderLine instance.
     */
    @Before
    public void setUp() throws Exception {
        orderProducts = new ArrayList<>();
        // Constructor adjusted to 2 parameters (removed circular dependency with SupOrder)
        orderLine = new OrderLine(new WholesaleProduct(2526, "Fifa 26", "EASports", "PS5 game", new Money(70, "euro")), 10);
        orderProducts.add(orderLine);
    }

    /**
     * Verifies that the total bill for the order line is calculated correctly
     * by multiplying the product price by the quantity.
     */
    @Test
    public void totalBill() {
        for (OrderLine ord : orderProducts) {
            BigDecimal bill = ord.totalBill();
            BigDecimal expectedAmount = ord.getProduct().getPrice().getAmount().multiply(BigDecimal.valueOf(ord.getQuantity()));
            assertEquals(expectedAmount, bill);
        }
    }

    /**
     * Verifies the getter and setter for the quantity attribute.
     */
    @Test
    public void testSetAndGetQuantity() {
        int expected = 50;
        orderLine.setQuantity(expected);
        assertEquals(expected, orderLine.getQuantity());
    }

    /**
     * Verifies the getter and setter for the product attribute.
     */
    @Test
    public void testSetAndGetProduct() {
        WholesaleProduct product = new WholesaleProduct(12526, "Fifa 26", "EASports", "PS5 game", new Money(70, "euro"));
        orderLine.setProduct(product);
        assertEquals(product, orderLine.getProduct());
    }
}
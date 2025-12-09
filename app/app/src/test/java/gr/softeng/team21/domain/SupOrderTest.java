package gr.softeng.team21.domain;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;


public class SupOrderTest {

    SupOrder sup;
    ArrayList<OrderLine> orderProducts = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        OrderLine orderLine =
                new OrderLine(
                        new WholesaleProduct(2526, "Fifa 26", "EASports", "PS5 game",
                                new Money(70, "euro")),
                        10,
                        null);

        OrderLine orderLine2 =
                new OrderLine(
                        new WholesaleProduct(22526, "Fifa 25", "EASports", "PS5 game",
                                new Money(70, "euro")),
                        10,
                        null);

        orderProducts.add(orderLine);
        orderProducts.add(orderLine2);

        sup = new SupOrder(new Date(), 123,  Admin.getInstance(), orderProducts);
    }


    @Test
    public void testFullAmount() {
        Money result = sup.fullAmount();

        BigDecimal expected = new BigDecimal(0)
                .add(orderProducts.get(0).totalBill())
                .add(orderProducts.get(1).totalBill());

        assertEquals(expected, result.getAmount());
        assertEquals("euro", result.getCurrency());
    }


    @Test
    public void testSetAndGetAdmin() {
        Admin admin = Admin.getInstance();
        sup.setAdmin(admin);
        assertEquals(admin, sup.getAdmin());
    }


    @Test
    public void testSetAndGetId() {
        int expectedId = 555;
        sup.setId(expectedId);
        assertEquals(expectedId, sup.getId());
    }


    @Test
    public void testSetAndGetDate() {
        Date now = new Date();
        sup.setDate(now);
        assertEquals(now, sup.getDate());
    }


    @Test
    public void testConstructorStoresOrderProducts() {
        assertEquals(orderProducts, sup.orderProducts);
        assertEquals(2, sup.orderProducts.size());
    }
}

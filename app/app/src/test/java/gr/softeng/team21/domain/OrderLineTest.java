package gr.softeng.team21.domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class OrderLineTest {

    OrderLine orderLine;

    ArrayList<OrderLine> orderProducts = new ArrayList<>();


    @Before
    public void setUp() throws Exception {

        orderLine = new OrderLine(new WholesaleProduct(2526, "Fifa 26","EASports","PS5 game", new Money(70 , "euro")) , 10 , new SupOrder(new Date() , 002 , Admin.getInstance() , orderProducts));
        orderProducts.add(orderLine);

    }

    @Test
    public void totalBill() {
        for(OrderLine ord : orderProducts){
            BigDecimal bill = ord.totalBill();
            assertEquals(bill , (ord.getProduct().getPrice().multiply(ord.getQuantity()).getAmount()));
        }
    }

    @Test
    public void testSetAndGetQuantity() {
        int expected = 50;
        orderLine.setQuantity(expected);
        assertEquals(expected, orderLine.getQuantity());
    }

    @Test
    public void testSetAndGetOrder() {
        SupOrder order = new SupOrder(new Date() , 005 , Admin.getInstance(), orderProducts);
        orderLine.setOrder(order);
        assertEquals(order, orderLine.getOrder());
    }


    @Test
    public void testSetAndGetProduct() {
        WholesaleProduct product = new WholesaleProduct(12526, "Fifa 26","EASports","PS5 game", new Money(70 , "euro"));
        orderLine.setProduct(product);
        assertEquals(product, orderLine.getProduct());
    }
}
package gr.softeng.team21.domain;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.memorydao.CustomerDAOMemory;

public class ShoppingCartTest {
    private ShoppingCart shoppingCart;
    private Customer customer;
    private ProductType p1, p2, p3;


    @Before
    public void setUp() throws Exception {
        customer = TestHelper.getCustomer();
        shoppingCart = new ShoppingCart(customer);
        p1 = TestHelper.getLaptop();
        shoppingCart.addItem(new CartItem(p1, 2));
        p2 = TestHelper.getMouse();
        shoppingCart.addItem(new CartItem(p2, 3));
    }

    @Test
    public void getTotalCostWhenAddProduct() {
        p3 = TestHelper.getKeyboard();
        shoppingCart.addItem(new CartItem(p3, 3));

        assertEquals(1390, shoppingCart.getTotalCost().getAmount().intValue());
        assertEquals("€", shoppingCart.getTotalCost().getCurrency());
    }

    @Test
    public void getTotalCostWhenRemoveProduct() {
        shoppingCart.removeItem(shoppingCart.getItems().get(0));


        assertEquals(150, shoppingCart.getTotalCost().getAmount().intValue());
        assertEquals("€", shoppingCart.getTotalCost().getCurrency());
    }


    @AfterClass
    public static void tearDownAfterClass() {
        CustomerDAOMemory.getInstance().getCustomers().clear();
    }
}
package gr.softeng.team21.domain;

import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;
import org.junit.After;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.util.Money;

/**
 * Unit tests for the {@link CartItem} class.
 * Verifies that subtotal calculations update correctly when quantity or price changes,
 * and checks the functionality of getters/setters and constructors.
 * @author PAVLOS GRATSANIS
 */
public class CartItemTest {
    private CartItem cartItem;
    private ProductType laptop;

    /**
     * Sets up the test environment before each test.
     * Initializes a CartItem with a specific product (Laptop) and quantity (3).
     */
    @Before
    public void setUp() throws Exception {
        laptop = TestHelper.getLaptop();
        cartItem = new CartItem(laptop, 3);
    }

    /**
     * Tests that the initial subtotal calculation is correct based on price and quantity.
     */
    @Test
    public void calculateSubtotal() {
        assertEquals(new BigDecimal(1500), cartItem.getSubtotal_amount().getAmount());
    }

    /**
     * Tests that the subtotal updates correctly when the quantity is increased.
     */
    @Test
    public void calculateSubtotalWhenQuantityIncreased() {
        cartItem.setQuantity(cartItem.getQuantity() + 2);
        assertEquals(2500, cartItem.getSubtotal_amount().getAmount().intValue());
    }

    /**
     * Tests that the subtotal updates correctly when the quantity is decreased.
     */
    @Test
    public void calculateSubtotalWhenQuantityDecreased() {
        cartItem.setQuantity(cartItem.getQuantity() - 2);
        assertEquals(new BigDecimal(500), cartItem.getSubtotal_amount().getAmount());
    }

    /**
     * Tests that the subtotal reflects changes in the product's price.
     * Note: This assumes CartItem recalculates subtotal upon quantity set,
     * or implies checking dynamic price changes if logic allows.
     */
    @Test
    public void calculateSubtotalWhenChangePrice() {
        laptop.setPrice(new Money(2000, "€"));
        cartItem.setQuantity(3);

        assertEquals(new BigDecimal(6000), cartItem.getSubtotal_amount().getAmount());
    }

    /**
     * Tests all getters and setters to ensure fields are correctly assigned and retrieved.
     */
    @Test
    public void testAllGettersAndSetters() {
        cartItem.setId(5);
        assertEquals(5, cartItem.getId());

        cartItem.setQuantity(10);
        assertEquals(10, cartItem.getQuantity());

        assertEquals(5000, cartItem.getSubtotal_amount().getAmount().intValue());
        assertEquals(laptop, cartItem.getProductType());
    }

    /**
     * Tests the second constructor which initializes only with ProductType.
     * Verifies default values (quantity 0, subtotal null).
     */
    @Test
    public void testSecondConstructor() {
        CartItem item2 = new CartItem(laptop);
        assertNotNull(item2);
        assertEquals(laptop, item2.getProductType());
        assertEquals(0, item2.getQuantity());
        assertNull(item2.getSubtotal_amount());
    }

    /**
     * Resets the environment after each test.
     * Restores the Laptop price to its default value to avoid side effects on other tests.
     */
    @After
    public void tearDown() throws Exception {
        TestHelper.getLaptop().setPrice(new Money(500, "€"));
    }
}
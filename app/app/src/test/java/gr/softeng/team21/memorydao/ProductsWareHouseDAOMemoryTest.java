package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.util.Money;

/**
 * Unit tests for the {@link ProductsWareHouseDAOMemory} class.
 * This suite validates the inventory and warehouse management logic, ensuring
 * atomic operations for stock updates, validation of quantities, and correct
 * handling of product persistence in memory.
 * @author Γιάννης Μονοχολιάς
 */
public class ProductsWareHouseDAOMemoryTest {
    ProductType product1;
    private ProductsWareHouseDAOMemory wareHouse;

    /**
     * Sets up the testing environment before each test.
     * Initializes the warehouse singleton, clears existing state, and prepares
     * a sample product for testing.
     */
    @Before
    public void setUp() {
        wareHouse = ProductsWareHouseDAOMemory.getInstance();
        wareHouse.clear();
        product1 = new ProductType("p1", "Laptop", new Money(1000,"€"), "product1245");
    }

    /**
     * Verifies that the warehouse starts with an empty product stock map.
     */
    @Test
    public void testGetProductsInitiallyEmptyTest() {
        assertTrue(wareHouse.getProductStocks().isEmpty());
    }

    /**
     * Verifies the correct implementation of the Singleton pattern.
     */
    @Test
    public void getInstanceReturnsSameReferencesTest() {
        ProductsWareHouseDAOMemory wareHouse2 = ProductsWareHouseDAOMemory.getInstance();
        assertSame(wareHouse2,wareHouse);
    }

    /**
     * Verifies that passing a null product to {@code getProductStock}
     * results in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getProductStock_NullArgumentTest() {
        // Null argument passed
        wareHouse.getProductStock(null);
    }

    /**
     * Verifies that requesting stock for a product not registered in the
     * warehouse returns null.
     */
    @Test
    public void getProductStock_NonRegisteredProductTest() {
        // case where getProductStock's argument is not in the wareHouse
        assertNull(wareHouse.getProductStock(product1));
    }

    /**
     * Tests the successful registration of a product in the warehouse.
     */
    @Test
    public void getProductStockSuccessTest() {
        wareHouse.insertProduct(product1);
        assertTrue(wareHouse.getProductStocks().containsKey(product1));
    }

    /**
     * Verifies that inserting a null product throws an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertProduct_NullArgumentTest() {
        // Null argument passed
        wareHouse.insertProduct(null);
    }

    /**
     * Verifies that the system prevents duplicate registration of the same product.
     */
    @Test(expected = IllegalArgumentException.class)
    public void insertProduct_AlreadyExistingProductTest() {
        // case where insertProduct's argument is already in the wareHouse
        wareHouse.insertProduct(product1);
        wareHouse.insertProduct(product1);
    }

    /**
     * Tests successful product insertion and map integrity.
     */
    @Test
    public void insertProductSuccessTest() {
        wareHouse.insertProduct(product1);
        assertTrue(wareHouse.getProductStocks().containsKey(product1));
    }

    /**
     * Verifies that deleting a null product throws an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteProduct_NullArgumentTest() {
        // Null argument passed
        wareHouse.deleteProduct(null);
    }

    /**
     * Verifies that attempting to delete a product not present in the warehouse
     * throws a {@link NoSuchElementException}.
     */
    @Test(expected = NoSuchElementException.class)
    public void deleteProduct_NonRegisteredProductTest() {
        // case where deleteProduct's argument is not in the wareHouse
        wareHouse.deleteProduct(product1);
    }

    /**
     * Tests successful stock increase for a registered product.
     */
    @Test
    public void increaseStockSuccessTest() {
        wareHouse.insertProduct(product1);
        assertTrue(wareHouse.increaseProductStock(product1, 15));
        Assertions.assertEquals(15, wareHouse.getProductStock(product1));
    }

    /**
     * Tests stock reduction when sufficient quantity is available.
     */
    @Test
    public void decreaseStockSufficientTest() {
        wareHouse.insertProduct(product1);
        wareHouse.increaseProductStock(product1, 15);

        assertTrue(wareHouse.decreaseProductStock(product1, 5));
        Assertions.assertEquals(10, wareHouse.getProductStock(product1));
    }

    /**
     * Verifies that stock reduction fails if the requested amount exceeds
     * available inventory, and ensures stock remains unchanged.
     */
    @Test
    public void decreaseStockInsufficientTest() {
        wareHouse.insertProduct(product1);
        wareHouse.increaseProductStock(product1, 10);

        assertFalse(wareHouse.decreaseProductStock(product1, 20));
        Assertions.assertEquals(10, wareHouse.getProductStock(product1)); // stock did not change
    }

    /**
     * Verifies that negative amounts are rejected for stock increase.
     */
    @Test
    public void increaseStockInvalidAmountTest() {
        wareHouse.insertProduct(product1);
        assertFalse(wareHouse.increaseProductStock(product1, -1));
    }

    /**
     * Verifies that negative amounts are rejected for stock decrease.
     */
    @Test
    public void decreaseStockInvalidAmountTest() {
        wareHouse.insertProduct(product1);
        assertFalse(wareHouse.decreaseProductStock(product1, -1));
    }

    /**
     * Verifies that stock operations fail gracefully for unregistered products.
     */
    @Test
    public void increaseDecreaseStockNonRegisteredProductTest() {
        ProductType otherProduct = new ProductType("p2", "Phone", new Money(500,"€"), "product5678");
        assertFalse(wareHouse.increaseProductStock(otherProduct, 5));
        assertFalse(wareHouse.decreaseProductStock(otherProduct, 5));
    }

    /**
     * Tests the boundary checks for stock availability.
     */
    @Test
    public void sufficientStock_TrueFalseTest() {
        wareHouse.insertProduct(product1);
        wareHouse.increaseProductStock(product1,10);

        assertTrue(wareHouse.sufficientStock(product1, 9));
        assertFalse(wareHouse.sufficientStock(product1, 11));
    }

    /**
     * Tests the validation logic for input quantities (must be positive).
     */
    @Test
    public void isValidAmount_TrueFalseTest() {
        assertTrue(wareHouse.isValidAmount(5));
        assertTrue(wareHouse.isValidAmount(1));
        assertFalse(wareHouse.isValidAmount(0));
        assertFalse(wareHouse.isValidAmount(-3));
        assertFalse(wareHouse.isValidAmount(-1));
    }

    /**
     * Verifies correct retrieval of the entire inventory map and its values.
     */
    @Test
    public void getProductStocksTest() {
        wareHouse.insertProduct(product1);
        wareHouse.increaseProductStock(product1,10);

        assertEquals(1, wareHouse.getProductStocks().size());
        assertEquals(10, (int)wareHouse.getProductStocks().get(product1));
    }

    /**
     * Tests the configuration of the maximum warehouse capacity.
     */
    @Test
    public void testSetAndGetMaxCapacity() {
        //default max capacity
        assertEquals(1000, wareHouse.getMaxCapacity());

        //increase max capacity
        wareHouse.setMaxCapacity(2000);
        assertEquals(2000, wareHouse.getMaxCapacity());
    }

    /**
     * Verifies that the clear operation successfully resets the warehouse state.
     */
    @Test
    public void clear() {
        wareHouse.insertProduct(product1);

        // Before clear
        assertFalse(wareHouse.getProductStocks().isEmpty());
        wareHouse.clear();
        //After clear
        assertTrue(wareHouse.getProductStocks().isEmpty());
    }

    /**
     * Ensures repository cleanup after each test.
     */
    @After
    public void tearDownTest() {
        wareHouse.clear();
    }
}
package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletionException;

import static org.junit.Assert.*;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.util.Money;

/**
 * Unit tests for the {@link ProductsWareHouseDAOMemory} class.
 * This suite validates the inventory and warehouse management logic, ensuring
 * atomic operations for stock updates, validation of quantities, and correct
 * handling of product persistence in memory.
 * Utilizes assertThrows to accurately validate CompletableFuture exceptions.
 * @author
 */
public class ProductsWareHouseDAOMemoryTest {

    private ProductsWareHouseDAOMemory wareHouse;
    private ProductType product1;

    /**
     * Initializes the testing environment before each test.
     * @throws Exception if setup fails.
     */
    @Before
    public void setUp() throws Exception {
        wareHouse = ProductsWareHouseDAOMemory.getInstance();

        // Clear memory, while waiting for asynchronous completion
        wareHouse.clear().join();

        product1 = new ProductType("p1", "Laptop", new Money(1000, "€"), "product1245");
    }

    /**
     * Cleans up the in-memory warehouse map after each test to ensure isolation.
     * @throws Exception if teardown fails.
     */
    @After
    public void tearDown() throws Exception {
        wareHouse.clear().join();
    }

    @Test
    public void testGetProductsInitiallyEmptyTest() {
        HashMap<ProductType, Integer> stocks = wareHouse.getProductStocks().join();
        assertTrue(stocks.isEmpty());
    }

    @Test
    public void getInstanceReturnsSameReferencesTest() {
        ProductsWareHouseDAOMemory wareHouse2 = ProductsWareHouseDAOMemory.getInstance();
        assertSame(wareHouse2, wareHouse);
    }

    @Test
    public void getProductStock_NullArgumentTest() {
        CompletionException exception = assertThrows(CompletionException.class, () -> {
            wareHouse.getProductStock(null).join();
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("type argument cannot be null", exception.getCause().getMessage());
    }

    @Test
    public void getProductStock_NonRegisteredProductTest() {
        assertNull(wareHouse.getProductStock(product1).join());
    }

    @Test
    public void insertProductSuccessTest() {
        wareHouse.insertProduct(product1).join();
        HashMap<ProductType, Integer> stocks = wareHouse.getProductStocks().join();

        assertTrue(stocks.containsKey(product1));
        assertEquals(0, (int) stocks.get(product1)); // Αρχικό απόθεμα πρέπει να είναι 0
    }

    @Test
    public void insertProduct_NullArgumentTest() {
        CompletionException exception = assertThrows(CompletionException.class, () -> {
            wareHouse.insertProduct(null).join();
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("type argument cannot be null", exception.getCause().getMessage());
    }

    @Test
    public void insertProduct_AlreadyExistingProductTest() {
        wareHouse.insertProduct(product1).join();

        CompletionException exception = assertThrows(CompletionException.class, () -> {
            wareHouse.insertProduct(product1).join();
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("The provided type already exists in stock", exception.getCause().getMessage());
    }

    @Test
    public void deleteProduct_NullArgumentTest() {
        CompletionException exception = assertThrows(CompletionException.class, () -> {
            wareHouse.deleteProduct(null).join();
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("type argument cannot be null", exception.getCause().getMessage());
    }

    @Test
    public void deleteProduct_NonRegisteredProductTest() {
        CompletionException exception = assertThrows(CompletionException.class, () -> {
            wareHouse.deleteProduct(product1).join();
        });

        assertTrue(exception.getCause() instanceof NoSuchElementException);
        assertEquals("Product not in stock", exception.getCause().getMessage());
    }

    @Test
    public void increaseStockSuccessTest() {
        wareHouse.insertProduct(product1).join();
        assertTrue(wareHouse.increaseProductStock(product1, 15).join());
        assertEquals(15, (int) wareHouse.getProductStock(product1).join());
    }

    @Test
    public void decreaseStockSufficientTest() {
        wareHouse.insertProduct(product1).join();
        wareHouse.increaseProductStock(product1, 15).join();

        assertTrue(wareHouse.decreaseProductStock(product1, 5).join());
        assertEquals(10, (int) wareHouse.getProductStock(product1).join());
    }

    @Test
    public void decreaseStockInsufficientTest() {
        wareHouse.insertProduct(product1).join();
        wareHouse.increaseProductStock(product1, 10).join();

        assertFalse(wareHouse.decreaseProductStock(product1, 20).join());
        assertEquals(10, (int) wareHouse.getProductStock(product1).join()); // Το απόθεμα δεν πρέπει να αλλάξει
    }

    @Test
    public void increaseStockInvalidAmountTest() {
        wareHouse.insertProduct(product1).join();
        assertFalse(wareHouse.increaseProductStock(product1, -1).join());
    }

    @Test
    public void decreaseStockInvalidAmountTest() {
        wareHouse.insertProduct(product1).join();
        assertFalse(wareHouse.decreaseProductStock(product1, -1).join());
    }

    @Test
    public void increaseDecreaseStockNonRegisteredProductTest() {
        ProductType otherProduct = new ProductType("p2", "Phone", new Money(500, "€"), "product5678");
        assertFalse(wareHouse.increaseProductStock(otherProduct, 5).join());
        assertFalse(wareHouse.decreaseProductStock(otherProduct, 5).join());
    }

    @Test
    public void sufficientStock_TrueFalseTest() {
        wareHouse.insertProduct(product1).join();
        wareHouse.increaseProductStock(product1, 10).join();

        assertTrue(wareHouse.sufficientStock(product1, 9).join());
        assertFalse(wareHouse.sufficientStock(product1, 11).join());
    }

    @Test
    public void isValidAmount_TrueFalseTest() {
        assertTrue(wareHouse.isValidAmount(5).join());
        assertTrue(wareHouse.isValidAmount(1).join());
        assertFalse(wareHouse.isValidAmount(0).join());
        assertFalse(wareHouse.isValidAmount(-3).join());
        assertFalse(wareHouse.isValidAmount(-1).join());
    }

    @Test
    public void testSetAndGetMaxCapacity() {
        // default max capacity
        assertEquals(1000, (int) wareHouse.getMaxCapacity().join());

        // increase max capacity
        wareHouse.setMaxCapacity(2000).join();
        assertEquals(2000, (int) wareHouse.getMaxCapacity().join());
    }

    /**
     * Final cleanup of the singleton instance's data after all tests in the class have finished.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        ProductsWareHouseDAOMemory.getInstance().clear().join();
    }
}
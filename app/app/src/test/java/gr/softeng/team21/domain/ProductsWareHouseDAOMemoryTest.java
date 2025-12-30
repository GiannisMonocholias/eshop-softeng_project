package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;

public class ProductsWareHouseDAOMemoryTest {
    ProductType product1;
    private ProductsWareHouseDAOMemory wareHouse;

    @Before
    public void setUp() {
        wareHouse = ProductsWareHouseDAOMemory.getInstance();
        wareHouse.clear();
        product1 = new ProductType("p1", "Laptop", new Money(1000,"€"), "product1245");
    }

    @Test
    public void testGetProductsInitiallyEmptyTest() {
        assertTrue(wareHouse.getProductStocks().isEmpty());
    }

    @Test
    public void getInstanceReturnsSameReferencesTest() {
        ProductsWareHouseDAOMemory wareHouse2 = ProductsWareHouseDAOMemory.getInstance();
        assertSame(wareHouse2,wareHouse);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getProductStock_NullArgumentTest() {
        // Null argument passed
        wareHouse.getProductStock(null);
    }

    @Test
    public void getProductStock_NonRegisteredProductTest() {
        // case where getProductStock's argument is not in the wareHouse
        assertNull(wareHouse.getProductStock(product1));
    }


    @Test
    public void getProductStockSuccessTest(){
        wareHouse.insertProduct(product1);

        assertTrue(wareHouse.getProductStocks().containsKey(product1));
    }



    @Test(expected = IllegalArgumentException.class)
    public void insertProduct_NullArgumentTest() {
        // Null argument passed
        wareHouse.insertProduct(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void insertProduct_AlreadyExistingProductTest() {
        // case where insertProduct's argument is already in the wareHouse
        wareHouse.insertProduct(product1);
        wareHouse.insertProduct(product1);
    }

    @Test
    public void insertProductSuccessTest() {
        wareHouse.insertProduct(product1);
        assertTrue(wareHouse.getProductStocks().containsKey(product1));
    }


    @Test(expected = IllegalArgumentException.class)
    public void deleteProduct_NullArgumentTest() {
        // Null argument passed
        wareHouse.deleteProduct(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteProduct_NonRegisteredProductTest() {
        // case where deleteProduct's argument is not in the wareHouse
        wareHouse.deleteProduct(product1);
    }


    @Test
    public void increaseStockSuccessTest() {
        wareHouse.insertProduct(product1);
        assertTrue(wareHouse.increaseProductStock(product1, 15));
        Assertions.assertEquals(15, wareHouse.getProductStock(product1));
    }

    @Test
    public void decreaseStockSufficientTest() {
        wareHouse.insertProduct(product1);
        wareHouse.increaseProductStock(product1, 15);

        assertTrue(wareHouse.decreaseProductStock(product1, 5));
        Assertions.assertEquals(10, wareHouse.getProductStock(product1));
    }

    @Test
    public void decreaseStockInsufficientTest() {
        wareHouse.insertProduct(product1);
        wareHouse.increaseProductStock(product1, 10);

        assertFalse(wareHouse.decreaseProductStock(product1, 20));
        Assertions.assertEquals(10, wareHouse.getProductStock(product1)); // stock did not change
    }

    @Test
    public void increaseStockInvalidAmountTest() {
        wareHouse.insertProduct(product1);
        assertFalse(wareHouse.increaseProductStock(product1, -1));
    }

    @Test
    public void decreaseStockInvalidAmountTest() {
        wareHouse.insertProduct(product1);
        assertFalse(wareHouse.decreaseProductStock(product1, -1));
    }

    @Test
    public void increaseDecreaseStockNonRegisteredProductTest() {
        ProductType otherProduct = new ProductType("p2", "Phone", new Money(500,"€"), "product5678");
        assertFalse(wareHouse.increaseProductStock(otherProduct, 5));
        assertFalse(wareHouse.decreaseProductStock(otherProduct, 5));
    }


    @Test
    public void sufficientStock_TrueFalseTest() {
        wareHouse.insertProduct(product1);
        wareHouse.increaseProductStock(product1,10);

        assertTrue(wareHouse.sufficientStock(product1, 9));
        assertFalse(wareHouse.sufficientStock(product1, 11));
    }

    @Test
    public void isValidAmount_TrueFalseTest() {
        assertTrue(wareHouse.isValidAmount(5));
        assertTrue(wareHouse.isValidAmount(1));
        assertFalse(wareHouse.isValidAmount(0));
        assertFalse(wareHouse.isValidAmount(-3));
        assertFalse(wareHouse.isValidAmount(-1));
    }

    @Test
    public void getProductStocksTest() {
        wareHouse.insertProduct(product1);
        wareHouse.increaseProductStock(product1,10);

        assertEquals(1, wareHouse.getProductStocks().size());
        assertEquals(10, (int)wareHouse.getProductStocks().get(product1));
    }

    @Test
    public void testSetAndGetMaxCapacity() {
        //default max capacity
        assertEquals(1000, wareHouse.getMaxCapacity());

        //increase max capacity
        wareHouse.setMaxCapacity(2000);
        assertEquals(2000, wareHouse.getMaxCapacity());
    }


    @Test
    public void clear() {
        wareHouse.insertProduct(product1);

        // Before clear
        assertFalse(wareHouse.getProductStocks().isEmpty());
        wareHouse.clear();
        //After clear
        assertTrue(wareHouse.getProductStocks().isEmpty());
    }

    @After
    public void tearDownTest(){
        wareHouse.clear();
    }
}
package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.CompletionException;

import gr.softeng.team21.util.Money;
import gr.softeng.team21.domain.ProductType;

/**
 * Unit tests for the {@link ProductTypeDAOMemory} class.
 * This suite verifies the asynchronous persistence logic for product definitions within the
 * catalogue, covering operations such as addition, retrieval, deletion, and
 * modification of product metadata in memory using CompletableFuture without try-catch blocks.
 * @author PAVLOS GRATSANIS
 */
public class ProductTypeDAOMemoryTest {
    private ProductTypeDAOMemory typesRepository;
    private ProductType product1;

    /**
     * Initializes the testing environment before each test.
     * Obtains the singleton instance, clears any previous state asynchronously, and prepares
     * a sample product for validation.
     */
    @Before
    public void setUp() {
        typesRepository = ProductTypeDAOMemory.getInstance();
        typesRepository.clear().join();
        product1 = new ProductType("p1", "Laptop", new Money(1000,"€"), "product1245");
    }

    /**
     * Verifies that the repository is empty upon initialization or after a clear operation.
     */
    @Test
    public void testGetProductsInitiallyEmpty() {
        assertTrue(typesRepository.getProducts().join().isEmpty());
    }

    /**
     * Verifies that {@link ProductTypeDAOMemory} correctly implements the
     * Singleton pattern by returning the same instance reference.
     */
    @Test
    public void getInstanceReturnsSameReferences() {
        ProductTypeDAOMemory typesRepository2 = ProductTypeDAOMemory.getInstance();
        assertSame(typesRepository, typesRepository2);
    }

    /**
     * Tests the successful asynchronous retrieval of a product using its unique product code.
     */
    @Test
    public void getProductTestSuccess() {
        typesRepository.addProductType(product1).join();

        ProductType returnedProduct = typesRepository.getProduct("product1245").join();
        assertTrue(typesRepository.getProducts().join().containsKey("product1245"));
        assertSame(returnedProduct, typesRepository.getProduct("product1245").join());
    }

    /**
     * Verifies that searching for a non-existing product code returns null asynchronously.
     */
    @Test
    public void getProductNonExistingProductTest() {
        typesRepository.addProductType(product1).join();

        ProductType returnedProduct1 = typesRepository.getProduct("product1244").join();
        assertNull(returnedProduct1);
    }

    /**
     * Verifies that providing a null product code to {@code getProduct}
     * results in a CompletionException wrapping an IllegalArgumentException.
     */
    @Test(expected = CompletionException.class)
    public void getProductNullArgumentTest() {
        typesRepository.addProductType(product1).join();

        typesRepository.getProduct(null).join();
    }

    /**
     * Tests the successful asynchronous addition of a new product type to the repository.
     */
    @Test
    public void addProductSuccessTest() {
        typesRepository.addProductType(product1).join();
        assertEquals(product1, typesRepository.getProduct("product1245").join());
    }

    /**
     * Verifies that attempting to add a null product type throws a CompletionException.
     */
    @Test(expected = CompletionException.class)
    public void addProductNullArgumentTest() {
        typesRepository.addProductType(null).join();
    }

    /**
     * Verifies that the system prevents the registration of the same
     * product type more than once, throwing a CompletionException.
     */
    @Test(expected = CompletionException.class)
    public void addProductAlreadyRegisteredTest() {
        typesRepository.addProductType(product1).join();
        typesRepository.addProductType(product1).join();
    }

    /**
     * Tests the successful asynchronous removal of a product type from the repository.
     */
    @Test
    public void SuccessDeleteProductTypeTest() {
        typesRepository.addProductType(product1).join();

        typesRepository.deleteProductType(product1).join();
        assertNull(typesRepository.getProduct("product1245").join());
    }

    /**
     * Verifies that attempting to delete a null product type results in a CompletionException.
     */
    @Test(expected = CompletionException.class)
    public void deleteProductNullArgumentTest() {
        typesRepository.deleteProductType(null).join();
    }

    /**
     * Verifies that attempting to delete a product type that is not registered
     * results in a CompletionException.
     */
    @Test(expected = CompletionException.class)
    public void deleteProductNonRegisteredTest() {
        typesRepository.deleteProductType(product1).join();
    }

    /**
     * Tests the successful asynchronous update of an existing product's metadata
     * (name, price, description) while keeping the same product code.
     */
    @Test
    public void processProductSuccessTest() {
        typesRepository.addProductType(product1).join();
        ProductType updatedProduct = new ProductType("p2", "Gaming Laptop", new Money(1500,"€"), "product1245");

        typesRepository.processProduct(updatedProduct).join();

        ProductType updatedProductTest = typesRepository.getProduct("product1245").join();
        assertEquals("p2", updatedProductTest.getProductname());
        assertEquals(1500, updatedProductTest.getPrice().getAmount().intValue());
        assertEquals("€", updatedProductTest.getPrice().getCurrency());
        assertEquals("Gaming Laptop", updatedProductTest.getDescription());
    }

    /**
     * Verifies that passing a null argument to the update process throws a CompletionException.
     */
    @Test(expected = CompletionException.class)
    public void processProductNullArgumentTest() {
        typesRepository.processProduct(null).join();
    }

    /**
     * Verifies that attempting to update a product that does not exist in
     * the repository throws a CompletionException wrapping an IllegalStateException.
     */
    @Test(expected = CompletionException.class)
    public void processProductNonRegisteredTest() {
        typesRepository.processProduct(product1).join();
    }

    /**
     * Verifies the growth and reduction of the product collection during
     * various lifecycle events asynchronously.
     */
    @Test
    public void getProducts() {
        typesRepository.addProductType(product1).join();
        assertEquals(1, typesRepository.getProducts().join().size());

        ProductType product2 = new ProductType("p2", "Gaming Laptop", new Money(1500,"€"),"product1246");
        typesRepository.addProductType(product2).join();
        assertEquals(2, typesRepository.getProducts().join().size());

        typesRepository.deleteProductType(product1).join();
        assertEquals(1, typesRepository.getProducts().join().size());

        typesRepository.deleteProductType(product2).join();
        assertEquals(0, typesRepository.getProducts().join().size());
    }

    /**
     * Verifies that the {@code clear} method successfully resets the repository state asynchronously.
     */
    @Test
    public void clear() {
        typesRepository.clear().join();
        assertEquals(0, typesRepository.getProducts().join().size());
    }

    /**
     * Ensures clean state isolation by clearing the repository after each test.
     */
    @After
    public void tearDownTest(){
        typesRepository.clear().join();
    }
}
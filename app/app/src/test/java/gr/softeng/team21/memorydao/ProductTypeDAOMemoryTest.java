package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.util.Money;
import gr.softeng.team21.domain.ProductType;

/**
 * Unit tests for the {@link ProductTypeDAOMemory} class.
 * This suite verifies the persistence logic for product definitions within the
 * catalogue, covering operations such as addition, retrieval, deletion, and
 * modification of product metadata in memory.
 * @author Γιάννης Μονοχολιάς
 */
public class ProductTypeDAOMemoryTest {
    private ProductTypeDAOMemory typesRepository;
    private ProductType product1;

    /**
     * Initializes the testing environment before each test.
     * Obtains the singleton instance, clears any previous state, and prepares
     * a sample product for validation.
     */
    @Before
    public void setUp() {
        typesRepository = ProductTypeDAOMemory.getInstance();

        typesRepository.clear();

        product1 = new ProductType("p1", "Laptop", new Money(1000,"€"), "product1245");
    }

    /**
     * Verifies that the repository is empty upon initialization or after a clear operation.
     */
    @Test
    public void testGetProductsInitiallyEmpty() {
        assertTrue(typesRepository.getProducts().isEmpty());
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
     * Tests the successful retrieval of a product using its unique product code.
     */
    @Test
    public void getProductTestSuccess() {
        typesRepository.addProductType(product1);
        ProductType returnedProduct = ProductTypeDAOMemory.getInstance().getProduct("product1245");
        assertTrue(ProductTypeDAOMemory.getInstance().getProducts().containsKey("product1245"));
        assertSame(returnedProduct, ProductTypeDAOMemory.getInstance().getProduct("product1245"));
    }

    /**
     * Verifies that searching for a non-existing product code returns null.
     */
    @Test
    public void getProductNonExistingProductTest(){
        typesRepository.addProductType(product1);
        //Non existing product type
        ProductType returnedProduct1 = ProductTypeDAOMemory.getInstance().getProduct("product1244");
        assertNull(returnedProduct1);
    }

    /**
     * Verifies that providing a null product code to {@code getProduct}
     * results in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getProductNullArgumentTest(){
        typesRepository.addProductType(product1);
        //Null productCode argument
        ProductType returnedProduct2 = ProductTypeDAOMemory.getInstance().getProduct(null);
    }

    /**
     * Tests the successful addition of a new product type to the repository.
     */
    @Test
    public void addProductSuccessTest() {
        typesRepository.addProductType(product1);

        assertEquals(product1,typesRepository.getProduct("product1245"));
    }

    /**
     * Verifies that attempting to add a null product type throws
     * an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addProductNullArgumentTest() {
        // null argument passed
        typesRepository.addProductType(null);
    }

    /**
     * Verifies that the system prevents the registration of the same
     * product type more than once.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addProductAlreadyRegisteredTest() {
        // Already registered productType
        typesRepository.addProductType(product1);
        typesRepository.addProductType(product1);
    }

    /**
     * Tests the successful removal of a product type from the repository.
     */
    @Test
    public void SuccessDeleteProductTypeTest() {
        typesRepository.addProductType(product1);

        typesRepository.deleteProductType(product1);
        assertNull(typesRepository.getProduct("product1245"));
    }

    /**
     * Verifies that attempting to delete a null product type results
     * in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteProductNullArgumentTest() {
        // null argument passed
        typesRepository.deleteProductType(null);
    }

    /**
     * Verifies that attempting to delete a product type that is not registered
     * results in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteProductNonRegisteredTest() {
        // Non registered productType
        typesRepository.deleteProductType(product1);
    }

    /**
     * Tests the successful update of an existing product's metadata
     * (name, price, description) while keeping the same product code.
     */
    @Test
    public void processProductSuccessTest() {
        typesRepository.addProductType(product1);
        ProductType updatedProduct = new ProductType("p2", "Gaming Laptop", new Money(1500,"€"), "product1245");

        typesRepository.processProduct(updatedProduct);


        ProductType updatedProductTest = typesRepository.getProduct("product1245");
        assertEquals("p2", updatedProductTest.getProductname());
        assertEquals(1500, updatedProductTest.getPrice().getAmount().intValue());
        assertEquals("€", updatedProductTest.getPrice().getCurrency());
        assertEquals("Gaming Laptop", updatedProductTest.getDescription());
    }

    /**
     * Verifies that passing a null argument to the update process
     * throws an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void processProductNullArgumentTest() {
        // null argument passed
        typesRepository.processProduct(null);
    }

    /**
     * Verifies that attempting to update a product that does not exist in
     * the repository throws an {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void processProductNonRegisteredTest() {
        // Non registered productType

        typesRepository.processProduct(product1);
    }

    /**
     * Verifies the growth and reduction of the product collection during
     * various lifecycle events (addition and deletion).
     */
    @Test
    public void getProducts() {
        //product1 ProductType is constructed in the setUp method
        typesRepository.addProductType(product1);
        assertEquals(1, typesRepository.getProducts().size());


        ProductType product2 = new ProductType("p2", "Gaming Laptop", new Money(1500,"€"),"product1246");
        typesRepository.addProductType(product2);
        assertEquals(2, typesRepository.getProducts().size());


        typesRepository.deleteProductType(product1);
        assertEquals(1, typesRepository.getProducts().size());

        typesRepository.deleteProductType(product2);
        assertEquals(0, typesRepository.getProducts().size());
    }

    /**
     * Verifies that the {@code clear} method successfully resets the repository state.
     */
    @Test
    public void clear() {
        typesRepository.clear();
        assertEquals(0, typesRepository.getProducts().size());
    }

    /**
     * Ensures clean state isolation by clearing the repository after each test.
     */
    @After
    public void tearDownTest(){
        typesRepository.clear();
    }
}
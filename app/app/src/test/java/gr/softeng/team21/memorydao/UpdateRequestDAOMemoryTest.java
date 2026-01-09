package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.domain.AllowedRequest;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;
import gr.softeng.team21.domain.ProductType;

/**
 * Unit tests for the {@link UpdateRequestDAOMemory} class.
 * This suite verifies the in-memory persistence of catalogue update requests,
 * ensuring that requests for product insertions, updates, and deletions are
 * correctly stored, retrieved, and managed.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateRequestDAOMemoryTest {
    ProductType product1;
    CatalogueUpdateRequest insertRequest;
    UpdateRequestDAOMemory requestsRepository;

    /**
     * Initializes the testing environment before each test.
     * Obtains the singleton instance, clears any previous data, and populates
     * the repository with an initial sample update request.
     * @throws Exception if setup fails.
     */
    @Before
    public void setUp() throws Exception {
        requestsRepository = UpdateRequestDAOMemory.getInstance();
        requestsRepository.clear();

        product1 = new ProductType ("Laptop Dell", "High End",  new Money( 500, "€" ), "product1245");
        insertRequest = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product1, AllowedRequest.INSERT_PRODUCT, 1);
        requestsRepository.addUpdateRequest(insertRequest);
    }

    /**
     * Verifies that the {@link UpdateRequestDAOMemory} correctly implements
     * the Singleton pattern by returning the same object reference.
     */
    @Test
    public void getInstanceReturnsSameReferences() {
        UpdateRequestDAOMemory requestsRepositoyry2 = UpdateRequestDAOMemory.getInstance();
        assertSame(requestsRepository, requestsRepositoyry2);
    }

    /**
     * Verifies that searching for a request ID that does not exist
     * in the repository returns null.
     */
    @Test
    public void getUpdateRequestNonExistingRequestTest() {
        // Non existing request
        assertNull(requestsRepository.getUpdateRequest(5));
    }

    /**
     * Verifies that the repository prevents adding a request with an ID
     * that is already registered, throwing an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addUpdateRequest_AlreadyExistingRequestTest(){
        // Already existing request (με ίδιο ID)
        requestsRepository.addUpdateRequest(insertRequest);
    }

    /**
     * Verifies that attempting to add a null request to the repository
     * results in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addUpdateRequest_NullArgumentTest(){
        // Null argument passed in addUpdateRequest() method
        requestsRepository.addUpdateRequest(null);
    }

    /**
     * Tests the successful addition and retrieval of a valid update request.
     */
    @Test
    public void addUpdateRequestSuccessTest(){
        ProductType product2 = new ProductType ("Laptop Lenovo", "High End",  new Money ( 600, "€" ), "product1246");
        CatalogueUpdateRequest insertRequest2 = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product2, AllowedRequest.INSERT_PRODUCT, 2);
        requestsRepository.addUpdateRequest(insertRequest2);
        assertSame(insertRequest2, requestsRepository.getUpdateRequest(2));
    }

    /**
     * Verifies that attempting to delete a request that is not registered
     * in the system results in an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteUpdateRequest_NonRegisteredRequestTest() {
        CatalogueUpdateRequest insertRequest2 = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product1, AllowedRequest.DELETE_PRODUCT, 2);
        // Trying to delete a request that is not registered in the repository
        requestsRepository.deleteUpdateRequest(insertRequest2);
    }

    /**
     * Verifies that the delete method throws an {@link IllegalArgumentException}
     * when provided with a null argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteUpdateRequest_NullArgumentTest() {
        // The request argument must not be null
        requestsRepository.deleteUpdateRequest(null);
    }

    /**
     * Verifies the growth and reduction of the update request collection
     * as items are added and removed.
     */
    @Test
    public void getUpdateRequests() {
        assertEquals(1,requestsRepository.getUpdateRequests().size());

        ProductType product2 = new ProductType ("Laptop Lenovo", "High End",  new Money ( 600, "€" ), "product1246");
        CatalogueUpdateRequest insertRequest2 = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product2, AllowedRequest.INSERT_PRODUCT, 2);
        requestsRepository.addUpdateRequest(insertRequest2);

        assertEquals(2,requestsRepository.getUpdateRequests().size());



        requestsRepository.deleteUpdateRequest(insertRequest2);
        assertEquals(1,requestsRepository.getUpdateRequests().size());

        requestsRepository.deleteUpdateRequest(insertRequest);
        assertEquals(0,requestsRepository.getUpdateRequests().size());

    }

    /**
     * Verifies that the {@code clear} method successfully wipes all
     * stored requests from the memory.
     */
    @Test
    public void clear() {
        requestsRepository.clear();
        assertEquals(0, requestsRepository.getUpdateRequests().size());
    }

    /**
     * Ensures repository cleanup after each test to maintain state isolation.
     */
    @After
    public void tearDownTest() {
        requestsRepository.clear();
    }
}
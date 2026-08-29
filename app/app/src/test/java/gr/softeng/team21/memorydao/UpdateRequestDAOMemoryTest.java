package gr.softeng.team21.memorydao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.concurrent.CompletionException;

import gr.softeng.team21.domain.AllowedRequest;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;
import gr.softeng.team21.domain.ProductType;

/**
 * Unit tests for the {@link UpdateRequestDAOMemory} class.
 * This suite verifies the asynchronous in-memory persistence of catalogue update requests,
 * ensuring that requests are correctly stored, updated, retrieved via Foreign Keys, and managed.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateRequestDAOMemoryTest {

    private ProductType product1;
    private CatalogueUpdateRequest insertRequest;
    private UpdateRequestDAOMemory requestsRepository;

    /**
     * Initializes the testing environment before each test.
     * Obtains the singleton instance, clears any previous data asynchronously, and populates
     * the repository with an initial sample update request.
     */
    @Before
    public void setUp() {
        requestsRepository = UpdateRequestDAOMemory.getInstance();
        requestsRepository.clear().join();

        product1 = new ProductType("Laptop Dell", "High End", new Money(500, "€"), "product1245");
        insertRequest = new CatalogueUpdateRequest(new Date(1,12,2025), "Insert laptop", product1, AllowedRequest.INSERT_PRODUCT, 1);
        requestsRepository.addUpdateRequest(insertRequest).join();
    }

    /**
     * Verifies that the updateRequest method successfully overwrites an existing document's properties
     * without throwing duplication errors.
     */
    @Test
    public void updateRequestSuccessTest() {
        insertRequest.setUpdateDescription("Updated Description");
        requestsRepository.updateRequest(insertRequest).join();

        CatalogueUpdateRequest fetched = requestsRepository.getUpdateRequest(1).join();
        assertEquals("Updated Description", fetched.getUpdateDescription());
    }

    /**
     * Verifies that the indexed query simulation efficiently returns only the requests
     * assigned to a specific employee ID using the assignedEmployeeId Foreign Key.
     */
    @Test
    public void getRequestsByEmployeeIdReturnsCorrectListTest() {
        insertRequest.setAssignedEmployeeId("EMP-100");
        requestsRepository.updateRequest(insertRequest).join();

        ArrayList<CatalogueUpdateRequest> assigned = requestsRepository.getRequestsByEmployeeId("EMP-100").join();
        assertEquals(1, assigned.size());
        assertEquals("EMP-100", assigned.get(0).getAssignedEmployeeId());

        ArrayList<CatalogueUpdateRequest> emptyList = requestsRepository.getRequestsByEmployeeId("EMP-999").join();
        assertTrue(emptyList.isEmpty());
    }

    /**
     * Ensures repository cleanup after each test to maintain state isolation.
     */
    @After
    public void tearDownTest() {
        requestsRepository.clear().join();
    }
}
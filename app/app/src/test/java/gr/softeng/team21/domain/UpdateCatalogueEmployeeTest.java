package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.ProductsWareHouseDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;
import gr.softeng.team21.util.Date;
import gr.softeng.team21.util.Money;

/**
 * Unit tests for the {@link UpdateCatalogueEmployee} class.
 * This suite verifies the lifecycle of catalogue update requests, including
 * request assignment to employees and the execution of product insertions,
 * deletions, and modifications in the system's repository.
 * @author Γιάννης Μονοχολιάς
 */
public class UpdateCatalogueEmployeeTest {

    private UpdateCatalogueEmployee employee;
    private CatalogueUpdateRequest insertRequest;
    private CatalogueUpdateRequest deleteRequest;
    private CatalogueUpdateRequest processRequest;
    private ProductType product1;
    private ProductType productUpdated;

    /**
     * Initializes the testing environment before each test case.
     * Clears repositories and sets up sample products and update requests
     * (Insert, Delete, Process) to be used in the tests.
     */
    @Before
    public void setUp(){
        ProductTypeDAOMemory.getInstance().clear();
        ProductsWareHouseDAOMemory.getInstance().clear();
        UpdateRequestDAOMemory.getInstance().clear();
        EmployeeDAOMemory.getInstance().clear();

        product1 = new ProductType ("Laptop Dell", "High End",  new Money( 500, "€" ), "product1245");
        productUpdated = new ProductType("Laptop Dell Pro","High End",new Money(800,"€"),"product1245");

        insertRequest = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product1, AllowedRequest.INSERT_PRODUCT, 1);

        deleteRequest = new CatalogueUpdateRequest(new Date(2,12,2025),
                "Delete laptop", product1, AllowedRequest.DELETE_PRODUCT, 2);

        processRequest = new CatalogueUpdateRequest(new Date(3,12,2025),
                "Update laptop details", productUpdated, AllowedRequest.PROCESS_PRODUCT, 3);


        UpdateRequestDAOMemory.getInstance().addUpdateRequest(insertRequest);
        UpdateRequestDAOMemory.getInstance().addUpdateRequest(deleteRequest);
        UpdateRequestDAOMemory.getInstance().addUpdateRequest(processRequest);

        employee =new UpdateCatalogueEmployee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"OPE_1",100,1000,8,
                EmployeeState.ACTIVE, new Date(3,5,2025));
    }

    /**
     * Verifies that a new employee starts with zero recorded catalogue updates.
     */
    @Test
    public void getTotalCatalogueUpdatesInitiallyZeroTest() {
        assertEquals(0, employee.getTotalCatalogueUpdates());
    }

    /**
     * Tests the successful assignment of a pending request to the employee.
     */
    @Test
    public void assignRequestSuccessTest() {
        boolean result = employee.assignRequest(1);
        assertTrue(result);
        assertEquals(UpdateRequestDAOMemory.getInstance().getUpdateRequest(1), employee.selectRequest(1));
    }

    /**
     * Verifies that attempting to assign a non-existing request ID returns false.
     */
    @Test
    public void assignRequest_NonExistingRequestTest() {
        // The request does not exist in UpdateRequestsRepository
        boolean result = employee.assignRequest(99);
        assertFalse(result);
    }

    /**
     * Verifies that a request cannot be assigned to the same employee more than once.
     */
    @Test
    public void assignRequest_ReassignFailsTest() {
        // The request is already assigned to the employee
        employee.assignRequest(1);

        // Trying to reassign the request
        assertFalse(employee.assignRequest(1));
    }

    /**
     * Verifies that the employee cannot select a request if it has not
     * been previously assigned to them.
     */
    @Test
    public void SelectRequestReturnsNullIfNotAssignedTest() {
        assertNull(employee.selectRequest(2)); // δεν έχει γίνει assign
    }

    /**
     * Tests the execution of an "INSERT_PRODUCT" request.
     * Checks if the product is correctly added to the ProductType repository.
     */
    @Test
    public void ExecuteUpdateInsertProductTest() {
        employee.executeUpdate(insertRequest);
        assertTrue(ProductTypeDAOMemory.getInstance().getProducts().containsKey(insertRequest.getProduct().getProductCode()));
    }

    /**
     * Tests the execution of a "DELETE_PRODUCT" request.
     * Checks if the product is correctly removed from the repository.
     */
    @Test
    public void ExecuteUpdateDeleteProductTest() {

        ProductTypeDAOMemory.getInstance().addProductType(product1);
        assertTrue(ProductTypeDAOMemory.getInstance().getProducts().containsKey(deleteRequest.getProduct().getProductCode()));

        employee.executeUpdate(deleteRequest);
        assertFalse(ProductTypeDAOMemory.getInstance().getProducts().containsKey(deleteRequest.getProduct().getProductCode()));
    }

    /**
     * Tests the execution of a "PROCESS_PRODUCT" (Update) request.
     * Verifies that the product's attributes (name, price, description) are updated
     * while maintaining the same product code.
     */
    @Test
    public void testExecuteUpdateProcessProduct() {
        ProductTypeDAOMemory.getInstance().addProductType(product1);
        employee.executeUpdate(processRequest);


        ProductType result = ProductTypeDAOMemory.getInstance().getProduct("product1245");

        assertEquals("Laptop Dell Pro", result.getProductname());
        assertEquals(new Money(800, "€"), result.getPrice());
        assertEquals("High End", result.getDescription());
        assertEquals("Update laptop details", processRequest.getUpdateDescription());

    }

    /**
     * Verifies that executing an update with a null request results in
     * an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void ExecuteWithIllegalArgumentTest() {
        employee.executeUpdate(null);
    }

    /**
     * Cleans up all memory repositories after each test to maintain state isolation.
     */
    @After
    public void tearDownTest(){
        ProductTypeDAOMemory.getInstance().clear();
        ProductsWareHouseDAOMemory.getInstance().clear();
        UpdateRequestDAOMemory.getInstance().clear();
        EmployeeDAOMemory.getInstance().clear();
    }
}
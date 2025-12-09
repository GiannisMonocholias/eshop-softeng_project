package gr.softeng.team21.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UpdateCatalogueEmployeeTest {

    private UpdateCatalogueEmployee employee;
    private CatalogueUpdateRequest insertRequest;
    private CatalogueUpdateRequest deleteRequest;
    private CatalogueUpdateRequest processRequest;
    private ProductType product1;
    private ProductType productUpdated;

    @Before
    public void setUp(){
        ProductTypesRepository.getInstance().clear();
        ProductsWareHouse.getInstance().clear();
        UpdateRequestsRepository.getInstance().clear();
        EmployeeRepository.getInstance().clear();

        product1 = new ProductType ("Laptop Dell", "High End",  new Money ( 500, "€" ), "product1245");
        productUpdated = new ProductType("Laptop Dell Pro","High End",new Money(800,"€"),"product1245");

        insertRequest = new CatalogueUpdateRequest(new Date(1,12,2025),
                "Insert laptop", product1, AllowedRequest.INSERT_PRODUCT, 1);

        deleteRequest = new CatalogueUpdateRequest(new Date(2,12,2025),
                "Delete laptop", product1, AllowedRequest.DELETE_PRODUCT, 2);

        processRequest = new CatalogueUpdateRequest(new Date(3,12,2025),
                "Update laptop details", productUpdated, AllowedRequest.PROCESS_PRODUCT, 3);


        UpdateRequestsRepository.getInstance().addUpdateRequest(insertRequest);
        UpdateRequestsRepository.getInstance().addUpdateRequest(deleteRequest);
        UpdateRequestsRepository.getInstance().addUpdateRequest(processRequest);

        employee =new UpdateCatalogueEmployee("GP","Giorgos","abcd123","Papadopoulos","3029761482",
                new EmailAddress("GP@gmail.com"),"OPE_1",100,1000,8,
                EmployeeState.ACTIVE, new Date(3,5,2025));
    }


    @Test
    public void getTotalCatalogueUpdatesInitiallyZeroTest() {
        assertEquals(0, employee.getTotalCatalogueUpdates());
    }


    @Test
    public void assignRequestSuccessTest() {
        boolean result = employee.assignRequest(1);
        assertTrue(result);
        assertEquals(UpdateRequestsRepository.getInstance().getUpdateRequest(1), employee.selectRequest(1));
    }



    @Test
    public void assignRequest_NonExistingRequestTest() {
        // The request does not exist in UpdateRequestsRepository
        boolean result = employee.assignRequest(99);
        assertFalse(result);
    }

    @Test
    public void assignRequest_ReassignFailsTest() {
        // The request is already assigned to the employee
        employee.assignRequest(1);

        // Trying to reassign the request
        assertFalse(employee.assignRequest(1));
    }


    @Test
    public void SelectRequestReturnsNullIfNotAssignedTest() {
        assertNull(employee.selectRequest(2)); // δεν έχει γίνει assign
    }


    @Test
    public void ExecuteUpdateInsertProductTest() {
        employee.executeUpdate(insertRequest);
        assertTrue(ProductTypesRepository.getInstance().getProducts().containsKey(insertRequest.getProduct().getProductCode()));
    }

    @Test
    public void ExecuteUpdateDeleteProductTest() {

        ProductTypesRepository.getInstance().addProductType(product1);
        assertTrue(ProductTypesRepository.getInstance().getProducts().containsKey(deleteRequest.getProduct().getProductCode()));

        employee.executeUpdate(deleteRequest);
        assertFalse(ProductTypesRepository.getInstance().getProducts().containsKey(deleteRequest.getProduct().getProductCode()));
    }

    @Test
    public void testExecuteUpdateProcessProduct() {
        ProductTypesRepository.getInstance().addProductType(product1);
        employee.executeUpdate(processRequest);


        ProductType result = ProductTypesRepository.getInstance().getProduct("product1245");

        assertEquals("Laptop Dell Pro", result.getProductname());
        assertEquals(new Money(800, "€"), result.getPrice());
        assertEquals("High End", result.getDescription());
        assertEquals("Update laptop details", processRequest.getUpdateDescription());

    }


    @Test(expected = IllegalArgumentException.class)
    public void ExecuteWithIllegalArgumentTest() {
        employee.executeUpdate(null);
    }

    @After
    public void tearDownTest(){
        ProductTypesRepository.getInstance().clear();
        ProductsWareHouse.getInstance().clear();
        UpdateRequestsRepository.getInstance().clear();
        EmployeeRepository.getInstance().clear();
    }

}
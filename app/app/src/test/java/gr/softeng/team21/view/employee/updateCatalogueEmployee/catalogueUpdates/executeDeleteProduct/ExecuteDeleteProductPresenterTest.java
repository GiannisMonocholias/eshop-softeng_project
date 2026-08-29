package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Unit tests for {@link ExecuteDeleteProductPresenter}.
 * This suite verifies the full workflow of deleting a product based on an approved request asynchronously.
 * Ensures proper data loading, user confirmation triggering, and synchronized state updates across
 * product and request DAOs using Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteDeleteProductPresenterTest {

    private ExecuteDeleteProductPresenter presenter;
    private ExecuteDeleteProductViewStub viewStub;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int DELETE_REQUEST_ID = 4;
    private static final String PRODUCT_CODE = "TECH-015";

    /**
     * Initializes the testing environment before each test.
     * Prepares memory data asynchronously, sets up the presenter with injected dependencies,
     * and assigns a specific delete request to the test employee via Foreign Key.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new ExecuteDeleteProductViewStub();

        presenter = new ExecuteDeleteProductPresenter(
                viewStub,
                EmployeeDAOMemory.getInstance(),
                UpdateRequestDAOMemory.getInstance(),
                ProductTypeDAOMemory.getInstance()
        );

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequest(DELETE_REQUEST_ID).join();
        if (request != null) {
            request.setAssignedEmployeeId(EMPLOYEE_ID);
            request.setStatus(RequestStatusType.ASSIGNED);
            UpdateRequestDAOMemory.getInstance().updateRequest(request).join();
        }
    }

    /**
     * Verifies that the product details associated with the delete request
     * are correctly loaded asynchronously and passed to the view for display.
     */
    @Test
    public void loadRequestDetailsValidDataSetsViewDetails() {
        presenter.loadRequestDetails(EMPLOYEE_ID, DELETE_REQUEST_ID);

        Assert.assertEquals("Samsung 990 Pro 1TB", viewStub.getName());
        Assert.assertEquals(PRODUCT_CODE, viewStub.getCode());
    }

    /**
     * Verifies the successful asynchronous execution of the product deletion:
     * 1. Confirms the product exists before deletion using .join().
     * 2. Executes deletion and verifies the product is removed from the catalogue.
     * 3. Checks if the request status transitions to SERVED.
     * 4. Confirms success feedback is dispatched to the user interface.
     */
    @Test
    public void onDeleteConfirmedSuccessDeletesProductAndUpdatesRequest() {
        presenter.loadRequestDetails(EMPLOYEE_ID, DELETE_REQUEST_ID);
        Assert.assertNotNull(ProductTypeDAOMemory.getInstance().getProduct(PRODUCT_CODE).join());

        presenter.onDeleteConfirmed();

        // Verification of product removal
        ProductType deletedProduct = ProductTypeDAOMemory.getInstance().getProduct(PRODUCT_CODE).join();
        Assert.assertNull(deletedProduct);

        // Request lifecycle verification
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequest(DELETE_REQUEST_ID).join();
        Assert.assertEquals(RequestStatusType.SERVED, request.getStatus());

        // View feedback verification
        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));
    }

    /**
     * Clears shared memory state to maintain test isolation.
     */
    @After
    public void tearDown() {
        EmployeeDAOMemory.getInstance().clear();
        ProductTypeDAOMemory.getInstance().clear();
        UpdateRequestDAOMemory.getInstance().clear().join();
    }
}
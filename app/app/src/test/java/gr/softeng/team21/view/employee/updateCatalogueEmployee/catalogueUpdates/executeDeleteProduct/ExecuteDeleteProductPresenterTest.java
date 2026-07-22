package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeDeleteProduct;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Unit tests for {@link ExecuteDeleteProductPresenter}.
 * This suite verifies the full workflow of deleting a product based on an approved request asynchronously,
 * ensuring proper data loading, user confirmation, and synchronized updates across
 * product and request repositories using Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteDeleteProductPresenterTest {

    private ExecuteDeleteProductPresenter presenter;
    private ExecuteDeleteProductViewStub viewStub;
    private UpdateCatalogueEmployee catEmployee;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int DELETE_REQUEST_ID = 4;
    private static final String PRODUCT_CODE = "TECH-015";

    /**
     * Initializes the testing environment before each test.
     * Prepares memory data asynchronously, sets up the presenter with injected dependencies,
     * and assigns a specific delete request to the test employee.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new ExecuteDeleteProductViewStub();
        presenter = new ExecuteDeleteProductPresenter(
                viewStub,
                EmployeeDAOMemory.getInstance(),
                UpdateRequestDAOMemory.getInstance(),
                ProductTypeDAOMemory.getInstance()
        );

        // Fetch objects using .join() due to the CompletableFuture architecture
        catEmployee = (UpdateCatalogueEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID).join();
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().join().get(DELETE_REQUEST_ID);

        catEmployee.assignRequest(request.getId());
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
        Assert.assertTrue(viewStub.getPrice().contains("109.9"));
    }

    /**
     * Verifies that attempting to load a non-existing request results in
     * an error message in the view asynchronously.
     */
    @Test
    public void loadRequestDetailsInvalidIdShowsError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, -999);
        Assert.assertTrue(viewStub.getErrorMessage().contains("Σφάλμα"));
    }

    /**
     * Tests if clicking the delete button triggers the confirmation dialog.
     */
    @Test
    public void onDeleteButtonClickedShowsConfirmationDialog() {
        presenter.loadRequestDetails(EMPLOYEE_ID, DELETE_REQUEST_ID);
        presenter.onDeleteButtonClicked();

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
    }

    /**
     * Verifies the successful asynchronous execution of the product deletion:
     * 1. Confirms the product exists before deletion using .join().
     * 2. Executes deletion and verifies the product is removed from the catalogue.
     * 3. Checks if the request status transitions to SERVED.
     * 4. Ensures the request is removed from the employee's active assigned tasks.
     * 5. Confirms success feedback to the user.
     */
    @Test
    public void onDeleteConfirmedSuccessDeletesProductAndUpdatesRequest() {
        presenter.loadRequestDetails(EMPLOYEE_ID, DELETE_REQUEST_ID);

        // Pre-condition check (assuming ProductTypeDAOMemory has been adapted to CompletableFuture)
        Assert.assertNotNull(ProductTypeDAOMemory.getInstance().getProduct(PRODUCT_CODE).join());

        presenter.onDeleteConfirmed();

        // Verification of product removal
        ProductType deletedProduct = ProductTypeDAOMemory.getInstance().getProduct(PRODUCT_CODE).join();
        Assert.assertNull("Το προϊόν έπρεπε να έχει διαγραφεί", deletedProduct);

        // Request lifecycle verification
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().join().get(DELETE_REQUEST_ID);
        Assert.assertEquals(RequestStatusType.SERVED, request.getStatus());

        // Employee task list cleanup verification
        Assert.assertFalse(catEmployee.getAssignedRequests().containsKey(DELETE_REQUEST_ID));

        // View feedback verification
        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));
    }
}
package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

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
 * Unit tests for {@link ExecuteProcessProductPresenter}.
 * This suite verifies the update workflow for existing products, ensuring data integrity,
 * correct price validation, and synchronized status updates between products
 * and administrative requests.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteProcessProductPresenterTest {

    private ExecuteProcessProductPresenter presenter;
    private ExecuteProcessProductViewStub viewStub;
    private UpdateCatalogueEmployee catEmployee;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int PROCESS_REQUEST_ID = 2;
    private static final String PRODUCT_CODE = "TECH-004";

    /**
     * Initializes the test environment, prepares memory data, and simulates
     * a request assignment before each test case.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new ExecuteProcessProductViewStub();
        presenter = new ExecuteProcessProductPresenter(viewStub, EmployeeDAOMemory.getInstance(),
                UpdateRequestDAOMemory.getInstance(), ProductTypeDAOMemory.getInstance()
        );

        catEmployee = (UpdateCatalogueEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(PROCESS_REQUEST_ID);
        catEmployee.assignRequest(request.getId());
    }

    /**
     * Verifies that existing product details and request descriptions are
     * correctly loaded into the edit form.
     */
    @Test
    public void loadRequestDetailsValidDataDisplaysDetails() {
        presenter.loadRequestDetails(EMPLOYEE_ID, PROCESS_REQUEST_ID);

        Assert.assertEquals("Razer DeathAdder V3", viewStub.getProductName());
        Assert.assertEquals(PRODUCT_CODE, viewStub.getProductCode());
        Assert.assertTrue(viewStub.getProductPrice().contains("79.9"));
        Assert.assertTrue(viewStub.getDescriptionShown().contains("Διόρθωση τυπογραφικού λάθους στα DPI του αισθητήρα."));
    }

    /**
     * Verifies that error handling works for non-existent request IDs.
     */
    @Test
    public void loadRequestDetailsInvalidDataShowsError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, -999);
        Assert.assertTrue(viewStub.getErrorMessage().contains("Σφάλμα"));
    }

    /**
     * Verifies that a valid price input leads to the confirmation dialog
     * instead of an error.
     */
    @Test
    public void onSaveClickedValidPriceShowsConfirmation() {
        presenter.loadRequestDetails(EMPLOYEE_ID, PROCESS_REQUEST_ID);

        viewStub.setPriceInput("1150.50");

        presenter.onSaveClicked();

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertEquals("", viewStub.getErrorField());
    }

    /**
     * Verifies that non-numeric price inputs trigger a validation error.
     */
    @Test
    public void onSaveClickedInvalidPriceShowsInputError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, PROCESS_REQUEST_ID);

        viewStub.setPriceInput("invalid_price");

        presenter.onSaveClicked();

        Assert.assertEquals("price", viewStub.getErrorField());
        Assert.assertFalse(viewStub.isConfirmationDialogShown());
    }

    /**
     * Verifies that negative price inputs are rejected.
     */
    @Test
    public void onSaveClickedNegativePriceShowsInputError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, PROCESS_REQUEST_ID);

        viewStub.setPriceInput("-100");

        presenter.onSaveClicked();

        Assert.assertEquals("price", viewStub.getErrorField());
        Assert.assertFalse(viewStub.isConfirmationDialogShown());
    }

    /**
     * Verifies the complete update workflow:
     * 1. Updates the product details in the catalogue.
     * 2. Sets the request status to SERVED.
     * 3. Removes the task from the employee's assigned list.
     */
    @Test
    public void onSaveConfirmedUpdatesProductAndRequest() {
        presenter.loadRequestDetails(EMPLOYEE_ID, PROCESS_REQUEST_ID);

        String newName = "Razer DeathAdder V3 updated";
        String newDesc = "Updated Description";
        String newPrice = "1100.0";

        viewStub.setNameInput(newName);
        viewStub.setDescInput(newDesc);
        viewStub.setPriceInput(newPrice);

        presenter.onSaveConfirmed();

        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));

        // Verify catalogue persistence
        ProductType updatedProduct = ProductTypeDAOMemory.getInstance().getProduct(PRODUCT_CODE);
        Assert.assertEquals(newName, updatedProduct.getProductname());
        Assert.assertEquals(newDesc, updatedProduct.getDescription());
        Assert.assertEquals(1100.0, updatedProduct.getPrice().getAmount().doubleValue(), 0.001);

        // Verify request state
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(PROCESS_REQUEST_ID);
        Assert.assertEquals(RequestStatusType.SERVED, request.getStatus());

        // Verify employee task cleanup
        Assert.assertFalse(catEmployee.getAssignedRequests().containsKey(PROCESS_REQUEST_ID));
    }
}
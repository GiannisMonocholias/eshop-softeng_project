package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeProcessProduct;

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
 * Unit tests for {@link ExecuteProcessProductPresenter}.
 * This suite verifies the asynchronous update workflow for existing products, ensuring data integrity,
 * correct price validation, and synchronized status updates between product DAOs
 * and request DAOs without relying on deprecated domain collections.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteProcessProductPresenterTest {

    private ExecuteProcessProductPresenter presenter;
    private ExecuteProcessProductViewStub viewStub;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int PROCESS_REQUEST_ID = 2;
    private static final String PRODUCT_CODE = "TECH-004";

    /**
     * Initializes the test environment, prepares memory data asynchronously, and simulates
     * a request assignment before each test case using the new Foreign Key DAO logic.
     * @throws Exception if data initialization fails.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new ExecuteProcessProductViewStub();
        presenter = new ExecuteProcessProductPresenter(
                viewStub,
                EmployeeDAOMemory.getInstance(),
                UpdateRequestDAOMemory.getInstance(),
                ProductTypeDAOMemory.getInstance()
        );

        // Fetch request and assign it to the employee asynchronously using the DAO
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequest(PROCESS_REQUEST_ID).join();
        if (request != null) {
            request.setAssignedEmployeeId(EMPLOYEE_ID);
            request.setStatus(RequestStatusType.ASSIGNED);
            UpdateRequestDAOMemory.getInstance().updateRequest(request).join();
        }
    }

    /**
     * Verifies that existing product details and admin request descriptions are
     * correctly loaded asynchronously from the DAOs and pushed to the UI form.
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
     * Verifies that error handling works gracefully for non-existent request IDs
     * without crashing the application.
     */
    @Test
    public void loadRequestDetailsInvalidDataShowsError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, -999);
        Assert.assertTrue(viewStub.getErrorMessage().contains("Σφάλμα"));
    }

    /**
     * Verifies that a valid numeric price input successfully passes validation
     * and triggers the confirmation dialog.
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
     * Verifies that non-numeric price inputs (e.g., text) trigger a specific validation error
     * preventing further processing.
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
     * Verifies that negative price inputs are strictly rejected by the validation logic.
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
     * Verifies the complete asynchronous update workflow:
     * 1. Updates the product details inside the ProductTypeDAO.
     * 2. Overwrites the request status as SERVED inside the UpdateRequestDAO.
     * 3. Confirms UI success feedback generation.
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

        // Verify catalogue persistence asynchronously with .join()
        ProductType updatedProduct = ProductTypeDAOMemory.getInstance().getProduct(PRODUCT_CODE).join();
        Assert.assertEquals(newName, updatedProduct.getProductname());
        Assert.assertEquals(newDesc, updatedProduct.getDescription());
        Assert.assertEquals(1100.0, updatedProduct.getPrice().getAmount().doubleValue(), 0.001);

        // Verify request state transition
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequest(PROCESS_REQUEST_ID).join();
        Assert.assertEquals(RequestStatusType.SERVED, request.getStatus());
    }

    /**
     * Clears shared memory state to maintain strict test isolation.
     */
    @After
    public void tearDown() {
        EmployeeDAOMemory.getInstance().clear();
        ProductTypeDAOMemory.getInstance().clear();
        UpdateRequestDAOMemory.getInstance().clear().join();
    }
}
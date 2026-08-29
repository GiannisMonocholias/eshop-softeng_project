package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

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
 * Unit tests for {@link ExecuteInsertProductPresenter}.
 * This suite verifies the administrative task of inserting a new product into the catalogue asynchronously,
 * ensuring correct request handling, data validation for prices, and final record persistence
 * using mock DAOs without tight coupling to the Domain collections.
 *
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteInsertProductPresenterTest {

    private ExecuteInsertProductPresenter presenter;
    private ExecuteInsertProductViewStub viewStub;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int INSERT_REQUEST_ID = 5;

    /**
     * Initializes the testing environment before each test.
     * Populates memory repositories asynchronously, instantiates the presenter with injected dependencies,
     * and assigns an insertion request to the employee via Foreign Key logic.
     *
     * @throws Exception if data initialization fails.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new ExecuteInsertProductViewStub();
        presenter = new ExecuteInsertProductPresenter(
                viewStub,
                EmployeeDAOMemory.getInstance(),
                UpdateRequestDAOMemory.getInstance(),
                ProductTypeDAOMemory.getInstance()
        );

        // Fetch request and assign it to the employee asynchronously using the DAO
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequest(INSERT_REQUEST_ID).join();
        if (request != null) {
            request.setAssignedEmployeeId(EMPLOYEE_ID);
            request.setStatus(RequestStatusType.ASSIGNED);
            UpdateRequestDAOMemory.getInstance().updateRequest(request).join();
        }
    }

    /**
     * Verifies that the description of the insertion request is correctly loaded asynchronously
     * and passed to the view to assist the employee.
     */
    @Test
    public void loadRequestDetailsDisplaysDescription() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);
        Assert.assertTrue(viewStub.getRequestDescription().contains("εκτυπωτή"));
    }

    /**
     * Verifies that errors are gracefully handled when attempting to load request
     * details with invalid employee or request IDs asynchronously.
     */
    @Test
    public void loadRequestDetailsInvalidDataShowsError() {
        presenter.loadRequestDetails("INVALID_EMP_ID", INSERT_REQUEST_ID);
        Assert.assertTrue(viewStub.getErrorMessage().contains("Σφάλμα"));

        viewStub.showError(""); // Clear message for next test phase

        presenter.loadRequestDetails(EMPLOYEE_ID, -999);
        Assert.assertTrue(viewStub.getErrorMessage().contains("Σφάλμα"));
    }

    /**
     * Verifies the successful asynchronous product insertion workflow:
     * 1. Valid input data is mocked in the view stub.
     * 2. The presenter saves the new product directly to the ProductTypeDAO.
     * 3. The associated catalogue update request transitions to SERVED status.
     * 4. A success message is triggered in the view.
     */
    @Test
    public void onConfirmInsertValidDataSuccess() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);

        viewStub.setCodeInput("TECH-NEW-01");
        viewStub.setNameInput("Canon Pixma TS3450");
        viewStub.setPriceInput("55.90");
        viewStub.setDescInput("Πολυμηχάνημα Inkjet");

        presenter.onConfirmInsert();

        // Verification of success message dispatched to the UI
        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));

        // Domain persistence verification using synchronous join()
        ProductType newProduct = ProductTypeDAOMemory.getInstance().getProduct("TECH-NEW-01").join();
        Assert.assertNotNull("Το νέο προϊόν έπρεπε να υπάρχει στο DAO", newProduct);
        Assert.assertEquals("Canon Pixma TS3450", newProduct.getProductname());
        Assert.assertEquals(55.90, newProduct.getPrice().getAmount().doubleValue(), 0.001);

        // Request lifecycle verification
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequest(INSERT_REQUEST_ID).join();
        Assert.assertEquals(RequestStatusType.SERVED, request.getStatus());
    }

    /**
     * Verifies that the presenter rejects invalid price formats (e.g., text instead of numbers)
     * and triggers a specific field-level validation error on the UI.
     */
    @Test
    public void onConfirmInsertInvalidPriceShowsInputError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);

        viewStub.setCodeInput("TECH-FAIL");
        viewStub.setNameInput("Fail Product");
        viewStub.setPriceInput("abc"); // Invalid Number Format
        viewStub.setDescInput("Desc");

        presenter.onConfirmInsert();

        Assert.assertEquals("price", viewStub.getInputErrorField());
    }

    /**
     * Verifies that the presenter rejects negative price values as invalid
     * and prevents persistence to the database.
     */
    @Test
    public void onConfirmInsertNegativePriceShowsInputError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);

        viewStub.setCodeInput("TECH-FAIL");
        viewStub.setNameInput("Fail Product");
        viewStub.setPriceInput("-10.0"); // Invalid Business Logic (Negative Price)
        viewStub.setDescInput("Desc");

        presenter.onConfirmInsert();

        Assert.assertEquals("price", viewStub.getInputErrorField());
    }

    /**
     * Clears shared memory state to maintain test isolation.
     */
    @After
    public void tearDown() {
        EmployeeDAOMemory.getInstance().clear();
        UpdateRequestDAOMemory.getInstance().clear().join();
        ProductTypeDAOMemory.getInstance().clear();
    }
}
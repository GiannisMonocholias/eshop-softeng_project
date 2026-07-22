package gr.softeng.team21.view.employee.updateCatalogueEmployee.catalogueUpdates.executeInsertProduct;

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
 * Unit tests for {@link ExecuteInsertProductPresenter}.
 * This suite verifies the administrative task of inserting a new product into the catalogue asynchronously,
 * ensuring correct request handling, data validation for prices, and final record persistence.
 * @author Γιάννης Μονοχολιάς
 */
public class ExecuteInsertProductPresenterTest {

    private ExecuteInsertProductPresenter presenter;
    private ExecuteInsertProductViewStub viewStub;
    private UpdateCatalogueEmployee catEmployee;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int INSERT_REQUEST_ID = 5;

    /**
     * Initializes the testing environment before each test.
     * Populates memory repositories asynchronously, instantiates the presenter with injected dependencies,
     * and simulates the assignment of an insertion request to the employee.
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

        // Fetch objects using .join() due to the CompletableFuture architecture
        catEmployee = (UpdateCatalogueEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID).join();
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().join().get(INSERT_REQUEST_ID);

        catEmployee.assignRequest(request.getId());
    }

    /**
     * Verifies that the description of the insertion request is correctly loaded asynchronously
     * and displayed to help the employee understand what product to insert.
     */
    @Test
    public void loadRequestDetailsDisplaysDescription() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);
        Assert.assertTrue(viewStub.getRequestDescription().contains("εκτυπωτή"));
    }

    /**
     * Verifies that errors are handled correctly when attempting to load request
     * details with invalid IDs asynchronously.
     */
    @Test
    public void loadRequestDetailsInvalidDataShowsError() {
        presenter.loadRequestDetails("INVALID_EMP_ID", INSERT_REQUEST_ID);
        Assert.assertTrue(viewStub.getErrorMessage().contains("Σφάλμα"));

        viewStub.showError(""); // Clear message for next part

        presenter.loadRequestDetails(EMPLOYEE_ID, -999);
        Assert.assertTrue(viewStub.getErrorMessage().contains("Σφάλμα"));
    }

    /**
     * Verifies the successful asynchronous product insertion workflow:
     * 1. Valid data is provided through the view.
     * 2. The presenter saves the new product to the ProductTypeDAO.
     * 3. The request status is updated to SERVED.
     * 4. The request is removed from the employee's pending task list.
     */
    @Test
    public void onConfirmInsertValidDataSuccess() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);

        viewStub.setCodeInput("TECH-NEW-01");
        viewStub.setNameInput("Canon Pixma TS3450");
        viewStub.setPriceInput("55.90");
        viewStub.setDescInput("Πολυμηχάνημα Inkjet");

        presenter.onConfirmInsert();

        // Verification of success message
        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));

        // Domain persistence verification via .join()
        ProductType newProduct = ProductTypeDAOMemory.getInstance().getProduct("TECH-NEW-01").join();
        Assert.assertNotNull("Το νέο προϊόν έπρεπε να υπάρχει στο DAO", newProduct);
        Assert.assertEquals("Canon Pixma TS3450", newProduct.getProductname());
        Assert.assertEquals(55.90, newProduct.getPrice().getAmount().doubleValue(), 0.001);

        // Request lifecycle verification
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().join().get(INSERT_REQUEST_ID);
        Assert.assertEquals(RequestStatusType.SERVED, request.getStatus());

        // Employee task cleanup verification
        Assert.assertFalse(catEmployee.getAssignedRequests().containsKey(INSERT_REQUEST_ID));
    }

    /**
     * Verifies that the presenter rejects invalid price formats (non-numeric strings)
     * and triggers a field-specific error.
     */
    @Test
    public void onConfirmInsertInvalidPriceShowsInputError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);

        viewStub.setCodeInput("TECH-FAIL");
        viewStub.setNameInput("Fail Product");
        viewStub.setPriceInput("abc"); // Invalid Number
        viewStub.setDescInput("Desc");

        presenter.onConfirmInsert();

        Assert.assertEquals("price", viewStub.getInputErrorField());
    }

    /**
     * Verifies that the presenter rejects negative price values as invalid data.
     */
    @Test
    public void onConfirmInsertNegativePriceShowsInputError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);

        viewStub.setCodeInput("TECH-FAIL");
        viewStub.setNameInput("Fail Product");
        viewStub.setPriceInput("-10.0");
        viewStub.setDescInput("Desc");

        presenter.onConfirmInsert();

        Assert.assertEquals("price", viewStub.getInputErrorField());
    }
}
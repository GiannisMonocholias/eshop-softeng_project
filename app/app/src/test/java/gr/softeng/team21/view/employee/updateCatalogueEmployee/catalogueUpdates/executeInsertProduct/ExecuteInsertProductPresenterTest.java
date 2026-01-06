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

public class ExecuteInsertProductPresenterTest {

    private ExecuteInsertProductPresenter presenter;
    private ExecuteInsertProductViewStub viewStub;
    private UpdateCatalogueEmployee catEmployee;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int INSERT_REQUEST_ID = 5;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new ExecuteInsertProductViewStub();
        presenter = new ExecuteInsertProductPresenter(viewStub, EmployeeDAOMemory.getInstance(),
                UpdateRequestDAOMemory.getInstance(), ProductTypeDAOMemory.getInstance()
        );

        catEmployee = (UpdateCatalogueEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(INSERT_REQUEST_ID);
        catEmployee.assignRequest(request.getId());
    }


    @Test
    public void loadRequestDetailsDisplaysDescription() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);


        Assert.assertTrue(viewStub.getRequestDescription().contains("εκτυπωτή"));
    }

    @Test
    public void loadRequestDetailsInvalidDataShowsError() {
        presenter.loadRequestDetails("INVALID_EMP_ID", INSERT_REQUEST_ID);
        Assert.assertEquals("Σφάλμα: Τα στοιχεία δεν βρέθηκαν.", viewStub.getErrorMessage());

        viewStub.showError("");

        presenter.loadRequestDetails(EMPLOYEE_ID, -999);
        Assert.assertEquals("Σφάλμα: Τα στοιχεία δεν βρέθηκαν.", viewStub.getErrorMessage());
    }

    @Test
    public void onConfirmInsertValidDataSuccess() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);

        viewStub.setCodeInput("TECH-NEW-01");
        viewStub.setNameInput("Canon Pixma TS3450");
        viewStub.setPriceInput("55.90");
        viewStub.setDescInput("Πολυμηχάνημα Inkjet");

        presenter.onConfirmInsert();

        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));


        ProductType newProduct = ProductTypeDAOMemory.getInstance().getProduct("TECH-NEW-01");
        Assert.assertNotNull("Το νέο προϊόν έπρεπε να υπάρχει στο DAO", newProduct);
        Assert.assertEquals("Canon Pixma TS3450", newProduct.getProductname());
        Assert.assertEquals(55.90, newProduct.getPrice().getAmount().doubleValue(), 0.001);

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(INSERT_REQUEST_ID);
        Assert.assertEquals(RequestStatusType.SERVED, request.getStatus());

        Assert.assertFalse(catEmployee.getAssignedRequests().containsKey(INSERT_REQUEST_ID));
    }

    @Test
    public void onConfirmInsertInvalidPriceShowsInputError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, INSERT_REQUEST_ID);

        viewStub.setCodeInput("TECH-FAIL");
        viewStub.setNameInput("Fail Product");
        viewStub.setPriceInput("abc"); // Invalid Number
        viewStub.setDescInput("Desc");

        presenter.onConfirmInsert();

        Assert.assertEquals("price", viewStub.getInputErrorField());
        Assert.assertNotNull(viewStub.getErrorMessage());
    }

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
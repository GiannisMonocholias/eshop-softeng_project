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

public class ExecuteDeleteProductPresenterTest {

    private ExecuteDeleteProductPresenter presenter;
    private ExecuteDeleteProductViewStub viewStub;
    private UpdateCatalogueEmployee catEmployee;

    private static final String EMPLOYEE_ID = "CAT-301";

    private static final int DELETE_REQUEST_ID = 4;
    private static final String PRODUCT_CODE = "TECH-015";

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

        catEmployee = (UpdateCatalogueEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);


        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(DELETE_REQUEST_ID);
        catEmployee.assignRequest(request.getId());
    }


    @Test
    public void loadRequestDetailsValidDataSetsViewDetails() {
        presenter.loadRequestDetails(EMPLOYEE_ID, DELETE_REQUEST_ID);

        Assert.assertEquals("Samsung 990 Pro 1TB", viewStub.getName());
        Assert.assertEquals(PRODUCT_CODE, viewStub.getCode());
        Assert.assertTrue(viewStub.getPrice().contains("109.9"));
    }

    @Test
    public void loadRequestDetailsInvalidIdShowsError() {
        presenter.loadRequestDetails(EMPLOYEE_ID, -999);
        Assert.assertTrue(viewStub.getErrorMessage().contains("Σφάλμα"));
    }

    @Test
    public void onDeleteButtonClickedShowsConfirmationDialog() {
        presenter.onDeleteButtonClicked();
        Assert.assertTrue(viewStub.isConfirmationDialogShown());
    }

    @Test
    public void onDeleteConfirmedSuccessDeletesProductAndUpdatesRequest() {
        presenter.loadRequestDetails(EMPLOYEE_ID, DELETE_REQUEST_ID);

        Assert.assertNotNull(ProductTypeDAOMemory.getInstance().getProduct(PRODUCT_CODE));

        presenter.onDeleteConfirmed();

        ProductType deletedProduct = ProductTypeDAOMemory.getInstance().getProduct(PRODUCT_CODE);
        Assert.assertNull("Το προϊόν έπρεπε να έχει διαγραφεί", deletedProduct);

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(DELETE_REQUEST_ID);
        Assert.assertEquals(RequestStatusType.SERVED, request.getStatus());

        Assert.assertFalse(catEmployee.getAssignedRequests().containsKey(DELETE_REQUEST_ID));

        Assert.assertTrue(viewStub.getSuccessMessage().contains("επιτυχώς"));
    }
}
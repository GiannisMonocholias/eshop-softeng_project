package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

public class AvailableRequestsToAssignPresenterTest {

    private AvailableRequestsToAssignPresenter presenter;
    private AvailableRequestsToAssignViewStub viewStub;
    private UpdateCatalogueEmployee catEmployee;
    private static final String EMPLOYEE_ID = "CAT-301";

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new AvailableRequestsToAssignViewStub();

        presenter = new AvailableRequestsToAssignPresenter(
                viewStub,
                EmployeeDAOMemory.getInstance(),
                UpdateRequestDAOMemory.getInstance()
        );

        catEmployee = (UpdateCatalogueEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);
    }

    @Test
    public void loadAvailableRequestsReturnsOnlyNewRequests() {
        CatalogueUpdateRequest assignedRequest = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(1);
        assignedRequest.setStatus(RequestStatusType.ASSIGNED);

        ArrayList<CatalogueUpdateRequest> result = presenter.loadAvailableRequests(EMPLOYEE_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.size());

        for(CatalogueUpdateRequest req : result) {
            Assert.assertNotEquals(1, req.getId());
            Assert.assertEquals(RequestStatusType.NEW, req.getStatus());
        }
    }

    @Test
    public void onRequestClickedShowsConfirmationDialog() {
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(2);

        presenter.onRequestClicked(request);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertEquals(request, viewStub.getLastInteractedRequest());
        Assert.assertTrue(viewStub.getConfirmationMessage().contains("Θέλετε να αναλάβετε"));
    }

    @Test
    public void onRequestConfirmedSuccessAssignsRequestAndUpdatesView() {
        presenter.loadAvailableRequests(EMPLOYEE_ID);

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(3);
        Assert.assertEquals(RequestStatusType.NEW, request.getStatus());

        presenter.onRequestConfirmed(request);

        Assert.assertEquals(RequestStatusType.ASSIGNED, request.getStatus());
        Assert.assertTrue(catEmployee.getAssignedRequests().containsKey(request.getId()));

        Assert.assertTrue(viewStub.getMessageShown().contains("επιτυχώς"));
        Assert.assertTrue(viewStub.isListUpdated());
        Assert.assertEquals(request, viewStub.getRemovedRequest());
    }

    @Test
    public void onRequestClickedShowsCorrectConfirmationMessage() {
        CatalogueUpdateRequest request = gr.softeng.team21.memorydao.UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(2);

        presenter.onRequestClicked(request);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());

        Assert.assertEquals("Θέλετε να αναλάβετε αυτή την παραγγελία;", viewStub.getConfirmationMessage());

        Assert.assertEquals(request, viewStub.getLastInteractedRequest());
    }

    @Test
    public void onRequestConfirmedAssignmentFailedShowsErrorMessage() {
        presenter.loadAvailableRequests(EMPLOYEE_ID);


        CatalogueUpdateRequest nonExistingRequest = new CatalogueUpdateRequest(new gr.softeng.team21.util.Date(), "Fake Request",
                null, gr.softeng.team21.domain.AllowedRequest.DELETE_PRODUCT, -1
        );


        presenter.onRequestConfirmed(nonExistingRequest);

        Assert.assertTrue(viewStub.getErrorShown().contains("Σφάλμα: Δεν υπάρχει ή δεν σας έχει ανατεθεί"));
        Assert.assertTrue(viewStub.getErrorShown().contains("-1"));

        Assert.assertNull(viewStub.getRemovedRequest());
    }
}
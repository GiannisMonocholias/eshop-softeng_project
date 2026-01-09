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

/**
 * Unit tests for {@link AvailableRequestsToAssignPresenter}.
 * This suite verifies the logic for filtering available catalogue update requests,
 * assigning them to specific employees, and handling errors for non-existent requests.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableRequestsToAssignPresenterTest {

    private AvailableRequestsToAssignPresenter presenter;
    private AvailableRequestsToAssignViewStub viewStub;
    private UpdateCatalogueEmployee catEmployee;
    private static final String EMPLOYEE_ID = "CAT-301";

    /**
     * Initializes the testing environment before each test.
     * Prepares memory data and instantiates the presenter with its dependencies.
     */
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

    /**
     * Verifies that the list of available requests contains only those with "NEW" status
     * and excludes those already "ASSIGNED".
     */
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

    /**
     * Verifies that clicking an available request triggers a confirmation dialog
     * with the correct request data and message.
     */
    @Test
    public void onRequestClickedShowsConfirmationDialog() {
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(2);

        presenter.onRequestClicked(request);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertEquals(request, viewStub.getLastInteractedRequest());
        Assert.assertTrue(viewStub.getConfirmationMessage().contains("Θέλετε να αναλάβετε"));
    }

    /**
     * Verifies the full request assignment workflow:
     * 1. Request status transitions from NEW to ASSIGNED.
     * 2. The request is correctly added to the employee's personal map.
     * 3. The UI receives a success message and refreshes the list.
     */
    @Test
    public void onRequestConfirmedSuccessAssignsRequestAndUpdatesView() {
        presenter.loadAvailableRequests(EMPLOYEE_ID);

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(3);
        Assert.assertEquals(RequestStatusType.NEW, request.getStatus());

        presenter.onRequestConfirmed(request);

        // State update verification
        Assert.assertEquals(RequestStatusType.ASSIGNED, request.getStatus());
        Assert.assertTrue(catEmployee.getAssignedRequests().containsKey(request.getId()));

        // UI callback verification
        Assert.assertTrue(viewStub.getMessageShown().contains("επιτυχώς"));
        Assert.assertTrue(viewStub.isListUpdated());
        Assert.assertEquals(request, viewStub.getRemovedRequest());
    }

    /**
     * Verifies that the confirmation message shown in the dialog is accurate.
     */
    @Test
    public void onRequestClickedShowsCorrectConfirmationMessage() {
        CatalogueUpdateRequest request = gr.softeng.team21.memorydao.UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(2);

        presenter.onRequestClicked(request);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());

        Assert.assertEquals("Θέλετε να αναλάβετε αυτή την παραγγελία;", viewStub.getConfirmationMessage());

        Assert.assertEquals(request, viewStub.getLastInteractedRequest());
    }

    /**
     * Verifies that attempting to confirm an assignment for a non-existing request
     * (e.g., negative ID) fails gracefully with an error message.
     */
    @Test
    public void onRequestConfirmedAssignmentFailedShowsErrorMessage() {
        presenter.loadAvailableRequests(EMPLOYEE_ID);


        CatalogueUpdateRequest nonExistingRequest = new CatalogueUpdateRequest(new gr.softeng.team21.util.Date(), "Fake Request",
                null, gr.softeng.team21.domain.AllowedRequest.DELETE_PRODUCT, -1
        );


        presenter.onRequestConfirmed(nonExistingRequest);

        Assert.assertTrue(viewStub.getErrorShown().contains("δεν υπάρχει ή δεν σας έχει ανατεθεί"));
        Assert.assertTrue(viewStub.getErrorShown().contains("-1"));

        Assert.assertNull(viewStub.getRemovedRequest());
    }
}
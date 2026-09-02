package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.UpdateRequestDAO;
import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.memorydao.MemoryInitializer;

/**
 * Unit tests for {@link AvailableRequestsToAssignPresenter}.
 * This suite verifies the asynchronous logic for filtering available catalogue update requests
 * and assigning them to specific employees via Dependency Injection.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableRequestsToAssignPresenterTest {

    private AvailableRequestsToAssignPresenter presenter;
    private AvailableRequestsToAssignViewStub viewStub;
    private UpdateRequestDAO requestDAO;
    private static final String EMPLOYEE_ID = "CAT-301";

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new AvailableRequestsToAssignViewStub();

        EmployeeDAO employeeDAO = MemoryInitializer.getEmployeeDAO();
        requestDAO = MemoryInitializer.getUpdateRequestDAO();

        presenter = new AvailableRequestsToAssignPresenter(viewStub, employeeDAO, requestDAO);
    }

    @Test
    public void loadAvailableRequestsReturnsOnlyNewRequests() {
        CatalogueUpdateRequest assignedRequest = requestDAO.getUpdateRequests().join().get(1);
        assignedRequest.setStatus(RequestStatusType.ASSIGNED);

        presenter.loadAvailableRequests(EMPLOYEE_ID);
        ArrayList<CatalogueUpdateRequest> result = viewStub.getLoadedRequests();

        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.size());

        for(CatalogueUpdateRequest req : result) {
            Assert.assertNotEquals(1, req.getId());
            Assert.assertEquals(RequestStatusType.NEW, req.getStatus());
        }
    }

    @Test
    public void onRequestClickedShowsConfirmationDialog() {
        presenter.loadAvailableRequests(EMPLOYEE_ID);

        CatalogueUpdateRequest request = requestDAO.getUpdateRequests().join().get(2);
        presenter.onRequestClicked(request);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertEquals(request, viewStub.getLastInteractedRequest());
        Assert.assertTrue(viewStub.getConfirmationMessage().contains("Θέλετε να αναλάβετε"));
    }

    @Test
    public void onRequestConfirmedSuccessAssignsRequestAndUpdatesView() {
        presenter.loadAvailableRequests(EMPLOYEE_ID);

        CatalogueUpdateRequest request = requestDAO.getUpdateRequests().join().get(3);
        Assert.assertEquals(RequestStatusType.NEW, request.getStatus());

        presenter.onRequestConfirmed(request);

        // Verification of Domain update
        Assert.assertEquals(RequestStatusType.ASSIGNED, request.getStatus());

        // Verification of UI callbacks
        Assert.assertTrue(viewStub.getMessageShown().contains("επιτυχώς"));
        Assert.assertTrue(viewStub.isListUpdated());
        Assert.assertEquals(request, viewStub.getRemovedRequest());
    }

    @Test
    public void onRequestClickedShowsCorrectConfirmationMessage() {
        presenter.loadAvailableRequests(EMPLOYEE_ID);
        CatalogueUpdateRequest request = requestDAO.getUpdateRequests().join().get(2);

        presenter.onRequestClicked(request);

        Assert.assertTrue(viewStub.isConfirmationDialogShown());
        Assert.assertEquals("Θέλετε να αναλάβετε αυτή την παραγγελία;", viewStub.getConfirmationMessage());
        Assert.assertEquals(request, viewStub.getLastInteractedRequest());
    }

    @Test
    public void onRequestConfirmedWithNonExistingRequest_SucceedsInMemoryDAO() {
        presenter.loadAvailableRequests(EMPLOYEE_ID);

        CatalogueUpdateRequest nonExistingRequest = new CatalogueUpdateRequest(
                new gr.softeng.team21.util.Date(),
                "Fake Request",
                null,
                gr.softeng.team21.domain.AllowedRequest.DELETE_PRODUCT,
                -1
        );

        presenter.onRequestConfirmed(nonExistingRequest);

        Assert.assertEquals("Δεν εμφανίζεται σφάλμα διότι το MemoryDAO αποθηκεύει το νέο ID κανονικά",
                "", viewStub.getErrorShown());

        Assert.assertNotNull("Πρέπει να εμφανιστεί μήνυμα επιτυχίας", viewStub.getMessageShown());
        Assert.assertTrue(viewStub.getMessageShown().contains("επιτυχώς"));
        Assert.assertEquals(nonExistingRequest, viewStub.getRemovedRequest());
    }
}
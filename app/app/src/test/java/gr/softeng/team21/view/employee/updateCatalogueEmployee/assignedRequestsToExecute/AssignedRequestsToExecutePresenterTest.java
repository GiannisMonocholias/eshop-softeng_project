package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.UpdateCatalogueEmployee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Unit tests for {@link AssignedRequestsToExecutePresenter}.
 * This suite ensures that the list of catalogue update requests already assigned to an employee
 * is correctly retrieved asynchronously via Dependency Injection and that user interaction
 * triggers the proper navigation.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedRequestsToExecutePresenterTest {

    private AssignedRequestsToExecutePresenter presenter;
    private AssignedRequestsToExecuteViewStub viewStub;
    private UpdateCatalogueEmployee catEmployee;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int REQUEST_ID = 1;

    /**
     * Initializes the testing environment before each test.
     * Prepares memory data, sets up the presenter with injected dependencies, and assigns
     * a specific request to the test employee to simulate an active workload.
     */
    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new AssignedRequestsToExecuteViewStub();
        presenter = new AssignedRequestsToExecutePresenter(viewStub, EmployeeDAOMemory.getInstance());

        // Retrieve Employee using .join() due to CompletableFuture architecture
        catEmployee = (UpdateCatalogueEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID).join();

        // UpdateRequestDAOMemory map retrieval
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(REQUEST_ID);

        // Simulate the assignment of the request to the employee
        catEmployee.assignRequest(request.getId());
    }

    /**
     * Verifies that the presenter retrieves only the requests assigned to the specific employee
     * asynchronously and correctly pushes them to the view.
     */
    @Test
    public void loadAssignedRequestsReturnsCorrectList() {
        presenter.loadAssignedRequests(EMPLOYEE_ID);

        ArrayList<CatalogueUpdateRequest> result = viewStub.getLoadedRequests();

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(REQUEST_ID, result.get(0).getId());
    }

    /**
     * Verifies the presenter gracefully handles an invalid employee ID.
     */
    @Test
    public void loadAssignedRequestsInvalidEmployeeShowsError() {
        presenter.loadAssignedRequests("INVALID_ID");
        Assert.assertTrue(viewStub.getErrorMessage().contains("δεν βρέθηκε"));
    }

    /**
     * Verifies that clicking on an assigned request correctly triggers the navigation
     * to the details view with the required employee and request context.
     */
    @Test
    public void onClickRequestNavigatesToDetails() {
        // Load requests first to initialize the internal loggedInEmployee in the presenter
        presenter.loadAssignedRequests(EMPLOYEE_ID);

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(REQUEST_ID);

        presenter.onClickRequest(request);

        // Verification of navigation state via Stub
        Assert.assertTrue(viewStub.isNavigationCalled());
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedEmployeeId());
        Assert.assertEquals(request, viewStub.getNavigatedRequest());
        Assert.assertEquals(REQUEST_ID, viewStub.getNavigatedRequest().getId());
    }
}
package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import gr.softeng.team21.domain.CatalogueUpdateRequest;
import gr.softeng.team21.domain.RequestStatusType;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;
import gr.softeng.team21.memorydao.MemoryInitializer;
import gr.softeng.team21.memorydao.UpdateRequestDAOMemory;

/**
 * Unit tests for {@link AssignedRequestsToExecutePresenter}.
 * This suite ensures that requests mapped via Foreign Keys are correctly retrieved
 * asynchronously from the DAOs and that user interaction triggers proper navigation logic.
 *
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedRequestsToExecutePresenterTest {

    private AssignedRequestsToExecutePresenter presenter;
    private AssignedRequestsToExecuteViewStub viewStub;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int REQUEST_ID = 1;

    /**
     * Initializes the testing environment before each test.
     * Prepares memory data, sets up the presenter with injected DAOs, and assigns
     * a specific request to the test employee using Foreign Key assignment logic.
     */
    @Before
    public void setUp() {
        MemoryInitializer.prepareData();
        viewStub = new AssignedRequestsToExecuteViewStub();

        presenter = new AssignedRequestsToExecutePresenter(viewStub, EmployeeDAOMemory.getInstance(), UpdateRequestDAOMemory.getInstance());

        // Assign a request asynchronously using the updated DAO architecture
        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequest(REQUEST_ID).join();
        if (request != null) {
            request.setAssignedEmployeeId(EMPLOYEE_ID);
            request.setStatus(RequestStatusType.ASSIGNED);
            UpdateRequestDAOMemory.getInstance().updateRequest(request).join();
        }
    }

    /**
     * Verifies that the presenter successfully retrieves the requests assigned to the specific
     * employee asynchronously via the DAO and pushes them to the view.
     */
    @Test
    public void loadAssignedRequestsReturnsCorrectList() {
        presenter.loadAssignedRequests(EMPLOYEE_ID);
        ArrayList<CatalogueUpdateRequest> result = viewStub.getLoadedRequests();

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(EMPLOYEE_ID, result.get(0).getAssignedEmployeeId());
    }

    /**
     * Verifies that clicking on an assigned request triggers the correct navigation
     * callback in the view interface with accurate employee and request context.
     */
    @Test
    public void onClickRequestNavigatesToDetails() {
        presenter.loadAssignedRequests(EMPLOYEE_ID);
        CatalogueUpdateRequest request = viewStub.getLoadedRequests().get(0);

        presenter.onClickRequest(request);

        Assert.assertTrue(viewStub.isNavigationCalled());
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedEmployeeId());
        Assert.assertEquals(REQUEST_ID, viewStub.getNavigatedRequest().getId());
    }

    /**
     * Clears all memory state to ensure tests run in isolation.
     */
    @After
    public void tearDown() {
        EmployeeDAOMemory.getInstance().clear();
        UpdateRequestDAOMemory.getInstance().clear().join();
    }
}
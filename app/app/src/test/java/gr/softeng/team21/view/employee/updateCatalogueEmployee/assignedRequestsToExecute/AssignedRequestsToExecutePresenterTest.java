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

public class AssignedRequestsToExecutePresenterTest {

    private AssignedRequestsToExecutePresenter presenter;
    private AssignedRequestsToExecuteViewStub viewStub;
    private UpdateCatalogueEmployee catEmployee;

    private static final String EMPLOYEE_ID = "CAT-301";
    private static final int REQUEST_ID = 1;

    @Before
    public void setUp() throws Exception {
        MemoryInitializer.prepareData();

        viewStub = new AssignedRequestsToExecuteViewStub();
        presenter = new AssignedRequestsToExecutePresenter(viewStub, EmployeeDAOMemory.getInstance());

        catEmployee = (UpdateCatalogueEmployee) EmployeeDAOMemory.getInstance().getEmployee(EMPLOYEE_ID);

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(REQUEST_ID);


        catEmployee.assignRequest(request.getId());
    }



    @Test
    public void loadAssignedRequestsReturnsCorrectList() {
        ArrayList<CatalogueUpdateRequest> result = presenter.loadAssignedRequests(EMPLOYEE_ID);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());

        Assert.assertEquals(REQUEST_ID, result.get(0).getId());
    }

    @Test
    public void onClickRequestNavigatesToDetails() {
        presenter.loadAssignedRequests(EMPLOYEE_ID);

        CatalogueUpdateRequest request = UpdateRequestDAOMemory.getInstance().getUpdateRequests().get(REQUEST_ID);

        presenter.onClickRequest(request);

        Assert.assertTrue(viewStub.isNavigationCalled());
        Assert.assertEquals(EMPLOYEE_ID, viewStub.getNavigatedEmployeeId());
        Assert.assertEquals(request, viewStub.getNavigatedRequest());

        Assert.assertEquals(REQUEST_ID, viewStub.getNavigatedRequest().getId());
    }
}
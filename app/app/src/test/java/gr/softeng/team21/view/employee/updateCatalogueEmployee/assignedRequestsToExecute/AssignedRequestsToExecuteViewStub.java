package gr.softeng.team21.view.employee.updateCatalogueEmployee.assignedRequestsToExecute;

import java.util.ArrayList;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * A stub implementation of the {@link AssignedRequestsToExecuteView} interface for unit testing.
 * It provides a way to verify that asynchronous list updates and navigation to the
 * detailed request execution screen are triggered with the correct parameters.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedRequestsToExecuteViewStub implements AssignedRequestsToExecuteView {

    private String navigatedEmployeeId = "";
    private CatalogueUpdateRequest navigatedRequest = null;
    private boolean navigationCalled = false;

    private ArrayList<CatalogueUpdateRequest> loadedRequests;
    private String errorMessage = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAssignedRequestsList(ArrayList<CatalogueUpdateRequest> requests) {
        this.loadedRequests = requests;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateToRequestDetails(String employeeId, CatalogueUpdateRequest request) {
        this.navigationCalled = true;
        this.navigatedEmployeeId = employeeId;
        this.navigatedRequest = request;
    }

    // --- Accessor methods for verification during testing ---

    public String getNavigatedEmployeeId() {
        return navigatedEmployeeId;
    }

    public CatalogueUpdateRequest getNavigatedRequest() {
        return navigatedRequest;
    }

    public boolean isNavigationCalled() {
        return navigationCalled;
    }

    public ArrayList<CatalogueUpdateRequest> getLoadedRequests() {
        return loadedRequests;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
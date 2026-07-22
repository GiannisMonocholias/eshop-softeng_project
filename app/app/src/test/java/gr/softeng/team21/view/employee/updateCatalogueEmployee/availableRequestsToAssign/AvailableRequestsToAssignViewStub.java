package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import java.util.ArrayList;
import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * A stub implementation of the {@link AvailableRequestsToAssignView} interface for unit testing.
 * It simulates the UI for an employee viewing available catalogue update requests,
 * capturing asynchronous feedback messages, list updates, and confirmation dialog data.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableRequestsToAssignViewStub implements AvailableRequestsToAssignView {

    private ArrayList<CatalogueUpdateRequest> loadedRequests;
    private String messageShown = "";
    private String errorShown = "";
    private boolean listUpdated = false;

    private boolean confirmationDialogShown = false;
    private String confirmationMessage = "";
    private CatalogueUpdateRequest lastInteractedRequest;
    private CatalogueUpdateRequest removedRequest;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAvailableRequestsList(ArrayList<CatalogueUpdateRequest> requests) {
        this.loadedRequests = requests;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        this.errorShown = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRequestAssignedSuccess(CatalogueUpdateRequest request) {
        this.removedRequest = request;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateList() {
        this.listUpdated = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showConfirmationDialog(CatalogueUpdateRequest request, String confirmationMessage) {
        this.confirmationDialogShown = true;
        this.lastInteractedRequest = request;
        this.confirmationMessage = confirmationMessage;
    }

    // --- Accessor methods for verification during assertions ---

    public ArrayList<CatalogueUpdateRequest> getLoadedRequests() {
        return loadedRequests;
    }

    public String getMessageShown() {
        return messageShown;
    }

    public String getErrorShown() {
        return errorShown;
    }

    public boolean isListUpdated() {
        return listUpdated;
    }

    public boolean isConfirmationDialogShown() {
        return confirmationDialogShown;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public CatalogueUpdateRequest getLastInteractedRequest() {
        return lastInteractedRequest;
    }

    public CatalogueUpdateRequest getRemovedRequest() {
        return removedRequest;
    }
}
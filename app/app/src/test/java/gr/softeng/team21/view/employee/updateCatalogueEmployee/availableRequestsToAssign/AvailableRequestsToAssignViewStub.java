package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

/**
 * A stub implementation of the {@link AvailableRequestsToAssignView} interface for unit testing.
 * It simulates the UI for an employee viewing available catalogue update requests,
 * capturing feedback messages, list update triggers, and confirmation dialog data.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableRequestsToAssignViewStub implements AvailableRequestsToAssignView {

    private String messageShown = "";
    private String errorShown = "";
    private boolean listUpdated = false;

    private boolean confirmationDialogShown = false;
    private String confirmationMessage = "";
    private CatalogueUpdateRequest lastInteractedRequest;

    // Success callback tracking
    private CatalogueUpdateRequest removedRequest;

    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    @Override
    public void showError(String message) {
        this.errorShown = message;
    }

    /**
     * Records the request that was successfully assigned to the employee.
     * @param request The update request that should be removed from the available list.
     */
    @Override
    public void onRequestAssignedSuccess(CatalogueUpdateRequest request) {
        this.removedRequest = request;
    }

    /**
     * Marks that a request to refresh the UI list was triggered.
     */
    @Override
    public void updateList() {
        this.listUpdated = true;
    }

    /**
     * Captures the state and content of the confirmation dialog shown to the user.
     * @param request The request the user clicked on.
     * @param confirmationMessage The message displayed in the dialog.
     */
    @Override
    public void showConfirmationDialog(CatalogueUpdateRequest request, String confirmationMessage) {
        this.confirmationDialogShown = true;
        this.lastInteractedRequest = request;
        this.confirmationMessage = confirmationMessage;
    }

    // --- Accessor methods for verification during assertions ---

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
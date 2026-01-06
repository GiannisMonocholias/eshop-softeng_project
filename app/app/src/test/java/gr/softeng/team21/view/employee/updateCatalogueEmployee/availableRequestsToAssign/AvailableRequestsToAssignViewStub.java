package gr.softeng.team21.view.employee.updateCatalogueEmployee.availableRequestsToAssign;

import gr.softeng.team21.domain.CatalogueUpdateRequest;

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

    @Override
    public void onRequestAssignedSuccess(CatalogueUpdateRequest request) {
        this.removedRequest = request;
    }

    @Override
    public void updateList() {
        this.listUpdated = true;
    }

    @Override
    public void showConfirmationDialog(CatalogueUpdateRequest request, String confirmationMessage) {
        this.confirmationDialogShown = true;
        this.lastInteractedRequest = request;
        this.confirmationMessage = confirmationMessage;
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
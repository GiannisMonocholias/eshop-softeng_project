package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import gr.softeng.team21.domain.Order;

public class AvailableOrdersToAssignViewStub implements AvailableOrdersToAssignView {

    private String messageShown = "";
    private String errorShown = "";
    private boolean listUpdated = false;

    private boolean confirmationDialogShown = false;
    private String confirmationMessage = "";
    private Order lastInteractedOrder;
    private Order removedOrder;

    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    @Override
    public void showError(String message) {
        this.errorShown = message;
    }

    @Override
    public void onOrderAssignedSuccess(Order order) {
        this.removedOrder = order;
    }

    @Override
    public void showConfirmationDialog(Order order, String message) {
        this.confirmationDialogShown = true;
        this.lastInteractedOrder = order;
        this.confirmationMessage = message;
    }

    @Override
    public void updateList() {
        this.listUpdated = true;
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

    public Order getLastInteractedOrder() {
        return lastInteractedOrder;
    }

    public Order getRemovedOrder() {
        return removedOrder;
    }
}
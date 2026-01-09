package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import gr.softeng.team21.domain.Order;

/**
 * A stub implementation of the {@link AvailableOrdersToAssignView} interface for unit testing.
 * It provides a simulated UI environment to capture messages, confirmation dialogs,
 * and list update events during the order assignment process.
 * @author Γιάννης Μονοχολιάς
 */
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

    /**
     * Records the order that was successfully assigned and should be removed from the available list.
     * @param order The assigned order.
     */
    @Override
    public void onOrderAssignedSuccess(Order order) {
        this.removedOrder = order;
    }

    /**
     * Captures the state and message of the confirmation dialog shown to the user.
     * @param order The order to be assigned.
     * @param message The confirmation message text.
     */
    @Override
    public void showConfirmationDialog(Order order, String message) {
        this.confirmationDialogShown = true;
        this.lastInteractedOrder = order;
        this.confirmationMessage = message;
    }

    /**
     * Marks that the UI list was requested to be refreshed.
     */
    @Override
    public void updateList() {
        this.listUpdated = true;
    }

    // --- Getters for Testing verification ---

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
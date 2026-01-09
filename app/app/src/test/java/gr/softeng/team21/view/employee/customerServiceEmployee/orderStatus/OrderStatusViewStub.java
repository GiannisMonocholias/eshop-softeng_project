package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import gr.softeng.team21.domain.Order;

/**
 * A stub implementation of the {@link OrderStatusView} interface for unit testing.
 * It simulates the UI state for managing order status notifications, capturing
 * error messages, confirmation dialog details, and list update triggers.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusViewStub implements OrderStatusView {

    private String errorMsg = "";
    private String messageMsg = "";
    private String selectedOrderCode = "";
    private boolean listUpdated = false;

    private Order confirmationOrder;
    private String confirmationMessage;
    private boolean confirmationDialogShown = false;

    /**
     * Captures error messages sent to the UI.
     * @param message The error message content.
     */
    @Override
    public void showError(String message) {
        this.errorMsg = message;
    }

    /**
     * Records the code of the order that was selected in the UI.
     * @param order The selected order.
     */
    @Override
    public void onOrderSelected(Order order) {
        this.selectedOrderCode = order.getOrdercode();
    }

    /**
     * Captures feedback messages (e.g., success notifications) sent to the UI.
     * @param message The message content.
     */
    @Override
    public void showMessage(String message) {
        this.messageMsg = message;
    }

    /**
     * Marks that a request to refresh the order list was triggered.
     */
    @Override
    public void updateList() {
        this.listUpdated = true;
    }

    /**
     * Captures the state and content of a confirmation dialog.
     * @param order The order associated with the dialog.
     * @param message The message to be displayed in the dialog.
     */
    @Override
    public void showConfirmationDialog(Order order, String message) {
        this.confirmationDialogShown = true;
        this.confirmationOrder = order;
        this.confirmationMessage = message;
    }

    // --- Accessor methods for verification during assertions ---

    public String getErrorMsg() { return errorMsg; }
    public String getMessageMsg() { return messageMsg; }
    public boolean isListUpdated() { return listUpdated; }
    public boolean isConfirmationDialogShown() { return confirmationDialogShown; }
    public String getConfirmationMessage() { return confirmationMessage; }
    public String getSelectedOrderCode() { return selectedOrderCode; }
}
package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import gr.softeng.team21.domain.Order;

/**
 * A stub implementation of the {@link DelivererOrdersListView} interface for unit testing.
 * It captures UI updates requested by the presenter, such as order removal from
 * the visible list and display of feedback or error messages.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrdersListViewStub implements DelivererOrdersListView {

    private Order removedOrder;
    private String messageShown = "";
    private String errorShown = "";

    /**
     * Captures the request to remove a specific order from the UI list.
     * @param order The order to be removed.
     */
    @Override
    public void removeOrderFromList(Order order) {
        this.removedOrder = order;
    }

    /**
     * Captures success or feedback messages sent to the UI.
     * @param message The message content.
     */
    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    /**
     * Captures error messages sent to the UI.
     * @param message The error message content.
     */
    @Override
    public void showError(String message) {
        this.errorShown = message;
    }

    // --- Accessor methods for verification during assertions ---

    public Order getRemovedOrder() {
        return removedOrder;
    }

    public String getMessageShown() {
        return messageShown;
    }

    public String getErrorShown() {
        return errorShown;
    }
}
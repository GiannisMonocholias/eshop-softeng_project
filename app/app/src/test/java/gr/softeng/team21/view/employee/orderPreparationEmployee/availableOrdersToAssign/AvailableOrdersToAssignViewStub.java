package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import java.util.ArrayList;
import gr.softeng.team21.domain.Order;

/**
 * A stub implementation of the {@link AvailableOrdersToAssignView} interface for unit testing.
 * It provides a simulated UI environment to capture messages, confirmation dialogs,
 * and asynchronous list update events during the order assignment process.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableOrdersToAssignViewStub implements AvailableOrdersToAssignView {

    private ArrayList<Order> loadedOrders;
    private String messageShown = "";
    private String errorShown = "";
    private boolean listUpdated = false;

    private boolean confirmationDialogShown = false;
    private String confirmationMessage = "";
    private Order lastInteractedOrder;
    private Order removedOrder;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAvailableOrdersList(ArrayList<Order> orders) {
        this.loadedOrders = orders;
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
    public void onOrderAssignedSuccess(Order order) {
        this.removedOrder = order;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showConfirmationDialog(Order order, String message) {
        this.confirmationDialogShown = true;
        this.lastInteractedOrder = order;
        this.confirmationMessage = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateList() {
        this.listUpdated = true;
    }

    // --- Getters for Testing verification ---

    public ArrayList<Order> getLoadedOrders() {
        return loadedOrders;
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
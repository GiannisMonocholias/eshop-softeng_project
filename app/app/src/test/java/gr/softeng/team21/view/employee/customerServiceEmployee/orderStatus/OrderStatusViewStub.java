package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import java.util.ArrayList;
import gr.softeng.team21.domain.Order;

/**
 * A stub implementation of the {@link OrderStatusView} interface for unit testing.
 * It simulates the UI state for managing order status notifications, capturing
 * error messages, confirmation dialog details, and asynchronous list update triggers.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusViewStub implements OrderStatusView {

    private String errorMsg = "";
    private String messageMsg = "";
    private String selectedOrderCode = "";
    private boolean listUpdated = false;
    private ArrayList<Order> loadedOrders;

    private Order confirmationOrder;
    private String confirmationMessage;
    private boolean confirmationDialogShown = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        this.errorMsg = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOrderSelected(Order order) {
        this.selectedOrderCode = order.getOrdercode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showMessage(String message) {
        this.messageMsg = message;
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
    public void showConfirmationDialog(Order order, String message) {
        this.confirmationDialogShown = true;
        this.confirmationOrder = order;
        this.confirmationMessage = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrders(ArrayList<Order> orders) {
        this.loadedOrders = orders;
    }

    // --- Accessor methods for verification during assertions ---

    public String getErrorMsg() { return errorMsg; }
    public String getMessageMsg() { return messageMsg; }
    public boolean isListUpdated() { return listUpdated; }
    public boolean isConfirmationDialogShown() { return confirmationDialogShown; }
    public String getConfirmationMessage() { return confirmationMessage; }
    public String getSelectedOrderCode() { return selectedOrderCode; }
    public ArrayList<Order> getLoadedOrders() { return loadedOrders; }
}
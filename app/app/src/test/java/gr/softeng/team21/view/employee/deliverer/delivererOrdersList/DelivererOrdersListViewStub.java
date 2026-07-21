package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import java.util.ArrayList;
import gr.softeng.team21.domain.Order;

/**
 * A stub implementation of the {@link DelivererOrdersListView} interface for unit testing.
 * It captures asynchronous UI updates requested by the presenter, such as order loading,
 * removal from the visible list, and display of feedback or error messages.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrdersListViewStub implements DelivererOrdersListView {

    private ArrayList<Order> loadedOrders;
    private Order removedOrder;
    private String messageShown = "";
    private String errorShown = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOrdersList(ArrayList<Order> orders) {
        this.loadedOrders = orders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeOrderFromList(Order order) {
        this.removedOrder = order;
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

    // --- Accessor methods for verification during assertions ---

    public ArrayList<Order> getLoadedOrders() {
        return loadedOrders;
    }

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
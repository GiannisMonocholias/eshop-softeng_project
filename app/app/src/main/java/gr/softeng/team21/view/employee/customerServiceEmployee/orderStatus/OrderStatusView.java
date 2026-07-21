package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import java.util.ArrayList;
import gr.softeng.team21.domain.Order;

/**
 * Defines the UI operations for displaying order lists, confirmation dialogs,
 * and feedback messages to the Customer Service Employee.
 * Defines the contract between the Presenter and the UI for asynchronous operations.
 * @author Γιάννης Μονοχολιάς
 */
public interface OrderStatusView {

    /**
     * Displays an error message.
     * @param message The error description.
     */
    void showError(String message);

    /**
     * Triggered when an order is selected for viewing without further action.
     * @param order The selected order.
     */
    void onOrderSelected(Order order);

    /**
     * Displays a general informative message (e.g., success notification).
     * @param message The text to display.
     */
    void showMessage(String message);

    /**
     * Requests the UI to refresh the order list, after an order
     * has been processed and removed from the employee's queue.
     */
    void updateList();

    /**
     * Shows a confirmation dialog before sending an email notification to a customer.
     * @param order   The order associated with the notification.
     * @param message The confirmation text.
     */
    void showConfirmationDialog(Order order, String message);

    /**
     * Updates the UI with the retrieved list of assigned orders asynchronously.
     * @param orders An ArrayList of Order objects to be displayed.
     */
    void updateOrders(ArrayList<Order> orders);
}
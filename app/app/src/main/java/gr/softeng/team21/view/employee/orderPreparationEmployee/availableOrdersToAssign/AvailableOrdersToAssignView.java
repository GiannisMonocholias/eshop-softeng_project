package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import java.util.ArrayList;
import gr.softeng.team21.domain.Order;

/**
 * Defines the UI operations for listing unassigned orders and managing
 * the manual assignment workflow via dialogs.
 * Acts as the contract between the Presenter and the Activity for asynchronous operations.
 * @author Γιάννης Μονοχολιάς
 */
public interface AvailableOrdersToAssignView {

    /**
     * Updates the UI with the retrieved list of available orders asynchronously.
     * @param orders An ArrayList of orders that have the "NEW" status.
     */
    void updateAvailableOrdersList(ArrayList<Order> orders);

    /**
     * Displays an informative message to the user.
     * @param message The text content of the message.
     */
    void showMessage(String message);

    /**
     * Displays an error alert dialog.
     * @param message The error description.
     */
    void showError(String message);

    /**
     * Triggered when an order is successfully assigned.
     * Typically removes the order from the available list.
     * @param order The newly assigned order.
     */
    void onOrderAssignedSuccess(Order order);

    /**
     * Shows a confirmation dialog before an employee takes responsibility for an order.
     * @param order   The order to be assigned.
     * @param message The confirmation prompt message.
     */
    void showConfirmationDialog(Order order, String message);

    /**
     * Refreshes the UI list to reflect the current state of available orders.
     */
    void updateList();
}
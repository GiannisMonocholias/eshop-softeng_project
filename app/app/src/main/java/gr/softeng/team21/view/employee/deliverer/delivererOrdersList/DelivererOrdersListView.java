package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import gr.softeng.team21.domain.Order;

/**
 * View contract for the screen displaying a Deliverer's assigned orders.
 * Defines methods for updating the order list dynamically and handling
 * user feedback via messages or alerts.
 * @author Γιάννης Μονοχολιάς
 */
public interface DelivererOrdersListView {

    /**
     * Removes a specific order from the UI list, typically after delivery completion.
     * @param order The order to be removed from the view.
     */
    void removeOrderFromList(Order order);

    /**
     * Displays a success or information message to the user.
     * @param message The text content to display.
     */
    void showMessage(String message);

    /**
     * Displays an error alert dialog to the user.
     * @param message The error description.
     */
    void showError(String message);
}
package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import gr.softeng.team21.domain.OrderStatusType;

/**
 * View contract for the Order Preparation Details screen.
 * Defines methods for displaying specific order data, handling success/error
 * feedback, and managing the activity lifecycle.
 * @author Γιάννης Μονοχολιάς
 */
public interface OrderPreparationDetailsView {
    /**
     * Updates the UI with the detailed information of the order.
     * @param ordercode The unique identifier of the order.
     * @param customerName The full name of the customer.
     * @param submissionDate The date the order was placed.
     * @param price The total monetary value of the order.
     * @param status The current status of the order.
     */
    void setOrderDetails(String ordercode, String customerName, String submissionDate, String price, OrderStatusType status);

    /**
     * Displays an error alert, typically for stock shortages or invalid access.
     * @param message The error description.
     */
    void showErrorMessage(String message);

    /**
     * Displays a success alert, usually followed by closing the screen.
     * @param message The success description.
     */
    void showSuccessMessage(String message);

    /**
     * Terminates the current activity and returns to the previous screen.
     */
    void finishActivity();
}
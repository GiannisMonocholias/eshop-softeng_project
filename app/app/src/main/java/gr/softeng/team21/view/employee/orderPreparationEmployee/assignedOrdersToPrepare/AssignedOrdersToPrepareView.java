package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

import java.util.ArrayList;
import gr.softeng.team21.domain.Order;

/**
 * View contract for the Assigned Orders list screen.
 * Defines the navigation logic required to move from the list of assignments
 * to the detailed preparation view and handles asynchronous UI updates.
 * @author Γιάννης Μονοχολιάς
 */
public interface AssignedOrdersToPrepareView {

    /**
     * Updates the UI with the retrieved list of assigned orders asynchronously.
     * @param orders An ArrayList of Order objects assigned to the employee.
     */
    void updateAssignedOrdersList(ArrayList<Order> orders);

    /**
     * Displays an error message to the user, typically via a dialog.
     * @param message The error description to display.
     */
    void showError(String message);

    /**
     * Navigates to the detailed preparation screen for a specific order.
     * @param employeeId The ID of the employee performing the preparation.
     * @param ordercode  The unique code of the selected order.
     */
    void navigateToOrderPreparationDetails(String employeeId, String ordercode);
}
package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

/**
 * View contract for the Assigned Orders list screen.
 * Defines the navigation logic required to move from the list of assignments
 * to the detailed preparation view.
 * @author Γιάννης Μονοχολιάς
 */
public interface AssignedOrdersToPrepareView {

    /**
     * Navigates to the detailed preparation screen for a specific order.
     * @param employeeId The ID of the employee performing the preparation.
     * @param ordercode  The unique code of the selected order.
     */
    void navigateToOrderPreparationDetails(String employeeId, String ordercode);
}
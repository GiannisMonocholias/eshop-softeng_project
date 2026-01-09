package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

/**
 * A stub implementation of the {@link AssignedOrdersToPrepareView} interface for unit testing.
 * It provides a way to verify that the navigation to the detailed order preparation
 * screen is triggered with the correct parameters (employee ID and order code).
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedOrdersToPrepareViewStub implements AssignedOrdersToPrepareView {

    private String navigatedEmployeeId = "";
    private String navigatedOrderCode = "";
    private boolean navigationCalled = false;

    /**
     * Captures navigation data when the presenter requests to show order details.
     * @param employeeId The ID of the employee currently preparing the order.
     * @param ordercode The unique code of the order to be displayed.
     */
    @Override
    public void navigateToOrderPreparationDetails(String employeeId, String ordercode) {
        this.navigationCalled = true;
        this.navigatedEmployeeId = employeeId;
        this.navigatedOrderCode = ordercode;
    }

    // --- Accessor methods for verification during testing ---

    public String getNavigatedEmployeeId() {
        return navigatedEmployeeId;
    }

    public String getNavigatedOrderCode() {
        return navigatedOrderCode;
    }

    public boolean isNavigationCalled() {
        return navigationCalled;
    }
}
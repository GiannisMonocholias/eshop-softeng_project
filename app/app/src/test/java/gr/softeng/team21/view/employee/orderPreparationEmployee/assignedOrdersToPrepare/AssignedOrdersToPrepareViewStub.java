package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

import java.util.ArrayList;
import gr.softeng.team21.domain.Order;

/**
 * A stub implementation of the {@link AssignedOrdersToPrepareView} interface for unit testing.
 * It provides a way to verify that navigation and asynchronous data updates
 * are triggered with the correct parameters.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedOrdersToPrepareViewStub implements AssignedOrdersToPrepareView {

    private String navigatedEmployeeId = "";
    private String navigatedOrderCode = "";
    private boolean navigationCalled = false;

    private ArrayList<Order> loadedOrders;
    private String errorMessage = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAssignedOrdersList(ArrayList<Order> orders) {
        this.loadedOrders = orders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    /**
     * {@inheritDoc}
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

    public ArrayList<Order> getLoadedOrders() {
        return loadedOrders;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
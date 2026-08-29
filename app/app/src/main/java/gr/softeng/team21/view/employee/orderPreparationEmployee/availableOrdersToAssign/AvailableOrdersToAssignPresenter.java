package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Available Orders to Assign screen.
 * Handles the asynchronous logic of filtering unassigned orders and manages the
 * transactional logic of assigning an order to the currently logged-in employee
 * using Foreign Keys and DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableOrdersToAssignPresenter {

    private final AvailableOrdersToAssignView view;
    private final EmployeeDAO employeeDAO;
    private final OrderDAO orderDAO;
    private OrderPreparationEmployee loggedInEmployee;

    /**
     * Initializes the presenter with injected DAOs and the view interface.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO The data source for employee records.
     * @param orderDAO The data source for all system orders.
     */
    public AvailableOrdersToAssignPresenter(AvailableOrdersToAssignView view, EmployeeDAO employeeDAO, OrderDAO orderDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Asynchronously loads the employee data and then fetches all orders,
     * filtering for those that have a status of NEW and are unassigned, before updating the view.
     * @param employeeId The unique ID of the employee browsing the list.
     */
    public void loadAvailableOrders(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof OrderPreparationEmployee) {
                this.loggedInEmployee = (OrderPreparationEmployee) employee;

                // Fetch orders asynchronously
                orderDAO.getOrders().thenAccept(ordersMap -> {
                    ArrayList<Order> newOrders = new ArrayList<>();
                    for (Order cur_order : ordersMap.values()) {
                        // Ensure order is NEW and not yet assigned to anyone
                        if (cur_order != null && cur_order.getOrderstatus() == OrderStatusType.NEW && cur_order.getPreparationEmployeeId() == null) {
                            newOrders.add(cur_order);
                        }
                    }
                    if (view != null) view.updateAvailableOrdersList(newOrders);
                }).exceptionally(e -> {
                    if (view != null) view.showError("Σφάλμα ανάκτησης παραγγελιών: " + e.getMessage());
                    return null;
                });

            } else {
                if (view != null) view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
            return null;
        });
    }

    /**
     * Triggered when a user clicks on an available order.
     * Requests the view to show a confirmation prompt.
     * @param order The selected order.
     */
    public void onOrderClicked(Order order) {
        if (loggedInEmployee == null) {
            if (view != null) view.showError("Δεν υπάρχει ενεργή συνεδρία υπαλλήλου.");
            return;
        }
        if (view != null) view.showConfirmationDialog(order, "Θέλετε να αναλάβετε αυτή την παραγγελία;");
    }

    /**
     * Finalizes the assignment process.
     * Assigns the order to the employee via Foreign Key, updates the order status
     * to PROCESSING, and persists the changes asynchronously.
     * @param order The order confirmed for assignment.
     */
    public void onOrderConfirmed(Order order) {
        if (loggedInEmployee == null) return;

        // Apply domain state changes locally using Foreign Keys
        order.setPreparationEmployeeId(loggedInEmployee.getEmployeeId());
        order.setOrderstatus(OrderStatusType.PROCESSING);

        // Update the order in the database asynchronously (overwrite document)
        orderDAO.updateOrder(order).thenAccept(v -> {
            if (view != null) {
                view.showMessage("Η παραγγελία ανατέθηκε επιτυχώς!");
                view.onOrderAssignedSuccess(order);
                view.updateList();
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα κατά την ενημέρωση της παραγγελίας: " + e.getMessage());
            return null;
        });
    }
}
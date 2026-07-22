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
 * transactional logic of assigning an order to the currently logged-in employee.
 * Utilizes Dependency Injection to decouple data sources from the presentation logic.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableOrdersToAssignPresenter {

    private AvailableOrdersToAssignView view;
    private EmployeeDAO employeeDAO;
    private OrderDAO orderDAO;
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
     * filtering for those that have a status of NEW, before updating the view.
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
                        if (cur_order != null && cur_order.getOrderstatus() == OrderStatusType.NEW) {
                            newOrders.add(cur_order);
                        }
                    }
                    view.updateAvailableOrdersList(newOrders);
                }).exceptionally(e -> {
                    view.showError("Σφάλμα ανάκτησης παραγγελιών: " + e.getMessage());
                    return null;
                });

            } else {
                view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }
        }).exceptionally(e -> {
            view.showError("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
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
            view.showError("Δεν υπάρχει ενεργή συνεδρία υπαλλήλου.");
            return;
        }
        String confirmationMessage = "Θέλετε να αναλάβετε αυτή την παραγγελία;";
        view.showConfirmationDialog(order, confirmationMessage);
    }

    /**
     * Finalizes the assignment process.
     * Adds the order to the employee's list and updates the order status
     * to {@link OrderStatusType#PROCESSING}.
     * @param order The order confirmed for assignment.
     */
    public void onOrderConfirmed(Order order) {
        if (loggedInEmployee == null) return;

        loggedInEmployee.addOrder(order);
        order.setOrderstatus(OrderStatusType.PROCESSING);

        // Update the order in the database asynchronously if required
        orderDAO.addOrder(order).thenAccept(v -> {
            view.showMessage("Η παραγγελία ανατέθηκε επιτυχώς!");
            view.onOrderAssignedSuccess(order);
            view.updateList();
        }).exceptionally(e -> {
            view.showError("Σφάλμα κατά την ενημέρωση της παραγγελίας: " + e.getMessage());
            return null;
        });
    }
}
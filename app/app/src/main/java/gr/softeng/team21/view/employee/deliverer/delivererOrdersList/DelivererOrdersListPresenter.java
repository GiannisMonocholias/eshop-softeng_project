package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Deliverer's Orders List.
 * Coordinates the asynchronous retrieval of orders specifically assigned to a deliverer
 * and handles the logic for completing a delivery.
 * Utilizes Dependency Injection to decouple the data sources from the presentation logic.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrdersListPresenter {
    private DelivererOrdersListView view;
    private OrderDAO orderDAO;
    private EmployeeDAO employeeDAO;
    private Deliverer loggedInEmployee;

    /**
     * Initializes the presenter with required injected DAOs and the view interface.
     * @param view The view implementation (Activity or Stub).
     * @param orderDAO The data source for orders.
     * @param employeeDAO The data source for employee/deliverer records.
     */
    public DelivererOrdersListPresenter(DelivererOrdersListView view, OrderDAO orderDAO, EmployeeDAO employeeDAO) {
        this.view = view;
        this.orderDAO = orderDAO;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Asynchronously loads the orders currently assigned to the deliverer and updates the view.
     * @param employeeId The unique identifier of the deliverer.
     */
    public void loadShippedOrders(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            this.loggedInEmployee = (Deliverer) employee;

            if (loggedInEmployee == null) {
                view.showError("Σφάλμα: Ο διανομέας δεν βρέθηκε.");
                return;
            }

            ArrayList<Order> shippedOrders = new ArrayList<>();
            if (loggedInEmployee.getOrders() != null) {
                shippedOrders.addAll(loggedInEmployee.getOrders());
            }

            view.updateOrdersList(shippedOrders);

        }).exceptionally(e -> {
            view.showError("Σφάλμα ανάκτησης δεδομένων: " + e.getMessage());
            return null;
        });
    }

    /**
     * Processes the completion of a delivery.
     * Updates the order status to {@link OrderStatusType#DELIVERED},
     * removes the order from the deliverer's assignment, and notifies the view.
     * @param order The order that was successfully delivered.
     */
    public void onOrderConfirmed(Order order) {
        order.setOrderstatus(OrderStatusType.DELIVERED);

        view.showMessage("Order #" + order.getOrdercode() + " ολοκληρώθηκε!");
        view.removeOrderFromList(order);

        if (loggedInEmployee != null && loggedInEmployee.getOrders() != null) {
            loggedInEmployee.getOrders().remove(order);
        }
    }
}
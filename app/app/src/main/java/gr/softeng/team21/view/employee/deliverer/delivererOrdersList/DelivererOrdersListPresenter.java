package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Deliverer's Orders List.
 * Coordinates the retrieval of orders specifically assigned to a deliverer
 * and handles the logic for completing a delivery.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrdersListPresenter {
    private DelivererOrdersListView view;
    private OrderDAO orderDAO;
    private EmployeeDAO employeeDAO;
    private Deliverer loggedInEmployee;

    /**
     * Initializes the presenter with required DAOs and the view interface.
     * @param view The view implementation.
     * @param orderDAO The data source for orders.
     * @param employeeDAO The data source for employee/deliverer records.
     */
    public DelivererOrdersListPresenter(DelivererOrdersListView view, OrderDAO orderDAO, EmployeeDAO employeeDAO) {
        this.view = view;
        this.orderDAO = orderDAO;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Loads the orders currently assigned to the deliverer.
     * @param employeeId The unique identifier of the deliverer.
     * @return A list of orders to be delivered, or null if the deliverer is not found.
     */
    public ArrayList<Order> loadShippedOrders(String employeeId) {
        this.loggedInEmployee = (Deliverer) employeeDAO.getEmployee(employeeId);

        if (loggedInEmployee == null) {
            view.showError("Σφάλμα: Ο διανομέας δεν βρέθηκε.");
            return null;
        }

        ArrayList<Order> shippedOrders = new ArrayList<>();
        if (loggedInEmployee.getOrders() != null) {
            shippedOrders.addAll(loggedInEmployee.getOrders());
        }

        return shippedOrders;
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
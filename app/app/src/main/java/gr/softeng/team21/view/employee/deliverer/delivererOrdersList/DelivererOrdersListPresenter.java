package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Deliverer's Orders List.
 * Retrieves only the specific orders assigned to the logged-in deliverer
 * utilizing highly optimized database queries via OrderDAO.
 * @author Γιάννης Μονοχολιάς
 */
public class DelivererOrdersListPresenter {
    private final DelivererOrdersListView view;
    private final OrderDAO orderDAO;
    private final EmployeeDAO employeeDAO;
    private Deliverer loggedInEmployee;

    /**
     * Initializes the presenter with required injected DAOs.
     */
    public DelivererOrdersListPresenter(DelivererOrdersListView view, OrderDAO orderDAO, EmployeeDAO employeeDAO) {
        this.view = view;
        this.orderDAO = orderDAO;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Asynchronously verifies the deliverer and performs a highly optimized
     * database query to fetch ONLY the orders assigned to their specific ID.
     * @param employeeId The unique identifier of the deliverer.
     */
    public void loadShippedOrders(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            this.loggedInEmployee = (Deliverer) employee;

            if (loggedInEmployee == null) {
                if (view != null) view.showError("Σφάλμα: Ο διανομέας δεν βρέθηκε.");
                return;
            }

            // Optimized DB Query: Fetch ONLY orders for this deliverer
            orderDAO.getOrdersByDelivererId(employeeId).thenAccept(orders -> {
                ArrayList<Order> activeOrders = new ArrayList<>();

                // Filter the small subset locally to find only SHIPPED orders
                for (Order order : orders) {
                    if (order.getOrderstatus() == OrderStatusType.SHIPPED) {
                        activeOrders.add(order);
                    }
                }
                if (view != null) view.updateOrdersList(activeOrders);
            }).exceptionally(e -> {
                if (view != null) view.showError("Σφάλμα ανάκτησης παραγγελιών: " + e.getMessage());
                return null;
            });

        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα ανάκτησης δεδομένων: " + e.getMessage());
            return null;
        });
    }

    public void onOrderConfirmed(Order order) {
        order.setOrderstatus(OrderStatusType.DELIVERED);

        orderDAO.updateOrder(order).thenAccept(v -> {
            if (loggedInEmployee != null) {
                loggedInEmployee.completeOrder();
            }
            if (view != null) {
                view.showMessage("Order #" + order.getOrdercode() + " ολοκληρώθηκε!");
                view.removeOrderFromList(order);
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Αποτυχία ενημέρωσης συστήματος: " + e.getMessage());
            return null;
        });
    }
}
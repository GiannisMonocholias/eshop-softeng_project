package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Deliverer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;

public class DelivererOrdersListPresenter {
    private DelivererOrdersListView view;
    private OrderDAO orderDAO;
    private EmployeeDAO employeeDAO;
    private Deliverer loggedInEmployee;

    public DelivererOrdersListPresenter(DelivererOrdersListView view, OrderDAO orderDAO, EmployeeDAO employeeDAO) {
        this.view = view;
        this.orderDAO = orderDAO;
        this.employeeDAO = employeeDAO;
    }

    public ArrayList<Order> loadShippedOrders(String employeeId) {
        this.loggedInEmployee = (Deliverer) employeeDAO.getEmployee(employeeId);

        if (loggedInEmployee == null) {
            view.showError("Σφάλμα: Ο διανομέας δεν βρέθηκε.");
            return null;
        }

        ArrayList<Order> shippedOrders = new ArrayList<>();

        if (loggedInEmployee.getOrders() != null) {
            for (Order order : loggedInEmployee.getOrders()) {
                    shippedOrders.add(order);
            }
        }

        return shippedOrders;
    }

    public void onOrderConfirmed(Order order) {

        order.setOrderstatus(OrderStatusType.DELIVERED);

        view.showMessage("Η παραγγελία #" + order.getOrdercode() + " ολοκληρώθηκε!");

        view.removeOrderFromList(order);

        if (loggedInEmployee != null && loggedInEmployee.getOrders() != null) {
            loggedInEmployee.getOrders().remove(order);
        }

    }
}
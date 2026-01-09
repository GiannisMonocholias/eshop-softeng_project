package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Available Orders to Assign screen.
 * Handles the logic of filtering unassigned orders and manages the
 * transactional logic of assigning an order to the currently logged in employee.
 * @author Γιάννης Μονοχολιάς
 */
public class AvailableOrdersToAssignPresenter {

    private AvailableOrdersToAssignView view;
    private EmployeeDAO employeeDAO;
    private OrderDAO orderDAO;
    private OrderPreparationEmployee loggedInEmployee;

    /**
     * Initializes the presenter with required DAOs and the view interface.
     * @param view The view implementation.
     * @param employeeDAO The data source for employee records.
     * @param orderDAO The data source for all system orders.
     */
    public AvailableOrdersToAssignPresenter(AvailableOrdersToAssignView view, EmployeeDAO employeeDAO, OrderDAO orderDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Filters and loads all orders that have a status of NEW.
     * @param employeeId The unique ID of the employee browsing the list.
     * @return An ArrayList of orders available for assignment.
     */
    public ArrayList<Order> loadAvailableOrders(String employeeId){
        this.loggedInEmployee = (OrderPreparationEmployee) employeeDAO.getEmployee(employeeId);

        ArrayList<Order> newOrders = new ArrayList<>();
        for(String ordId: orderDAO.getOrders().keySet()){
            Order cur_order = orderDAO.getOrders().get(ordId);
            if(cur_order != null && cur_order.getOrderstatus() == OrderStatusType.NEW){
                newOrders.add(cur_order);
            }
        }
        return newOrders;
    }

    /**
     * Triggered when a user clicks on an available order.
     * Requests the view to show a confirmation prompt.
     * @param order The selected order.
     */
    public void onOrderClicked(Order order) {
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
        loggedInEmployee.addOrder(order);
        order.setOrderstatus(OrderStatusType.PROCESSING);

        view.showMessage("Η παραγγελία ανατέθηκε επιτυχώς!");
        view.onOrderAssignedSuccess(order);
        view.updateList();
    }
}
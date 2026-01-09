package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Order Status screen.
 * Manages the logic for filtering orders assigned to the Customer Service Employee
 * and handles the triggering of automated customer notifications based on order status.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusPresenter {

    private OrderStatusView view;
    private EmployeeDAO employeeDAO;
    private CustomerServiceEmployee loggedInEmployee;

    /**
     * Initializes the presenter with the view and employee data access.
     * @param view The View implementation.
     * @param employeeDAO The data source for employee records.
     */
    public OrderStatusPresenter(OrderStatusView view, EmployeeDAO employeeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Loads the list of orders specifically assigned to the customer service employee.
     * @param employeeId The unique identifier of the logged-in employee.
     * @return A list of orders requiring customer notification.
     */
    public ArrayList<Order> loadOrders(String employeeId) {
        Employee employee = employeeDAO.getEmployee(employeeId);
        if (employee == null) {
            view.showError("Σφάλμα: Δεν βρέθηκε ID υπαλλήλου.");
            return new ArrayList<Order>();
        }

        ArrayList<Order> orders;
        if (employee instanceof CustomerServiceEmployee) {
            loggedInEmployee = (CustomerServiceEmployee) employee;
            orders = loggedInEmployee.getOrders();
        } else {
            view.showError("Ο υπάλληλος δεν ανήκει στην εξυπηρέτηση πελατών");
            return new ArrayList<Order>();
        }
        return orders;
    }

    /**
     * Processes a click event on an order. Determines the appropriate confirmation
     * message based on whether the order is SHIPPED or DELAYED.
     * @param order The order that was clicked.
     */
    public void onOrderClicked(Order order) {
        if(loggedInEmployee == null){
            view.showError("Σφάλμα: Δεν υπάρχει συνδεδεμένος υπάλληλος");
            return;
        }

        OrderStatusType status = order.getOrderstatus();
        String confirmationMessage = "";

        switch (status) {
            case DELAYED:
                confirmationMessage = "Αποστολή ενημέρωσης ΚΑΘΥΣΤΕΡΗΣΗΣ στον πελάτη."
                        + order.getShoppingCart().getCustomer().getLastname() + "?";
                view.showConfirmationDialog(order, confirmationMessage);
                break;

            case SHIPPED:
                confirmationMessage = "Αποστολή ενημέρωσης ΕΤΟΙΜΟΤΗΤΑΣ notification to customer "
                        + order.getShoppingCart().getCustomer().getLastname() + "?";
                view.showConfirmationDialog(order, confirmationMessage);
                break;

            default:
                view.onOrderSelected(order);
                break;
        }
    }

    /**
     * Finalizes the notification process once the user confirms the action.
     * Sends the notification to the customer and removes the order from the employee's pending list.
     * @param order The order to be processed.
     */
    public void onOrderConfirmed(Order order) {
        Customer customer = order.getShoppingCart().getCustomer();
        OrderStatusType status = order.getOrderstatus();

        switch (status){
            case DELAYED:
                loggedInEmployee.notifyCustomerDelay(order, customer);
                view.showMessage("Επιτυχία! Εστάλη email καθυστέρησης.");
                loggedInEmployee.removeOrder(order);
                break;

            case SHIPPED:
                loggedInEmployee.notifyCustomerReady(order, customer);
                view.showMessage("Επιτυχία! Εστάλη email ετοιμότητας.");
                loggedInEmployee.removeOrder(order);
                break;
        }
        view.updateList();
    }
}
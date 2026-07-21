package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Order Status screen.
 * Manages the logic for filtering orders assigned to the Customer Service Employee
 * asynchronously and handles the triggering of automated customer notifications based on order status.
 * Utilizes Dependency Injection to decouple the data source from the presentation logic.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusPresenter {

    private OrderStatusView view;
    private EmployeeDAO employeeDAO;
    private CustomerServiceEmployee loggedInEmployee;

    /**
     * Initializes the presenter with the view and injected employee data access object.
     * @param view The View implementation (Activity or Stub).
     * @param employeeDAO The data source for employee records.
     */
    public OrderStatusPresenter(OrderStatusView view, EmployeeDAO employeeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Asynchronously loads the list of orders specifically assigned to the customer service employee.
     * Updates the view with the retrieved orders or displays an error if validation fails.
     * @param employeeId The unique identifier of the logged-in employee.
     */
    public void loadOrders(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee == null) {
                view.showError("Σφάλμα: Δεν βρέθηκε ID υπαλλήλου.");
                return;
            }

            if (employee instanceof CustomerServiceEmployee) {
                loggedInEmployee = (CustomerServiceEmployee) employee;
                view.updateOrders(loggedInEmployee.getOrders());
            } else {
                view.showError("Ο υπάλληλος δεν ανήκει στην εξυπηρέτηση πελατών");
            }
        }).exceptionally(e -> {
            view.showError("Σφάλμα κατά την ανάκτηση παραγγελιών: " + e.getMessage());
            return null;
        });
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
                confirmationMessage = "Αποστολή ενημέρωσης ΚΑΘΥΣΤΕΡΗΣΗΣ στον πελάτη "
                        + order.getShoppingCart().getCustomer().getLastname() + "?";
                view.showConfirmationDialog(order, confirmationMessage);
                break;

            case SHIPPED:
                confirmationMessage = "Αποστολή ενημέρωσης ΕΤΟΙΜΟΤΗΤΑΣ στον πελάτη "
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
     * Updates the underlying UI list.
     * @param order The order to be processed.
     */
    public void onOrderConfirmed(Order order) {
        if (loggedInEmployee == null) return;

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
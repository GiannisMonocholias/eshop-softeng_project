package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Employee;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;

public class OrderStatusPresenter {

    private OrderStatusView view;
    private EmployeeDAO employeeDAO;
    private CustomerServiceEmployee loggedInEmployee;

    public OrderStatusPresenter(OrderStatusView view, EmployeeDAO employeeDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
    }


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
            view.showError("Ο υπάλληλος δεν βρέθηκε ή δεν ανήκει στην εξυπηρέτηση πελατών.");
            return new ArrayList<Order>();
        }
        return orders;
    }


    public void onOrderClicked(Order order) {
        if(loggedInEmployee == null){
            view.showError("Σφάλμα: Δεν υπάρχει συνδεδεμένος υπάλληλος");
            return;
        }

        OrderStatusType status = order.getOrderstatus();
        String confirmationMessage = "";

        switch (status) {
            case DELAYED:
                confirmationMessage = "Θέλετε να στείλετε ενημέρωση ΚΑΘΥΣΤΕΡΗΣΗΣ στον πελάτη "
                        + order.getShoppingCart().getCustomer().getLastname() + ";";
                view.showConfirmationDialog(order, confirmationMessage);
                break;

            case SHIPPED:
                confirmationMessage = "Θέλετε να στείλετε ενημέρωση ΕΤΟΙΜΟΤΗΤΑΣ στον πελάτη "
                        + order.getShoppingCart().getCustomer().getLastname() + ";";
                view.showConfirmationDialog(order, confirmationMessage);
                break;

            default:
                view.onOrderSelected(order);
                break;
        }
    }

    public void onOrderConfirmed(Order order) {
        Customer customer = order.getShoppingCart().getCustomer();
        OrderStatusType status = order.getOrderstatus();

        switch (status){
            case DELAYED:
                loggedInEmployee.notifyCustomerDelay(order,customer);
                view.showMessage("Επιτυχία! Στάλθηκε email καθυστέρησης.");
                loggedInEmployee.removeOrder(order);
                break;

            case SHIPPED:
                loggedInEmployee.notifyCustomerReady(order, customer);
                view.showMessage("Επιτυχία! Στάλθηκε email ετοιμότητας.");
                loggedInEmployee.removeOrder(order);
                break;
        }
        view.updateList();

    }

}
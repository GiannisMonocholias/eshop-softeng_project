package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.util.Date;

/**
 * Presenter for the Order Status screen.
 * Handles the asynchronous retrieval of assigned orders via OrderDAO and the dispatching
 * of notification emails to customers using the unified EmailDAO.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusPresenter {
    private final OrderStatusView view;
    private final EmployeeDAO employeeDAO;
    private final OrderDAO orderDAO;
    private final EmailDAO emailDAO;
    private CustomerServiceEmployee loggedInEmployee;

    public OrderStatusPresenter(OrderStatusView view, EmployeeDAO employeeDAO, OrderDAO orderDAO, EmailDAO emailDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
        this.emailDAO = emailDAO;
    }

    public void loadOrders(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof CustomerServiceEmployee) {
                this.loggedInEmployee = (CustomerServiceEmployee) employee;

                // Optimized DB Query: Fetch all orders and filter locally by customerServiceId
                orderDAO.getOrders().thenAccept(ordersMap -> {
                    ArrayList<Order> assignedOrders = new ArrayList<>();
                    for (Order order : ordersMap.values()) {
                        if (employeeId.equals(order.getCustomerServiceId())) {
                            assignedOrders.add(order);
                        }
                    }
                    if (view != null) view.updateOrders(assignedOrders);
                });
            } else {
                if (view != null) view.showError("Ο υπάλληλος δεν ανήκει στην εξυπηρέτηση πελατών");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα: " + e.getMessage());
            return null;
        });
    }

    public void onOrderClicked(Order order) {
        if(loggedInEmployee == null){
            if (view != null) view.showError("Σφάλμα: Δεν υπάρχει συνδεδεμένος υπάλληλος");
            return;
        }

        OrderStatusType status = order.getOrderstatus();
        String confirmationMessage = "";

        switch (status) {
            case DELAYED:
                confirmationMessage = "Αποστολή ενημέρωσης ΚΑΘΥΣΤΕΡΗΣΗΣ στον πελάτη " + order.getShoppingCart().getCustomer().getLastname() + "?";
                if (view != null) view.showConfirmationDialog(order, confirmationMessage);
                break;
            case SHIPPED:
                confirmationMessage = "Αποστολή ενημέρωσης ΕΤΟΙΜΟΤΗΤΑΣ στον πελάτη " + order.getShoppingCart().getCustomer().getLastname() + "?";
                if (view != null) view.showConfirmationDialog(order, confirmationMessage);
                break;
            default:
                if (view != null) view.onOrderSelected(order);
                break;
        }
    }

    public void onOrderConfirmed(Order order) {
        if (loggedInEmployee == null) return;

        Customer customer = order.getShoppingCart().getCustomer();
        boolean isDelay = (order.getOrderstatus() == OrderStatusType.DELAYED);

        sendNotificationEmail(order, customer, isDelay).thenRun(() -> {
            // Remove assignment so it disappears from the queue
            order.setCustomerServiceId(null);

            orderDAO.updateOrder(order).thenRun(() -> {
                if (view != null) {
                    view.showMessage(isDelay ? "Επιτυχία! Εστάλη email καθυστέρησης." : "Επιτυχία! Εστάλη email ετοιμότητας.");
                    loadOrders(loggedInEmployee.getEmployeeId()); // Refresh the view
                }
            });
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα αποστολής email: " + e.getMessage());
            return null;
        });
    }

    private CompletableFuture<Void> sendNotificationEmail(Order order, Customer customer, boolean isDelay) {
        String subject = isDelay ? "Order Delay Notification" : "Order Ready for Delivery";
        StringBuilder msg = new StringBuilder("Dear Customer,\n\n");
        if (isDelay) {
            msg.append("Your order ").append(order.getOrdercode()).append(" is delayed due to insufficient stock:\n\nWe apologize for the inconvenience.\nCustomer Service Team");
        } else {
            msg.append("Your order ").append(order.getOrdercode()).append(" is now ready for delivery.\nYou will be contacted by our courier shortly.\n\nBest regards,\nCustomer Service Team");
        }

        EmailMessage email = new EmailMessage(loggedInEmployee.getEmailAddress(), customer.getEmailAddress(), subject, msg.toString(), new Date());

        // ΜΙΑ ενιαία κλήση αποθήκευσης email βάσει της νέας αρχιτεκτονικής
        return emailDAO.saveEmail(email);
    }
}
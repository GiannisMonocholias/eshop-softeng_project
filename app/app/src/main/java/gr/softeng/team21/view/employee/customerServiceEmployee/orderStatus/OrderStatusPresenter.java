package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.CustomerServiceEmployee;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderStatusType;
import gr.softeng.team21.util.Date;

/**
 * Presenter for the Order Status screen.
 * Manages the logic for filtering assigned orders asynchronously and generates
 * automated customer email notifications, safely delegating persistence to EmailDAO
 * via CompletableFutures.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderStatusPresenter {

    private final OrderStatusView view;
    private final EmployeeDAO employeeDAO;
    private final EmailDAO emailDAO;
    private CustomerServiceEmployee loggedInEmployee;

    /**
     * Initializes the presenter with the required View and DAOs.
     * @param view The View interface implementation.
     * @param employeeDAO The data source for employee records.
     * @param emailDAO The data source for dispatching notification emails.
     */
    public OrderStatusPresenter(OrderStatusView view, EmployeeDAO employeeDAO, EmailDAO emailDAO) {
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.emailDAO = emailDAO;
    }

    /**
     * Asynchronously loads the list of orders assigned to the logged-in employee.
     * @param employeeId The unique identifier of the logged-in employee.
     */
    public void loadOrders(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee == null) {
                if (view != null) view.showError("Σφάλμα: Δεν βρέθηκε ID υπαλλήλου.");
                return;
            }
            if (employee instanceof CustomerServiceEmployee) {
                loggedInEmployee = (CustomerServiceEmployee) employee;
                if (view != null) view.updateOrders(loggedInEmployee.getOrders());
            } else {
                if (view != null) view.showError("Ο υπάλληλος δεν ανήκει στην εξυπηρέτηση πελατών");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα κατά την ανάκτηση παραγγελιών: " + e.getMessage());
            return null;
        });
    }

    /**
     * Processes a click event on an order. Determines the appropriate confirmation
     * message based on the order's status (DELAYED or SHIPPED).
     * @param order The order that was clicked by the user.
     */
    public void onOrderClicked(Order order) {
        if(loggedInEmployee == null){
            if (view != null) view.showError("Σφάλμα: Δεν υπάρχει συνδεδεμένος υπάλληλος");
            return;
        }

        OrderStatusType status = order.getOrderstatus();
        String confirmationMessage = "";

        switch (status) {
            case DELAYED:
                confirmationMessage = "Αποστολή ενημέρωσης ΚΑΘΥΣΤΕΡΗΣΗΣ στον πελάτη "
                        + order.getShoppingCart().getCustomer().getLastname() + "?";
                if (view != null) view.showConfirmationDialog(order, confirmationMessage);
                break;
            case SHIPPED:
                confirmationMessage = "Αποστολή ενημέρωσης ΕΤΟΙΜΟΤΗΤΑΣ στον πελάτη "
                        + order.getShoppingCart().getCustomer().getLastname() + "?";
                if (view != null) view.showConfirmationDialog(order, confirmationMessage);
                break;
            default:
                if (view != null) view.onOrderSelected(order);
                break;
        }
    }

    /**
     * Finalizes the notification process once confirmed by the user.
     * Generates the email object, saves it via EmailDAO asynchronously,
     * and updates the UI state upon successful completion.
     * @param order The order to be processed.
     */
    public void onOrderConfirmed(Order order) {
        if (loggedInEmployee == null) return;

        Customer customer = order.getShoppingCart().getCustomer();
        boolean isDelay = (order.getOrderstatus() == OrderStatusType.DELAYED);

        sendNotificationEmail(order, customer, isDelay).thenAccept(v -> {
            if (view != null) {
                view.showMessage(isDelay ? "Επιτυχία! Εστάλη email καθυστέρησης." : "Επιτυχία! Εστάλη email ετοιμότητας.");
                loggedInEmployee.removeOrder(order);
                view.updateList();
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα αποστολής email: " + e.getMessage());
            return null;
        });
    }

    /**
     * Constructs the notification email string and saves it to the Inbox and Sent
     * folders simultaneously using the EmailDAO.
     * @param order The order triggering the notification.
     * @param customer The recipient customer.
     * @param isDelay True if the notification is for a delay; false if ready for delivery.
     * @return A CompletableFuture that completes when the email has been saved in both folders.
     */
    private CompletableFuture<Void> sendNotificationEmail(Order order, Customer customer, boolean isDelay) {
        String subject = isDelay ? "Order Delay Notification" : "Order Ready for Delivery";
        StringBuilder msg = new StringBuilder();

        msg.append("Dear Customer,\n\n");
        if (isDelay) {
            msg.append("Your order ").append(order.getOrdercode())
                    .append(" is delayed due to insufficient stock:\n\nWe apologize for the inconvenience.\nCustomer Service Team");
        } else {
            msg.append("Your order ").append(order.getOrdercode())
                    .append(" is now ready for delivery.\nYou will be contacted by our courier shortly.\n\nBest regards,\nCustomer Service Team");
        }

        EmailMessage email = new EmailMessage(loggedInEmployee.getEmailAddress(), customer.getEmailAddress(), subject, msg.toString(), new Date());

        CompletableFuture<Void> sentFuture = emailDAO.saveSentEmails(email);
        CompletableFuture<Void> inboxFuture = emailDAO.saveInboxEmails(email);

        // Wait for both database writes to finish
        return CompletableFuture.allOf(sentFuture, inboxFuture);
    }
}
package gr.softeng.team21.domain;

import java.util.ArrayList;
import gr.softeng.team21.contact.EmailAddress;
import gr.softeng.team21.contact.EmailMessage;
import gr.softeng.team21.util.Date;

/**
 * Represents an employee specialized in customer service.
 * Responsibilities include handling customer inquiries
 * and sending notifications regarding order status.
 * @author Γιάννης Μονοχολιάς
 */
public class CustomerServiceEmployee extends Employee {
    private int totalResponses;
    private ArrayList<Order> orders;


    /**
     * Default constructor
     * */
    public CustomerServiceEmployee() {
    }

    /**
     * Constructs a new CustomerServiceEmployee with full details.
     * * @param username      The unique account username.
     * @param firstname     The employee's first name.
     * @param password      The account password.
     * @param lastname      The employee's last name.
     * @param phoneNumber   The contact phone number.
     * @param emailaddress  The professional email address.
     * @param employeeId    The unique business ID.
     * @param bonus         Performance-based bonus amount.
     * @param salary        Base salary.
     * @param workingHours  Contracted weekly working hours.
     * @param employeeState The current state (e.g., ACTIVE, ON_LEAVE).
     * @param hireDate      The official employment date.
     */
    public CustomerServiceEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        totalResponses = 0;
        orders = new ArrayList<>();
    }

    /**
     * @return the number of customer emails that have not yet been replied to.
     */
    public int getUnansweredRequests() {
        return emailDAOMemory.getUnrepliedEmails().size();
    }

    /**
     * @return the total number of responses sent by this employee.
     */
    public int getTotalResponses() {
        return totalResponses;
    }

    /**
     * @return the list of orders managed by this employee.
     */
    public ArrayList<Order> getOrders() { return orders; }

    /**
     * Assigns a new order to this employee.
     * @param order the order to add.
     */
    public void addOrder(Order order) { orders.add(order); }

    /**
     * Removes an order from this employee's responsibility.
     * @param order the order to remove.
     */
    public void removeOrder(Order order) { orders.remove(order); }

    /**
     * Replaces the current list of orders with a new set.
     * @param orders the new list of orders.
     */
    public void setOrders(ArrayList<Order> orders) { this.orders = orders; }

    /**
     * Retrieves all messages currently in the employee's inbox.
     * @return an ArrayList of EmailMessage objects.
     */
    ArrayList<EmailMessage> getEmails() {
        return emailDAOMemory.getInboxEmails();
    }

    /**
     * Notifies a customer via email that their order has been delayed.
     * @param order    the delayed order.
     * @param customer the recipient customer.
     * @throws NullPointerException if the order or customer is null.
     */
    public void notifyCustomerDelay(Order order, Customer customer) {
        if(order == null) throw new NullPointerException("The Order order argument is null");
        if(customer == null) throw new NullPointerException("The Customer customer argument is null");

        StringBuilder msg = new StringBuilder();
        msg.append("Dear Customer,\n\n");
        msg.append("Your order ").append(order.getOrdercode());
        msg.append(" is delayed due to insufficient stock:\n");
        msg.append("\nWe apologize for the inconvenience.\nCustomer Service Team");

        Date dateSent = new Date();
        sendEmail(this, customer, "Order Delay Notification", msg.toString(), dateSent);
    }

    /**
     * Notifies a customer via email that their order is ready for delivery.
     * @param order    the ready order.
     * @param customer the recipient customer.
     * @throws NullPointerException if the order or customer is null.
     */
    public void notifyCustomerReady(Order order, Customer customer) {
        if(order == null) throw new NullPointerException("The Order order argument is null");
        if(customer == null) throw new NullPointerException("The Customer customer argument is null");

        StringBuilder msg = new StringBuilder();
        msg.append("Dear Customer,\n\n");
        msg.append("Your order ").append(order.getOrdercode()).append(" is now ready for delivery.\n");
        msg.append("You will be contacted by our courier shortly.\n\n");
        msg.append("Best regards,\nCustomer Service Team");

        Date dateSent = new Date();
        sendEmail(this, customer, "Order Ready for Delivery", msg.toString(), dateSent);
    }

    /**
     * Replies to a specific customer inquiry and updates the total response count.
     * @param customer     the customer to reply to.
     * @param inquiry      the original email inquiry.
     * @param responseBody the content of the reply.
     * @throws NullPointerException if any argument is null.
     */
    public void replyToCustomerInquiry(Customer customer, EmailMessage inquiry, String responseBody) {
        if(customer == null) throw new NullPointerException("The Customer customer argument is null");
        if(inquiry == null) throw new NullPointerException("The EmailMessage inquiry argument is null");
        if(responseBody == null) throw new NullPointerException("The responseBody inquiry argument is null");

        String msg = "Dear Customer,\n\n" + responseBody + "\n\n" +
                "If you have further questions, feel free to contact us.\n" +
                "Customer Service Team";

        String subject = "Reply to inquiry: '" + inquiry.getSubject() + "'";
        totalResponses++;

        Date dateSent = new Date();
        replyToEmail(this, customer, inquiry, subject, msg, dateSent);
    }
}
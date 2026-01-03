package gr.softeng.team21.domain;

import java.util.ArrayList;

public  class CustomerServiceEmployee extends Employee{
    private int totalResponses;
    private ArrayList<Order> orders;


    public CustomerServiceEmployee(String username, String firstname, String password, String lastname, String phoneNumber, EmailAddress emailaddress, String employeeId, int bonus, int salary, int workingHours, EmployeeState employeeState, Date hireDate) {
        super(username, firstname, password, lastname, phoneNumber, emailaddress, employeeId, bonus, salary, workingHours, employeeState, hireDate);
        totalResponses = 0;
        orders = new ArrayList<>();
    }

    public int getUnansweredRequests() {
        return emailDAOMemory.getUnrepliedEmails().size();
    }


    public int getTotalResponses() {
        return totalResponses;
    }

    public ArrayList<Order> getOrders(){return orders;}

    public void addOrder(Order order){orders.add(order);}

    public void setOrders(ArrayList<Order> orders){this.orders = orders;}


    ArrayList<EmailMessage> getEmails(){
        return emailDAOMemory.getInboxEmails();
    }


    public void notifyCustomerDelay(Order order, Customer customer) {
        if(order == null)
            throw new NullPointerException("The Order order argument is null");
        if(customer == null)
            throw new NullPointerException("The Customer customer argument is null");

        StringBuilder msg = new StringBuilder();
        msg.append("Dear Customer,\n\n");
        msg.append("Your order ").append(order.getOrdercode());
        msg.append(" is delayed due to insufficient stock:\n");
        msg.append("\nWe apologize for the inconvenience.\nCustomer Service Team");

        sendEmail(this, customer, "Order Delay Notification", msg.toString());
    }

    public void notifyCustomerReady(Order order, Customer customer) {
        if(order == null)
            throw  new NullPointerException("The Order order argument is null");
        if(customer == null)
            throw  new NullPointerException("The Customer customer argument is null");

        StringBuilder msg =new StringBuilder();
        msg.append("Dear Customer,\n\n");
        msg.append("Your order " + order.getOrdercode() + " is now ready for delivery.\n");
        msg.append("You will be contacted by our courier shortly.\n\n");
        msg.append("Best regards,\nCustomer Service Team");

        sendEmail(this, customer, "Order Ready for Delivery", msg.toString());
    }


    public void replyToCustomerInquiry(Customer customer,EmailMessage inquiry,String responseBody) {
        if(customer == null)
            throw  new NullPointerException("The Customer customer argument is null");
        if(inquiry == null)
            throw  new NullPointerException("The EmailMessage inquiry argument is null");
        if(responseBody == null)
            throw  new NullPointerException("The responseBody inquiry argument is null");

        String msg = "Dear Customer,\n\n" +
                responseBody + "\n\n" +
                "If you have further questions, feel free to contact us.\n" +
                "Customer Service Team";

        String subject = "Reply to inquiry: '" + inquiry.getSubject() + "'";
        totalResponses++;

        replyToEmail(this,customer, inquiry, subject, msg);
    }


}
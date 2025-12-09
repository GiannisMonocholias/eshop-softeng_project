package gr.softeng.team21.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SystemCalendar {
    private static SystemCalendar instance;
    private Date currentdate;
    protected EmailAddress emailAddress;
    protected EmailProviderStub emailProviderStub;

    private SystemCalendar (Date currentdate) {
        this.currentdate = currentdate;
        this.emailAddress = new EmailAddress("SystemCalendar");
        emailProviderStub = new EmailProviderStub();
    }

    public static SystemCalendar getInstance(){
        if(instance == null){
            instance =  new SystemCalendar(new Date());
        }

        return instance;
    }

    public Date getCurrnetdate ( ) {
        return currentdate;
    }

    public void setCurrentdate (Date currentdate) {
        this.currentdate = currentdate;
    }

    public EmailProviderStub getEmailProviderStub() {
        return emailProviderStub;
    }

    public void setEmailProviderStub(EmailProviderStub emailProviderStub) {
        this.emailProviderStub = emailProviderStub;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailaddress) {
        this.emailAddress = emailaddress;
    }


    public void monitorOrders() {
        HashMap<String,Order> orders = OrdersRepository.getInstance().getOrders();
        for (String orderId : orders.keySet()) {
            Order curOrder = orders.get(orderId);
            if (curOrder.getOrderstatus().equals(StatusType.DELAYED)) {
                notifyCustomerDelay(curOrder);
            }
            else if(curOrder.getOrderstatus().equals(StatusType.SHIPPED)){
                notifyCustomerReady(curOrder);
            }
        }
    }


    public void sendEmail(SystemCalendar sender, User recipient, String subject, String body) {
        deliverEmail(sender,recipient,null,subject,body,false);
    }

    public void deliverEmail(SystemCalendar sender, User recipient,EmailMessage original, String subject, String body, boolean isReplyMessage) {
        EmailMessage email = new EmailMessage();
        email.setFrom(sender.getEmailAddress());
        email.setTo(recipient.getEmailAddress());
        email.setSubject(subject);
        email.setBody(body);
        email.setReplyMessage(isReplyMessage);

        if(original != null)
            original.setReplied(true);

        recipient.getEmailProviderStub().saveInboxEmails(email);
        sender.getEmailProviderStub().saveSentEmails(email);
    }




    public void notifyCustomerDelay(Order order) {

        String subject = "ORDER DELAY";
        String body = "Delay! You are informed that your order with id: " + order.getOrdercode() +
                " is going to delay, due to insufficient stock";
        sendEmail(this,order.getShoppingCart().getCustomer(),subject,body);

    }


    public void notifyCustomerReady(Order order) {

        String subject = "ORDER READY";
        String body = "We inform you, that your order with id: " + order.getOrdercode() +
                " is now ready and has been shipped (or is ready for pickup). Thank you for shopping with us!";

        sendEmail(this, order.getShoppingCart().getCustomer(), subject, body);

    }


}

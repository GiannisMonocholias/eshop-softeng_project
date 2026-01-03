package gr.softeng.team21.view.customer.EmailList;

public interface CustomerEmailListView {
    void goToCreateNewMessage(String customerId);
    void goToEmailDetails(String subject, String body, String sender, String receiver, String customerId);
}

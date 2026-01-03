package gr.softeng.team21.view.customer.EmailList;

import java.util.ArrayList;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.EmailMessage;

public class CustomerEmailListPresenter {
    private CustomerDAO customerDAO;
    private CustomerEmailListView view;

    public CustomerEmailListPresenter(CustomerEmailListView view, CustomerDAO customerDAO){
        this.customerDAO = customerDAO;
        this.view = view;
    }

    public ArrayList<EmailMessage> getInbox(String customerId){
        Customer customer = customerDAO.getCustomer(customerId);
        if (customer != null) {
            return customer.getEmailProvider().getInboxEmails();
        }
        return new ArrayList<>();
    }

    public void onCreateNewMsgSelected(String customerId){
        view.goToCreateNewMessge(customerId);
    }

    public void onEmailSelected(EmailMessage email, String customerId){
        email.setRead(true);
        view.goToEmailDetails(
                email.getSubject(), email.getBody(),
                email.getFrom().toString(), email.getTo().toString(),
                customerId
        );
    }
}
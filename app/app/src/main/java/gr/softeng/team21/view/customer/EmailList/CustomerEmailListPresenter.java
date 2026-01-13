package gr.softeng.team21.view.customer.EmailList;

import java.util.ArrayList;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Presenter for the Customer Email List activity.
 * Handles the logic of retrieving emails and processing user interactions
 * before triggering navigation commands to the view.
 * @author PAVLOS GRATSANIS
 */
public class CustomerEmailListPresenter {
    private CustomerDAO customerDAO;
    private CustomerEmailListView view;

    /**
     * Constructs a presenter with the  CustomerDAO and view references.
     * @param view The view implementation .
     * @param customerDAO The data access object for customer information.
     */
    public CustomerEmailListPresenter(CustomerEmailListView view, CustomerDAO customerDAO){
        this.customerDAO = customerDAO;
        this.view = view;
    }

    /**
     * Retrieves all incoming emails for a specific customer.
     * @param customerId The ID of the customer.
     * @return An ArrayList of EmailMessage objects or an empty list if customer is null object.
     */
    public ArrayList<EmailMessage> getInbox(String customerId){
        Customer customer = customerDAO.getCustomer(customerId);
        if (customer != null) {
            return customer.getEmailProvider().getInboxEmails();
        }
        return new ArrayList<>();
    }

    /**
     * Called when the user wants to compose a new email.
     * @param customerId The current customer's ID.
     */
    public void onCreateNewMsgSelectedClicked(String customerId){
        view.goToCreateNewMessge(customerId);
    }

    /**
     * Handles the selection of a specific email.
     * Marks the email as read and triggers navigation to the details screen.
     * @param email The selected email message.
     * @param customerId The current customer's ID.
     */
    public void onEmailSelected(EmailMessage email, String customerId){
        email.setRead(true);
        view.goToEmailDetails(
                email.getSubject(), email.getBody(),
                email.getFrom().toString(), email.getTo().toString(),
                customerId
        );
    }
}
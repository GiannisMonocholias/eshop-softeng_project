package gr.softeng.team21.view.customer.EmailList;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Presenter for the Customer Email List activity.
 * Handles the asynchronous retrieval of emails using CompletableFuture
 * and processes user interactions.
 * @author PAVLOS GRATSANIS
 */
public class CustomerEmailListPresenter {

    private final CustomerDAO customerDAO;
    private final CustomerEmailListView view;

    /**
     * Constructs a presenter with the CustomerDAO and view references via Dependency Injection.
     *
     * @param view The view implementation.
     * @param customerDAO The data access object for customer information.
     */
    public CustomerEmailListPresenter(CustomerEmailListView view, CustomerDAO customerDAO){
        this.customerDAO = customerDAO;
        this.view = view;
    }

    /**
     * Retrieves all incoming emails for a specific customer asynchronously
     * and pushes the result to the view.
     *
     * @param customerId The ID of the customer.
     */
    public void loadInbox(String customerId) {
        customerDAO.getCustomer(customerId).thenAccept(customer -> {

            if (customer != null) {
                customer.getEmailProvider().getInboxEmails()
                        .thenAccept(emails -> {
                            if (view != null) view.showEmails(emails);
                        })
                        .exceptionally(e -> {
                            if (view != null) view.showError("Σφάλμα φόρτωσης: " + e.getMessage());
                            return null;
                        });
            } else {
                if (view != null) view.showError("Ο πελάτης δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            if(view != null) view.showError("Σφάλμα επικοινωνίας με τη βάση: " + e.getMessage());
            return null;
        });
    }

    /**
     * Called when the user wants to compose a new email.
     *
     * @param customerId The current customer's ID.
     */
    public void onCreateNewMsgSelectedClicked(String customerId){
        if (view != null) view.goToCreateNewMessge(customerId);
    }

    /**
     * Handles the selection of a specific email.
     * Marks the email as read and triggers navigation to the details screen.
     *
     * @param email The selected email message.
     * @param customerId The current customer's ID.
     */
    public void onEmailSelected(EmailMessage email, String customerId){
        email.setRead(true);
        if (view != null) {
            view.goToEmailDetails(
                    email.getSubject(), email.getBody(),
                    email.getFrom().toString(), email.getTo().toString(),
                    customerId
            );
        }
    }
}
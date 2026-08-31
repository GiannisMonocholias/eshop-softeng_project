package gr.softeng.team21.view.customer.EmailList;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.EmailDAO;
import gr.softeng.team21.contact.EmailMessage;

/**
 * Presenter for the Customer Email List activity.
 * Handles the asynchronous retrieval of emails directly from the centralized EmailDAO
 * using the customer's email address, strictly decoupling domain entities from data access.
 *
 * @author PAVLOS GRATSANIS
 */
public class CustomerEmailListPresenter {

    private final CustomerDAO customerDAO;
    private final EmailDAO emailDAO;
    private final CustomerEmailListView view;

    /**
     * Constructs a presenter with the DAOs and view references via Dependency Injection.
     *
     * @param view The view implementation.
     * @param customerDAO The data access object for customer information.
     * @param emailDAO The centralized data access object for emails.
     */
    public CustomerEmailListPresenter(CustomerEmailListView view, CustomerDAO customerDAO, EmailDAO emailDAO){
        this.customerDAO = customerDAO;
        this.emailDAO = emailDAO;
        this.view = view;
    }

    /**
     * Retrieves all incoming emails for a specific customer asynchronously.
     * First fetches the customer to get their exact email address, then queries the unified EmailDAO.
     *
     * @param customerId The ID of the customer.
     */
    public void loadInbox(String customerId) {
        customerDAO.getCustomer(customerId).thenAccept(customer -> {
            if (customer != null && customer.getEmailAddress() != null) {

                String customerEmailAddress = customer.getEmailAddress().toString();

                // Fetch emails from the unified collection using the receiver's address
                emailDAO.getEmailsForUser(customerEmailAddress)
                        .thenAccept(emails -> {
                            if (view != null) view.showEmails(emails);
                        })
                        .exceptionally(e -> {
                            if (view != null) view.showError("Σφάλμα φόρτωσης emails: " + e.getMessage());
                            return null;
                        });

            } else {
                if (view != null) view.showError("Ο πελάτης δεν βρέθηκε ή δεν έχει δηλωμένο email.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα επικοινωνίας με τη βάση πελατών: " + e.getMessage());
            return null;
        });
    }

    public void onCreateNewMsgSelectedClicked(String customerId){
        if (view != null) view.goToCreateNewMessge(customerId);
    }

    /**
     * Handles the selection of a specific email.
     * Marks the email as read locally, asynchronously updates the state in the database,
     * and triggers navigation to the details screen.
     */
    public void onEmailSelected(EmailMessage email, String customerId){
        // Αλλαγή κατάστασης
        email.setRead(true);

        // Αποθήκευση της αλλαγής στη βάση ασύγχρονα (fire-and-forget)
        emailDAO.updateEmail(email);

        if (view != null) {
            view.goToEmailDetails(
                    email.getSubject(), email.getBody(),
                    email.getFrom().toString(), email.getTo().toString(),
                    customerId
            );
        }
    }
}
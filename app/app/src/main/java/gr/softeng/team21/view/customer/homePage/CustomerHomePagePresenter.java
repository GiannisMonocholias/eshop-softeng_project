package gr.softeng.team21.view.customer.homePage;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Customer;

/**
 * Presenter for the CustomerHomePageActivity.
 * Handles interactions between the {@link CustomerHomePageView} and the domain logic,
 * including navigation, message display, and asynchronous account deletion.
 * @author PAVLOS GRATSANIS
 */
public class CustomerHomePagePresenter {
    private CustomerHomePageView view;
    private Customer customer;
    private CustomerDAO customerDAO;
    private UserCredentialsDAO userCredentialsDAO;

    /**
     * Initializes the presenter and attempts to fetch the customer asynchronously.
     * @param view The view interface.
     * @param customerId The ID of the customer.
     * @param customerDAO The DAO for customer operations.
     * @param userCredentialsDAO The DAO for user credential operations.
     */
    public CustomerHomePagePresenter(CustomerHomePageView view, String customerId, CustomerDAO customerDAO, UserCredentialsDAO userCredentialsDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.userCredentialsDAO = userCredentialsDAO;
        loadCustomer(customerId);
    }

    /**
     * Retrieves the customer asynchronously from the repository.
     * @param customerId The ID of the customer to load.
     */
    private void loadCustomer(String customerId) {
        customerDAO.getCustomer(customerId).thenAccept(loadedCustomer -> {
            if (loadedCustomer != null) {
                this.customer = loadedCustomer;
            } else {
                view.showMessage("Προσοχή: Ο πελάτης δεν βρέθηκε!");
                view.goToLogin();
            }
        }).exceptionally(e -> {
            view.showMessage("Σφάλμα φόρτωσης πελάτη: " + e.getMessage());
            return null;
        });
    }

    /**
     * Handles the click for editing user data.
     */
    public void EditDataClicked() {
        if (customer != null) {
            view.goToEditData(customer.getCustomer_id());
        }
    }

    /**
     * Handles the logout click.
     */
    public void LogoutClicked() {
        view.goToLogin();
    }

    /**
     * Handles the click for finding products.
     */
    public void FindProductClicked() {
        if (customer != null) {
            view.goToFindProduct(customer.getCustomer_id());
        }
    }

    /**
     * Handles the click for account deletion request.
     */
    public void DeleteClicked() {
        if (customer != null) {
            view.showDeleteConfirmation();
        }
    }

    /**
     * Confirms and executes the account deletion process asynchronously.
     * Removes the user from both credentials and customer repositories.
     */
    public void DeleteConfirm() {

        if (customer != null) {
            // First delete customer's credentials
            userCredentialsDAO.removeUser(customer.getUsername()).thenAccept(v1 -> {
                // Subsequently customer itself
                customerDAO.removeCustomer(customer).thenAccept(v2 -> {
                    view.showMessage("Ο λογαριασμός σας διαγράφηκε.");
                    view.goToLogin();
                }).exceptionally(e -> {
                    view.showMessage("Σφάλμα διαγραφής προφίλ: " + e.getMessage());
                    return null;
                });
            }).exceptionally(e -> {
                view.showMessage("Σφάλμα διαγραφής κωδικών: " + e.getMessage());
                return null;
            });
        }

    }

    /**
     * Handles the click for viewing the inbox.
     */
    public void InboxClicked() {
        if (customer != null) {
            view.goToInbox(customer.getCustomer_id());
        }
    }
}
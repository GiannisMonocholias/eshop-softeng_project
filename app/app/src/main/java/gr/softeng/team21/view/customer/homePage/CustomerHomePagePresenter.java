package gr.softeng.team21.view.customer.homePage;

import gr.softeng.team21.dao.UserCredentialsDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.memorydao.CustomerDAOMemory;
import gr.softeng.team21.memorydao.UserCredentialsDAOMemory;

/**
 * Presenter for the CustomerHomePageActivity.
 * Handles interactions between the {@link CustomerHomePageView} and the domain logic,
 * including navigation,message display and account deletion.
 * @author PAVLOS GRATSANIS
 */
public class CustomerHomePagePresenter {
    private CustomerHomePageView view;
    private Customer customer;

    /**
     * Initializes the presenter with the view and the logged-in customer.
     * @param view The view interface.
     * @param customer The customer domain object.
     */
    public CustomerHomePagePresenter(CustomerHomePageView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    /**
     * Handles the click  for editing user data.
     */
    public void EditDataClicked() {
        view.goToEditData();
    }

    /**
     * Handles the logout click .
     */
    public void LogoutClicked() {
        view.goToLogin();
    }

    /**
     * Handles the click  for finding products.
     */
    public void FindProductClicked() {
        view.goToFindProduct();
    }

    /**
     * Handles the click  for account deletion request.
     */
    public void DeleteClicked() {
        view.showDeleteConfirmation();
    }

    /**
     * Confirms and executes the account deletion process.
     * Removes the user from repositories and navigates to login.
     */
    public void DeleteConfirm() {
        UserCredentialsDAOMemory userrepo = UserCredentialsDAOMemory.getInstance();
        CustomerDAOMemory customerrepo = CustomerDAOMemory.getInstance();
        if(customer!=null){
            userrepo.removeUser(customer.getUsername());
            customerrepo.removeCustomer(customer);
            view.showMessage("Ο λογαριασμός σας διαγράφηκε.");
            view.goToLogin();
        }
    }

    /**
     * Handles the click  for viewing the inbox.
     */
    public void InboxClicked() {
        view.goToInbox();
    }
}
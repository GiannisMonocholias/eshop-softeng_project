package gr.softeng.team21.view.customer.homePage;

/**
 * Interface for the CustomerHomePage.
 * Defines the methods of navigation and message display.
 * @author PAVLOS GRATSANIS
 */
public interface CustomerHomePageView {

    /**
     * Navigates the user back to the login screen.
     */
    void goToLogin();

    /**
     * Navigates to the user data editing screen.
     * @param customerId The ID of the current customer.
     */
    void goToEditData(String customerId);

    /**
     * Navigates to the product search screen.
     * @param customerId The ID of the current customer.
     */
    void goToFindProduct(String customerId);

    /**
     * Navigates to the customer's email inbox.
     * @param customerId The ID of the current customer.
     */
    void goToInbox(String customerId);

    /**
     * Displays a confirmation dialog for account deletion.
     */
    void showDeleteConfirmation();

    /**
     * Displays a toast message to the user.
     * @param msg The message to display.
     */
    void showMessage(String msg);
}
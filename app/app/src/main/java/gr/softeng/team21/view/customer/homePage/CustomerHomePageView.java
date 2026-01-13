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
     */
    void goToEditData();

    /**
     * Navigates to the product search screen.
     */
    void goToFindProduct();

    /**
     * Navigates to the customer's email inbox.
     */
    void goToInbox();

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
package gr.softeng.team21.view.customer.homePage;

/**
 * Stub implementation of {@link CustomerHomePageView} for testing purposes.
 * It tracks the number of times navigation methods are called and captures messages
 * to verify the presenter's logic regarding the menu actions.
 * @author PAVLOS GRATSANIS
 */
public class CustomerHomePageViewStub implements CustomerHomePageView {

    private int LoginCount = 0;
    private int editDataCount = 0;
    private int findProductCount = 0;
    private int inboxCount = 0;
    private int deleteCount = 0;
    private String message = "";

    /**
     * Sets the message string manually (helper for tests).
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the number of times navigation to login was triggered.
     * Used for verification in tests.
     * @return The login navigation count.
     */
    public int getLoginCount() {
        return LoginCount;
    }

    /**
     * Returns the number of times navigation to edit data was triggered.
     * Used for verification in tests.
     * @return The edit data navigation count.
     */
    public int getEditDataCount() {
        return editDataCount;
    }

    /**
     * Returns the number of times navigation to find product was triggered.
     * Used for verification in tests.
     * @return The find product navigation count.
     */
    public int getFindProductCount() {
        return findProductCount;
    }

    /**
     * Returns the last message.
     * Used for verification in tests.
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the number of times the delete confirmation was shown.
     * Used for verification in tests.
     * @return The delete confirmation count.
     */
    public int getDeleteCount() {
        return deleteCount;
    }

    /**
     * Returns the number of times navigation to inbox was triggered.
     * Used for verification in tests.
     * @return The inbox navigation count.
     */
    public int getInboxCount() {
        return inboxCount;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for login navigation.
     */
    @Override
    public void goToLogin() {
        LoginCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for edit data navigation.
     */
    @Override
    public void goToEditData() {
        editDataCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for product search navigation.
     */
    @Override
    public void goToFindProduct() {
        findProductCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for inbox navigation.
     */
    @Override
    public void goToInbox() {
        inboxCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the delete confirmation counter and simulates setting a success message.
     */
    @Override
    public void showDeleteConfirmation() {
        deleteCount++;
        setMessage("Ο λογαριασμός σας διαγράφηκε.");
    }

    /**
     * {@inheritDoc}
     * Stores the message in a variable for verification.
     */
    @Override
    public void showMessage(String msg) {
        this.message = msg;
    }
}
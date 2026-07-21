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
    private String passedCustomerId = null; // Νέα μεταβλητή για έλεγχο στο Test

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLoginCount() { return LoginCount; }
    public int getEditDataCount() { return editDataCount; }
    public int getFindProductCount() { return findProductCount; }
    public String getMessage() { return message; }
    public int getDeleteCount() { return deleteCount; }
    public int getInboxCount() { return inboxCount; }

    /**
     * Επιστρέφει το ID που περάστηκε στην τελευταία πλοήγηση.
     */
    public String getPassedCustomerId() { return passedCustomerId; }

    @Override
    public void goToLogin() {
        LoginCount++;
    }

    @Override
    public void goToEditData(String customerId) {
        editDataCount++;
        passedCustomerId = customerId;
    }

    @Override
    public void goToFindProduct(String customerId) {
        findProductCount++;
        passedCustomerId = customerId;
    }

    @Override
    public void goToInbox(String customerId) {
        inboxCount++;
        passedCustomerId = customerId;
    }

    @Override
    public void showDeleteConfirmation() {
        deleteCount++;
    }

    @Override
    public void showMessage(String msg) {
        this.message = msg;
    }
}
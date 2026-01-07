package gr.softeng.team21.view.customer.homePage;

public class CustomerHomePageViewStub implements CustomerHomePageView{

   private int LogoutCount = 0;
    private int editDataCount = 0;
    private int findProductCount = 0;
    private int mainCount = 0;
    private int inboxCount = 0;
    private int deleteCount = 0;
    private String message = "";

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLogoutCount() {
        return LogoutCount;
    }

    public int getEditDataCount() {
        return editDataCount;
    }

    public int getFindProductCount() {
        return findProductCount;
    }

    public int getMainCount() {
        return mainCount;
    }

    public String getMessage() {
        return message;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public int getInboxCount() {
        return inboxCount;
    }

    @Override
    public void goToLogin() {
        LogoutCount++;
    }

    @Override
    public void goToEditData() {
        editDataCount++;
    }

    @Override
    public void goToFindProduct() {
        findProductCount++;
    }

    @Override
    public void goToMain() {
        mainCount++;
    }

    @Override
    public void goToInbox() {
        inboxCount++;
    }

    @Override
    public void showDeleteConfirmation() {
        deleteCount++;
        setMessage("Ο λογαριασμός σας διαγράφηκε.");
    }

    @Override
    public void showMessage(String msg) {
        this.message = msg;
    }
}

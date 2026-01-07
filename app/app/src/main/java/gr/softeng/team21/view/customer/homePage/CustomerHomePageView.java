package gr.softeng.team21.view.customer.homePage;

public interface CustomerHomePageView {
    void goToLogin();
    void goToEditData();
    void goToFindProduct();
    void goToInbox();
    void showDeleteConfirmation();
    void showMessage(String msg);
}
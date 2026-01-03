package gr.softeng.team21.view.employee.deliverer.delivererMenu;

public interface DelivererMenuView {
    void showEmployeeName(String fullName);

    void navigateToOrdersList(String employeeId);


//    void navigateToProcessAccount(String employeeId);

    void showDeleteAccountConfirmation();

    void navigateToLogin();

    void showMessage(String message);
}
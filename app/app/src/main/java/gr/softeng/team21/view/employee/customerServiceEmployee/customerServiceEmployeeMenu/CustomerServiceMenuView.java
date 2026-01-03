package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu;

public interface CustomerServiceMenuView {
    void showEmployeeName(String fullName);

    void navigateToOrderStatus(String employeeId);

    void navigateToEmailInbox(String employeeId);

    void showDeleteAccountConfirmation();
    void navigateToLogin();
    void showMessage(String message);
}
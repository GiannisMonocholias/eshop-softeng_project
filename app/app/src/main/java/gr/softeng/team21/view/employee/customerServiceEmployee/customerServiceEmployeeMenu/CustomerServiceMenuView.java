package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeMenu;

public interface CustomerServiceMenuView {
    public void showEmployeeName(String fullName);

    void navigateToOrderStatus(String employeeId);

    void navigateToEmailInbox(String employeeId);
}

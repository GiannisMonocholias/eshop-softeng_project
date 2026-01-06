package gr.softeng.team21.view.employee.customerServiceEmployee.customerServiceEmployeeEmailList;

public interface CustomerServiceEmployeeEmailListView {

    void navigateToCreateNewMsg(String employeeId);
    void navigateToEmailDetails(String subject, String body, String sender, String receiver, String employeeId);
}

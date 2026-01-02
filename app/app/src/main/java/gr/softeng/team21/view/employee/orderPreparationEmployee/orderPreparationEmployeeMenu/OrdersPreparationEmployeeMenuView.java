package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationEmployeeMenu;

public interface OrdersPreparationEmployeeMenuView {
    void showEmployeeName(String fullName);

    void navigateToAssignedOrders(String employeeId);

    void navigateToAvailableOrdersToAssign(String employeeId);

}

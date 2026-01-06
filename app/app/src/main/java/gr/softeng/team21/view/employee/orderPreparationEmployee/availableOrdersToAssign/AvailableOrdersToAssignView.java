package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import gr.softeng.team21.domain.Order;

public interface AvailableOrdersToAssignView {
    void showMessage(String message);

    void showError(String message);

    void onOrderAssignedSuccess(Order order);

    void showConfirmationDialog(Order order, String message);

    void updateList();
}

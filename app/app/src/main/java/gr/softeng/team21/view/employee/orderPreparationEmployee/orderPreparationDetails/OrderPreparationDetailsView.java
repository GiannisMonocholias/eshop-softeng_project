package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import gr.softeng.team21.domain.OrderStatusType;

public interface OrderPreparationDetailsView {
    void setOrderDetails(String ordercode, String customerName, String submissionDate, String  price, OrderStatusType status);

    public void showErrorMessage(String message);

    void showSuccessMessage(String message);
    void finishActivity();
}

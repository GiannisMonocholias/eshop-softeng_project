package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;


import java.util.List;
import gr.softeng.team21.domain.Order;

public interface OrderStatusView {


    void showError(String message);


    void onOrderSelected(Order order);

    void showMessage(String message);

    void updateList();

    void showConfirmationDialog(Order order, String message);

}
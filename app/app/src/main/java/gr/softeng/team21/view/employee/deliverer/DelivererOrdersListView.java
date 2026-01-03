package gr.softeng.team21.view.employee.deliverer;

import java.util.List;
import gr.softeng.team21.domain.Order;

public interface DelivererOrdersListView {

    void removeOrderFromList(Order order);
    void showMessage(String message);

    void showError(String message);
}
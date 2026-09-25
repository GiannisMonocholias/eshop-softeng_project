package gr.softeng.team21.view.customer.OrdersHistoric;

import java.util.ArrayList;

import gr.softeng.team21.domain.Order;

public interface CustomerOrderHistoryView {

    void showOrders(ArrayList<Order> ordersList);
    void showError(String message);


}

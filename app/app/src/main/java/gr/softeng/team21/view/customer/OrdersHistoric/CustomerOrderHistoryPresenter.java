package gr.softeng.team21.view.customer.OrdersHistoric;

import java.util.ArrayList;

import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;

public class CustomerOrderHistoryPresenter {

private OrderDAO orderDAO;
private CustomerOrderHistoryView view;
private ArrayList<Order> ordersList;

    public CustomerOrderHistoryPresenter( CustomerOrderHistoryView view, OrderDAO orderDAO) {
        this.view = view;
        this.orderDAO = orderDAO;
    }

    public void loadOrders(String customerId) {
        orderDAO.getOrdersByCustomerId(customerId).thenAccept(ordersMap -> {
            this.ordersList = ordersMap;
            if (view != null) view.showOrders(this.ordersList);
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα φόρτωσης προϊόντων: " + e.getMessage());
            return null;
        });
    }

    void OrderClicked(Order order){

    }
}

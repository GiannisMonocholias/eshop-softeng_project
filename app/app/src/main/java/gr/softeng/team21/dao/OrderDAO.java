package gr.softeng.team21.dao;

import java.util.HashMap;

import gr.softeng.team21.domain.Order;

public interface OrderDAO {

     Order getOrder(String orderCode);
     void addOrder(Order order);

     HashMap<String,Order> getOrders();

     void clear();
}

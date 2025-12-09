package gr.softeng.team21.domain;

import java.util.ArrayList;
import java.util.HashMap;

public class OrdersRepository {
    private static OrdersRepository instance;

    private HashMap<String,Order> orders;

    private OrdersRepository(){
        orders = new HashMap<>();
    }

    public static OrdersRepository getInstance(){
        if (instance == null){
            instance = new OrdersRepository();
        }
        return instance;
    }


    public Order getOrder(String orderCode){
        if(orderCode == null)
            throw new IllegalArgumentException("The orderCode must not be null");

        if(orders.containsKey(orderCode))
            return orders.get(orderCode);
        else
            return null;
    }

    public void addOrder(Order order){
        if(order != null){
            if(!orders.containsKey(order.getOrdercode())){
                orders.put(order.getOrdercode(),order);
            }
            else {
                throw new IllegalArgumentException("The given order is already in the repository");
            }
        }
        else
            throw new IllegalArgumentException("The Order order argument must not be null");
    }

    public HashMap<String,Order> getOrders(){
        return orders;
    }

    public void clear(){
        this.orders.clear();
    }
}

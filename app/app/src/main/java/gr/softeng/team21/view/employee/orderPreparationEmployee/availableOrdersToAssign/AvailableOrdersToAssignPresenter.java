package gr.softeng.team21.view.employee.orderPreparationEmployee.availableOrdersToAssign;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;

public class AvailableOrdersToAssignPresenter {

    private AvailableOrdersToAssignView view;
    private EmployeeDAO employeeDAO;
    private OrderDAO orderDAO;
    private OrderPreparationEmployee loggedInEmployee;

    public AvailableOrdersToAssignPresenter(AvailableOrdersToAssignView view, EmployeeDAO employeeDAO, OrderDAO orderDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
    }

    public ArrayList<Order> loadAvailableOrders(String employeeId){
        this.loggedInEmployee = (OrderPreparationEmployee) employeeDAO.getEmployee(employeeId);

        Order cur_order;
        ArrayList<Order> newOrders = new ArrayList<>();

        for(String ordId: orderDAO.getOrders().keySet()){
            cur_order = orderDAO.getOrders().get(ordId);
            if(cur_order != null){
                if(cur_order.getOrderstatus() == OrderStatusType.NEW)
                    newOrders.add(cur_order);
            }
        }

        return newOrders;
    }

    public void onOrderClicked(Order order) {
        String confirmationMessage = "Θέλετε να αναλάβετε αυτή την παραγγελία;";
        view.showConfirmationDialog(order ,confirmationMessage);
    }



    public void onOrderConfirmed(Order order) {
        loggedInEmployee.addOrder(order);

        order.setOrderstatus(OrderStatusType.PROCESSING);

        view.showMessage("Η παραγγελία ανατέθηκε επιτυχώς!");
        view.onOrderAssignedSuccess(order);



        view.updateList();

    }
}

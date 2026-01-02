package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.memorydao.EmployeeDAOMemory;

public class AssignedOrdersToPreparePresenter {
    private AssignedOrdersToPrepareView view;
    private EmployeeDAO employeeDAO;
    private OrderPreparationEmployee loggedInEmployee;

    public AssignedOrdersToPreparePresenter(AssignedOrdersToPrepareView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO  =employeeDAO;
    }

    public ArrayList<Order> loadAssignedOrders(String employeeId){
        this.loggedInEmployee = (OrderPreparationEmployee) employeeDAO.getEmployee(employeeId);
        return loggedInEmployee.getAssignedOrders();
    }

    public void onClickOrder(Order order){
        view.navigateToOrderPreparationDetails(loggedInEmployee.getEmployeeId(), order.getOrdercode());
    }
}

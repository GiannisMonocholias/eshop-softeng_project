package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

import java.util.ArrayList;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;

/**
 * Presenter for the Assigned Orders list.
 * Handles the retrieval of orders assigned to a specific preparation employee
 * and processes the selection of an order for detailed view.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedOrdersToPreparePresenter {
    private AssignedOrdersToPrepareView view;
    private EmployeeDAO employeeDAO;
    private OrderPreparationEmployee loggedInEmployee;

    /**
     * Initializes the presenter with the view and employee data access.
     * @param view The view implementation.
     * @param employeeDAO The data access object for employee records.
     */
    public AssignedOrdersToPreparePresenter(AssignedOrdersToPrepareView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Fetches the list of orders assigned to the preparation employee.
     * @param employeeId The unique ID of the employee.
     * @return An ArrayList of assigned orders.
     */
    public ArrayList<Order> loadAssignedOrders(String employeeId){
        this.loggedInEmployee = (OrderPreparationEmployee) employeeDAO.getEmployee(employeeId);
        return loggedInEmployee.getAssignedOrders();
    }

    /**
     * Processes the selection of an order from the list.
     * @param order The order object that was clicked.
     */
    public void onClickOrder(Order order){
        view.navigateToOrderPreparationDetails(loggedInEmployee.getEmployeeId(), order.getOrdercode());
    }
}
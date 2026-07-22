package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;

/**
 * Presenter for the Assigned Orders list.
 * Handles the asynchronous retrieval of orders assigned to a specific preparation employee
 * and processes the selection of an order for detailed view.
 * Utilizes Dependency Injection to decouple the data source from presentation logic.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedOrdersToPreparePresenter {
    private AssignedOrdersToPrepareView view;
    private EmployeeDAO employeeDAO;
    private OrderPreparationEmployee loggedInEmployee;

    /**
     * Initializes the presenter with the view and injected employee data access object.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO The data access object for employee records.
     */
    public AssignedOrdersToPreparePresenter(AssignedOrdersToPrepareView view, EmployeeDAO employeeDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
    }

    /**
     * Asynchronously fetches the list of orders assigned to the preparation employee
     * and triggers a UI update.
     * @param employeeId The unique ID of the employee.
     */
    public void loadAssignedOrders(String employeeId){
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof OrderPreparationEmployee) {
                this.loggedInEmployee = (OrderPreparationEmployee) employee;
                view.updateAssignedOrdersList(loggedInEmployee.getAssignedOrders());
            } else {
                view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }
        }).exceptionally(e -> {
            view.showError("Σφάλμα κατά την ανάκτηση παραγγελιών: " + e.getMessage());
            return null;
        });
    }

    /**
     * Processes the selection of an order from the list and triggers navigation.
     * @param order The order object that was clicked.
     */
    public void onClickOrder(Order order){
        if (loggedInEmployee != null) {
            view.navigateToOrderPreparationDetails(loggedInEmployee.getEmployeeId(), order.getOrdercode());
        } else {
            view.showError("Δεν υπάρχει ενεργή συνεδρία υπαλλήλου.");
        }
    }
}
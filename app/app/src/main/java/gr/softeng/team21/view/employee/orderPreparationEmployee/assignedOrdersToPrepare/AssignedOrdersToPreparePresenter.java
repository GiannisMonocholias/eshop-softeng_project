package gr.softeng.team21.view.employee.orderPreparationEmployee.assignedOrdersToPrepare;

import java.util.ArrayList;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Assigned Orders list.
 * Retrieves only the specific orders assigned to the logged-in preparation employee
 * utilizing highly optimized database queries via OrderDAO.
 * @author Γιάννης Μονοχολιάς
 */
public class AssignedOrdersToPreparePresenter {
    private final AssignedOrdersToPrepareView view;
    private final EmployeeDAO employeeDAO;
    private final OrderDAO orderDAO;
    private OrderPreparationEmployee loggedInEmployee;

    /**
     * Initializes the presenter with injected DAOs.
     */
    public AssignedOrdersToPreparePresenter(AssignedOrdersToPrepareView view, EmployeeDAO employeeDAO, OrderDAO orderDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Asynchronously loads the logged-in employee and performs a highly optimized
     * database query to fetch ONLY the orders assigned to their specific ID.
     * @param employeeId The unique ID of the employee.
     */
    public void loadAssignedOrders(String employeeId) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof OrderPreparationEmployee) {
                this.loggedInEmployee = (OrderPreparationEmployee) employee;

                // Optimized DB Query: Fetch ONLY orders for this employee
                orderDAO.getOrdersByPreparationEmployeeId(employeeId).thenAccept(orders -> {
                    ArrayList<Order> pendingOrders = new ArrayList<>();

                    // Filter the small subset locally to find only NEW status orders
                    for (Order order : orders) {
                        if (order.getOrderstatus() == OrderStatusType.NEW) {
                            pendingOrders.add(order);
                        }
                    }
                    if (view != null) view.updateAssignedOrdersList(pendingOrders);
                }).exceptionally(e -> {
                    if (view != null) view.showError("Σφάλμα κατά την ανάκτηση παραγγελιών: " + e.getMessage());
                    return null;
                });
            } else {
                if (view != null) view.showError("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
            return null;
        });
    }

    public void onClickOrder(Order order){
        if (loggedInEmployee != null && view != null) {
            view.navigateToOrderPreparationDetails(loggedInEmployee.getEmployeeId(), order.getOrdercode());
        } else if (view != null) {
            view.showError("Δεν υπάρχει ενεργή συνεδρία υπαλλήλου.");
        }
    }
}
package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Order Preparation Details screen.
 * Manages the logic for loading order items and executing the stock verification
 * process through the domain model.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationDetailsPresenter {
    private OrderPreparationDetailsView view;
    private OrderPreparationEmployee loggedInEmployee;
    private Order orderToPrepare;
    private EmployeeDAO employeeDAO;
    private OrderDAO orderDAO;

    /**
     * Initializes the presenter with the view and data access layers.
     * @param view The view implementation.
     * @param employeeDAO Data access for employee information.
     * @param orderDAO Data access for order information.
     */
    public OrderPreparationDetailsPresenter(OrderPreparationDetailsView view, EmployeeDAO employeeDAO, OrderDAO orderDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Loads the order data and prepares the view for display.
     * @param employeeId The ID of the employee performing the preparation.
     * @param ordercode  The code of the order to load.
     * @return A list of items contained in the order's shopping cart.
     */
    public ArrayList<CartItem> loadOrder(String employeeId, String ordercode){
        this.loggedInEmployee = (OrderPreparationEmployee) employeeDAO.getEmployee(employeeId);
        this.orderToPrepare = orderDAO.getOrder(ordercode);

        String customerFullName = orderToPrepare.getShoppingCart().getCustomer().getFirstname() + " " +
                orderToPrepare.getShoppingCart().getCustomer().getLastname();

        view.setOrderDetails(ordercode, customerFullName, orderToPrepare.getSubmissiondate().toString(),
                orderToPrepare.getTotal_amount().toString(), orderToPrepare.getOrderstatus());

        return new ArrayList<>(orderToPrepare.getShoppingCart().getItems());
    }

    /**
     * Executes the stock check and preparation logic.
     * Depending on the domain result, it updates the view with success (SHIPPED)
     * or error (DELAYED due to insufficient stock) messages.
     */
    public void checkStockOrder() {
        try {
            this.loggedInEmployee.prepareOrder(this.orderToPrepare);

            if(this.orderToPrepare.getOrderstatus() == OrderStatusType.DELAYED){
                view.showErrorMessage("Ανεπαρκές απόθεμα: Δεν μπορούν να συγκεντρωθούν όλα τα προϊόντα της παραγγελίας");
            }
            else if(this.orderToPrepare.getOrderstatus() == OrderStatusType.SHIPPED){
                view.showSuccessMessage("Ο έλεγχος αποθέματος ολοκληρώθηκε επιτυχώς! Η παραγγελία είναι τώρα έτοιμη προς παράδοση.");
                loggedInEmployee.removeOrder(orderToPrepare);
            }
        }
        catch(IllegalArgumentException e){
            view.showErrorMessage("Σφάλμα: Δεν δόθηκε παραγγελία (null Order pointer)");
        }
        catch(NoSuchElementException e){
            view.showErrorMessage("Σφάλμα: Δεν σας έχει ανατεθεί η συγκεκριμένη παραγγελία");
        }
    }

    /**
     * Manually sets the order to be prepared (primarily used for junit testing).
     * @param order The order instance.
     */
    public void setOrderToPrepare(Order order){
        this.orderToPrepare = order;
    }
}
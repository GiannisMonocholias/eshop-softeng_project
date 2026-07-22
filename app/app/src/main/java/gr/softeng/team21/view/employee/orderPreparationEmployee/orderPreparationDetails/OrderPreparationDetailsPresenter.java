package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * Presenter for the Order Preparation Details screen.
 * Manages the asynchronous logic for loading order items and executing the stock verification
 * process through the domain model. Uses Dependency Injection for decoupled DAOs.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationDetailsPresenter {
    private OrderPreparationDetailsView view;
    private OrderPreparationEmployee loggedInEmployee;
    private Order orderToPrepare;
    private EmployeeDAO employeeDAO;
    private OrderDAO orderDAO;

    /**
     * Initializes the presenter with the view and injected data access layers.
     * @param view The view implementation (Activity or Stub).
     * @param employeeDAO Data access for employee information.
     * @param orderDAO Data access for order information.
     */
    public OrderPreparationDetailsPresenter(OrderPreparationDetailsView view, EmployeeDAO employeeDAO, OrderDAO orderDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Asynchronously loads the order data and prepares the view for display.
     * Fetches the employee and the order, then triggers the UI update methods.
     * @param employeeId The ID of the employee performing the preparation.
     * @param ordercode  The code of the order to load.
     */
    public void loadOrder(String employeeId, String ordercode) {
        employeeDAO.getEmployee(employeeId).thenAccept(employee -> {
            if (employee instanceof OrderPreparationEmployee) {
                this.loggedInEmployee = (OrderPreparationEmployee) employee;

                orderDAO.getOrder(ordercode).thenAccept(order -> {
                    if (order != null) {
                        this.orderToPrepare = order;
                        String customerFullName = order.getShoppingCart().getCustomer().getFirstname() + " " +
                                order.getShoppingCart().getCustomer().getLastname();

                        view.setOrderDetails(ordercode, customerFullName, order.getSubmissiondate().toString(),
                                order.getTotal_amount().toString(), order.getOrderstatus());

                        view.updateCartItems(new ArrayList<>(order.getShoppingCart().getItems()));
                    } else {
                        view.showErrorMessage("Σφάλμα: Η παραγγελία δεν βρέθηκε.");
                    }
                }).exceptionally(e -> {
                    view.showErrorMessage("Σφάλμα ανάκτησης παραγγελίας: " + e.getMessage());
                    return null;
                });
            } else {
                view.showErrorMessage("Σφάλμα: Ο υπάλληλος δεν βρέθηκε ή δεν έχει τον σωστό ρόλο.");
            }
        }).exceptionally(e -> {
            view.showErrorMessage("Σφάλμα ανάκτησης υπαλλήλου: " + e.getMessage());
            return null;
        });
    }

    /**
     * Executes the stock check and preparation logic.
     * Depending on the domain result, it asynchronously updates the order in the database
     * and signals the view with success (SHIPPED) or error (DELAYED) messages.
     */
    public void checkStockOrder() {
        try {
            // This domain method throws exceptions if order is null or not assigned
            this.loggedInEmployee.prepareOrder(this.orderToPrepare);

            // Persist the changes asynchronously
            orderDAO.addOrder(this.orderToPrepare).thenAccept(v -> {
                if (this.orderToPrepare.getOrderstatus() == OrderStatusType.DELAYED) {
                    view.showErrorMessage("Ανεπαρκές απόθεμα: Δεν μπορούν να συγκεντρωθούν όλα τα προϊόντα της παραγγελίας");
                } else if (this.orderToPrepare.getOrderstatus() == OrderStatusType.SHIPPED) {
                    view.showSuccessMessage("Ο έλεγχος αποθέματος ολοκληρώθηκε επιτυχώς! Η παραγγελία είναι τώρα έτοιμη προς παράδοση.");
                    loggedInEmployee.removeOrder(orderToPrepare);
                }
            }).exceptionally(e -> {
                view.showErrorMessage("Σφάλμα ενημέρωσης κατάστασης: " + e.getMessage());
                return null;
            });

        } catch (IllegalArgumentException e) {
            view.showErrorMessage("Σφάλμα: Δεν δόθηκε παραγγελία (null Order pointer)");
        } catch (NoSuchElementException e) {
            view.showErrorMessage("Σφάλμα: Δεν σας έχει ανατεθεί η συγκεκριμένη παραγγελία");
        }
    }

    /**
     * Manually sets the order to be prepared (primarily used for unit testing).
     * @param order The order instance.
     */
    public void setOrderToPrepare(Order order){
        this.orderToPrepare = order;
    }
}
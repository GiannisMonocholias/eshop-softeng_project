package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import gr.softeng.team21.dao.EmployeeDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.OrderPreparationEmployee;
import gr.softeng.team21.domain.OrderStatusType;

public class OrderPreparationDetailsPresenter {
    OrderPreparationDetailsView view;
    private OrderPreparationEmployee loggedInEmployee;
    private Order orderToPrepare;
    EmployeeDAO employeeDAO;
    OrderDAO orderDAO;

    public OrderPreparationDetailsPresenter(OrderPreparationDetailsView view, EmployeeDAO employeeDAO, OrderDAO orderDAO){
        this.view = view;
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
    }

    public ArrayList<CartItem> loadOrder(String employeeId,String ordercode){
        this.loggedInEmployee = (OrderPreparationEmployee) employeeDAO.getEmployee(employeeId);
        this.orderToPrepare = orderDAO.getOrder(ordercode);

        String customerFullName = orderToPrepare.getShoppingCart().getCustomer().getFirstname() + " " +
                orderToPrepare.getShoppingCart().getCustomer().getLastname();

        view.setOrderDetails(ordercode, customerFullName, orderToPrepare.getSubmissiondate().toString(), orderToPrepare.getTotal_amount().toString(),orderToPrepare.getOrderstatus());
        return new ArrayList<>(orderToPrepare.getShoppingCart().getItems());
    }

    public void checkStockOrder() {
        try{
            this.loggedInEmployee.prepareOrder(this.orderToPrepare);

            if(this.orderToPrepare.getOrderstatus() == OrderStatusType.DELAYED){
                view.showErrorMessage("Ανεπαρκές απόθεμα: Ανεπαρκές απόθεμα για την παραγγελία.");
            }
            else if(this.orderToPrepare.getOrderstatus() == OrderStatusType.SHIPPED){
                view.showSuccessMessage("Ο έλεγχος ολοκληρώθηκε επιτυχώς! Η παραγγελία είναι έτοιμη προς αποστολή");
                loggedInEmployee.removeOrder(orderToPrepare);
            }
        }

        catch(IllegalStateException e){
            view.showErrorMessage("Σφάλμα: Δεν δόθηκε παραγγελία (null Order pointer)");
        }
        catch(NoSuchElementException e){
            view.showErrorMessage("Σφάλμα: Δεν ασας έχει ανατεθεί η συγκεκριμένη παραγγελία");
        }


    }
}

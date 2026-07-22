package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import java.util.ArrayList;
import gr.softeng.team21.domain.CartItem;
import gr.softeng.team21.domain.OrderStatusType;

/**
 * A stub implementation of the {@link OrderPreparationDetailsView} interface for unit testing.
 * It captures and stores order data, item lists, and feedback messages passed by the presenter,
 * allowing for verification of asynchronous UI logic during tests.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationDetailsViewStub implements OrderPreparationDetailsView {

    private String displayedOrderCode = "";
    private String displayedCustomerName = "";
    private String displayedPrice = "";
    private OrderStatusType displayedStatus;

    private ArrayList<CartItem> loadedItems;

    private String errorMessage = "";
    private String successMessage = "";
    private boolean finishActivityCalled = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrderDetails(String ordercode, String customerName, String submissionDate, String price, OrderStatusType status) {
        this.displayedOrderCode = ordercode;
        this.displayedCustomerName = customerName;
        this.displayedPrice = price;
        this.displayedStatus = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCartItems(ArrayList<CartItem> items) {
        this.loadedItems = items;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showErrorMessage(String message) {
        this.errorMessage = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finishActivity() {
        this.finishActivityCalled = true;
    }

    // --- Accessor methods for verification during testing ---

    public String getDisplayedOrderCode() {
        return displayedOrderCode;
    }

    public String getDisplayedCustomerName() {
        return displayedCustomerName;
    }

    public ArrayList<CartItem> getLoadedItems() {
        return loadedItems;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public boolean isFinishActivityCalled() {
        return finishActivityCalled;
    }
}
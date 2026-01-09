package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import gr.softeng.team21.domain.OrderStatusType;

/**
 * A stub implementation of the {@link OrderPreparationDetailsView} interface for unit testing.
 * It captures and stores order data and feedback messages passed by the presenter,
 * allowing for verification of the UI logic during order fulfillment tests.
 * @author Γιάννης Μονοχολιάς
 */
public class OrderPreparationDetailsViewStub implements OrderPreparationDetailsView {

    private String displayedOrderCode = "";
    private String displayedCustomerName = "";
    private String displayedPrice = "";
    private OrderStatusType displayedStatus;

    private String errorMessage = "";
    private String successMessage = "";
    private boolean finishActivityCalled = false;

    /**
     * Captures order metadata for display verification.
     */
    @Override
    public void setOrderDetails(String ordercode, String customerName, String submissionDate, String price, OrderStatusType status) {
        this.displayedOrderCode = ordercode;
        this.displayedCustomerName = customerName;
        this.displayedPrice = price;
        this.displayedStatus = status;
    }

    @Override
    public void showErrorMessage(String message) {
        this.errorMessage = message;
    }

    @Override
    public void showSuccessMessage(String message) {
        this.successMessage = message;
    }

    /**
     * Marks that the activity finish request was triggered.
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }
}
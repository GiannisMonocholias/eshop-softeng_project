package gr.softeng.team21.view.employee.orderPreparationEmployee.orderPreparationDetails;

import gr.softeng.team21.domain.OrderStatusType;

public class OrderPreparationDetailsViewStub implements OrderPreparationDetailsView {

    private String displayedOrderCode = "";
    private String displayedCustomerName = "";
    private String displayedPrice = "";
    private OrderStatusType displayedStatus;

    private String errorMessage = "";
    private String successMessage = "";
    private boolean finishActivityCalled = false;

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

    @Override
    public void finishActivity() {
        this.finishActivityCalled = true;
    }

    // --- Getters for Tests ---

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
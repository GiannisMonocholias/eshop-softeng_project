package gr.softeng.team21.view.employee.customerServiceEmployee.orderStatus;

import gr.softeng.team21.domain.Order;

public class OrderStatusViewStub implements OrderStatusView {

    private String errorMsg = "";
    private String messageMsg = "";
    private String selectedOrderCode = "";
    private boolean listUpdated = false;

    private Order confirmationOrder;
    private String confirmationMessage;
    private boolean confirmationDialogShown = false;

    @Override
    public void showError(String message) {
        this.errorMsg = message;
    }

    @Override
    public void onOrderSelected(Order order) {
        this.selectedOrderCode = order.getOrdercode();
    }

    @Override
    public void showMessage(String message) {
        this.messageMsg = message;
    }

    @Override
    public void updateList() {
        this.listUpdated = true;
    }

    @Override
    public void showConfirmationDialog(Order order, String message) {
        this.confirmationDialogShown = true;
        this.confirmationOrder = order;
        this.confirmationMessage = message;
    }

    public String getErrorMsg() { return errorMsg; }
    public String getMessageMsg() { return messageMsg; }
    public boolean isListUpdated() { return listUpdated; }
    public boolean isConfirmationDialogShown() { return confirmationDialogShown; }
    public String getConfirmationMessage() { return confirmationMessage; }
    public String getSelectedOrderCode() { return selectedOrderCode; }
}
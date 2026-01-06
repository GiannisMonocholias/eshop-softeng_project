package gr.softeng.team21.view.employee.deliverer.delivererOrdersList;

import gr.softeng.team21.domain.Order;

public class DelivererOrdersListViewStub implements DelivererOrdersListView {

    private Order removedOrder;
    private String messageShown = "";
    private String errorShown = "";

    @Override
    public void removeOrderFromList(Order order) {
        this.removedOrder = order;
    }

    @Override
    public void showMessage(String message) {
        this.messageShown = message;
    }

    @Override
    public void showError(String message) {
        this.errorShown = message;
    }


    public Order getRemovedOrder() {
        return removedOrder;
    }

    public String getMessageShown() {
        return messageShown;
    }

    public String getErrorShown() {
        return errorShown;
    }
}
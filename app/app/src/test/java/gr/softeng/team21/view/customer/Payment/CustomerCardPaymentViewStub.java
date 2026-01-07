package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

public class CustomerCardPaymentViewStub implements CustomerCardPaymentView {
    private Money confirmationAmount;
    private String message;
    private int homePageCount = 0;

    public Money getConfirmationAmount() {
        return confirmationAmount;
    }

    public int getHomePageCount() {
        return homePageCount;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void showConfirmation(Money amount) {
        confirmationAmount = amount;
    }

    @Override
    public void showMessage(String msg) {
        message = msg;
    }

    @Override
    public void goToCustomerHomePage() {
        homePageCount++;

    }
}

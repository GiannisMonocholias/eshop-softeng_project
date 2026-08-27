package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

/**
 * Stub implementation of {@link CustomerCardPaymentView} for unit testing purposes.
 * Captures UI feedback (confirmation amounts, messages) and tracks navigation events.
 * @author PAVLOS GRATSANIS
 */
public class CustomerCardPaymentViewStub implements CustomerCardPaymentView {

    private Money confirmationAmount;
    private String message;
    private int homePageCount = 0;

    @Override
    public void showConfirmation(Money amount) {
        this.confirmationAmount = amount;
    }

    @Override
    public void showMessage(String msg) {
        this.message = msg;
    }

    @Override
    public void goToCustomerHomePage() {
        this.homePageCount++;
    }

    // --- Getters for Tests ---
    public Money getConfirmationAmount() { return confirmationAmount; }
    public String getMessage() { return message; }
    public int getHomePageCount() { return homePageCount; }
}
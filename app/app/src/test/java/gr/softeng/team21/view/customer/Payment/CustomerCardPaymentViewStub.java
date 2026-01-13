package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

/**
 * Stub implementation of {@link CustomerCardPaymentView} for testing purposes.
 * It provides a mechanism to capture UI feedback (confirmation amount, messages) and
 * track the state of UI actions (navigation) during the card payment flow.
 * @author PAVLOS GRATSANIS
 */
public class CustomerCardPaymentViewStub implements CustomerCardPaymentView {
    private Money confirmationAmount;
    private String message;
    private int homePageCount = 0;

    /**
     * Returns the confirmation amount.
     * Used for verification in tests.
     * @return The money object representing the amount.
     */
    public Money getConfirmationAmount() {
        return confirmationAmount;
    }

    /**
     * Returns the number of times navigation to the home page was triggered.
     * Used for verification in tests.
     * @return The navigation count.
     */
    public int getHomePageCount() {
        return homePageCount;
    }

    /**
     * Returns the last message (success or error).
     * Used for verification in tests.
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     * Stores the confirmation amount in a variable for verification.
     */
    @Override
    public void showConfirmation(Money amount) {
        confirmationAmount = amount;
    }

    /**
     * {@inheritDoc}
     * Stores the message in a variable for verification.
     */
    @Override
    public void showMessage(String msg) {
        message = msg;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for home page navigation.
     */
    @Override
    public void goToCustomerHomePage() {
        homePageCount++;

    }
}
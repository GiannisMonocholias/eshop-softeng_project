package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

/**
 * Stub implementation of {@link CustomerPaymentView} for testing purposes.
 * It provides a mechanism to capture UI feedback (messages, amounts, shipping details) and
 * track the state of UI actions (navigation) during the payment flow.
 * @author PAVLOS GRATSANIS
 */
public class CustomerPaymentViewStub implements CustomerPaymentView {
    private String message;
    private int HomePageCount = 0;
    private int CardPaymentCount = 0;
    private Money confirmationAmount;
    private String TotalAmount;
    private String shippingName, shippingAddress, shippingPhone;

    /**
     * Returns the last message shown.
     * @return The message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the count of home page navigation events.
     * @return The count.
     */
    public int getHomePageCount() {
        return HomePageCount;
    }

    /**
     * Returns the count of card payment screen navigation events.
     * @return The count.
     */
    public int getCardPaymentCount() {
        return CardPaymentCount;
    }

    /**
     * Returns the confirmation amount.
     * @return The money object.
     */
    public Money getConfirmationAmount() {
        return confirmationAmount;
    }

    /**
     * Returns the total amount string displayed.
     * @return The amount string.
     */
    public String getTotalAmount() {
        return TotalAmount;
    }

    /**
     * Returns the shipping address displayed.
     * @return The address string.
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    /**
     * Returns the shipping phone displayed.
     * @return The phone string.
     */
    public String getShippingPhone() {
        return shippingPhone;
    }

    /**
     * Returns the shipping name displayed.
     * @return The name string.
     */
    public String getShippingName() {
        return shippingName;
    }

    /**
     * {@inheritDoc}
     * Stores the message for verification.
     */
    @Override
    public void showMessage(String msg) {
        message=msg;

    }

    /**
     * {@inheritDoc}
     * Increments the home page navigation counter.
     */
    @Override
    public void goToCustomerHomePage() {
        HomePageCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the card payment navigation counter.
     */
    @Override
    public void goToToCardPayment() {
        CardPaymentCount++;
    }

    /**
     * {@inheritDoc}
     * Stores the confirmation amount for verification.
     */
    @Override
    public void showConfirmation(Money amount) {
        confirmationAmount=amount;

    }

    /**
     * {@inheritDoc}
     * Stores the total amount string for verification.
     */
    @Override
    public void showTotalAmount(String amount) {
        TotalAmount = amount;

    }

    /**
     * {@inheritDoc}
     * Stores the shipping details for verification.
     */
    @Override
    public void showShippingDetails(String name, String address, String phone) {
        this.shippingName = name;
        this.shippingPhone = phone;

    }
}
package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

/**
 * Stub implementation of {@link CustomerPaymentView} for unit testing purposes.
 * Captures UI state changes, navigation calls, and messages.
 * @author PAVLOS GRATSANIS
 */
public class CustomerPaymentViewStub implements CustomerPaymentView {
    private String message;
    private int homePageCount = 0;
    private int cardPaymentCount = 0;
    private Money confirmationAmount;
    private String totalAmount;
    private String shippingName, shippingAddress, shippingPhone;

    @Override
    public void showMessage(String msg) {
        this.message = msg;
    }

    @Override
    public void goToCustomerHomePage() {
        homePageCount++;
    }

    @Override
    public void goToToCardPayment() {
        cardPaymentCount++;
    }

    @Override
    public void showConfirmation(Money amount) {
        this.confirmationAmount = amount;
    }

    @Override
    public void showTotalAmount(String amount) {
        this.totalAmount = amount;
    }

    @Override
    public void showShippingDetails(String name, String address, String phone) {
        this.shippingName = name;
        this.shippingAddress = address;
        this.shippingPhone = phone;
    }

    // --- Getters for Tests ---
    public String getMessage() { return message; }
    public int getHomePageCount() { return homePageCount; }
    public int getCardPaymentCount() { return cardPaymentCount; }
    public Money getConfirmationAmount() { return confirmationAmount; }
    public String getTotalAmount() { return totalAmount; }
    public String getShippingName() { return shippingName; }
    public String getShippingAddress() { return shippingAddress; }
    public String getShippingPhone() { return shippingPhone; }
}
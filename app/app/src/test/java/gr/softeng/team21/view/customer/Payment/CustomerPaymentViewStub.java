package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

public class CustomerPaymentViewStub implements CustomerPaymentView {
    private String message;
    private int HomePageCount = 0;
    private int CardPaymentCount = 0;
    private Money confirmationAmount;
    private String TotalAmount;
    private String shippingName, shippingAddress, shippingPhone;

    public String getMessage() {
        return message;
    }

    public int getHomePageCount() {
        return HomePageCount;
    }

    public int getCardPaymentCount() {
        return CardPaymentCount;
    }

    public Money getConfirmationAmount() {
        return confirmationAmount;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public String getShippingName() {
        return shippingName;
    }

    @Override
    public void showMessage(String msg) {
        message=msg;

    }

    @Override
    public void goToCustomerHomePage() {
        HomePageCount++;
    }

    @Override
    public void goToToCardPayment() {
        CardPaymentCount++;
    }

    @Override
    public void showConfirmation(Money amount) {
        confirmationAmount=amount;

    }

    @Override
    public void showTotalAmount(String amount) {
        TotalAmount = amount;

    }

    @Override
    public void showShippingDetails(String name, String address, String phone) {
        this.shippingName = name;
        this.shippingPhone = phone;

    }
}
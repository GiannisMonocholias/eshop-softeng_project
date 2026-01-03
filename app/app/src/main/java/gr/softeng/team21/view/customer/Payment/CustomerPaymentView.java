package gr.softeng.team21.view.customer.Payment;

public interface CustomerPaymentView {
    void showMessage(String message);
    void goToCustomerHomePage();
    void goToToCardPayment();
    void showConfirmation();
    void showTotalAmount(String amount);
    void showShippingDetails(String name, String address, String phone);
}

package gr.softeng.team21.view.customer.Payment;

public interface CustomerCardPaymentView {
    void showConfirmation(String amount);
    void showMessage(String message);
    void goToCustomerHomePage();
}

package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

public interface CustomerCardPaymentView {
    void showConfirmation(Money amount);
    void showMessage(String message);
    void goToCustomerHomePage();
}

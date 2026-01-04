package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.domain.Money;

public interface CustomerPaymentView {
    void showMessage(String message);
    void goToCustomerHomePage();
    void goToToCardPayment();
    void showConfirmation(Money amount);
    void showTotalAmount(String amount);
    void showShippingDetails(String name, String address, String phone);
}

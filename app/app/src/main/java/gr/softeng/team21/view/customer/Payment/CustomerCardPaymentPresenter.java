package gr.softeng.team21.view.customer.Payment;

import android.widget.Toast;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;

public class CustomerCardPaymentPresenter {
    private CustomerCardPaymentView view;
    private Customer customer;
    private  Order order;

    public CustomerCardPaymentPresenter(CustomerCardPaymentView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    public void CardPaymentClicked(String cardNumber) {
        if (cardNumber.isEmpty()) {
            view.showMessage("Παρακαλώ εισάγετε τον αριθμό κάρτας!");
            return;
        }
        order = customer.Checkout();
        if (order == null) {
            view.showMessage("Το καλάθι είναι άδειο!");
            return;
        }
        customer.selectPaymentType(PaymentType.CARD, cardNumber, order);
        view.showConfirmation(order.getTotal_amount());
    }

    public void ConfirmClicked() {
        try {
            customer.Confirm("CONFIRM", order);
            view.showMessage("Η παραγγελία σας καταχωρήθυκε.");
            view.goToCustomerHomePage();
        } catch (Exception e) {
            view.showMessage("Σφάλμα: " + e.getMessage());
        }
    }

    public void CancelClicked() {
        try {
            customer.Confirm("CANCEL", order);
            view.showMessage("Η παραγγελία σας ακυρώθηκε.");
            view.goToCustomerHomePage();
        } catch (Exception e) {
            view.showMessage("Σφάλμα: " + e.getMessage());
        }
    }
}

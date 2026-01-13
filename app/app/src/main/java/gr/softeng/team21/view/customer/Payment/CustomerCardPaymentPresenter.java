package gr.softeng.team21.view.customer.Payment;

import android.widget.Toast;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;

/**
 * Presenter for the Customer Card Payment activity.
 * Handles validation of card details and finalization of the order.
 * @author PAVLOS GRATSANIS
 */
public class CustomerCardPaymentPresenter {
    private CustomerCardPaymentView view;
    private Customer customer;
    private  Order order;

    /**
     * Initializes the presenter with the view and  customer.
     * @param view The view interface.
     * @param customer The customer domain object.
     */
    public CustomerCardPaymentPresenter(CustomerCardPaymentView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    /**
     * Validates the card number and initiates the checkout process.
     * If successful, prompts the view to show a confirmation dialog.
     * @param cardNumber The card number entered by the user.
     */
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

    /**
     * Confirms the order and navigates back to the HomePage.
     */
    public void ConfirmClicked() {
        try {
            customer.Confirm("CONFIRM", order);
            view.showMessage("Η παραγγελία σας καταχωρήθυκε.");
            view.goToCustomerHomePage();
        } catch (Exception e) {
            view.showMessage("Σφάλμα: " + e.getMessage());
        }
    }

    /**
     * Cancels the order and navigates back to the HomePage.
     */
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
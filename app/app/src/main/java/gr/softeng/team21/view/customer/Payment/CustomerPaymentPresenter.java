package gr.softeng.team21.view.customer.Payment;

import android.widget.Toast;

import gr.softeng.team21.contact.Address;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;

/**
 * Presenter for the Customer Payment activity.
 * Handles the business logic for payment selection, order checkout, confirmation and cancellation.
 * @author PAVLOS GRATSANIS
 */
public class CustomerPaymentPresenter {
    private CustomerPaymentView view;
    private Order order;
    private Customer customer;

    /**
     * Initializes the presenter with the view and customer.
     * @param view The view interface.
     * @param customer The customer domain object.
     */
    public CustomerPaymentPresenter(CustomerPaymentView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    /**
     * Handles the click for payment.
     * Initiates checkout and directs to either cash confirmation or card payment screen.
     * @param cashCheck True if payment is by cash, false if by card.
     */
    public void paymentClicked(boolean cashCheck) {
        if (cashCheck) {
            order = customer.Checkout();
            if (order == null) {
                view.showMessage("Το καλάθι είναι άδειο!");
                return;
            }
            customer.selectPaymentType(PaymentType.CASH, "", order);
            view.showConfirmation(order.getTotal_amount());
        } else
            view.goToToCardPayment();
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

    /**
     * Sets the total payment amount in the view.
     * @param amount The amount string to display.
     */
    public void setpaymentClicked(String amount) {
        view.showTotalAmount(amount);
    }

    /**
     * Loads the customer's shipping details (name, address, phone) and updates the view.
     */
    public void loadShippingDetails() {
        String fullName = customer.getFirstname() + " " + customer.getLastname();
        String address = customer.getAddress().toString();
        String phone = customer.getPhonenumber();
        view.showShippingDetails(fullName, address, phone);
    }
}
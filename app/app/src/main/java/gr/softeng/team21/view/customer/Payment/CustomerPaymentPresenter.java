package gr.softeng.team21.view.customer.Payment;

import android.widget.Toast;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;

public class CustomerPaymentPresenter {
    private CustomerPaymentView view;
    private Order order;
    private Customer customer;

    public CustomerPaymentPresenter(CustomerPaymentView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    public void paymentClicked(boolean cashCheck) {
        if (cashCheck) {
            try {
                order = customer.Checkout();
                if (order == null) {
                    view.showMessage("Το καλάθι είναι άδειο!");
                    return;
                }
                customer.selectPaymentType(PaymentType.CASH, "", order);
                view.showConfirmation(order.getTotal_amount());
            } catch (Exception e) {
                view.showMessage("Σφάλμα: " + e.getMessage());
            }
        } else
            view.goToToCardPayment();
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

    public void setpaymentClicked(String amount) {
        String address;
        String fullName = customer.getFirstname() + " " + customer.getLastname();
        if (customer.getAddress() != null) {
            address = customer.getAddress().toString();
        } else {
            address = "Δεν έχει δηλωθεί διεύθυνση";
        }
        String phone = customer.getPhonenumber();
        view.showTotalAmount(amount);
        view.showShippingDetails(fullName, address, phone);
    }
}

package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;

/**
 * Presenter for the Customer Payment activity.
 * Handles the business logic for payment method selection, order checkout,
 * confirmation, cancellation, and asynchronous persistence via DAOs.
 * @author PAVLOS GRATSANIS
 */
public class CustomerPaymentPresenter {
    private final CustomerPaymentView view;
    private final CustomerDAO customerDAO;
    private final OrderDAO orderDAO;

    private Customer customer;
    private Order order;

    /**
     * Constructs a presenter instance using Dependency Injection.
     * @param view The payment view interface.
     * @param customerDAO The data access object for customers.
     * @param orderDAO The data access object for orders.
     */
    public CustomerPaymentPresenter(CustomerPaymentView view, CustomerDAO customerDAO, OrderDAO orderDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Asynchronously loads initial customer data, calculates total cart cost,
     * and displays shipping details.
     * @param customerId The unique identifier of the customer.
     */
    public void loadInitialData(String customerId) {
        customerDAO.getCustomer(customerId).thenAccept(loadedCustomer -> {
            if (loadedCustomer != null) {
                this.customer = loadedCustomer;
                loadShippingDetails();

                if (customer.getShoppingCart() != null && customer.getShoppingCart().getTotalCost() != null) {
                    view.showTotalAmount(customer.getShoppingCart().getTotalCost().toString());
                }
            } else {
                if (view != null) view.showMessage("Ο πελάτης δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showMessage("Σφάλμα ανάκτησης στοιχείων: " + e.getMessage());
            return null;
        });
    }

    /**
     * Handles payment button click based on selected payment type (Cash vs Card).
     * @param cashCheck True if payment is by cash, false if by card.
     */
    public void paymentClicked(boolean cashCheck) {
        if (customer == null || customer.getShoppingCart() == null || customer.getShoppingCart().getItems().isEmpty()) {
            if (view != null) view.showMessage("Το καλάθι είναι άδειο!");
            return;
        }

        if (cashCheck) {
            order = customer.Checkout();
            if (order == null) {
                if (view != null) view.showMessage("Το καλάθι είναι άδειο!");
                return;
            }
            customer.selectPaymentType(PaymentType.CASH, "", order);
            if (view != null) view.showConfirmation(order.getTotal_amount());
        } else {
            if (view != null) view.goToToCardPayment();
        }
    }

    /**
     * Confirms the order, saves it to the database asynchronously, and navigates home.
     */
    public void ConfirmClicked() {
        if (order == null || customer == null) {
            if (view != null) view.showMessage("Σφάλμα: Order cannot be null!!!");
            return;
        }

        try {
            customer.Confirm("CONFIRM", order);
            orderDAO.addOrder(order);
            if (view != null) {
                view.showMessage("Η παραγγελία σας καταχωρήθυκε.");
                view.goToCustomerHomePage();
            }
        } catch (Exception e) {
            if (view != null) view.showMessage("Σφάλμα: " + e.getMessage());
        }
    }

    /**
     * Cancels the active order and navigates back to the home page.
     */
    public void CancelClicked() {
        if (order == null || customer == null) {
            if (view != null) view.showMessage("Σφάλμα: Order cannot be null!!!");
            return;
        }

        try {
            customer.Confirm("CANCEL", order);
            if (view != null) {
                view.showMessage("Η παραγγελία σας ακυρώθηκε.");
                view.goToCustomerHomePage();
            }
        } catch (Exception e) {
            if (view != null) view.showMessage("Σφάλμα: " + e.getMessage());
        }
    }

    /**
     * Updates the total payment amount displayed in the view.
     * @param amount The formatted total amount string.
     */
    public void setpaymentClicked(String amount) {
        if (view != null) view.showTotalAmount(amount);
    }

    /**
     * Extracts and populates customer shipping information in the view.
     */
    public void loadShippingDetails() {
        if (customer != null && view != null) {
            String fullName = customer.getFirstname() + " " + customer.getLastname();
            String address = customer.getAddress() != null ? customer.getAddress().toString() : "";
            String phone = customer.getPhonenumber();
            view.showShippingDetails(fullName, address, phone);
        }
    }
}
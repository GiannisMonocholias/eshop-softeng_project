package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.OrderDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.Order;
import gr.softeng.team21.domain.PaymentType;

/**
 * Presenter for the Customer Card Payment activity.
 * Handles validation of card details, asynchronous fetching of customer data,
 * and finalization of the order using Dependency Injection for DAOs.
 * @author PAVLOS GRATSANIS
 */
public class CustomerCardPaymentPresenter {

    private final CustomerCardPaymentView view;
    private final CustomerDAO customerDAO;
    private final OrderDAO orderDAO;

    private Customer customer;
    private Order order;

    /**
     * Initializes the presenter using Dependency Injection.
     * @param view The view interface.
     * @param customerDAO The data access object for customer information.
     * @param orderDAO The data access object for saving orders.
     */
    public CustomerCardPaymentPresenter(CustomerCardPaymentView view, CustomerDAO customerDAO, OrderDAO orderDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.orderDAO = orderDAO;
    }

    /**
     * Asynchronously loads the customer data required for the checkout process.
     * @param customerId The ID of the customer.
     */
    public void loadInitialData(String customerId) {
        customerDAO.getCustomer(customerId).thenAccept(loadedCustomer -> {
            if (loadedCustomer != null) {
                this.customer = loadedCustomer;
            } else {
                if (view != null) view.showMessage("Ο πελάτης δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showMessage("Σφάλμα ανάκτησης δεδομένων: " + e.getMessage());
            return null;
        });
    }

    /**
     * Validates the card number and initiates the checkout process.
     * If successful, prompts the view to show a confirmation dialog.
     * @param cardNumber The card number entered by the user.
     */
    public void CardPaymentClicked(String cardNumber) {
        if (customer == null) {
            if (view != null) view.showMessage("Τα δεδομένα του πελάτη δεν έχουν φορτωθεί ακόμα.");
            return;
        }

        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            if (view != null) view.showMessage("Παρακαλώ εισάγετε τον αριθμό κάρτας!");
            return;
        }

        order = customer.Checkout();
        if (order == null) {
            if (view != null) view.showMessage("Το καλάθι είναι άδειο!");
            return;
        }

        customer.selectPaymentType(PaymentType.CARD, cardNumber, order);
        if (view != null) view.showConfirmation(order.getTotal_amount());
    }

    /**
     * Confirms the order, saves it asynchronously via OrderDAO, and navigates home.
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
     * Cancels the active order and navigates back to the HomePage.
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
}
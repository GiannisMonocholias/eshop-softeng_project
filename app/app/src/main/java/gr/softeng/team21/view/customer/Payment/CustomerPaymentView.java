package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

/**
 * View interface for the Customer Payment screen.
 * Defines methods for displaying feedback messages, order totals, shipping information,
 * confirmation dialogs, and triggering navigation.
 * @author PAVLOS GRATSANIS
 */
public interface CustomerPaymentView {

    /**
     * Displays a feedback message to the user (e.g., success, info, or error).
     * @param msg The message to display.
     */
    void showMessage(String msg);

    /**
     * Navigates to the customer's home page.
     */
    void goToCustomerHomePage();

    /**
     * Navigates to the card payment screen.
     */
    void goToToCardPayment();

    /**
     * Shows a confirmation dialog for finalizing the cash payment.
     * @param amount The total order amount.
     */
    void showConfirmation(Money amount);

    /**
     * Displays the total formatted payment amount on the screen.
     * @param amount The formatted amount string.
     */
    void showTotalAmount(String amount);

    /**
     * Displays the customer's shipping details on the screen.
     * @param name The customer's full name.
     * @param address The formatted shipping address.
     * @param phone The contact phone number.
     */
    void showShippingDetails(String name, String address, String phone);
}
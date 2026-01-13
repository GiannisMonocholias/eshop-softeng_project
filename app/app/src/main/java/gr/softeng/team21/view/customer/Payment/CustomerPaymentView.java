package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

/**
 * Interface for the Customer Payment.
 * Defines methods for displaying messages, navigation (HomePage and CardPayment), confirmation dialogs and updating UI details.
 * @author PAVLOS GRATSANIS
 */
public interface CustomerPaymentView {
    /**
     * Displays a message to the user (error or success).
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
     * Shows a confirmation dialog for the order with the specified amount.
     * @param amount The total amount of the order.
     */
    void showConfirmation(Money amount);

    /**
     * Displays the total amount on the screen.
     * @param amount The amount as a formatted string.
     */
    void showTotalAmount(String amount);

    /**
     * Displays the shipping details on the screen.
     * @param name The customer's name.
     * @param address The shipping address.
     * @param phone The contact phone number.
     */
    void showShippingDetails(String name, String address, String phone);
}
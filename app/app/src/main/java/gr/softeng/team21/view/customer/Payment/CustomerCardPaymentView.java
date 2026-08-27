package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

/**
 * View interface for the Customer Card Payment screen.
 * Defines methods for showing confirmation dialogs, feedback messages, and navigation.
 * @author PAVLOS GRATSANIS
 */
public interface CustomerCardPaymentView {

    /**
     * Shows a confirmation dialog for the card charge.
     * @param amount The total amount to be charged to the card.
     */
    void showConfirmation(Money amount);

    /**
     * Displays a feedback message to the user.
     * @param msg The message string to display.
     */
    void showMessage(String msg);

    /**
     * Navigates back to the customer's home page after a successful or cancelled transaction.
     */
    void goToCustomerHomePage();
}
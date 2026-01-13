package gr.softeng.team21.view.customer.Payment;

import gr.softeng.team21.util.Money;

/**
 * Interface for the Customer Card Payment screen.
 * Defines methods for showing confirmation dialogs, messages, and navigation.
 * @author PAVLOS GRATSANIS
 */
public interface CustomerCardPaymentView {
    /**
     * Shows a confirmation dialog for the card charge.
     * @param amount The amount to be charged.
     */
    void showConfirmation(Money amount);

    /**
     * Displays a message to the user.
     * @param msg The message to display.
     */
    void showMessage(String msg);

    /**
     * Navigates to the HomePage.
     */
    void goToCustomerHomePage();
}
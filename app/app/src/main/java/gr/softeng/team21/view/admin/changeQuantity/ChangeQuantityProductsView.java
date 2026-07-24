package gr.softeng.team21.view.admin.changeQuantity;

import java.util.ArrayList;
import gr.softeng.team21.domain.ProductType;

/**
 * Defines the UI contract for the Change Quantity screen.
 * Handles the asynchronous delivery of product data and error messages to the View.
 *
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public interface ChangeQuantityProductsView {

    /**
     * Updates the UI with the retrieved list of products asynchronously.
     *
     * @param products The list of available products fetched from the database.
     */
    void showProducts(ArrayList<ProductType> products);

    /**
     * Displays an error message to the user (e.g., in a Toast or AlertDialog).
     *
     * @param message The specific error description.
     */
    void showError(String message);
}
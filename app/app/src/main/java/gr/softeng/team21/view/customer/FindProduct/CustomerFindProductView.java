package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;
import gr.softeng.team21.domain.ProductType;

/**
 * Interface for the FindProduct .
 * Defines methods for displaying product lists, navigating to details and ShoppingCart and updating UI elements.
 * @author PAVLOS GRATSANIS
 */
public interface CustomerFindProductView {

    /**
     * Navigates to the details screen of a specific product.
     * @param productCode The unique code of the selected product.
     */
    void goToProductDetails(String productCode);

    /**
     * Displays the list of available products in the UI.
     * @param products The list of ProductType objects.
     */
    void showProducts(ArrayList<ProductType> products);

    /**
     * Navigates to the shopping cart screen.
     */
    void goToShoppingCart();

    /**
     * Displays a message to the user when the shopping cart is empty.
     * @param msg The message to display.
     */
    void showEmptyShoppingCartMessage(String msg);

    /**
     * Updates text view displaying the number of items in the cart.
     * @param quantity The total quantity of items.
     */
    void updateShoppingCartQuantity(int quantity);
}
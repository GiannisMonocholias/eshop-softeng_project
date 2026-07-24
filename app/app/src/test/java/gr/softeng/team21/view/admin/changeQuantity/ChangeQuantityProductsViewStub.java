package gr.softeng.team21.view.admin.changeQuantity;

import java.util.ArrayList;
import gr.softeng.team21.domain.ProductType;

/**
 * A stub implementation of the {@link ChangeQuantityProductsView} interface used for unit testing.
 * Captures asynchronous data loading behavior and error states to allow assertion validation
 * without requiring an actual Android UI context.
 *
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public class ChangeQuantityProductsViewStub implements ChangeQuantityProductsView {

    private ArrayList<ProductType> loadedProducts;
    private String errorMessage;

    /**
     * {@inheritDoc}
     */
    @Override
    public void showProducts(ArrayList<ProductType> products) {
        this.loadedProducts = products;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    /**
     * Retrieves the list of products passed to the view stub during a test.
     *
     * @return The ArrayList of ProductType objects.
     */
    public ArrayList<ProductType> getLoadedProducts() {
        return loadedProducts;
    }

    /**
     * Retrieves the error message passed to the view stub during a test.
     *
     * @return The error message String, or null if no error was passed.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
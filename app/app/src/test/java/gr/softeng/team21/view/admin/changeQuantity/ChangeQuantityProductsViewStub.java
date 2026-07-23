package gr.softeng.team21.view.admin.changeQuantity;

import java.util.ArrayList;
import gr.softeng.team21.domain.ProductType;

/**
 * A stub implementation of the {@link ChangeQuantityProductsView} interface for unit testing.
 * Captures asynchronous data loading and error states for assertion validation.
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public class ChangeQuantityProductsViewStub implements ChangeQuantityProductsView {

    private ArrayList<ProductType> loadedProducts;
    private String errorMessage;

    @Override
    public void showProducts(ArrayList<ProductType> products) {
        this.loadedProducts = products;
    }

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    // --- Accessor methods for verification during testing ---

    public ArrayList<ProductType> getLoadedProducts() {
        return loadedProducts;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
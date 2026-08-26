package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;
import gr.softeng.team21.domain.ProductType;

/**
 * Stub implementation of {@link CustomerFindProductView} for testing purposes.
 *
 * @author PAVLOS GRATSANIS
 */
public class CustomerFindProductViewStub implements CustomerFindProductView {

    private String productCode;
    private ArrayList<ProductType> showedProducts;
    private int showProductsCount = 0;
    private int shoppingCartCount = 0;
    private String emptyShoppingCartMessage;
    private int updateShoppingCartCount = -1;
    private String errorMessage;

    @Override
    public void showError(String message) {
        this.errorMessage = message;
    }

    @Override
    public void goToProductDetails(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public void showProducts(ArrayList<ProductType> products) {
        this.showedProducts = products;
        showProductsCount++;
    }

    @Override
    public void goToShoppingCart() {
        shoppingCartCount++;
    }

    @Override
    public void showEmptyShoppingCartMessage(String msg) {
        emptyShoppingCartMessage = msg;
    }

    @Override
    public void updateShoppingCartQuantity(int quantity) {
        updateShoppingCartCount = quantity;
    }

    // --- Getters for Tests ---
    public int getUpdateShoppingCartCount() { return updateShoppingCartCount; }
    public int getShoppingCartCount() { return shoppingCartCount; }
    public String getEmptyShoppingCartMessage() { return emptyShoppingCartMessage; }
    public String getProductCode() { return productCode; }
    public ArrayList<ProductType> getShowedProducts() { return showedProducts; }
    public int getShowProductsCount() { return showProductsCount; }
    public String getErrorMessage() { return errorMessage; }
}
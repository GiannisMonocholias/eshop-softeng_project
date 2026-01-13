package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;

/**
 * Stub implementation of {@link CustomerFindProductView} for testing purposes.
 * It provides a mechanism to capture UI feedback (shown products, navigation, error messages) and
 * track the state of UI updates during the product search flow.
 * @author PAVLOS GRATSANIS
 */
public class CustomerFindProductViewStub implements CustomerFindProductView {
    private String ProductCode;
    private ArrayList<ProductType> showedProducts;
    private int showProductsCount = 0;
    private int shoppingCartCount = 0;

    private String emptyShoppingCartMessage;
    private int updateShoppingCartCount = -1;

    /**
     * Returns the last quantity set for the ShoppingCart indicator.
     * Used for verification in tests.
     * @return The quantity count.
     */
    public int getUpdateShoppingCartCount() {
        return updateShoppingCartCount;
    }

    /**
     * Returns the number of times navigation to the ShoppingCart was triggered.
     * Used for verification in tests.
     * @return The shopping cart navigation count.
     */
    public int getShoppingCartCount() {
        return shoppingCartCount;
    }

    /**
     * Returns the last error message shown regarding the empty ShoppingCart.
     * Used for verification in tests.
     * @return The error message string.
     */
    public String getEmptyShoppingCartMessage() {
        return emptyShoppingCartMessage;
    }

    /**
     * {@inheritDoc}
     * Stores the product code to simulate navigation to the details screen.
     */
    @Override
    public void goToProductDetails(String productCode) {
        this.ProductCode = productCode;
    }

    /**
     * {@inheritDoc}
     * Stores the list of products displayed to the user and increments the display counter.
     */
    @Override
    public void showProducts(ArrayList<ProductType> products) {
        this.showedProducts = products;
        showProductsCount++;
    }

    /**
     * {@inheritDoc}
     * Increments the counter for ShoppingCart navigation.
     */
    @Override
    public void goToShoppingCart() {
        shoppingCartCount++;
    }

    /**
     * {@inheritDoc}
     * Stores the empty cart message in a variable for verification.
     */
    @Override
    public void showEmptyShoppingCartMessage(String msg) {
        emptyShoppingCartMessage = msg;
    }

    /**
     * {@inheritDoc}
     * Stores the cart quantity in a variable for verification.
     */
    @Override
    public void updateShoppingCartQuantity(int quantity) {
        updateShoppingCartCount = quantity;
    }

    /**
     * Returns the product code stored from navigation.
     * Used for verification in tests.
     * @return The product code string.
     */
    public String getProductCode() {
        return ProductCode;
    }

    /**
     * Returns the list of products currently displayed in the view.
     * Used for verification in tests.
     * @return The list of ProductType objects.
     */
    public ArrayList<ProductType> getShowedProducts() {
        return showedProducts;
    }

    /**
     * Returns the number of times the product list was updated.
     * Used for verification in tests.
     * @return The show products count.
     */
    public int getShowProductsCount() {
        return showProductsCount;
    }
}
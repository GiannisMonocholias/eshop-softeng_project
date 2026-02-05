package gr.softeng.team21.view.product;

import java.util.HashMap;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

/**
 * Presenter for the Product Details activity.
 * Handles interactions between the {@link ProductDetailsView} and the domain logic,
 * including loading product data and managing cart additions.
 * @author PAVLOS GRATSANIS
 */
public class ProductDetailsPresenter {
    private ProductDetailsView view;
    private Customer customer;

    private ProductType foundProduct;
    private int currentQuantity = 1;

    /**
     * Initializes the presenter with the view and the specific customer.
     * @param view The view interface.
     * @param customer The customer domain object.
     */
    public ProductDetailsPresenter(ProductDetailsView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    /**
     * Increases the selected quantity by 1 and updates the view.
     */
    public void plusClicked() {
        currentQuantity++;
        view.showQuantity(currentQuantity);
    }

    /**
     * Decreases the selected quantity by 1 ,if currentQuantity>1, and updates the view.
     */
    public void minusClicked() {
        if (currentQuantity > 1) {
            currentQuantity--;
            view.showQuantity(currentQuantity);
        }
    }

    /**
     * Adds the selected product with ths specific quantity to the customer's shopping cart.
     * Displays a success message or error if the operation fails.
     */
    public void addToCartClicked() {
        try {
            customer.addItemToCart(foundProduct, currentQuantity);
            view.showAddToCartSuccess();
        } catch (Exception e) {
            view.showMessage("Σφάλμα: " + e.getMessage());
        }
    }

    /**
     * Handles the navigation to the shopping cart.
     */
    public void openShoppingCartClicked() {
        view.goToCart();
        view.showMessage("Μετάβαση στο Καλάθι...");
    }

    /**
     * Loads the product details from the repository based on the provided product code and updates the view.
     * @param productCode The unique code of the product to load.
     */
    public void loadProduct(String productCode) {
        if (productCode != null) {
            foundProduct = ProductTypeDAOMemory.getInstance().getProduct(productCode);
        }
        if(foundProduct!=null){
            view.showProductDetails(foundProduct.getProductname(),
                    foundProduct.getProductCode(),
                    foundProduct.getPrice().toString(),
                    foundProduct.getDescription(), foundProduct.getProductCode());
        }
        view.showQuantity(1);
    }
}
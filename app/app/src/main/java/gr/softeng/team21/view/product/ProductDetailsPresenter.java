package gr.softeng.team21.view.product;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;

/**
 * Presenter for the Product Details activity.
 * Handles interactions between the {@link ProductDetailsView} and the domain logic,
 * including loading product data and managing cart additions asynchronously.
 * @author PAVLOS GRATSANIS
 */
public class ProductDetailsPresenter {
    private final ProductDetailsView view;
    private final CustomerDAO customerDAO;
    private final ProductTypeDAO productDAO;

    private Customer customer;
    private ProductType foundProduct;
    private int currentQuantity = 1;

    /**
     * Initializes the presenter with the view and data access objects.
     * @param view The view interface.
     * @param customerDAO The customer data access object.
     * @param productDAO The product data access object.
     */
    public ProductDetailsPresenter(ProductDetailsView view, CustomerDAO customerDAO, ProductTypeDAO productDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
    }

    /**
     * Asynchronously loads the customer and then triggers the product loading.
     * @param customerId The ID of the currently logged-in customer.
     * @param productCode The unique code of the product to load.
     */
    public void loadInitialData(String customerId, String productCode) {
        customerDAO.getCustomer(customerId).thenAccept(loadedCustomer -> {
            if (loadedCustomer != null) {
                this.customer = loadedCustomer;
                loadProduct(productCode); // Αλυσίδα: Φόρτωσε το προϊόν αφότου βρεις τον πελάτη
            } else {
                if (view != null) view.showMessage("Ο πελάτης δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showMessage("Σφάλμα σύνδεσης: " + e.getMessage());
            return null;
        });
    }

    /**
     * Increases the selected quantity by 1 and updates the view.
     */
    public void plusClicked() {
        currentQuantity++;
        if (view != null) view.showQuantity(currentQuantity);
    }

    /**
     * Decreases the selected quantity by 1 ,if currentQuantity>1, and updates the view.
     */
    public void minusClicked() {
        if (currentQuantity > 1) {
            currentQuantity--;
            if (view != null) view.showQuantity(currentQuantity);
        }
    }

    /**
     * Adds the selected product with ths specific quantity to the customer's shopping cart.
     * Displays a success message or error if the operation fails.
     */
    public void addToCartClicked() {
        if (customer == null || foundProduct == null) {
            if (view != null) view.showMessage("Δεν έχει φορτωθεί πλήρως το προϊόν ή ο πελάτης.");
            return;
        }

        try {
            customer.addItemToCart(foundProduct, currentQuantity);
            if (view != null) view.showAddToCartSuccess();
        } catch (Exception e) {
            if (view != null) view.showMessage("Σφάλμα: " + e.getMessage());
        }
    }

    /**
     * Handles the navigation to the shopping cart.
     */
    public void openShoppingCartClicked() {
        if (view != null) {
            view.goToCart();
            view.showMessage("Μετάβαση στο Καλάθι...");
        }
    }

    /**
     * Loads the product details from the repository based on the provided product code and updates the view asynchronously.
     * @param productCode The unique code of the product to load.
     */
    public void loadProduct(String productCode) {
        if (productCode != null) {
            productDAO.getProduct(productCode).thenAccept(product -> {
                this.foundProduct = product;
                if (foundProduct != null && view != null) {
                    view.showProductDetails(
                            foundProduct.getProductname(),
                            foundProduct.getProductCode(),
                            foundProduct.getPrice().toString(),
                            foundProduct.getDescription(),
                            foundProduct.getProductCode()
                    );
                    view.showQuantity(1);
                } else if (view != null) {
                    view.showQuantity(1); // Default behavior for null object
                }
            }).exceptionally(e -> {
                if (view != null) view.showMessage("Σφάλμα φόρτωσης προϊόντος: " + e.getMessage());
                return null;
            });
        } else {
            if (view != null) view.showQuantity(1);
        }
    }
}
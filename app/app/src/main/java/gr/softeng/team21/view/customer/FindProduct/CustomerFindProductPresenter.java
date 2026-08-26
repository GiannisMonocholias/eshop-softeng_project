package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;

import gr.softeng.team21.dao.CustomerDAO;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;

/**
 * Presenter for the FindProductActivity.
 * Handles asynchronous interactions with Firebase via DAOs and updates the
 * {@link CustomerFindProductView}, including product filtering and cart status.
 *
 * @author PAVLOS GRATSANIS
 */
public class CustomerFindProductPresenter {
    private final CustomerFindProductView view;
    private final CustomerDAO customerDAO;
    private final ProductTypeDAO productDAO;

    private Customer customer;
    private ArrayList<ProductType> allProducts;

    /**
     * Initializes the presenter using Dependency Injection.
     * @param view The view interface.
     * @param customerDAO The data access object for customers.
     * @param productDAO The data access object for products.
     */
    public CustomerFindProductPresenter(CustomerFindProductView view, CustomerDAO customerDAO, ProductTypeDAO productDAO) {
        this.view = view;
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
        this.allProducts = new ArrayList<>();
    }

    /**
     * Asynchronously loads the customer data and then the available products list.
     * Updates the UI once data is fully retrieved.
     * @param customerId The ID of the currently logged-in customer.
     */
    public void loadInitialData(String customerId) {
        customerDAO.getCustomer(customerId).thenAccept(loadedCustomer -> {
            if (loadedCustomer != null) {
                this.customer = loadedCustomer;
                updateShoppingCartStatus(); // Update cart UI immediately

                // Now load the products asynchronously
                productDAO.getProducts().thenAccept(productsMap -> {
                    this.allProducts = new ArrayList<>(productsMap.values());
                    if (view != null) view.showProducts(this.allProducts);
                }).exceptionally(e -> {
                    if (view != null) view.showError("Σφάλμα φόρτωσης προϊόντων: " + e.getMessage());
                    return null;
                });

            } else {
                if (view != null) view.showError("Ο πελάτης δεν βρέθηκε.");
            }
        }).exceptionally(e -> {
            if (view != null) view.showError("Σφάλμα σύνδεσης με τη βάση: " + e.getMessage());
            return null;
        });
    }

    /**
     * Filters the already loaded product list based on the user's search query.
     * @param txt The search text entered by the customer.
     */
    public void filter(String txt) {
        ArrayList<ProductType> filteredList = new ArrayList<>();
        if (txt == null || txt.trim().isEmpty()) {
            filteredList.addAll(allProducts);
        } else {
            String searchText = txt.toLowerCase().trim();
            for (ProductType item : allProducts) {
                if (item.getProductname().toLowerCase().contains(searchText)) {
                    filteredList.add(item);
                }
            }
        }
        if (view != null) view.showProducts(filteredList);
    }

    /**
     * Handles selecting a product from the list to view its details.
     * @param selectedProduct The product clicked by the user.
     */
    public void ProductClicked(ProductType selectedProduct) {
        if (selectedProduct != null && view != null) {
            view.goToProductDetails(selectedProduct.getProductCode());
        }
    }

    /**
     * Handles the click on the shopping cart button.
     */
    public void openShoppingCartClicked() {
        if (customer == null) return;

        if (customer.getShoppingCart() == null || customer.getShoppingCart().getItems().isEmpty()) {
            if (view != null) view.showEmptyShoppingCartMessage("To καλάθι είναι άδειο!!");
        } else {
            if (view != null) view.goToShoppingCart();
        }
    }

    /**
     * Updates the shopping cart item count on the screen.
     */
    public void updateShoppingCartStatus() {
        if (customer == null) return;

        if (customer.getShoppingCart() != null) {
            int quantity = customer.getShoppingCart().getItems().size();
            if (view != null) view.updateShoppingCartQuantity(quantity);
        } else {
            if (view != null) view.updateShoppingCartQuantity(0);
        }
    }
}
package gr.softeng.team21.view.admin.changeQuantity;

import java.util.ArrayList;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.ProductType;

/**
 * Presenter responsible for loading available products for quantity management.
 * Utilizes {@link java.util.concurrent.CompletableFuture} for asynchronous data retrieval
 * via Dependency Injection of the ProductTypeDAO.
 *
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public class ChangeQuantityProductsPresenter {

    private ChangeQuantityProductsView view;
    private ProductTypeDAO productTypeDAO;

    /**
     * Initializes the presenter with the injected view and DAO.
     *
     * @param view           The view implementation (Activity or testing Stub).
     * @param productTypeDAO The Data Access Object responsible for product operations.
     */
    public ChangeQuantityProductsPresenter(ChangeQuantityProductsView view, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Asynchronously fetches all products from the DAO and forwards them to the view.
     * Handles potential exceptions during the fetch process.
     */
    public void loadProducts() {
        productTypeDAO.getProducts().thenAccept(productsMap -> {
            ArrayList<ProductType> products = new ArrayList<>(productsMap.values());
            view.showProducts(products);
        }).exceptionally(e -> {
            view.showError("Σφάλμα κατά τη φόρτωση των προϊόντων: " + e.getMessage());
            return null;
        });
    }
}
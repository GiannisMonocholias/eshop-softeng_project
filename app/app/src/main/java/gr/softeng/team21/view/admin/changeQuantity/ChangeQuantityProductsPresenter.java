package gr.softeng.team21.view.admin.changeQuantity;

import java.util.ArrayList;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.ProductType;

/**
 * Presenter for loading available products for quantity management.
 * Uses CompletableFuture for asynchronous data retrieval via Dependency Injection.
 * @author Αλέξανδρος Δρακάκης, Γιάννης Μονοχολιάς
 */
public class ChangeQuantityProductsPresenter {

    private ChangeQuantityProductsView view;
    private ProductTypeDAO productTypeDAO;

    /**
     * Initializes the presenter with the injected view and DAO.
     * @param view The view implementation (Activity or Stub).
     * @param productTypeDAO The Data Access Object for products.
     */
    public ChangeQuantityProductsPresenter(ChangeQuantityProductsView view, ProductTypeDAO productTypeDAO) {
        this.view = view;
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * Asynchronously fetches all products from the DAO and forwards them to the view.
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
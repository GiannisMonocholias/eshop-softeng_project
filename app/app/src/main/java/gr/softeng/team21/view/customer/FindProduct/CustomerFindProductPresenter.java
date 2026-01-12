package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

/**
 * Presenter for the FindProductActivity.
 * Handles interactions between the {@link CustomerFindProductView} and the domain logic,
 * including product filtering, selection and cart status updates.
 * @author PAVLOS GRATSANIS
 */
public class CustomerFindProductPresenter {
    private CustomerFindProductView view;
    private ProductTypeDAOMemory dao;
    private Customer customer;
    private ArrayList<ProductType> allProducts;

    /**
     * Initializes the presenter with the view and customer.
     * Loads all available products from the DAO.
     * @param view The view interface.
     * @param customer The customer domain object.
     */
    public CustomerFindProductPresenter(CustomerFindProductView view, Customer customer) {
        this.view = view;
        this.customer = customer;
        this.dao = ProductTypeDAOMemory.getInstance();
        this.allProducts = new ArrayList<>(dao.getProducts().values());
    }

    /**
     * Loads and displays the full list of products in the view.
     */
    public void loadList() {
        view.showProducts(allProducts);
    }

    /**
     * Filters the product list based on the user's search query.
     * @param txt The search text entered by the customer.
     */
    public void filter(String txt) {
        ArrayList<ProductType> filteredList = new ArrayList<>();
        if (txt == null || txt.isEmpty()) {
            filteredList.addAll(allProducts);
        } else {
            String searchText = txt.toLowerCase();
            for (ProductType item : allProducts) {
                if (item.getProductname().toLowerCase().contains(searchText)) {
                    filteredList.add(item);
                }
            }
        }
        view.showProducts(filteredList);
    }

    /**
     * Handles selecting a product from the list and clicking to view the details screen for that product.
     * @param selectedProduct The product clicked by the user.
     */
    public void ProductClicked(ProductType selectedProduct) {
        if (selectedProduct != null) {
            view.goToProductDetails(selectedProduct.getProductCode());
        }
    }

    /**
     * Handles the click on the shopping cart button.
     * Checks if the cart is not empty or is not exists before navigating.
     */
    public void openShoppingCartClicked() {
        if (customer.getShoppingCart() == null) {
            view.showEmptyShoppingCartMessage("To καλάθι είναι άδειο!!");
        } else {
            view.goToShoppingCart();
        }
    }

    /**
     * Updates the shopping cart item count in the screen.
     */
    public void updateShoppingCartStatus() {
        if (customer.getShoppingCart() != null) {
            int quantity = customer.getShoppingCart().getItems().size();
            view.updateShoppingCartQuantity(quantity);
        } else {
            view.updateShoppingCartQuantity(0);
        }
    }
}
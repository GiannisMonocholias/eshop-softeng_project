package gr.softeng.team21.view.product;

import android.widget.Toast;

import java.util.HashMap;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

public class ProductDetailsPresenter {
    private ProductDetailsView view;
    private Customer customer;

    private ProductType foundProduct;
    private int currentQuantity = 1;

    public ProductDetailsPresenter(ProductDetailsView view, Customer customer) {
        this.view = view;
        this.customer = customer;
    }

    public void plusClicked() {
        currentQuantity++;
        view.showQuantity(currentQuantity);
    }

    public void minusClicked() {
        if (currentQuantity > 1) {
            currentQuantity--;
            view.showQuantity(currentQuantity);
        }
    }

    public void addToCartClicked() {
        try {
            customer.addItemToCart(foundProduct, currentQuantity);
            view.showAddToCartSuccess();
        } catch (Exception e) {
            view.showMessage("Σφάλμα: " + e.getMessage());
        }
    }

    public void openShoppingCartClicked() {
        view.goToCart();
        view.showMessage("Μετάβαση στο Καλάθι...");
    }

    public void loadProduct(String productCode) {
        if (productCode != null) {
            HashMap<String, ProductType> allProducts = ProductTypeDAOMemory.getInstance().getProducts();
            foundProduct = customer.findProduct(allProducts, productCode);
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

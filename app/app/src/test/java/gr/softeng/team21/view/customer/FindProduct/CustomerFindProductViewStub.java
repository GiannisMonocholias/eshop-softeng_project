package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;

public class CustomerFindProductViewStub implements CustomerFindProductView {
    private String ProductCode;
    private ArrayList<ProductType> showedProducts;
    private int showProductsCount = 0;
    private int shoppingCartCount = 0;

    private String emptyShoppingCartMessage;
    private int updateShoppingCartCount=-1;

    public int getUpdateShoppingCartCount() {
        return updateShoppingCartCount;
    }

    public int getShoppingCartCount() {
        return shoppingCartCount;
    }

    public String getEmptyShoppingCartMessage() {
        return emptyShoppingCartMessage;
    }

    @Override
    public void goToProductDetails(String productCode) {
        this.ProductCode = productCode;
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
        emptyShoppingCartMessage=msg;
    }

    @Override
    public void updateShoppingCartQuantity(int quantity) {
        updateShoppingCartCount=quantity;
    }


    public String getProductCode() {
        return ProductCode;
    }

    public ArrayList<ProductType> getShowedProducts() {
        return showedProducts;
    }

    public int getShowProductsCount() {
        return showProductsCount;
    }
}

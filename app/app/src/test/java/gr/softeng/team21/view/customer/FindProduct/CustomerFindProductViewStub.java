package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;

public class CustomerFindProductViewStub implements CustomerFindProductView {
    private String ProductCode;
    private ArrayList<ProductType> showedProducts;
    private int showProductsCount = 0;

    @Override
    public void goToProductDetails(String productCode) {
        this.ProductCode = productCode;
    }

    @Override
    public void showProducts(ArrayList<ProductType> products) {
        this.showedProducts = products;
        showProductsCount++;
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

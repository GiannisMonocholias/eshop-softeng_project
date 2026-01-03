package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;

public interface CustomerFindProductView {
    void goToProductDetails(String productCode);
    void showProducts(ArrayList<ProductType> products);
}

package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;

import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

public class CustomerFindProductPresenter {
    private CustomerFindProductView view;
    private ProductTypeDAOMemory dao;
    private ArrayList<ProductType> allProducts = new ArrayList<>(dao.getInstance().getProducts().values());
    public CustomerFindProductPresenter(CustomerFindProductView view) {
        this.view = view;
    }

    public void loadList() {
        view.showProducts(allProducts);
    }

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

    public void ProductClicked(ProductType selectedProduct) {
        if (selectedProduct != null) {
            view.goToProductDetails(selectedProduct.getProductCode());
        }
    }
}

package gr.softeng.team21.view.customer.FindProduct;

import java.util.ArrayList;

import gr.softeng.team21.domain.Customer;
import gr.softeng.team21.domain.ProductType;
import gr.softeng.team21.memorydao.ProductTypeDAOMemory;

public class CustomerFindProductPresenter {
    private CustomerFindProductView view;
    private ProductTypeDAOMemory dao;
    private Customer customer;
    private ArrayList<ProductType> allProducts = new ArrayList<>(dao.getInstance().getProducts().values());

    public CustomerFindProductPresenter(CustomerFindProductView view,Customer customer) {
        this.view = view;
        this.customer=customer;
        this.dao =  ProductTypeDAOMemory.getInstance();
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

    public void openShoppingCartClicked() {
        if (customer.getShoppingCart()==null) {
            view.showEmptyShoppingCartMessage("To καλάθι είναι άδειο!!");
        } else {
            view.goToShoppingCart();
        }
    }

    public void updateShoppingCartStatus() {
        if(customer.getShoppingCart()!=null){
            int quantity=customer.getShoppingCart().getItems().size();
            view.updateShoppingCartQuantity(quantity);
        }else
            view.updateShoppingCartQuantity(0);
    }
}


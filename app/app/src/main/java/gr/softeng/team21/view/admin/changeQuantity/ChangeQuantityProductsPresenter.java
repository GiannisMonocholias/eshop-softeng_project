package gr.softeng.team21.view.admin.changeQuantity;

import android.util.Log;

import java.util.ArrayList;

import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.ProductType;

/**
 * Ο ChangeQuantityProductsPresenter συμβάλλει στην υλοποίηση τησ λειτουργίας που είναι υπεύθυνη για
 * να φορτώνονται τα προϊόντα που είναι διαθέσιμα στο κατάστημα.
 *
 * Χρησιμοποιεί ένα αντικείμενο του ProductTypeDAO για να έχει πρόσβαση στα δεδομένα που έχουν να κάνουν
 * με τα διαθέσιμα προϊόντα του καταστήματος
 */

public class ChangeQuantityProductsPresenter {

    private ChangeQuantityProductsView view;
    private ProductTypeDAO productTypeDAO;

    public ChangeQuantityProductsPresenter(ChangeQuantityProductsView view , ProductTypeDAO productTypeDAO){
        this.view = view;
        this.productTypeDAO = productTypeDAO;
    }

    public ArrayList<ProductType> loadProducts() {

        ArrayList<ProductType> products = new ArrayList<>();
        for (ProductType pd : productTypeDAO.getProducts().values()){
            products.add(pd);
        }


        return products;
    }
}

package gr.softeng.team21.view.product;

public interface ProductDetailsView {
    void showProductDetails(String name, String code, String price, String description, String imgCode);

    void showQuantity(int quantity);
    void showMessage(String msg);

    void showAddToCartSuccess();

    void goToCart();
}

package gr.softeng.team21.view.product;

public class ProductDetailsViewStub implements ProductDetailsView {

    private String name, code, price, description;
    private int quantity;
    private String message;
    private int addToCartCount = 0;
    private int CartCount = 0;
    @Override
    public void showProductDetails(String name, String code, String price, String description, String imgCode) {
        this.name = name;
        this.code = code;
        this.price = price;
        this.description = description;
    }

    @Override
    public void showQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void showMessage(String msg) {
        this.message = msg;
    }

    @Override
    public void showAddToCartSuccess() {
        this.addToCartCount++;
    }

    @Override
    public void goToCart() {
        this.CartCount++;
    }

    public String getName() { return name; }
    public String getCode() { return code; }
    public String getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getMessage() { return message; }
    public int getAddToCartCount() { return addToCartCount; }
    public int getCartCount() { return CartCount; }
}
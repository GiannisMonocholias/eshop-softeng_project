package gr.softeng.team21.domain;

public class ProductType {
    private String productname;
    private String description;
    private Money price;
    private String productcode;

    public ProductType(String productname, String description, Money price, String productcode) {
        this.productname = productname;
        this.description = description;
        this.price = price;
        this.productcode = productcode;

    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public String getProductCode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    @Override
    public String toString() {
        return productname + "---" + price;
    }
}

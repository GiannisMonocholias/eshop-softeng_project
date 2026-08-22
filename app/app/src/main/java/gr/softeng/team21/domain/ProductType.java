package gr.softeng.team21.domain;

import gr.softeng.team21.util.Money;

/**
 * Represents a specific type of product.
 *Contains details such as name, description, price and a unique code.
 * @author PAVLOS GRATSANIS
 */
public class ProductType {

    /** The name of the product */
    private String productname;

    /** The description of the product */
    private String description;

    /** The price of the product */
    private Money price;

    /** The unique code identifying the product */
    private String productcode;


    /**
     * Default constructor
     * */
    public ProductType() {
    }

    /**
     * Creates a new ProductType with the specified details.
     * @param productname the name of the product
     * @param description the description of the product
     * @param price the price of the product
     * @param productcode the unique code of the product
     */
    public ProductType(String productname, String description, Money price, String productcode) {
        this.productname = productname;
        this.description = description;
        this.price = price;
        this.productcode = productcode;
    }

    /**
     * Returns the name of the product.
     * @return the product name
     */
    public String getProductname() {
        return productname;
    }

    /**
     * Sets the name of the product.
     * @param productname the new product name
     */
    public void setProductname(String productname) {
        this.productname = productname;
    }

    /**
     * Returns the description of the product.
     * @return the product description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the product.
     * @param description the new product description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the price of the product.
     * @return the product price
     */
    public Money getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     * @param price the new product price
     */
    public void setPrice(Money price) {
        this.price = price;
    }

    /**
     * Returns the unique code of the product.
     * @return the product code
     */
    public String getProductCode() {
        return productcode;
    }

    /**
     * Sets the unique code of the product.
     * @param productcode the new product code
     */
    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    /**
     * Returns a string representation of the product type.
     * The string includes the product name and its price.
     * @return a formatted string representing the product
     */
    @Override
    public String toString() {
        return productname + "---" + price;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductType that = (ProductType) o;
        return productname != null ? productname.equals(that.productname) : that.productname == null;
    }

    @Override
    public int hashCode() {
        return productname != null ? productname.hashCode() : 0;
    }
}
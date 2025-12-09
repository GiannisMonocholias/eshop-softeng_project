package gr.softeng.team21.domain;

public class WholesaleProduct {
    private int id;
    private String name;
    private String supName;
    private String description;
    private Money price;

    public WholesaleProduct(int id , String name , String supName , String description , Money price){
        this.id = id;
        this.name = name;
        this.supName = supName;
        this.description = description;
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public String getSupName() {
        return supName;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}

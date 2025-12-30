package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.NoSuchElementException;

import gr.softeng.team21.domain.ProductType;

public class ProductsWareHouseDAOMemory {

    private static ProductsWareHouseDAOMemory instance;

    private static int maxCapacity = 1000;
    private static double totalProducts;

    private static HashMap<ProductType,Integer> productStocks;


    private ProductsWareHouseDAOMemory() {
        this.totalProducts = 0;
        this.productStocks = new HashMap<>();
    }

    public static ProductsWareHouseDAOMemory getInstance(){
        if(instance == null){
            instance = new ProductsWareHouseDAOMemory();
        }
        return instance;
    }

    public Integer getProductStock(ProductType type){
        if(type == null)
            throw new IllegalArgumentException("type argument cannot be null");

        if(productStocks.containsKey(type))
            return  productStocks.get(type);
        else
            return null;
    }

    public double getCapacityUtilization() {
        return totalProducts > 0 ? (double) totalProducts/maxCapacity : 0;
    }


    public void insertProduct(ProductType type) {
        if(type == null)
            throw new IllegalArgumentException("type argument cannot be null");

        if(!productStocks.containsKey(type)) {
            productStocks.put(type, 0);
        }
        else{
            throw new IllegalArgumentException("The provided type already exists in stock");
        }

    }

    public  void deleteProduct(ProductType type) throws NoSuchElementException {
        if(type == null)
            throw new IllegalArgumentException("type argument cannot be null");


        if(!productStocks.containsKey(type))
            throw new NoSuchElementException("Product not in stock");

        productStocks.remove(type);
    }

    public boolean increaseProductStock(ProductType type,int amount){
        if(!productStocks.containsKey(type)){
            return false;
        }

        if(!isValidAmount(amount)){
            return false;
        }

        int previousValue =productStocks.get(type);
        productStocks.put(type, previousValue + amount);

        return true;
    }

    public boolean decreaseProductStock(ProductType type,int amount){
        if(!productStocks.containsKey(type)){
            return false;
        }

        if(!isValidAmount(amount)){
            return false;
        }

        int previousValue =productStocks.get(type);
        if(sufficientStock(type, amount))
            productStocks.put(type, previousValue - amount);
        else
            return false;
        return true;
    }

    public boolean sufficientStock(ProductType type, int amount){
        int previousValue =productStocks.get(type);
        return (previousValue - amount > 0);
    }

    public boolean isValidAmount(int amount){
        return amount > 0;
    }

    public HashMap<ProductType, Integer> getProductStocks() {
        return productStocks;
    }


    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void clear(){
        productStocks.clear();
    }


}

package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.NoSuchElementException;

import gr.softeng.team21.domain.ProductType;

public interface ProductsWareHouseDAO {

     Integer getProductStock(ProductType type);

     double getCapacityUtilization();


     void insertProduct(ProductType type);
      void deleteProduct(ProductType type);

     boolean increaseProductStock(ProductType type,int amount);

     boolean decreaseProductStock(ProductType type,int amount);
     boolean sufficientStock(ProductType type, int amount);

     boolean isValidAmount(int amount);

     HashMap<ProductType, Integer> getProductStocks();


     int getMaxCapacity();

     void setMaxCapacity(int maxCapacity);

     void clear();

}

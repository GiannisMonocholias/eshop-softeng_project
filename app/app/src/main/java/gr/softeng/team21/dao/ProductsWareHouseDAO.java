package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.NoSuchElementException;

import gr.softeng.team21.domain.ProductType;

public interface ProductsWareHouseDAO {

     /**
      * Retrieves the current stock level for a specific product type.
      * @param type the product type to query.
      * @return the stock quantity, or null if the product type is not tracked.
      * @throws IllegalArgumentException if the type argument is null.
      */
     Integer getProductStock(ProductType type);

     /**
      * Calculates the percentage of warehouse capacity currently in use.
      * @return a double representing the utilization ratio (0.0 to 1.0).
      */
     double getCapacityUtilization();

     /**
      * Adds a new product type to the warehouse tracking system with zero initial stock.
      * @param type the new product type to insert.
      * @throws IllegalArgumentException if the type is null or already exists in the system.
      */
     void insertProduct(ProductType type);

     /**
      * Removes a product type from the warehouse tracking system.
      * @param type the product type to remove.
      * @throws IllegalArgumentException if the type is null.
      * @throws NoSuchElementException if the product type is not found in stock.
      */
     void deleteProduct(ProductType type);

     /**
      * Increases the stock level for a specific product.
      * @param type   the product type to update.
      * @param amount the quantity to add (must be positive).
      * @return true if the update was successful, false if the type is missing or amount is invalid.
      */
     boolean increaseProductStock(ProductType type,int amount);

     /**
      * Decreases the stock level for a specific product if sufficient stock exists.
      * @param type   the product type to update.
      * @param amount the quantity to subtract (must be positive).
      * @return true if the update was successful, false if the type is missing or amount is invalid or warehouse stock is
      * insufficient.
      */
     boolean decreaseProductStock(ProductType type,int amount);


     /**
      * Checks if there is enough stock available for a specific product.
      * @param type the product type to check.
      * @param amount the quantity requested.
      * @return true if current stock minus amount is greater than zero.
      */
     boolean sufficientStock(ProductType type, int amount);

     /**
      * Validates if the provided quantity is a positive integer.
      * @param amount the quantity to validate.
      * @return true if amount > 0.
      */
     boolean isValidAmount(int amount);

     /**
      * @return the complete map of product types and their respective stock quantities.
      */
     HashMap<ProductType, Integer> getProductStocks();

     /**
      * @return the maximum capacity limit of the warehouse.
      */
     int getMaxCapacity();

     /**
      * Updates the maximum capacity limit of the warehouse.
      * @param maxCapacity the new capacity limit.
      */
     void setMaxCapacity(int maxCapacity);

     /**
      * Resets the warehouse by clearing all product stock data.
      */
     void clear();

}

package gr.softeng.team21.dao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.domain.ProductType;

/**
 * Data Access Object (DAO) interface for managing warehouse product stocks.
 * Uses CompletableFuture for non-blocking, asynchronous operations.
 */
public interface ProductsWareHouseDAO {

    /**
     * Retrieves the current stock level for a specific product type.
     * @param type the product type to query.
     * @return A CompletableFuture containing the stock quantity, or null if the product type is not tracked.
     */
    CompletableFuture<Integer> getProductStock(ProductType type);

    /**
     * Calculates the percentage of warehouse capacity currently in use.
     * @return A CompletableFuture containing a Double representing the utilization ratio (0.0 to 1.0).
     */
    CompletableFuture<Double> getCapacityUtilization();

    /**
     * Adds a new product type to the warehouse tracking system with zero initial stock.
     * @param type the new product type to insert.
     * @return A CompletableFuture representing the completion of the operation.
     */
    CompletableFuture<Void> insertProduct(ProductType type);

    /**
     * Removes a product type from the warehouse tracking system.
     * @param type the product type to remove.
     * @return A CompletableFuture representing the completion of the deletion.
     */
    CompletableFuture<Void> deleteProduct(ProductType type);

    /**
     * Increases the stock level for a specific product.
     * @param type   the product type to update.
     * @param amount the quantity to add (must be positive).
     * @return A CompletableFuture containing true if the update was successful, false otherwise.
     */
    CompletableFuture<Boolean> increaseProductStock(ProductType type, int amount);

    /**
     * Decreases the stock level for a specific product if sufficient stock exists.
     * @param type   the product type to update.
     * @param amount the quantity to subtract (must be positive).
     * @return A CompletableFuture containing true if the update was successful, false otherwise.
     */
    CompletableFuture<Boolean> decreaseProductStock(ProductType type, int amount);

    /**
     * Checks if there is enough stock available for a specific product.
     * @param type the product type to check.
     * @param amount the quantity requested.
     * @return A CompletableFuture containing true if current stock minus amount is greater than zero.
     */
    CompletableFuture<Boolean> sufficientStock(ProductType type, int amount);

    /**
     * Validates if the provided quantity is a positive integer.
     * @param amount the quantity to validate.
     * @return A CompletableFuture containing true if amount > 0.
     */
    CompletableFuture<Boolean> isValidAmount(int amount);

    /**
     * @return A CompletableFuture containing the complete map of product types and their respective stock quantities.
     */
    CompletableFuture<HashMap<ProductType, Integer>> getProductStocks();

    /**
     * @return A CompletableFuture containing the maximum capacity limit of the warehouse.
     */
    CompletableFuture<Integer> getMaxCapacity();

    /**
     * Updates the maximum capacity limit of the warehouse.
     * @param maxCapacity the new capacity limit.
     * @return A CompletableFuture representing the completion of the operation.
     */
    CompletableFuture<Void> setMaxCapacity(int maxCapacity);

    /**
     * Resets the warehouse by clearing all product stock data.
     * @return A CompletableFuture representing the completion of the bulk deletion.
     */
    CompletableFuture<Void> clear();

}
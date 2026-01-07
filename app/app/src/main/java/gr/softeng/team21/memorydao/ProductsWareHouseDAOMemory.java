package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.NoSuchElementException;

import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.domain.ProductType;

/**
 * In-memory implementation of the {@link ProductsWareHouseDAO} interface.
 * Manages product stock levels, capacity utilization, and provides methods
 * for increasing or decreasing product inventory.
 * @author Γιάννης Μονοχολιάς
 */
public class ProductsWareHouseDAOMemory implements ProductsWareHouseDAO {

    private static ProductsWareHouseDAOMemory instance;
    private static int maxCapacity = 1000;
    private static double totalProducts;
    private static HashMap<ProductType, Integer> productStocks;

    /**
     * Private constructor for the Singleton pattern.
     * Initializes the stock storage map.
     */
    private ProductsWareHouseDAOMemory() {
        this.totalProducts = 0;
        this.productStocks = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the warehouse DAO.
     * @return The unique ProductsWareHouseDAOMemory instance.
     */
    public static ProductsWareHouseDAOMemory getInstance() {
        if (instance == null) {
            instance = new ProductsWareHouseDAOMemory();
        }
        return instance;
    }

    /**
     * Retrieves the current stock level for a specific product type.
     * @param type the product type to query.
     * @return the stock quantity, or null if the product type is not tracked.
     * @throws IllegalArgumentException if the type argument is null.
     */
    public Integer getProductStock(ProductType type) {
        if (type == null)
            throw new IllegalArgumentException("type argument cannot be null");

        return productStocks.get(type);
    }

    /**
     * Calculates the percentage of warehouse capacity currently in use.
     * @return a double representing the utilization ratio (0.0 to 1.0).
     */
    public double getCapacityUtilization() {
        return totalProducts > 0 ? (double) totalProducts / maxCapacity : 0;
    }

    /**
     * Adds a new product type to the warehouse tracking system with zero initial stock.
     * @param type the new product type to insert.
     * @throws IllegalArgumentException if the type is null or already exists in the system.
     */
    public void insertProduct(ProductType type) {
        if (type == null)
            throw new IllegalArgumentException("type argument cannot be null");

        if (!productStocks.containsKey(type)) {
            productStocks.put(type, 0);
        } else {
            throw new IllegalArgumentException("The provided type already exists in stock");
        }
    }

    /**
     * Removes a product type from the warehouse tracking system.
     * @param type the product type to remove.
     * @throws IllegalArgumentException if the type is null.
     * @throws NoSuchElementException if the product type is not found in stock.
     */
    public void deleteProduct(ProductType type) throws NoSuchElementException {
        if (type == null)
            throw new IllegalArgumentException("type argument cannot be null");

        if (!productStocks.containsKey(type))
            throw new NoSuchElementException("Product not in stock");

        productStocks.remove(type);
    }

    /**
     * Increases the stock level for a specific product.
     * @param type   the product type to update.
     * @param amount the quantity to add (must be positive).
     * @return true if the update was successful, false if the type is missing or amount is invalid.
     */
    public boolean increaseProductStock(ProductType type, int amount) {
        if (!productStocks.containsKey(type) || !isValidAmount(amount)) {
            return false;
        }

        int previousValue = productStocks.get(type);
        productStocks.put(type, previousValue + amount);
        return true;
    }

    /**
     * Decreases the stock level for a specific product if sufficient stock exists.
     * @param type   the product type to update.
     * @param amount the quantity to subtract (must be positive).
     * @return true if the update was successful, false if the type is missing or amount is invalid or warehouse stock is
     * insufficient.
     */
    public boolean decreaseProductStock(ProductType type, int amount) {
        if (!productStocks.containsKey(type) || !isValidAmount(amount)) {
            return false;
        }

        int previousValue = productStocks.get(type);
        if (sufficientStock(type, amount)) {
            productStocks.put(type, previousValue - amount);
            return true;
        }
        return false;
    }

    /**
     * Checks if there is enough stock available for a specific quantity.
     * @param type   the product type to check.
     * @param amount the quantity requested.
     * @return true if current stock minus amount is greater than zero.
     */
    public boolean sufficientStock(ProductType type, int amount) {
        Integer previousValue = productStocks.get(type);
        if (previousValue == null) return false;
        return (previousValue - amount >= 0);
    }

    /**
     * Validates if the provided quantity is a positive integer.
     * @param amount the quantity to validate.
     * @return true if amount > 0.
     */
    public boolean isValidAmount(int amount) {
        return amount > 0;
    }

    /**
     * @return the complete map of product types and their respective stock quantities.
     */
    public HashMap<ProductType, Integer> getProductStocks() {
        return productStocks;
    }

    /**
     * @return the maximum capacity limit of the warehouse.
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Updates the maximum capacity limit of the warehouse.
     * @param maxCapacity the new capacity limit.
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Resets the warehouse by clearing all product stock data.
     */
    public void clear() {
        productStocks.clear();
    }
}
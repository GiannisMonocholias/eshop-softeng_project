package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.ProductsWareHouseDAO;
import gr.softeng.team21.domain.ProductType;

public class ProductsWareHouseDAOMemory implements ProductsWareHouseDAO {

    private static ProductsWareHouseDAOMemory instance;
    private static int maxCapacity = 1000;
    private static double totalProducts;
    private static HashMap<ProductType, Integer> productStocks;


    private ProductsWareHouseDAOMemory() {
        totalProducts = 0;
        productStocks = new HashMap<>();
    }

    /**
     * Επιστρέφει τη μοναδική (singleton) παρουσία της κλάσης.
     */
    public static ProductsWareHouseDAOMemory getInstance() {
        if (instance == null) {
            instance = new ProductsWareHouseDAOMemory();
        }
        return instance;
    }

    @Override
    public CompletableFuture<Integer> getProductStock(ProductType type) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        if (type == null) {
            future.completeExceptionally(new IllegalArgumentException("type argument cannot be null"));
        } else {
            future.complete(productStocks.get(type));
        }

        return future;
    }

    @Override
    public CompletableFuture<Double> getCapacityUtilization() {
        double utilization = totalProducts > 0 ? totalProducts / maxCapacity : 0.0;


        return CompletableFuture.completedFuture(utilization);
    }

    @Override
    public CompletableFuture<Void> insertProduct(ProductType type) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (type == null) {
            future.completeExceptionally(new IllegalArgumentException("type argument cannot be null"));
        } else if (!productStocks.containsKey(type)) {
            productStocks.put(type, 0);
            future.complete(null);
        } else {
            future.completeExceptionally(new IllegalArgumentException("The provided type already exists in stock"));
        }

        return future;
    }

    @Override
    public CompletableFuture<Void> deleteProduct(ProductType type) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (type == null) {
            future.completeExceptionally(new IllegalArgumentException("type argument cannot be null"));
        } else if (!productStocks.containsKey(type)) {
            future.completeExceptionally(new NoSuchElementException("Product not in stock"));
        } else {
            totalProducts -= productStocks.get(type);
            productStocks.remove(type);
            future.complete(null);
        }

        return future;
    }

    @Override
    public CompletableFuture<Boolean> increaseProductStock(ProductType type, int amount) {
        if (!productStocks.containsKey(type) || amount <= 0) {
            return CompletableFuture.completedFuture(false);
        }

        int previousValue = productStocks.get(type);
        productStocks.put(type, previousValue + amount);
        totalProducts += amount;

        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> decreaseProductStock(ProductType type, int amount) {
        if (!productStocks.containsKey(type) || amount <= 0) {
            return CompletableFuture.completedFuture(false);
        }

        int previousValue = productStocks.get(type);
        if (previousValue - amount >= 0) {
            productStocks.put(type, previousValue - amount);
            totalProducts -= amount;
            return CompletableFuture.completedFuture(true);
        }

        return CompletableFuture.completedFuture(false);
    }

    @Override
    public CompletableFuture<Boolean> sufficientStock(ProductType type, int amount) {
        Integer previousValue = productStocks.get(type);
        if (previousValue == null) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.completedFuture(previousValue - amount >= 0);
    }

    @Override
    public CompletableFuture<Boolean> isValidAmount(int amount) {
        return CompletableFuture.completedFuture(amount > 0);
    }

    @Override
    public CompletableFuture<HashMap<ProductType, Integer>> getProductStocks() {
        return CompletableFuture.completedFuture(productStocks);
    }

    @Override
    public CompletableFuture<Integer> getMaxCapacity() {
        return CompletableFuture.completedFuture(maxCapacity);
    }

    @Override
    public CompletableFuture<Void> setMaxCapacity(int newMaxCapacity) {
        maxCapacity = newMaxCapacity;
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> clear() {
        productStocks.clear();
        totalProducts = 0;
        return CompletableFuture.completedFuture(null);
    }
}
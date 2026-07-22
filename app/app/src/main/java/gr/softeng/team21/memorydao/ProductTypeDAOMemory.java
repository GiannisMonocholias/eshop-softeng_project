package gr.softeng.team21.memorydao;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.ProductType;

/**
 * In-memory implementation of the {@link ProductTypeDAO} interface.
 * Provides a centralized, asynchronous repository for managing product types
 * and ensures synchronization with the warehouse stock levels using CompletableFuture.
 * @author PAVLOS GRATSANIS
 */
public class ProductTypeDAOMemory implements ProductTypeDAO {
    private static ProductTypeDAOMemory instance;
    private static HashMap<String, ProductType> products;

    /**
     * Private constructor for the Singleton pattern.
     * Initializes the product storage map.
     */
    private ProductTypeDAOMemory() {
        products = new HashMap<>();
    }

    /**
     * Returns the singleton instance of ProductTypeDAOMemory.
     * @return The unique instance of this DAO.
     */
    public static ProductTypeDAOMemory getInstance() {
        if (instance == null) {
            instance = new ProductTypeDAOMemory();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<ProductType> getProduct(String productCode) {
        CompletableFuture<ProductType> future = new CompletableFuture<>();
        if (productCode == null) {
            future.completeExceptionally(new IllegalArgumentException("Product code cannot be null"));
        } else {
            future.complete(products.get(productCode));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> addProductType(ProductType product) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (product == null) {
            future.completeExceptionally(new IllegalArgumentException("Product cannot be null"));
        } else if (!products.containsKey(product.getProductCode())) {
            products.put(product.getProductCode(), product);
            // Synchronous call to Warehouse (can be refactored to async if needed later)
            ProductsWareHouseDAOMemory.getInstance().insertProduct(product);
            future.complete(null);
        } else {
            future.completeExceptionally(new IllegalArgumentException("The given product type is already in the repository"));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> deleteProductType(ProductType product) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (product == null) {
            future.completeExceptionally(new IllegalArgumentException("Product cannot be null"));
        } else if (products.containsKey(product.getProductCode())) {
            products.remove(product.getProductCode());
            // Synchronous call to Warehouse
            ProductsWareHouseDAOMemory.getInstance().deleteProduct(product);
            future.complete(null);
        } else {
            future.completeExceptionally(new IllegalArgumentException("The given product type is not registered in the repository"));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> processProduct(ProductType updatedProduct) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (updatedProduct == null) {
            future.completeExceptionally(new IllegalArgumentException("Product cannot be null"));
        } else if (products.containsKey(updatedProduct.getProductCode())) {
            ProductType existingProduct = products.get(updatedProduct.getProductCode());
            existingProduct.setProductname(updatedProduct.getProductname());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDescription(updatedProduct.getDescription());
            future.complete(null);
        } else {
            future.completeExceptionally(new IllegalStateException("Product type with id " + updatedProduct.getProductCode() + " is not a registered product type."));
        }
        return future;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<HashMap<String, ProductType>> getProducts() {
        return CompletableFuture.completedFuture(new HashMap<>(products));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompletableFuture<Void> clear() {
        products.clear();
        return CompletableFuture.completedFuture(null);
    }
}
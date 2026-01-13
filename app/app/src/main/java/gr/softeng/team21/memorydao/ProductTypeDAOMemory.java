package gr.softeng.team21.memorydao;

import java.util.HashMap;
import gr.softeng.team21.dao.ProductTypeDAO;
import gr.softeng.team21.domain.ProductType;

/**
 * In-memory implementation of the {@link ProductTypeDAO} interface.
 * Provides a centralized repository for managing product types and ensures
 * synchronization with the warehouse stock levels.
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
     * Retrieves a specific product type based on its unique product code.
     * @param productCode the unique code of the product type to retrieve.
     * @return the ProductType object, or null if no such product code is registered.
     * @throws IllegalArgumentException if the productCode is null.
     */
    public ProductType getProduct(String productCode) {
        if (productCode == null)
            throw new IllegalArgumentException("Product code cannot be null");

        return products.get(productCode);
    }

    /**
     * Registers a new product type in the repository and initializes it in the warehouse.
     * @param product the ProductType object to add.
     * @throws IllegalArgumentException if the product is null or already exists in the repository.
     */
    public void addProductType(ProductType product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (!products.containsKey(product.getProductCode())) {
            products.put(product.getProductCode(), product);
            ProductsWareHouseDAOMemory.getInstance().insertProduct(product);
        } else {
            throw new IllegalArgumentException("The given product type is already in the repository");
        }
    }

    /**
     * Removes a product type from the repository and also deletes it from the warehouse.
     * @param product the ProductType object to remove.
     * @throws IllegalArgumentException if the product is null or not found in the repository.
     */
    public void deleteProductType(ProductType product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (products.containsKey(product.getProductCode())) {
            products.remove(product.getProductCode());
            ProductsWareHouseDAOMemory.getInstance().deleteProduct(product);
        } else {
            throw new IllegalArgumentException("The given product type is not registered in the repository");
        }
    }

    /**
     * Updates the details of an existing product type (name, price, description).
     * @param updatedProduct the ProductType object containing the updated information.
     * @throws IllegalArgumentException if the updatedProduct is null.
     * @throws IllegalStateException if the product code is not found in the repository.
     */
    public void processProduct(ProductType updatedProduct) {
        if (updatedProduct == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (products.containsKey(updatedProduct.getProductCode())) {
            ProductType existingProduct = products.get(updatedProduct.getProductCode());
            existingProduct.setProductname(updatedProduct.getProductname());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDescription(updatedProduct.getDescription());
        } else {
            throw new IllegalStateException("Product type with id " + updatedProduct.getProductCode() + " is not a registerd product type.");
        }
    }

    /**
     * @return a map containing all registered product types.
     */
    public HashMap<String, ProductType> getProducts() {
        return products;
    }

    /**
     * Clears all registered product types from the repository.
     */
    public void clear() {
        products.clear();
    }
}